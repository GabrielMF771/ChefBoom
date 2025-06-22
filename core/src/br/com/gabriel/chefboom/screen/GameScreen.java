package br.com.gabriel.chefboom.screen;

import br.com.gabriel.chefboom.entity.component.ClientComponent;
import br.com.gabriel.chefboom.entity.system.OrderSystem;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.world.LevelEnded;
import com.artemis.ComponentMapper;
import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.entity.component.RigidBodyComponent;
import br.com.gabriel.chefboom.entity.component.TransformComponent;
import br.com.gabriel.chefboom.world.World;
import br.com.gabriel.chefboom.hud.HudRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import br.com.gabriel.chefboom.world.CurrentLevel;

public class GameScreen extends ScreenAdapter {

    SpriteBatch batch;
    protected OrthographicCamera camera;
    protected World world;

    FitViewport viewport;
    public final Vector3 screenCordinate = new Vector3();

    private Texture backgroundTexture;
    private HudRenderer hudRenderer;

    private static Music gameMusic;

    @Override
    public void show() {
        gameMusic = Assets.manager.get(Assets.gameMusic);
        gameMusic.setLooping(true);
        gameMusic.setVolume(Config.MUSIC_VOLUME);
        gameMusic.play();

        viewport = new FitViewport(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT, camera);

        batch = new SpriteBatch();

        camera = new OrthographicCamera(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        // Mexer aqui para posicionar a câmeras
        camera.setToOrtho(false, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        backgroundTexture = Assets.manager.get(Assets.map);


        world = new World(camera);
        world.regenerate();

        OrderSystem orderSystem = world.getArtemis().getSystem(OrderSystem.class);
        ComponentMapper<ClientComponent> mClient = world.getArtemis().getMapper(ClientComponent.class);
        hudRenderer = new HudRenderer(orderSystem, world, mClient);
        hudRenderer.showLevelMessage(CurrentLevel.getLevel());


        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public GameScreen() {

    }

    public GameScreen(HudRenderer existingHudRenderer) {
        this.hudRenderer = existingHudRenderer;
    }


    @Override
    // FUNÇÃO PRA ATUALIZAR O TAMANHO DA TELAA
    public void resize(int width, int height) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();

        world.regenerate();

        float worldWidth = world.getWidth() * Block.TILE_SIZE;
        float worldHeight = world.getHeight() * Block.TILE_SIZE;

        camera.viewportWidth = worldWidth;
        camera.viewportHeight = worldHeight;

        camera.position.set(worldWidth / 2f, worldHeight / 2f, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        // Desenha o fundo
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();
        timespace();
        LevelEnded.levelEndedCheck();

        // Atualiza e desenha entidades
        world.update(delta);

        // Desenha a hud no topo
        batch.begin();
        hudRenderer.render(batch, camera);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ChefBoom.getInstance().setScreen(new MenuScreen());
            gameMusic.stop();
        }

        if (ChefBoom.DEBUG) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                if (world.getEntityTrackerWindow() != null) {
                    world.getEntityTrackerWindow().setVisible(!world.getEntityTrackerWindow().isVisible());
                }
            }

            /* TODO - TIRAR DEPOIS
            if (Gdx.app.getInput().isTouched()) {
                screenCordinate.set(Gdx.input.getX(), Gdx.input.getY(), 0);

                camera.unproject(screenCordinate);
                world.getArtemis().getEntity(world.getPlayer()).getComponent(TransformComponent.class).position.set(screenCordinate.x, screenCordinate.y);
                world.getArtemis().getEntity(world.getPlayer()).getComponent(RigidBodyComponent.class).velocity.set(Vector2.Zero);

            }
             */

        }
    }

    @Override
    public void dispose() {
        if (gameMusic != null) {
            gameMusic.stop();
            gameMusic.dispose();
        }
        super.dispose();
    }

    //SETA QUANTO TEMPO VAI PRECISAR PARA APARECER OS PRIMEIROS CLIENTES
    public void timespace() {

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                world.generateClients(world);
            }
        }, 1, 15);

    }

    public static Music getGameMusic() {
        return gameMusic;
    }
}