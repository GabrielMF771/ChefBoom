package br.com.gabriel.chefboom;

import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.screen.PreloadScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;

public class ChefBoom extends Game {
	private static ChefBoom instance;

	public static final boolean DEBUG = true;

	ChefBoom() {}

	@Override
	public void create() {
		this.setScreen(new PreloadScreen());
	}

	public static ChefBoom getInstance() {
		if(instance == null) {
			instance = new ChefBoom();
		}
		return instance;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();

		if (DEBUG) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
				if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
					// Libera recursos da tela atual
					if (getScreen() != null) {
						getScreen().dispose();
					}
					// Cria uma nova instância da tela de jogo e define como atual
					setScreen(new br.com.gabriel.chefboom.screen.GameScreen());
				}
			}
		}
	}

	@Override
	public void dispose () {
		if (getScreen() != null) {
			getScreen().dispose();
		}
		Assets.manager.dispose();
	}
}