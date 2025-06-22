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

public class SelectLevelScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = Config.SCREEN_WIDTH;
    private static final float WORLD_HEIGHT = Config.SCREEN_HEIGHT;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;

    private BitmapFont fontTitle;  // fonte para o título
    private GlyphLayout layoutTitle;

    private com.badlogic.gdx.graphics.Texture Level1ButtonTexture;
    private com.badlogic.gdx.graphics.Texture Level2ButtonTexture;
    private com.badlogic.gdx.graphics.Texture Level3ButtonTexture;
    private com.badlogic.gdx.graphics.Texture InfiniteLevelButtonTexture;
    private com.badlogic.gdx.graphics.Texture LevelLockedButtonTexture;
    private com.badlogic.gdx.graphics.Texture MenuButtonTexture;


    private float titleX, titleY, titleWidth, titleHeight;
    private float Level1ButtonX, Level1ButtonY, Level1ButtonWidth, Level1ButtonHeight;
    private float Level2ButtonX, Level2ButtonY, Level2ButtonWidth, Level2ButtonHeight;
    private float Level3ButtonX, Level3ButtonY, Level3ButtonWidth, Level3ButtonHeight;
    private float InfiniteLevelButtonX, InfiniteLevelButtonY, InfiniteLevelButtonWidth, InfiniteLevelButtonHeight;
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

        LevelLockedButtonTexture = Assets.manager.get(Assets.Bloqueado);
        Level1ButtonTexture = Assets.manager.get(Assets.Fase1);
        Level2ButtonTexture = Assets.manager.get(Assets.Fase2);
        Level3ButtonTexture = Assets.manager.get(Assets.Fase3);
        InfiniteLevelButtonTexture = Assets.manager.get(Assets.ModoInfinito);
        MenuButtonTexture = Assets.manager.get(Assets.VoltarProMenu);

        calculateDimensionsAndPositions();

    }

    private void calculateDimensionsAndPositions() {
        // Mesmas dimensões que usava para o título imagem
        titleWidth = WORLD_WIDTH / 2.5f;
        titleHeight = WORLD_HEIGHT / 3.5f;

        titleX = (WORLD_WIDTH - titleWidth) / 2.0f;
        titleY = WORLD_HEIGHT * 0.62f;
        //---------------------------------
        Level1ButtonWidth = WORLD_WIDTH / 6.0f;
        Level1ButtonHeight = WORLD_HEIGHT / 8.0f;

        Level1ButtonX = (WORLD_WIDTH - Level1ButtonWidth) / 5.0f;
        Level1ButtonY = WORLD_HEIGHT * 0.45f;

        //---------------------------------
        Level2ButtonWidth = WORLD_WIDTH / 6.0f;
        Level2ButtonHeight = WORLD_HEIGHT / 8.0f;

        Level2ButtonX = (WORLD_WIDTH - Level1ButtonWidth) / 2.0f;
        Level2ButtonY = WORLD_HEIGHT * 0.45f;

        //---------------------------------
        Level3ButtonWidth = WORLD_WIDTH / 6.0f;
        Level3ButtonHeight = WORLD_HEIGHT / 8.0f;

        Level3ButtonX = (WORLD_WIDTH - Level1ButtonWidth) / 1.25f;
        Level3ButtonY = WORLD_HEIGHT * 0.45f;

        //---------------------------------
        InfiniteLevelButtonWidth = WORLD_WIDTH / 6.0f;
        InfiniteLevelButtonHeight = WORLD_HEIGHT / 8.0f;

        InfiniteLevelButtonX = (WORLD_WIDTH - Level1ButtonWidth) / 2.0f;
        InfiniteLevelButtonY = WORLD_HEIGHT * 0.25f;

        //---------------------------------
        MenuButtonWidth = WORLD_WIDTH / 6.0f;
        MenuButtonHeight = WORLD_HEIGHT / 8.0f;

        MenuButtonX = (WORLD_WIDTH - Level1ButtonWidth) / 2.0f;
        MenuButtonY = WORLD_HEIGHT * 0.05f;


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
        String titleText = "SELECIONE A FASE";
        layoutTitle.setText(fontTitle, titleText);

        // Calcula X para centralizar o texto dentro da área do título (titleX até titleX + titleWidth)
        float textX = titleX + (titleWidth - layoutTitle.width) / 2f;

        // Calcula Y para posicionar o baseline do texto para ficar na vertical da área do título
        float textY = titleY + (titleHeight + layoutTitle.height) / 2f;

        fontTitle.draw(batch, layoutTitle, textX, textY);


            if(CurrentLevel.getMaxlevel() >= 0) {
                batch.draw(Level1ButtonTexture, Level1ButtonX, Level1ButtonY, Level1ButtonWidth, Level1ButtonHeight);

                if(CurrentLevel.getMaxlevel() >= 1) {
                    batch.draw(Level2ButtonTexture, Level2ButtonX, Level2ButtonY, Level2ButtonWidth, Level2ButtonHeight);

                    if (CurrentLevel.getMaxlevel() >= 2) {
                        batch.draw(Level3ButtonTexture, Level3ButtonX, Level3ButtonY, Level3ButtonWidth, Level3ButtonHeight);

                        if (CurrentLevel.getMaxlevel() == 3) {
                            batch.draw(InfiniteLevelButtonTexture, InfiniteLevelButtonX, InfiniteLevelButtonY, InfiniteLevelButtonWidth, InfiniteLevelButtonHeight);

                        }else {
                            batch.draw(LevelLockedButtonTexture, InfiniteLevelButtonX, InfiniteLevelButtonY, InfiniteLevelButtonWidth, InfiniteLevelButtonHeight);
                        }
                    }else{
                     batch.draw(LevelLockedButtonTexture, Level3ButtonX, Level3ButtonY, Level3ButtonWidth, Level3ButtonHeight);
                     batch.draw(LevelLockedButtonTexture, InfiniteLevelButtonX, InfiniteLevelButtonY, InfiniteLevelButtonWidth, InfiniteLevelButtonHeight);
                    }

                }else {
                    batch.draw(LevelLockedButtonTexture, Level2ButtonX, Level2ButtonY, Level2ButtonWidth, Level2ButtonHeight);
                    batch.draw(LevelLockedButtonTexture, Level3ButtonX, Level3ButtonY, Level3ButtonWidth, Level3ButtonHeight);
                    batch.draw(LevelLockedButtonTexture, InfiniteLevelButtonX, InfiniteLevelButtonY, InfiniteLevelButtonWidth, InfiniteLevelButtonHeight);
                }
            }

            batch.draw(MenuButtonTexture, MenuButtonX, MenuButtonY, MenuButtonWidth, MenuButtonHeight);



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

            
            //----------------------------------
            boolean Level1Button =
                    worldX >= Level1ButtonX && worldX <= Level1ButtonX + Level1ButtonWidth &&
                            worldY >= Level1ButtonY && worldY <= Level1ButtonY + Level1ButtonHeight;

            if (Level1Button && CurrentLevel.getMaxlevel() >= 0) {
                    CurrentLevel.setLevel(0);
                    ChefBoom.getInstance().setScreen(new GameScreen());
                }

            //----------------------------------
            boolean Level2Button =
                    worldX >= Level2ButtonX && worldX <= Level2ButtonX + Level2ButtonWidth &&
                            worldY >= Level2ButtonY && worldY <= Level2ButtonY + Level2ButtonHeight;

            if (Level2Button && CurrentLevel.getMaxlevel() >= 1) {
                CurrentLevel.setLevel(1);
                ChefBoom.getInstance().setScreen(new GameScreen());
            }

            //----------------------------------
            boolean Level3Button =
                    worldX >= Level3ButtonX && worldX <= Level3ButtonX + Level3ButtonWidth &&
                            worldY >= Level3ButtonY && worldY <= Level3ButtonY + Level3ButtonHeight;

            if (Level3Button && CurrentLevel.getMaxlevel() >= 2) {
                CurrentLevel.setLevel(2);
                ChefBoom.getInstance().setScreen(new GameScreen());
            }
            //---------------------------------

            boolean InfiniteLevelButton =
                    worldX >= InfiniteLevelButtonX && worldX <= InfiniteLevelButtonX + InfiniteLevelButtonWidth &&
                            worldY >= InfiniteLevelButtonY && worldY <= InfiniteLevelButtonY + InfiniteLevelButtonHeight;

            if (InfiniteLevelButton && CurrentLevel.getMaxlevel() >= 3) {
                CurrentLevel.setLevel(3);
                ChefBoom.getInstance().setScreen(new GameScreen());
            }
            //---------------------------------


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
        Level1ButtonTexture.dispose();
    }
}