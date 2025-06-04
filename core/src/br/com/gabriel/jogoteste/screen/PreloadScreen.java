package br.com.gabriel.jogoteste.screen;

import br.com.gabriel.jogoteste.JogoTeste;
import br.com.gabriel.jogoteste.resource.Assets;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class PreloadScreen extends ScreenAdapter {

    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Carrega a fonte TTF e configura parâmetros
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Bold.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 36; // tamanho da fonte
        parameter.color = Color.WHITE; // cor da fonte
        parameter.borderWidth = 2; // borda para destaque
        parameter.borderColor = Color.BLACK;

        font = generator.generateFont(parameter);
        generator.dispose();

        layout = new GlyphLayout();  // Para medir o texto

        Assets.load();
    }

    @Override
    public void render(float delta) {
        if (Assets.manager.update()) {
            JogoTeste.getInstance().setScreen(new MenuScreen());
        } else {
            String text = "Loading...";
            layout.setText(font, text);

            float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
            float y = (Gdx.graphics.getHeight() + layout.height) / 2f;

            batch.begin();
            font.draw(batch, layout, x, y);
            batch.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
