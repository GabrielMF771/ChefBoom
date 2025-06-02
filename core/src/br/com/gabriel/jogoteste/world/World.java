package br.com.gabriel.jogoteste.world;

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

    private final int [] [] [] map = new int [Gdx.graphics.getWidth()/16] [Gdx.graphics.getHeight()/16] [2];

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

        player = entitiesFactory.createPlayer(world, ((float) getWidth() / 2) * 16, ((float) getHeight() / 2) * 16);
    }

    public void regenerate(){
        for(int x = 0; x < getWidth(); x++){
            for(int y = 0; y < getHeight(); y++){
                for(int l = 0; l < getLayers(); l++) {
                    map[x][y][l] = Blocks.getIdByBlock(Blocks.GRASS);
                }
            }
        }
    }

    public void update(float delta) {
        world.setDelta(delta);
        world.process();
    }

    public Block getBlock(int x, int y, int layer){
        return Blocks.getBlockById(map[x][y][layer]);
    }

    public int getWidth(){
        return map.length;
    }

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
