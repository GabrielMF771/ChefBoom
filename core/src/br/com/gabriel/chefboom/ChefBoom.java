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

	private ChefBoom() {}

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
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();

		if(DEBUG) {
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