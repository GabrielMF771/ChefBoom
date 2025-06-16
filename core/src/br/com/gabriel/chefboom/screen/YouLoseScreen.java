package br.com.gabriel.chefboom.screen;

import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.resource.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class YouLoseScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = Config.SCREEN_WIDTH;
    private static final float WORLD_HEIGHT = Config.SCREEN_HEIGHT;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;

    private BitmapFont fontTitle;  // fonte para o título
    private GlyphLayout layoutTitle;

    private com.badlogic.gdx.graphics.Texture startButtonTexture;

    private float titleX, titleY, titleWidth, titleHeight;
    private float startButtonX, startButtonY, startButtonWidth, startButtonHeight;

    private Sound gameoverSound = Assets.manager.get(Assets.gameoverSound);

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        batch = new SpriteBatch();

        // Inicializa a fonte do título usando FreeTypeFontGenerator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Bold.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 80; // ajuste o tamanho para cobrir a área do título (ajuste conforme necessário)
        parameter.color = Color.WHITE;
        fontTitle = generator.generateFont(parameter);
        generator.dispose();

        layoutTitle = new GlyphLayout();

        startButtonTexture = Assets.manager.get(Assets.iniciarBotao);

        calculateDimensionsAndPositions();

        // Toca o som de game over
        gameoverSound.play(Config.EFFECTS_VOLUME);
    }

    private void calculateDimensionsAndPositions() {
        // Mesmas dimensões que usava para o título imagem
        titleWidth = WORLD_WIDTH / 2.5f;
        titleHeight = WORLD_HEIGHT / 3.5f;

        titleX = (WORLD_WIDTH - titleWidth) / 2.0f;
        titleY = WORLD_HEIGHT * 0.5f;

        startButtonWidth = WORLD_WIDTH / 6.0f;
        startButtonHeight = WORLD_HEIGHT / 8.0f;

        startButtonX = (WORLD_WIDTH - startButtonWidth) / 2.0f;
        startButtonY = WORLD_HEIGHT * 0.3f;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        handleInput();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Calcula o layout do texto "Jogo"
        String titleText = "SEUS CLIENTES EXPLODIRAM";
        layoutTitle.setText(fontTitle, titleText);

        // Calcula X para centralizar o texto dentro da área do título (titleX até titleX + titleWidth)
        float textX = titleX + (titleWidth - layoutTitle.width) / 2f;
        // Calcula Y para posicionar o baseline do texto para ficar na vertical da área do título
        // Como titleY é o canto inferior do retângulo que você usava para o título (provavelmente),
        // vamos posicionar o baseline um pouco acima de titleY para centralizar verticalmente:
        float textY = titleY + (titleHeight + layoutTitle.height) / 2f;

        fontTitle.draw(batch, layoutTitle, textX, textY);

        // Desenha o botão iniciar normalmente
        batch.draw(startButtonTexture, startButtonX, startButtonY, startButtonWidth, startButtonHeight);

        batch.end();
    }

    //IDENTIFICA SE O BOTÃO FOI CLICADO
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

            if (touchedStartButton) {
                ChefBoom.getInstance().setScreen(new GameScreen());
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        fontTitle.dispose();
        startButtonTexture.dispose();
    }
}
