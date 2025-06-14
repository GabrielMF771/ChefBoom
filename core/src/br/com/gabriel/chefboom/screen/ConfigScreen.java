package br.com.gabriel.chefboom.screen;

import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.Config;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class ConfigScreen extends ScreenAdapter {

    private Stage stage;
    private Skin skin;
    private Label musicLabel;
    private Label effectsLabel;
    private TextButton backButton;
    private BitmapFont robotoFont;

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Carrega a fonte Roboto com tamanho 24
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24; // tamanho da fonte em pixels
        robotoFont = generator.generateFont(parameter);
        generator.dispose();



        // Cria estilos com a fonte Roboto para labels e botões
        Label.LabelStyle robotoLabelStyle = new Label.LabelStyle();
        robotoLabelStyle.font = robotoFont;

        TextButton.TextButtonStyle robotoButtonStyle = new TextButton.TextButtonStyle();
        robotoButtonStyle.font = robotoFont;
        robotoButtonStyle.up = skin.getDrawable("default");
        robotoButtonStyle.down = skin.getDrawable("default-pressed");
        robotoButtonStyle.checked = skin.getDrawable("default");
        robotoButtonStyle.over = skin.getDrawable("default-over");

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Music Volume Label e Slider
        musicLabel = new Label("", robotoLabelStyle);
        updateMusicLabel();
        Slider musicSlider = new Slider(0f, 1f, 0.01f, false, skin);
        musicSlider.setValue(Config.MUSIC_VOLUME);
        musicSlider.addListener(event -> {
            Config.MUSIC_VOLUME = musicSlider.getValue();
            updateMusicLabel();
            return false;
        });

        // Effects Volume Label e Slider
        effectsLabel = new Label("", robotoLabelStyle);
        updateEffectsLabel();
        Slider effectsSlider = new Slider(0f, 1f, 0.01f, false, skin);
        effectsSlider.setValue(Config.EFFECTS_VOLUME);
        effectsSlider.addListener(event -> {
            Config.EFFECTS_VOLUME = effectsSlider.getValue();
            updateEffectsLabel();
            return false;
        });

        // Back Button com estilo da fonte Roboto
        backButton = new TextButton("Back", robotoButtonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ChefBoom.getInstance().setScreen(new MenuScreen());
            }
        });

        int labelWidth = 300;
        int sliderWidth = 600;

        table.add(musicLabel).width(labelWidth).left().padBottom(5);
        table.row();
        table.add(musicSlider).width(sliderWidth).left().padBottom(30);
        table.row();

        table.add(effectsLabel).width(labelWidth).left().padBottom(5);
        table.row();
        table.add(effectsSlider).width(sliderWidth).left().padBottom(30);
        table.row();

        table.add(backButton).colspan(2).width(150).center();
    }

    private void updateMusicLabel() {
        musicLabel.setText(String.format("Music Volume: %.0f%%", Config.MUSIC_VOLUME * 100));
    }

    private void updateEffectsLabel() {
        effectsLabel.setText(String.format("SFX Volume: %.0f%%", Config.EFFECTS_VOLUME * 100));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        robotoFont.dispose();
    }
}
