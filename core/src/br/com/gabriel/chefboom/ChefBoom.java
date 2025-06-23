package br.com.gabriel.chefboom;

import br.com.gabriel.chefboom.console.CommandExecutor;
import br.com.gabriel.chefboom.console.DevConsole;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.screen.GameScreen;
import br.com.gabriel.chefboom.screen.PreloadScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ChefBoom extends Game {
	private static ChefBoom instance;

	/* TODO:
	 * Fazer uma tela de pause com as configurações do jogo
	 * Remover o DEBUG quando for publicar o jogo
	 *
	 * Implementar um console para o jogo, onde o usuário pode digitar comandos
	 *
	 * Comandos do console:
	 * Ativar ou desativar fps
	 * Lista de comandos
	 * Ativar ou desativar debug
	 * Pegar um item específico
	 * Spawnar cliente em uma posição específica
	 * Ativar o modo secreto para jogar multiplayer local
	 */

	private DevConsole devConsole;
	private CommandExecutor commandExecutor;
	private boolean showFps = false;
	private SpriteBatch fpsBatch;
	private BitmapFont fpsFont;

	public static boolean DEBUG = true;

	private ChefBoom() {}

	@Override
	public void create() {
		instance = this;

		devConsole = new DevConsole(null);

		fpsFont = new BitmapFont();
		fpsBatch = new SpriteBatch();

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

		// Manipulação do console
		if (Gdx.input.isKeyJustPressed(Input.Keys.GRAVE)) { // Tecla `
			if (getScreen() instanceof br.com.gabriel.chefboom.screen.GameScreen) {
				devConsole.toggle();
			}
		}

		devConsole.render();

		if (DEBUG) {


			// Renderiza o FPS no canto superior esquerdo da tela
			if(showFps){
				fpsBatch.begin();
				fpsFont.draw(fpsBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, Gdx.graphics.getHeight() - 5);
				fpsBatch.end();
			}

			// Reinicia o jogo com a tecla CTRL + R
			if (!devConsole.isVisible()) {
				// Reinicia o jogo com a tecla CTRL + R
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
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		devConsole.resize(width, height);
	}

	@Override
	public void dispose () {
		if (getScreen() != null) {
			getScreen().dispose();
		}
		Assets.manager.dispose();
		devConsole.dispose();
		fpsBatch.dispose();
		fpsFont.dispose();
	}


	// Métodos para o console controlar
	public void toggleShowFps() {
		this.showFps = !this.showFps;
	}

	public boolean isShowingFps() {
		return showFps;
	}

	public GameScreen getGameScreen() {
		if (getScreen() instanceof GameScreen) {
			return (GameScreen) getScreen();
		}
		return null;
	}

	public DevConsole getDevConsole() {
		return devConsole;
	}

	public void setCommandExecutor(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
		if (devConsole != null) {
			devConsole.setCommandExecutor(commandExecutor);
		}
	}

	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}
}