package br.com.gabriel.jogoteste;

import br.com.gabriel.jogoteste.screen.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

public class JogoTeste extends Game {
	private static JogoTeste instance;

	public static final boolean DEBUG = true;

	private JogoTeste() {}

	@Override
	public void create() {
		this.setScreen(new GameScreen());
	}

	public static JogoTeste getInstance() {
		if(instance == null) {
			instance = new JogoTeste();
		}
		return instance;
	}

	@Override
	public void dispose () {

	}

}