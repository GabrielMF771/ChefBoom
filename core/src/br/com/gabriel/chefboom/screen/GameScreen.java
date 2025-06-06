package br.com.gabriel.chefboom.screen;

import br.com.gabriel.chefboom.resource.Assets;
import com.badlogic.gdx.graphics.Texture;
import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.entity.component.RigidBodyComponent;
import br.com.gabriel.chefboom.entity.component.TransformComponent;
import br.com.gabriel.chefboom.world.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends ScreenAdapter {

    SpriteBatch batch;
    protected OrthographicCamera camera;
    protected World world;

    FitViewport viewport;
    public final Vector3 screenCordinate = new Vector3();

    private Texture backgroundTexture;

    @Override
    public void show () {
        viewport = new FitViewport(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT, camera);

        batch = new SpriteBatch();

        camera = new OrthographicCamera(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        // Mexer aqui para posicionar a câmera
        camera.setToOrtho(false, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        backgroundTexture = Assets.manager.get(Assets.map);

        world = new World(camera);
        world.regenerate();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    // FUNÇÃO PRA ATUALIZAR O TAMANHO DA TELA
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
    public void render (float delta) {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.begin();
        batch.draw(
                backgroundTexture,
                0, 0,
                camera.viewportWidth, camera.viewportHeight
        );
        batch.end();

        world.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if(ChefBoom.DEBUG){
            if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
                if(world.getEntityTrackerWindow() != null){
                    world.getEntityTrackerWindow().setVisible(!world.getEntityTrackerWindow().isVisible());
                }
            }

            if(Gdx.app.getInput().isTouched()) {
                screenCordinate.set(Gdx.input.getX(), Gdx.input.getY(), 0);

                camera.unproject(screenCordinate);
                world.getArtemis().getEntity(world.getPlayer()).getComponent(TransformComponent.class).position.set(screenCordinate.x, screenCordinate.y);
                world.getArtemis().getEntity(world.getPlayer()).getComponent(RigidBodyComponent.class).velocity.set(Vector2.Zero);

            }
        }
    }
}
