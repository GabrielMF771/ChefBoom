package br.com.gabriel.jogoteste;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setForegroundFPS(Config.FPS);
		config.setTitle(Config.GAME_TITLE);
		config.setWindowIcon(Config.GAME_ICON);

		if (Config.FULLSCREEN) {
			config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		} else if (Config.MAXIMIZED){
			config.setMaximized(true);
		} else {
			config.setWindowedMode(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
		}

		config.setResizable(false);
		config.useVsync(true);
		new Lwjgl3Application(JogoTeste.getInstance(), config);
	}
}
