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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.namekdev.entity_tracker.EntityTracker;
import net.namekdev.entity_tracker.ui.EntityTrackerMainWindow;

public class World {

    private final EntityTrackerMainWindow entityTrackerWindow;

    //VETOR Q ARMAZENA O MAPA - OS TILES SÃO FORMADOS DEPENDENDO DO TAMANHO DA TELA
    //SE MUDAR O TILE SIZE, TEM Q MEXER AQUI TBM
    //2 COLUNAS DE PROFUNDIDADE - FOREGOUND E BACKGROUND
    private final int [] [] [] map = new int [Config.SCREEN_WIDTH/32] [Config.SCREEN_HEIGHT/32] [2];

    private com.artemis.World world;

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

        world = new com.artemis.World(config);

        entitiesFactory = new EntitiesFactory();
        world.inject(entitiesFactory);

        player = entitiesFactory.createPlayer(world, ((float) Config.SCREEN_WIDTH / 2), ((float) Config.SCREEN_HEIGHT  / 2));
    }
    //FUNÇÃO QUE GERA O TILES NO MUNDO - NO CASO VAMOS USAR SÓ 1 TIPO BLOCO
    public void regenerate() {
        float startX = (getWidth() / 2.5f);         // metade do mapa
        int endX = getWidth();               // até o fim
        int startY = 0;
        float endY = getHeight();

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                for (int l = 0; l < getLayers(); l++) {
                    // Região fora do retângulo (metade esquerda)
                    if (x < startX) {
                        map[x][y][l] = Blocks.getIdByBlock(Blocks.AIR);
                    }
                    // Dentro do retângulo da direita
                    else {
                        boolean isBorder = x == startX || x == endX - 1 || y == startY || y == endY - 1;
                        map[x][y][l] = Blocks.getIdByBlock(isBorder ? Blocks.GRASS : Blocks.AIR);
                    }
                }
            }
        }
    }


    public void update(float delta) {
        world.setDelta(delta);
        world.process();
    }


    //RETORNA O BLOCO DEPENDENDO DA COORDENADA INSERIDA
    public Block getBlock(int x, int y, int layer){
        return Blocks.getBlockById(map[x][y][layer]);
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
        world.dispose();
    }

    public EntityTrackerMainWindow getEntityTrackerWindow() {
        return entityTrackerWindow;
    }

    public int getPlayer() {
        return player;
    }

    public com.artemis.World getWorld() {
        return world;
    }
}
