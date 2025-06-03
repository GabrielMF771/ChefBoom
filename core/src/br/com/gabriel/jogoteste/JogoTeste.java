package br.com.gabriel.jogoteste;

import br.com.gabriel.jogoteste.resource.Assets;
import br.com.gabriel.jogoteste.screen.PreloadScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;

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
		super.render();

		if(DEBUG) {
			fpsLogger.log();
		}
	}

	@Override
	public void dispose () {
		Assets.manager.dispose();
	}

}