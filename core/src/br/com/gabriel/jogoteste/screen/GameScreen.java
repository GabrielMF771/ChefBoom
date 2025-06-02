package br.com.gabriel.jogoteste.screen;

import br.com.gabriel.jogoteste.JogoTeste;
import br.com.gabriel.jogoteste.entity.component.TransformComponent;
import br.com.gabriel.jogoteste.world.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends ScreenAdapter {

    SpriteBatch batch;
    protected OrthographicCamera camera;
    protected World world;

    public final Vector3 screenCordinate = new Vector3();

    @Override
    public void show () {
        batch = new SpriteBatch();

        camera = new OrthographicCamera(JogoTeste.SCREEN_WIDTH, JogoTeste.SCREEN_HEIGHT);
        camera.setToOrtho(false, JogoTeste.SCREEN_WIDTH, JogoTeste.SCREEN_HEIGHT);

        world = new World(camera);
        world.regenerate();

        /*
        if(JogoTeste.DEBUG){
            Gdx.input.setInputProcessor(new InputAdapter(){
                @Override
                public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                    screenCordinate.set(screenX, screenY, 0);
                    camera.unproject(screenCordinate);

                    int player = world.getPlayer();

                    world.getWorld().getEntity(player).getComponent(TransformComponent.class).position.set(screenCordinate.x, screenCordinate.y);
                    return true;
                }
            });
        }
         */
    }


    @Override
    public void render (float delta) {
        ScreenUtils.clear(1, 1, 1, 1);

        world.update(delta);

        if(JogoTeste.DEBUG){
            if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
                if(world.getEntityTrackerWindow() != null){
                    world.getEntityTrackerWindow().setVisible(!world.getEntityTrackerWindow().isVisible());
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }
}
