package br.com.gabriel.chefboom.world;


import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.dictionary.Blocks;
import br.com.gabriel.chefboom.entity.EntitiesFactory;
import br.com.gabriel.chefboom.entity.system.*;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import net.namekdev.entity_tracker.EntityTracker;
import net.namekdev.entity_tracker.ui.EntityTrackerMainWindow;

import com.badlogic.gdx.math.Rectangle;

public class World {

    private final EntityTrackerMainWindow entityTrackerWindow;

    public static final int BG = 1;
    public static final int FG = 0;


    //VETOR Q ARMAZENA O MAPA - OS TILES SÃO FORMADOS DEPENDENDO DO TAMANHO DA TELA
    //SE MUDAR O TILE SIZE, TEM Q MEXER AQUI TBM
    //2 COLUNAS DE PROFUNDIDADE - FOREGOUND E BACKGROUND
    private final int [] [] [] map = new int [(int) ((Config.SCREEN_WIDTH * 0.8f)/Block.TILE_SIZE)] [(int) ((Config.SCREEN_HEIGHT * 0.8f)/Block.TILE_SIZE)] [2];

    private final Rectangle[][] collisionBoxes = new Rectangle[Config.SCREEN_WIDTH/Block.TILE_SIZE][Config.SCREEN_HEIGHT/Block.TILE_SIZE];

    private final com.artemis.World artemis;

    private final int player;

    private int client, client2, client3;

    private boolean debugCollisionEnabled = false;

    public World(OrthographicCamera camera){
        WorldConfigurationBuilder worldConfigBuilder = new WorldConfigurationBuilder()
            .with(new PlayerControllerSystem())
            .with(new MovementSystem(this))
            .with(new StateSystem())
            .with(new TileRenderSystem(this, camera))
            .with(new SpriteRenderSystem(camera));


        if(ChefBoom.DEBUG){

            worldConfigBuilder.with(new CollisionDebugSystem(camera, this));
            entityTrackerWindow = new EntityTrackerMainWindow(false, false);
            worldConfigBuilder.with(new EntityTracker(entityTrackerWindow));
        }

        WorldConfiguration config = worldConfigBuilder.build();

        artemis = new com.artemis.World(config);

        EntitiesFactory entitiesFactory = new EntitiesFactory();
        artemis.inject(entitiesFactory);

        player = entitiesFactory.createPlayer(artemis, ((Config.SCREEN_WIDTH * 0.8f) / 2), ((Config.SCREEN_HEIGHT * 0.8f)  / 2));

        //CLIENTES

        client2 = entitiesFactory.createClient(artemis, ((Config.SCREEN_WIDTH * 0.6f) / 2), ((Config.SCREEN_HEIGHT * 1.0f) / 2));
        client3 = entitiesFactory.createClient(artemis, ((Config.SCREEN_WIDTH * 0.6f) / 2), ((Config.SCREEN_HEIGHT * 0.8f) / 2));
        client = entitiesFactory.createClient(artemis, ((Config.SCREEN_WIDTH * 0.6f) / 2), ((Config.SCREEN_HEIGHT * 0.6f) / 2));

    }

    public void regenerate() {
        float startX = (getWidth() / 2.5f);
        int endX = getWidth();
        int startY = 0;
        int endY = getHeight();

        // Define a margem interna do retângulo central de barreiras (quanto maior, menor o retângulo)
        int innerRectMargin = 5;
        int innerStartX = (int) startX + innerRectMargin + 2;
        int innerEndX = endX - 1 - innerRectMargin;
        int innerStartY = startY + innerRectMargin + 1;
        int innerEndY = endY - 1 - innerRectMargin - 1;

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                for (int l = 0; l < getLayers(); l++) {
                    Block block = null;

                    if (l == 0) { // FOREGROUND
                        if (x < startX) {
                            block = Blocks.AIR;
                        } else {
                            // Bordas externas
                            boolean isBorder =
                                    (x >= startX && x < startX + 2) || // borda esquerda
                                            (x >= endX - 2 && x < endX) || // borda direita
                                            (y >= startY && y < startY + 2) || // borda inferior
                                            (y >= endY - 2 && y < endY); // borda superior

                            // Retângulo interno preenchido de BARRIER
                            if (x >= innerStartX && x <= innerEndX &&
                                    y >= innerStartY && y <= innerEndY) {
                                block = Blocks.BARRIER;
                            } else if (isBorder) {
                                block = Blocks.BARRIER;
                            } else {
                                block = Blocks.AIR;
                            }
                        }
                    } else { // BACKGROUND
                        block = (x + y) % 2 == 0 ? Blocks.GROUND1 : Blocks.GROUND2;
                    }

                    map[x][y][l] = Blocks.getIdByBlock(block);
                }
            }
        }

        init();
    }


    public void init(){
        for(int x = 0; x < getWidth(); x++){
            for(int y = 0; y < getHeight(); y++){
                collisionBoxes[x][y] = getBlock(x, y, FG).getTileRectangle(this, x, y);
            }
        }
    }

    public void update(float delta) {
        if(ChefBoom.DEBUG){
            if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
                toggleCollisionDebug();
            }
        }

        artemis.setDelta(delta);
        artemis.process();
    }

    // Alterna o modo de debug de colisão
    public void toggleCollisionDebug() {
        debugCollisionEnabled = !debugCollisionEnabled;
    }

    // Verifica se o debug de colisão está ativo
    public boolean isDebugCollisionEnabled() {
        return debugCollisionEnabled;
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
        if(isValid(x,y)){
            return collisionBoxes[x][y];
        }

        return null;
    }

    //RETORNA A QUANTIDADE DE BLOCOS
    public void getTilesRectangle(float startX, float startY, float endX, float endY, Array<Rectangle> tileRectangles){
        getTilesRectangle(worldToMap(startX), worldToMap(startY), worldToMap(endX), worldToMap(endY), tileRectangles);
    }

    public void getTilesRectangle(int startX, int startY, int endX, int endY, Array<Rectangle> tileRectangles){
        tileRectangles.clear();

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

    public int getClient(){
        return client;
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
