package br.com.gabriel.chefboom.screen;

import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.resource.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CreditsScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = Config.SCREEN_WIDTH;
    private static final float WORLD_HEIGHT = Config.SCREEN_HEIGHT;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;

    private BitmapFont fontTitle;  // fonte para o título
    private BitmapFont fontNames; // fonte para os nomes dos desenvolvedores
    private GlyphLayout layoutTitle;
    private GlyphLayout layoutNames;

    private Texture TryAgainTexture, BackToMenuTexture;

    private float titleX, titleY, titleWidth, titleHeight;

    private Texture backgroundTexture;

    private final String[] developers = {
            "Andre Marcos de Souza Batista",
            "Gabriel Martins Fernandes",
            "Gustavo Henrique Sousa de Jesus",
            "Henrique Dantas Faria",
            "Joao Lucas Cavalcante Borges"
    };

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        batch = new SpriteBatch();

        // Inicializa a fonte do título usando FreeTypeFontGenerator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/MinecraftRegular.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.WHITE;
        parameter.shadowOffsetX = -6;
        parameter.shadowOffsetY = 6;
        parameter.shadowColor = Color.BLACK;
        fontTitle = generator.generateFont(parameter);
        fontNames = generator.generateFont(parameter);
        generator.dispose();

        layoutTitle = new GlyphLayout();
        layoutNames = new GlyphLayout();

        backgroundTexture = Assets.manager.get(Assets.menuBackground);

        calculateDimensionsAndPositions();
    }

    private void calculateDimensionsAndPositions() {
        // Mesmas dimensões que usava para o título imagem
        titleWidth = WORLD_WIDTH / 2.5f;
        titleHeight = WORLD_HEIGHT / 3.5f;

        titleX = (WORLD_WIDTH - titleWidth) / 2.0f;
        titleY = WORLD_HEIGHT - (titleHeight - 40f);
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
            ChefBoom.getInstance().setScreen(new MenuScreen());
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
        String titleText = "Creditos";
        layoutTitle.setText(fontTitle, titleText);

        // Calcula X para centralizar o texto dentro da área do título
        float textX = titleX + (titleWidth - layoutTitle.width) / 2f;

        // Calcula Y para posicionar o baseline do texto para ficar na vertical da área do título
        float textY = titleY + (titleHeight + layoutTitle.height) / 2f;

        fontTitle.draw(batch, layoutTitle, textX, textY);

        // Nomes centralizados
        float namesStartY = titleY - 80;
        for (int i = 0; i < developers.length; i++) {
            String name = developers[i];
            layoutNames.setText(fontNames, name);
            float nameX = (WORLD_WIDTH - layoutNames.width) / 2f;
            float nameY = namesStartY - i * 50;
            fontNames.draw(batch, layoutNames, nameX, nameY);
        }

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

            /*

            boolean touchedTryAgainButton =
                    worldX >= TryAgainButtonX && worldX <= TryAgainButtonX + TryAgainButtonWidth &&
                            worldY >= TryAgainButtonY && worldY <= TryAgainButtonY + TryAgainButtonHeight;

            if (touchedTryAgainButton) {
                ChefBoom.getInstance().setScreen(new GameScreen());
            }
             */

        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        fontTitle.dispose();
    }
}

