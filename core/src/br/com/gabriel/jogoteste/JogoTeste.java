package br.com.gabriel.jogoteste;

import br.com.gabriel.jogoteste.resource.Assets;
import br.com.gabriel.jogoteste.screen.PreloadScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;

public class JogoTeste extends Game {
	private static JogoTeste instance;

	public static final boolean DEBUG = true;

	FPSLogger fpsLogger = new FPSLogger();

	private JogoTeste() {}

	@Override
	public void create() {
		this.setScreen(new PreloadScreen());
	}

	public static JogoTeste getInstance() {
		if(instance == null) {
			instance = new JogoTeste();
		}
		return instance;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();

		if(DEBUG) {
			fpsLogger.log();

			if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
				if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
					getScreen().show();
				}
			}
		}
	}

	@Override
	public void dispose () {
		Assets.manager.dispose();
	}

}