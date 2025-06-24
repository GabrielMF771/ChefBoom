package br.com.gabriel.chefboom.screen;

import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.world.CurrentLevel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SelectLevelScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = Config.SCREEN_WIDTH;
    private static final float WORLD_HEIGHT = Config.SCREEN_HEIGHT;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;

    private BitmapFont fontTitle;
    private GlyphLayout layoutTitle;

    private Texture Level1ButtonTexture;
    private Texture Level2ButtonTexture;
    private Texture Level3ButtonTexture;
    private Texture InfiniteLevelButtonTexture;
    private Texture LevelLockedButtonTexture;
    private Texture MenuButtonTexture;

    private float titleX, titleY, titleWidth, titleHeight;
    private float Level1ButtonX, Level1ButtonY, Level1ButtonWidth, Level1ButtonHeight;
    private float Level2ButtonX, Level2ButtonY, Level2ButtonWidth, Level2ButtonHeight;
    private float Level3ButtonX, Level3ButtonY, Level3ButtonWidth, Level3ButtonHeight;
    private float InfiniteLevelButtonX, InfiniteLevelButtonY, InfiniteLevelButtonWidth, InfiniteLevelButtonHeight;
    private float MenuButtonX, MenuButtonY, MenuButtonWidth, MenuButtonHeight;

    protected br.com.gabriel.chefboom.world.World world;

    private Texture backgroundTexture;

    private static Music menuMusic;

    // Variáveis para fade
    private float fadeAlpha = 0f;
    private boolean fadingOut = false;
    private boolean startGameAfterFade = false;
    private int selectedLevel = -1;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        batch = new SpriteBatch();

        // Inicializa a fonte do título usando FreeTypeFontGenerator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/MinecraftRegular.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 60;
        parameter.color = Color.WHITE;
        parameter.shadowOffsetX = -6;
        parameter.shadowOffsetY = 6;
        parameter.shadowColor = Color.BLACK;
        fontTitle = generator.generateFont(parameter);
        generator.dispose();

        layoutTitle = new GlyphLayout();

        Level1ButtonTexture = Assets.manager.get(Assets.botaoFase1);
        Level2ButtonTexture = Assets.manager.get(Assets.botaoFase2);
        Level3ButtonTexture = Assets.manager.get(Assets.botaoFase3);
        InfiniteLevelButtonTexture = Assets.manager.get(Assets.botaoModoInfinito);
        LevelLockedButtonTexture = Assets.manager.get(Assets.botaoBloqueado);
        MenuButtonTexture = Assets.manager.get(Assets.botaoVoltarProMenu);

        backgroundTexture = Assets.manager.get(Assets.menuBackground);

        calculateDimensionsAndPositions();

        menuMusic = MenuScreen.getMenuMusic();
    }

    private void calculateDimensionsAndPositions() {
        titleWidth = WORLD_WIDTH / 2.5f;
        titleHeight = WORLD_HEIGHT / 1.5f;
        titleX = (WORLD_WIDTH - titleWidth) / 2.0f;
        titleY = WORLD_HEIGHT * 0.75f;

        Level1ButtonWidth = WORLD_WIDTH / 6.0f;
        Level1ButtonHeight = WORLD_HEIGHT / 8.0f;
        Level1ButtonX = (WORLD_WIDTH - Level1ButtonWidth) / 5.0f;
        Level1ButtonY = WORLD_HEIGHT * 0.45f;

        Level2ButtonWidth = WORLD_WIDTH / 6.0f;
        Level2ButtonHeight = WORLD_HEIGHT / 8.0f;
        Level2ButtonX = (WORLD_WIDTH - Level1ButtonWidth) / 2.0f;
        Level2ButtonY = WORLD_HEIGHT * 0.45f;

        Level3ButtonWidth = WORLD_WIDTH / 6.0f;
        Level3ButtonHeight = WORLD_HEIGHT / 8.0f;
        Level3ButtonX = (WORLD_WIDTH - Level1ButtonWidth) / 1.25f;
        Level3ButtonY = WORLD_HEIGHT * 0.45f;

        InfiniteLevelButtonWidth = WORLD_WIDTH / 6.0f;
        InfiniteLevelButtonHeight = WORLD_HEIGHT / 8.0f;
        InfiniteLevelButtonX = (WORLD_WIDTH - Level1ButtonWidth) / 2.0f;
        InfiniteLevelButtonY = WORLD_HEIGHT * 0.25f;

        MenuButtonWidth = WORLD_WIDTH / 6.0f;
        MenuButtonHeight = WORLD_HEIGHT / 8.0f;
        MenuButtonX = (WORLD_WIDTH - Level1ButtonWidth) / 2.0f;
        MenuButtonY = WORLD_HEIGHT * 0.05f;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        handleInput();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ChefBoom.getInstance().setScreen(new MenuScreen());
        }

        if (fadingOut) {
            fadeAlpha += delta * 2f;
            if (fadeAlpha >= 1f) {
                fadeAlpha = 1f;
                if (selectedLevel >= 0) {
                    CurrentLevel.setLevel(selectedLevel);
                    menuMusic.stop();
                    ChefBoom.getInstance().setScreen(new GameScreen());
                    return;
                }
            }
        } else if (fadeAlpha > 0f) {
            fadeAlpha -= delta * 2f;
            if (fadeAlpha < 0f) {
                fadeAlpha = 0f;
            }
        }

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Desenha o fundo do menu
        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // Desenha um retângulo preto semi-transparente sobre o fundo
        batch.setColor(0, 0, 0, 0.6f); // 40% opacidade
        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.setColor(1, 1, 1, 1); // Reseta a cor

        // Título
        String titleText = "SELECIONE A FASE";
        layoutTitle.setText(fontTitle, titleText);
        float textX = (WORLD_WIDTH - layoutTitle.width) / 2f;
        fontTitle.draw(batch, layoutTitle, textX, titleY);

        // Botão de Nível 1
        batch.draw(
                CurrentLevel.getMaxlevel() >= 0 ? Level1ButtonTexture : LevelLockedButtonTexture,
                Level1ButtonX, Level1ButtonY, Level1ButtonWidth, Level1ButtonHeight);

        // Botão de Nível 2
        batch.draw(
                CurrentLevel.getMaxlevel() >= 1 ? Level2ButtonTexture : LevelLockedButtonTexture,
                Level2ButtonX, Level2ButtonY, Level2ButtonWidth, Level2ButtonHeight);

        // Botão de Nível 3
        batch.draw(
                CurrentLevel.getMaxlevel() >= 2 ? Level3ButtonTexture : LevelLockedButtonTexture,
                Level3ButtonX, Level3ButtonY, Level3ButtonWidth, Level3ButtonHeight);

        // Botão de Nível Infinito
        batch.draw(
                CurrentLevel.getMaxlevel() >= 3 ? InfiniteLevelButtonTexture : LevelLockedButtonTexture,
                InfiniteLevelButtonX, InfiniteLevelButtonY, InfiniteLevelButtonWidth, InfiniteLevelButtonHeight);

        // Desenha o Botão Menu
        batch.draw(
                MenuButtonTexture,
                MenuButtonX, MenuButtonY, MenuButtonWidth, MenuButtonHeight);

        if (fadeAlpha > 0f) {
            Color originalColor = batch.getColor();
            batch.setColor(0, 0, 0, fadeAlpha);
            batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
            batch.setColor(originalColor);
        }

        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched() && !fadingOut) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.input.getY();

            com.badlogic.gdx.math.Vector3 touchPos = new com.badlogic.gdx.math.Vector3(touchX, touchY, 0);
            camera.unproject(touchPos, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

            float worldX = touchPos.x;
            float worldY = touchPos.y;

            // Botão Nível 1
            boolean Level1Button =
                    worldX >= Level1ButtonX && worldX <= Level1ButtonX + Level1ButtonWidth &&
                            worldY >= Level1ButtonY && worldY <= Level1ButtonY + Level1ButtonHeight;

            if (Level1Button && CurrentLevel.getMaxlevel() >= 0) {
                fadingOut = true;
                startGameAfterFade = true;
                selectedLevel = 0;
                return;
            }

            // Botão Nível 2
            boolean Level2Button =
                    worldX >= Level2ButtonX && worldX <= Level2ButtonX + Level2ButtonWidth &&
                            worldY >= Level2ButtonY && worldY <= Level2ButtonY + Level2ButtonHeight;

            if (Level2Button && CurrentLevel.getMaxlevel() >= 1) {
                fadingOut = true;
                startGameAfterFade = true;
                selectedLevel = 1;
                return;
            }

            // Botão Nível 3
            boolean Level3Button =
                    worldX >= Level3ButtonX && worldX <= Level3ButtonX + Level3ButtonWidth &&
                            worldY >= Level3ButtonY && worldY <= Level3ButtonY + Level3ButtonHeight;

            if (Level3Button && CurrentLevel.getMaxlevel() >= 2) {
                fadingOut = true;
                startGameAfterFade = true;
                selectedLevel = 2;
                return;
            }

            // Botão Nível Infinito
            boolean InfiniteLevelButton =
                    worldX >= InfiniteLevelButtonX && worldX <= InfiniteLevelButtonX + InfiniteLevelButtonWidth &&
                            worldY >= InfiniteLevelButtonY && worldY <= InfiniteLevelButtonY + InfiniteLevelButtonHeight;

            if (InfiniteLevelButton && CurrentLevel.getMaxlevel() >= 3) {
                fadingOut = true;
                startGameAfterFade = true;
                selectedLevel = 3;
                return;
            }

            // Botão Menu (Não tem fade)
            boolean touchedMenuButton =
                    worldX >= MenuButtonX && worldX <= MenuButtonX + MenuButtonWidth &&
                            worldY >= MenuButtonY && worldY <= MenuButtonY + MenuButtonHeight;

            if (touchedMenuButton) {
                ChefBoom.getInstance().setScreen(new MenuScreen());
            }
        }
    }
}