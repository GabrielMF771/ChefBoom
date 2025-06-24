package br.com.gabriel.chefboom.screen;

import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.resource.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class MenuScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = Gdx.graphics.getWidth();
    private static final float WORLD_HEIGHT = Gdx.graphics.getHeight();

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;

    private BitmapFont fontTitle;  // fonte para o título
    private GlyphLayout layoutTitle;

    private Texture startButtonTexture;
    private Texture configButtonTexture;
    private Texture creditsButtonTexture;
    private Texture tutorialButtonTexture;

    private float titleX, titleY, titleWidth, titleHeight;
    private float startButtonX, startButtonY, startButtonWidth, startButtonHeight;
    private float configButtonX, configButtonY, configButtonWidth, configButtonHeight;
    private float creditsButtonX, creditsButtonY, creditsButtonWidth, creditsButtonHeight;
    private float tutorialButtonX, tutorialButtonY, tutorialButtonWidth, tutorialButtonHeight;

    private Texture backgroundTexture;

    private static Music menuMusic;

    @Override
    public void show() {
        menuMusic = Assets.manager.get(Assets.menuMusic);
        menuMusic.setLooping(true);
        menuMusic.setVolume(Config.MUSIC_VOLUME);
        menuMusic.play();

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        batch = new SpriteBatch();

        // Inicializa a fonte do título usando FreeTypeFontGenerator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/MinecraftRegular.otf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 80;
        parameter.color = Color.WHITE;
        parameter.shadowOffsetX = -6;
        parameter.shadowOffsetY = 6;
        parameter.shadowColor = Color.BLACK;
        fontTitle = generator.generateFont(parameter);
        generator.dispose();

        layoutTitle = new GlyphLayout();

        startButtonTexture = Assets.manager.get(Assets.botaoIniciar);

        configButtonTexture = Assets.manager.get(Assets.botaoConfig);

        creditsButtonTexture = Assets.manager.get(Assets.botaoCreditos);

        tutorialButtonTexture = Assets.manager.get(Assets.botaoTutorial);

        backgroundTexture = Assets.manager.get(Assets.menuBackground);

        calculateDimensionsAndPositions();
    }

    private void calculateDimensionsAndPositions() {

        // Título
        titleWidth = WORLD_WIDTH / 2.5f;
        titleHeight = WORLD_HEIGHT / 3.5f;
        titleX = (WORLD_WIDTH - titleWidth) / 2.0f;
        titleY = WORLD_HEIGHT * 0.5f;

        // Botões (Iniciar, Configurações, Créditos)
        startButtonWidth = WORLD_WIDTH / 6.0f;
        startButtonHeight = WORLD_HEIGHT / 8.0f;
        startButtonX = (WORLD_WIDTH - startButtonWidth) / 2.0f;
        startButtonY = (WORLD_HEIGHT * 0.3f) + 30f;

        configButtonHeight = WORLD_HEIGHT / 7.5f;
        configButtonWidth = WORLD_WIDTH / 13.5f;
        creditsButtonHeight = WORLD_HEIGHT / 7.5f;
        creditsButtonWidth = WORLD_WIDTH / 13.5f;

        // Largura total dos dois botões juntos (com um pequeno espaçamento entre eles)
        float spacing = 20f;
        float totalWidth = configButtonWidth + creditsButtonWidth + spacing;

        // Centraliza em relação ao botão de start
        float centerX = startButtonX + startButtonWidth / 2f;

        configButtonX = centerX - totalWidth / 2f;
        creditsButtonX = configButtonX + configButtonWidth + spacing;

        configButtonY = (WORLD_HEIGHT * 0.15f) + 30f;
        creditsButtonY = (WORLD_HEIGHT * 0.15f) + 30f;

        // Botão Tutorial
        tutorialButtonWidth = WORLD_WIDTH / 13.5f;
        tutorialButtonHeight = WORLD_HEIGHT / 7.5f;
        tutorialButtonX = 10f;
        tutorialButtonY = 10f;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        handleInput();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Desenha o fundo do menu
        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // Desenha um retângulo preto semi-transparente sobre o fundo
        batch.setColor(0, 0, 0, 0.6f); // 40% opacidade
        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.setColor(1, 1, 1, 1); // Reseta a cor

        // Calcula o layout do texto "Jogo"
        String titleText = "Chef Boom!";
        layoutTitle.setText(fontTitle, titleText);

        // Calcula X para centralizar o texto dentro da área do título (titleX até titleX + titleWidth)
        float textX = titleX + (titleWidth - layoutTitle.width) / 2f;
        // Calcula Y para posicionar o baseline do texto para ficar na vertical da área do título
        float textY = titleY + (titleHeight + layoutTitle.height) / 2f;

        fontTitle.draw(batch, layoutTitle, textX, textY);

        // Desenha o botão iniciar normalmente
        batch.draw(startButtonTexture, startButtonX, startButtonY, startButtonWidth, startButtonHeight);
        // Desenha o botão quadrado abaixo do botão iniciar
        batch.draw(configButtonTexture, configButtonX, configButtonY, configButtonWidth, configButtonHeight);
        batch.draw(creditsButtonTexture, creditsButtonX, creditsButtonY, creditsButtonWidth, creditsButtonHeight);
        batch.draw(tutorialButtonTexture, tutorialButtonX, tutorialButtonY, tutorialButtonWidth, tutorialButtonHeight);
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.input.getY();

            com.badlogic.gdx.math.Vector3 touchPos = new com.badlogic.gdx.math.Vector3(touchX, touchY, 0);
            camera.unproject(touchPos, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

            float worldX = touchPos.x;
            float worldY = touchPos.y;

            boolean touchedStartButton =
                    worldX >= startButtonX && worldX <= startButtonX + startButtonWidth &&
                            worldY >= startButtonY && worldY <= startButtonY + startButtonHeight;

            boolean touchedConfigButton =
                    worldX >= configButtonX && worldX <= configButtonX + configButtonWidth &&
                            worldY >= configButtonY && worldY <= configButtonY + configButtonHeight;

            boolean touchedCreditsButton =
                    worldX >= creditsButtonX && worldX <= creditsButtonX + creditsButtonWidth &&
                            worldY >= creditsButtonY && worldY <= creditsButtonY + creditsButtonHeight;

            boolean touchedTutorialButton =
                    worldX >= tutorialButtonX && worldX <= tutorialButtonX + tutorialButtonWidth &&
                            worldY >= tutorialButtonY && worldY <= tutorialButtonY + tutorialButtonHeight;

            if (touchedStartButton) {
                ChefBoom.getInstance().setScreen(new SelectLevelScreen());
            } else if (touchedConfigButton) {
                ChefBoom.getInstance().setScreen(new ConfigScreen());
            } else if (touchedCreditsButton) {
                ChefBoom.getInstance().setScreen(new CreditsScreen());
            } else if( touchedTutorialButton) {
                ChefBoom.getInstance().setScreen(new TutorialScreen());
            }
        }
    }

    public static Music getMenuMusic() {
        return menuMusic;
    }

    @Override
    public void dispose() {
        batch.dispose();
        fontTitle.dispose();
        startButtonTexture.dispose();
    }
}
