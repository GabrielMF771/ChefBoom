package br.com.gabriel.jogoteste.screen;

import br.com.gabriel.jogoteste.JogoTeste;
import br.com.gabriel.jogoteste.Config;
import br.com.gabriel.jogoteste.entity.component.RigidBodyComponent;
import br.com.gabriel.jogoteste.entity.component.TransformComponent;
import br.com.gabriel.jogoteste.resource.Assets;
import br.com.gabriel.jogoteste.world.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GameScreen extends ScreenAdapter {

    SpriteBatch batch;
    protected OrthographicCamera camera;
    protected World world;

    public final Vector3 screenCordinate = new Vector3();

    @Override
    public void show () {
        batch = new SpriteBatch();

        camera = new OrthographicCamera(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        camera.setToOrtho(false, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        world = new World(camera);
        world.regenerate();

    }


    @Override
    public void render (float delta) {
        world.update(delta);

        if(JogoTeste.DEBUG){
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

    @Override
    // FUNÇÃO PRA ATUALIZAR O TAMANHO DA TELA
    public void resize(int width, int height) {

    }
}
