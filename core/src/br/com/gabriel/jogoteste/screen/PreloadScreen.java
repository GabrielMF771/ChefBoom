package br.com.gabriel.jogoteste.screen;

import br.com.gabriel.jogoteste.JogoTeste;
import br.com.gabriel.jogoteste.resource.Assets;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;

public class PreloadScreen extends ScreenAdapter {

    @Override
    public void show() {
        Assets.load();
    }

    @Override
    public void render(float delta) {
        if(Assets.manager.update()) {
            JogoTeste.getInstance().setScreen(new GameScreen());
        }
    }
}
