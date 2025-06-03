package br.com.gabriel.jogoteste.world;

import br.com.gabriel.jogoteste.Config;
import br.com.gabriel.jogoteste.JogoTeste;
import br.com.gabriel.jogoteste.block.Block;
import br.com.gabriel.jogoteste.dictionary.Blocks;
import br.com.gabriel.jogoteste.entity.EntitiesFactory;
import br.com.gabriel.jogoteste.entity.system.MovementSystem;
import br.com.gabriel.jogoteste.entity.system.PlayerControllerSystem;
import br.com.gabriel.jogoteste.entity.system.SpriteRenderSystem;
import br.com.gabriel.jogoteste.entity.system.TileRenderSystem;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.namekdev.entity_tracker.EntityTracker;
import net.namekdev.entity_tracker.ui.EntityTrackerMainWindow;

public class World {

    private final EntityTrackerMainWindow entityTrackerWindow;

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
                        // FOREGROUND: retângulo de GRASS nas bordas da metade direita
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
                            block = Blocks.GROUND1;
                        } else {
                            block = Blocks.GROUND2;
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
        return Blocks.getBlockById(map[worldToMap(x)][worldToMap(y)][worldToMap(layer)]);
    }

    //RETORNA A LARGURA DO MUNDO
    public int getWidth(){
        return map.length;
    }
    //RETORNA A ALTURA DO MUNDO
    public int getHeight(){
        return map[0].length;
    }

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
