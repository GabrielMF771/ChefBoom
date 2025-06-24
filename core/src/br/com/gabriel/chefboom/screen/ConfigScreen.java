package br.com.gabriel.chefboom.screen;

import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.resource.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ConfigScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = Config.SCREEN_WIDTH;
    private static final float WORLD_HEIGHT = Config.SCREEN_HEIGHT;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont fontSlider;

    private GlyphLayout layout;
    private GlyphLayout layoutTitle;

    private Texture backgroundTexture;
    private Texture backButtonTexture;
    private Texture sliderBackgroundTexture;
    private Texture sliderFillTexture;
    private Texture checkboxUncheckedTexture;
    private Texture checkboxCheckedTexture;

    // Posições dos botões e sliders
    private float titleX, titleY, titleWidth, titleHeight;
    private float backButtonX, backButtonY, backButtonWidth, backButtonHeight;
    private float musicSliderX, musicSliderY, musicSliderWidth, musicSliderHeight;
    private float effectsSliderX, effectsSliderY, effectsSliderWidth, effectsSliderHeight;
    private float fullscreenX, fullscreenY, fullscreenWidth, fullscreenHeight;
    private float twoPlayersX, twoPlayersY, twoPlayersWidth, twoPlayersHeight;

    private boolean draggingMusic = false;
    private boolean draggingEffects = false;

    private boolean fullscreenEnabled = Config.MAXIMIZED;

    private NinePatch sliderBackgroundNinePatch;
    private NinePatch sliderFillNinePatch;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        batch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/MinecraftRegular.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.color = Color.WHITE;
        parameter.shadowOffsetX = -3;
        parameter.shadowOffsetY = 3;
        parameter.shadowColor = Color.BLACK;
        font = generator.generateFont(parameter);

        // Parâmetros para o slider
        FreeTypeFontGenerator.FreeTypeFontParameter sliderParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        sliderParam.size = 32;
        sliderParam.color = Color.WHITE;
        fontSlider = generator.generateFont(sliderParam);

        generator.dispose();

        layout = new GlyphLayout();
        layoutTitle = new GlyphLayout();

        backgroundTexture = Assets.manager.get(Assets.menuBackground);
        backButtonTexture = Assets.manager.get(Assets.botaoVoltarProMenu);

        sliderBackgroundTexture = Assets.manager.get(Assets.sliderBackground);
        sliderFillTexture = Assets.manager.get(Assets.sliderFill);
        checkboxUncheckedTexture = Assets.manager.get(Assets.checkboxUnchecked);
        checkboxCheckedTexture = Assets.manager.get(Assets.checkboxChecked);

        sliderBackgroundNinePatch = new NinePatch(sliderBackgroundTexture, 10, 10, 10, 10);
        sliderFillNinePatch = new NinePatch(sliderFillTexture, 10, 10, 10, 10);

        // Centraliza e espaça verticalmente

        float startY = WORLD_HEIGHT * 0.7f;
        float spacing = 100f;

        titleWidth = WORLD_WIDTH / 2.5f;
        titleHeight = WORLD_HEIGHT / 3.5f;

        titleX = (WORLD_WIDTH - titleWidth) / 2.0f;
        titleY = WORLD_HEIGHT - (titleHeight - 40f);

        musicSliderWidth = WORLD_WIDTH * 0.6f;
        musicSliderHeight = 40;
        musicSliderX = (WORLD_WIDTH - musicSliderWidth) / 2f;
        musicSliderY = startY;

        effectsSliderWidth = musicSliderWidth;
        effectsSliderHeight = 40;
        effectsSliderX = musicSliderX;
        effectsSliderY = musicSliderY - spacing;

        fullscreenWidth = 40;
        fullscreenHeight = 40;
        fullscreenX = (WORLD_WIDTH - 300) / 2f;
        fullscreenY = effectsSliderY - spacing;

        twoPlayersWidth = 40;
        twoPlayersHeight = 40;
        twoPlayersX = fullscreenX;
        twoPlayersY = fullscreenY - spacing;

        backButtonWidth = WORLD_WIDTH / 6.0f;
        backButtonHeight = WORLD_HEIGHT / 8.0f;
        backButtonX = (WORLD_WIDTH - backButtonWidth) / 2.0f;
        backButtonY = twoPlayersY - spacing * 1.2f;
    }

    @Override
    public void render(float delta) {
        handleInput();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // Desenha um retângulo preto semi-transparente sobre o fundo
        batch.setColor(0, 0, 0, 0.6f); // 40% opacidade
        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.setColor(1, 1, 1, 1); // Reseta a cor

        // Calcula o layout do texto "Jogo"
        String titleText = "Configuraçoes";
        layoutTitle.setText(font, titleText);

        // Calcula X para centralizar o texto dentro da área do título
        float textX = titleX + (titleWidth - layoutTitle.width) / 2f;

        // Calcula Y para posicionar o baseline do texto para ficar na vertical da área do título
        float textY = titleY + (titleHeight + layoutTitle.height) / 2f;

        font.draw(batch, layoutTitle, textX, textY);

        // Música
        font.draw(batch, "Volume da Música", musicSliderX, musicSliderY + musicSliderHeight + 60);
        drawSlider(batch, musicSliderX, musicSliderY, musicSliderWidth, musicSliderHeight, Config.MUSIC_VOLUME);

        // Efeitos
        font.draw(batch, "Volume dos Efeitos", effectsSliderX, effectsSliderY + effectsSliderHeight + 50);
        drawSlider(batch, effectsSliderX, effectsSliderY, effectsSliderWidth, effectsSliderHeight, Config.EFFECTS_VOLUME);

        // Fullscreen
        font.draw(batch, "Tela Cheia", fullscreenX + fullscreenWidth + 20, fullscreenY + 40);
        drawCheckbox(batch, fullscreenX, fullscreenY, fullscreenWidth, fullscreenHeight, fullscreenEnabled);

        // Dois jogadores
        font.draw(batch, "Modo 2 Jogadores", twoPlayersX + twoPlayersWidth + 20, twoPlayersY + 40);
        drawCheckbox(batch, twoPlayersX, twoPlayersY, twoPlayersWidth, twoPlayersHeight, Config.TWO_PLAYERS);

        // Botão voltar
        batch.draw(backButtonTexture, backButtonX, backButtonY, backButtonWidth, backButtonHeight);

        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ChefBoom.getInstance().setScreen(new MenuScreen());
        }

        float touchX = Gdx.input.getX();
        float touchY = Gdx.input.getY();
        com.badlogic.gdx.math.Vector3 touchPos = new com.badlogic.gdx.math.Vector3(touchX, touchY, 0);
        camera.unproject(touchPos, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        float worldX = touchPos.x;
        float worldY = touchPos.y;

        if (Gdx.input.justTouched()) {
            // Slider música
            if (worldX >= musicSliderX && worldX <= musicSliderX + musicSliderWidth &&
                    worldY >= musicSliderY && worldY <= musicSliderY + musicSliderHeight) {
                draggingMusic = true;
                Config.MUSIC_VOLUME = (worldX - musicSliderX) / musicSliderWidth;
                Config.MUSIC_VOLUME = Math.max(0f, Math.min(1f, Config.MUSIC_VOLUME));
            }
            // Slider efeitos
            if (worldX >= effectsSliderX && worldX <= effectsSliderX + effectsSliderWidth &&
                    worldY >= effectsSliderY && worldY <= effectsSliderY + effectsSliderHeight) {
                draggingEffects = true;
                Config.EFFECTS_VOLUME = (worldX - effectsSliderX) / effectsSliderWidth;
                Config.EFFECTS_VOLUME = Math.max(0f, Math.min(1f, Config.EFFECTS_VOLUME));
            }
            // Checkbox fullscreen
            if (worldX >= fullscreenX && worldX <= fullscreenX + fullscreenWidth &&
                    worldY >= fullscreenY && worldY <= fullscreenY + fullscreenHeight) {
                Config.MAXIMIZED = !Config.MAXIMIZED;
                fullscreenEnabled = !fullscreenEnabled;


                if (Config.MAXIMIZED) {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                } else {
                    Gdx.graphics.setWindowedMode(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
                }
            }
            // Checkbox dois jogadores
            if (worldX >= twoPlayersX && worldX <= twoPlayersX + twoPlayersWidth &&
                    worldY >= twoPlayersY && worldY <= twoPlayersY + twoPlayersHeight) {
                Config.TWO_PLAYERS = !Config.TWO_PLAYERS;
            }
            // Botão voltar
            if (worldX >= backButtonX && worldX <= backButtonX + backButtonWidth &&
                    worldY >= backButtonY && worldY <= backButtonY + backButtonHeight) {
                ChefBoom.getInstance().setScreen(new MenuScreen());
            }
        }

        // Drag do mouse
        if (Gdx.input.isTouched()) {
            if (draggingMusic) {
                Config.MUSIC_VOLUME = (worldX - musicSliderX) / musicSliderWidth;
                Config.MUSIC_VOLUME = Math.max(0f, Math.min(1f, Config.MUSIC_VOLUME));
            }
            if (draggingEffects) {
                Config.EFFECTS_VOLUME = (worldX - effectsSliderX) / effectsSliderWidth;
                Config.EFFECTS_VOLUME = Math.max(0f, Math.min(1f, Config.EFFECTS_VOLUME));
            }
        } else {
            draggingMusic = false;
            draggingEffects = false;
        }
    }

    private void drawSlider(SpriteBatch batch, float x, float y, float width, float height, float value) {
        batch.setColor(Color.WHITE); // Reseta a cor para não tingir a textura

        // Barra de fundo com NinePatch
        sliderBackgroundNinePatch.draw(batch, x, y, width, height);

        float fillWidth = width * value;
        if (fillWidth > 0) {
            batch.flush();

            Rectangle scissors = new Rectangle();
            Rectangle clipBounds = new Rectangle(x, y, fillWidth, height);
            ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(), batch.getTransformMatrix(), clipBounds, scissors);

            if (ScissorStack.pushScissors(scissors)) {
                sliderFillNinePatch.draw(batch, x, y, width, height);

                batch.flush();
                ScissorStack.popScissors();
            }
        }

        // Porcentagem centralizada (código inalterado)
        int percent = Math.round(value * 100);
        String percentText = percent + "%";
        layout.setText(fontSlider, percentText);
        float textX = x + (width - layout.width) / 2f;
        float textY = y + ((height + layout.height) / 2f) + 4;
        fontSlider.draw(batch, percentText, textX, textY);
    }

    private void drawCheckbox(SpriteBatch batch, float x, float y, float width, float height, boolean checked) {
        batch.setColor(Color.WHITE);
        if (checked) {
            batch.draw(checkboxCheckedTexture, x, y, width, height);
        } else {
            batch.draw(checkboxUncheckedTexture, x, y, width, height);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}