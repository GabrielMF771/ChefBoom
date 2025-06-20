package br.com.gabriel.chefboom.screen;

import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.entity.component.ClientComponent;
import br.com.gabriel.chefboom.entity.system.OrderSystem;
import br.com.gabriel.chefboom.hud.HudRenderer;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.world.World;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import br.com.gabriel.chefboom.world.CurrentLevel;

public class NextLevelScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = Config.SCREEN_WIDTH;
    private static final float WORLD_HEIGHT = Config.SCREEN_HEIGHT;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;

    private BitmapFont fontTitle;  // fonte para o título
    private GlyphLayout layoutTitle;

    private com.badlogic.gdx.graphics.Texture startButtonTexture;
    private com.badlogic.gdx.graphics.Texture NextLevelTexture;

    private float titleX, titleY, titleWidth, titleHeight;
    private float startButtonX, startButtonY, startButtonWidth, startButtonHeight;
    private float MenuButtonX, MenuButtonY, MenuButtonWidth, MenuButtonHeight;

    protected World world;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        batch = new SpriteBatch();

        int level = World.getLevel();

        // Cria um novo HudRenderer
        OrderSystem orderSystem = new OrderSystem();
        World world = new World(camera);
        ComponentMapper<ClientComponent> mClient = world.getArtemis().getMapper(ClientComponent.class);

        HudRenderer hudRenderer = new HudRenderer(orderSystem, world, mClient);
        hudRenderer.showLevelMessage(level);

        // Inicializa a fonte do título usando FreeTypeFontGenerator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Bold.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 80; // ajuste o tamanho para cobrir a área do título (ajuste conforme necessário)
        parameter.color = Color.WHITE;
        fontTitle = generator.generateFont(parameter);
        generator.dispose();

        layoutTitle = new GlyphLayout();

        startButtonTexture = Assets.manager.get(Assets.iniciarBotao);
        NextLevelTexture = Assets.manager.get(Assets.iniciarProximaFase);

        calculateDimensionsAndPositions();

    }

    private void calculateDimensionsAndPositions() {
        // Mesmas dimensões que usava para o título imagem
        titleWidth = WORLD_WIDTH / 2.5f;
        titleHeight = WORLD_HEIGHT / 3.5f;

        titleX = (WORLD_WIDTH - titleWidth) / 2.0f;
        titleY = WORLD_HEIGHT * 0.62f;

        startButtonWidth = WORLD_WIDTH / 6.0f;
        startButtonHeight = WORLD_HEIGHT / 8.0f;

        startButtonX = (WORLD_WIDTH - startButtonWidth) / 2.0f;
        startButtonY = WORLD_HEIGHT * 0.45f;

        MenuButtonWidth = WORLD_WIDTH / 6.0f;
        MenuButtonHeight = WORLD_HEIGHT / 8.0f;

        MenuButtonX = (WORLD_WIDTH - startButtonWidth) / 2.0f;
        MenuButtonY = WORLD_HEIGHT * 0.25f;


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
        String titleText = "NÍVEL CONCLUIDO!";
        layoutTitle.setText(fontTitle, titleText);

        // Calcula X para centralizar o texto dentro da área do título (titleX até titleX + titleWidth)
        float textX = titleX + (titleWidth - layoutTitle.width) / 2f;

        // Calcula Y para posicionar o baseline do texto para ficar na vertical da área do título
        float textY = titleY + (titleHeight + layoutTitle.height) / 2f;

        fontTitle.draw(batch, layoutTitle, textX, textY);

        // Desenha o botão iniciar
        if (CurrentLevel.getLevel() < 3){
            batch.draw(NextLevelTexture, startButtonX, startButtonY, startButtonWidth, startButtonHeight);
            batch.draw(startButtonTexture, MenuButtonX, MenuButtonY, MenuButtonWidth, MenuButtonHeight);
        } else {
            batch.draw(startButtonTexture, startButtonX, startButtonY - (MenuButtonY / 2), startButtonWidth, startButtonHeight);
        }

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

            if (touchedStartButton && CurrentLevel.getLevel() < 3) {
                    ChefBoom.getInstance().setScreen(new GameScreen());
            }

            boolean touchedMenuButton =
                    worldX >= MenuButtonX && worldX <= MenuButtonX + MenuButtonWidth &&
                            worldY >= MenuButtonY && worldY <= MenuButtonY + MenuButtonHeight;

            if (touchedMenuButton) {
                    ChefBoom.getInstance().setScreen(new MenuScreen());
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