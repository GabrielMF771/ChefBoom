package br.com.gabriel.jogoteste.world;

import br.com.gabriel.jogoteste.Config;
import br.com.gabriel.jogoteste.JogoTeste;
import br.com.gabriel.jogoteste.block.Block;
import br.com.gabriel.jogoteste.dictionary.Blocks;
import br.com.gabriel.jogoteste.entity.EntitiesFactory;
import br.com.gabriel.jogoteste.entity.system.*;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import net.namekdev.entity_tracker.EntityTracker;
import net.namekdev.entity_tracker.ui.EntityTrackerMainWindow;

import com.badlogic.gdx.math.Rectangle;

public class World {

    private final EntityTrackerMainWindow entityTrackerWindow;

    public static final int BG = 0;
    public static final int FG = 1;

    //VETOR Q ARMAZENA O MAPA - OS TILES SÃO FORMADOS DEPENDENDO DO TAMANHO DA TELA
    //SE MUDAR O TILE SIZE, TEM Q MEXER AQUI TBM
    //2 COLUNAS DE PROFUNDIDADE - FOREGOUND E BACKGROUND
    private final int [] [] [] map = new int [Config.SCREEN_WIDTH/32] [Config.SCREEN_HEIGHT/32] [2];

    private com.artemis.World artemis;

    private int player;

    private EntitiesFactory entitiesFactory;

    public World(OrthographicCamera camera){
        WorldConfigurationBuilder worldConfigBuilder = new WorldConfigurationBuilder();
        worldConfigBuilder.with(new PlayerControllerSystem());
        worldConfigBuilder.with(new MovementSystem(this));
        worldConfigBuilder.with(new TileRenderSystem(this, camera));
        worldConfigBuilder.with(new SpriteRenderSystem(camera));

        if(JogoTeste.DEBUG){
            worldConfigBuilder.with(new CollisionDebugSystem(camera, this));

            entityTrackerWindow = new EntityTrackerMainWindow(false, false);
            worldConfigBuilder.with(new EntityTracker(entityTrackerWindow));
        }

        WorldConfiguration config = worldConfigBuilder.build();

        artemis = new com.artemis.World(config);

        entitiesFactory = new EntitiesFactory();
        artemis.inject(entitiesFactory);

        player = entitiesFactory.createPlayer(artemis, ((float) Config.SCREEN_WIDTH / 2), ((float) Config.SCREEN_HEIGHT  / 2));
    }
    //FUNÇÃO QUE GERA O TILES NO MUNDO - NO CASO VAMOS USAR SÓ 1 TIPO BLOCO
    public void regenerate() {
        float startX = (getWidth() / 2.5f);
        int endX = getWidth();
        int startY = 0;
        float endY = getHeight();

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                for (int l = 0; l < getLayers(); l++) {
                    Block block = null;

                    if(l == 0){
                        // FOREGROUND: retângulo de BARRIER nas bordas da metade direita
                        // Região fora do retângulo (metade esquerda)
                        if (x < startX) {
                            block = Blocks.AIR;
                        }
                        // Dentro do retângulo da direita
                        else {
                            boolean isBorder = x == startX || x == endX - 1 || y == startY || y == endY - 1;
                            block = isBorder ? Blocks.BARRIER : Blocks.AIR;
                        }
                    } else {
                        // BACKGROUND: padrão xadrez entre GROUND e GROUND2
                        if ((x + y) % 2 == 0) {
                            //block = Blocks.GROUND1;
                            block = Blocks.AIR;
                        } else {
                            //block = Blocks.GROUND2;
                            block = Blocks.AIR;
                        }
                    }

                    map[x][y][l] = Blocks.getIdByBlock(block);
                }
            }
        }
    }


    public void update(float delta) {
        artemis.setDelta(delta);
        artemis.process();
    }

    //RETORNA O BLOCO DEPENDENDO DA COORDENADA INSERIDA
    public Block getBlock(int x, int y, int layer){
        return Blocks.getBlockById(map[x][y][layer]);
    }

    public Block getBlock(float x, float y, float layer){
        return getBlock(worldToMap(x),worldToMap(y), layer);
    }

    //RETORNA SE O BLOCO É SÓLIDO OU NÃO
    public boolean isSolid(int x, int y){
        return isValid(x,y) && getBlock(x,y, FG).isSolid();
    }

    public boolean isSolid(float x, float y){
        return isSolid(worldToMap(x),worldToMap(y));
    }

    public Rectangle getTileRectangle(int x, int y){
        Rectangle rectangle = null;

        if(isSolid(x,y)){
            rectangle = new Rectangle((int) mapToWorld(x), (int) mapToWorld(y), Block.TILE_SIZE, Block.TILE_SIZE);
        }

        return rectangle;
    }

    //RETORNA A QUANTIDADE DE BLOCOS
    public void getTilesRectangle(float startX, float startY, float endX, float endY, Array<Rectangle> tileRectangles){
        getTilesRectangle(worldToMap(startX), worldToMap(startY), worldToMap(endX), worldToMap(endY), tileRectangles);
    }

    public void getTilesRectangle(int startX, int startY, int endX, int endY, Array<Rectangle> tileRectangles){
        Rectangle rectangle;

        for(int y = startY; y <= endY; y++){
            for(int x = startX; x <= endX; x++){
                rectangle = getTileRectangle(x, y);

                if(rectangle != null){
                    tileRectangles.add(rectangle);
                }
            }
        }
    }

    //RETORNA A LARGURA DO MUNDO
    public int getWidth(){
        return map.length;
    }

    //RETORNA A ALTURA DO MUNDO
    public int getHeight(){
        return map[0].length;
    }

    //RETORNA O LAYER DO MUNDO
    public int getLayers() {
        return map[0][0].length;
    }

    public void dispose() {
        artemis.dispose();
    }

    public boolean isValid(int x, int y){
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }


    public EntityTrackerMainWindow getEntityTrackerWindow() {
        return entityTrackerWindow;
    }

    public int getPlayer() {
        return player;
    }

    public com.artemis.World getArtemis() {
        return artemis;
    }

    public static float mapToWorld(int mapCoordinate){
        return mapCoordinate * Block.TILE_SIZE;
    }

    public static int worldToMap(float worldCoordinate){
        return (int) (worldCoordinate / Block.TILE_SIZE);
    }
}
