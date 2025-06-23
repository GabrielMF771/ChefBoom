package br.com.gabriel.chefboom.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class DevConsole {

    private final Stage stage;
    private final Skin skin;
    private CommandExecutor commandExecutor;
    private final Label logLabel;
    private final ScrollPane scrollPane;
    private final TextField textField;
    private final Table table;
    private boolean visible = false;
    private final Array<String> commandHistory = new Array<>();
    private int historyPointer = 0;

    public DevConsole(CommandExecutor commandExecutor) {
        stage = new Stage(new ScreenViewport());
        skin = createBasicSkin();
        this.commandExecutor = commandExecutor;

        TextField.TextFieldStyle textFieldStyle = skin.get(TextField.TextFieldStyle.class);
        textField = new TextField("", textFieldStyle);
        textField.setTextFieldListener((field, c) -> {
            if (c == '\n' || c == '\r') {
                String input = textField.getText().trim();
                if (!input.isEmpty()) {
                    log("> " + input);
                    if(this.commandExecutor != null){
                        String output = this.commandExecutor.execute(input);
                        if (output != null && !output.isEmpty()) {
                            log(output);
                        }
                    } else {
                        log("Os comandos ainda não estão disponíveis. Entre na tela de jogo.");
                    }

                    // Adiciona ao histórico se não for o mesmo que o último comando
                    if (commandHistory.size == 0 || !commandHistory.peek().equals(input)) {
                        commandHistory.add(input);
                    }
                    historyPointer = commandHistory.size; // Reseta o ponteiro para o final

                    textField.setText("");
                }
            }
        });
        textField.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.UP) {
                    if (commandHistory.size > 0) {
                        // Move o ponteiro para trás, sem passar do início
                        historyPointer = Math.max(0, historyPointer - 1);
                        textField.setText(commandHistory.get(historyPointer));
                        // Move o cursor para o final do texto
                        textField.setCursorPosition(textField.getText().length());
                    }
                    return true; // Evento consumido
                }
                if (keycode == Input.Keys.DOWN) {
                    if (historyPointer < commandHistory.size - 1) {
                        // Move o ponteiro para frente
                        historyPointer++;
                        textField.setText(commandHistory.get(historyPointer));
                        textField.setCursorPosition(textField.getText().length());
                    } else {
                        // Se já estiver no final, limpa o campo de texto
                        historyPointer = commandHistory.size;
                        textField.setText("");
                    }
                    return true; // Evento consumido
                }
                return false; // Outras teclas não são consumidas
            }
        });

        logLabel = new Label("", skin);
        logLabel.setWrap(true);

        scrollPane = new ScrollPane(logLabel, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // Desabilita rolagem horizontal

        table = new Table();
        // table.setFillParent(true); // Removido para permitir posicionamento manual
        stage.addActor(table);

        table.add(scrollPane).expand().fill().row();
        table.add(textField).expandX().fillX();
        table.bottom();
    }

    public void log(String message) {
        String currentLog = logLabel.getText().toString();
        String newLog = currentLog + message + "\n";
        logLabel.setText(newLog);

        stage.getViewport().apply();
        scrollPane.layout();
        scrollPane.setScrollPercentY(1.0f);
    }

    public void render() {
        if (!visible) return;
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        // Define o tamanho da tabela do console
        float consoleHeight = height / 3f;
        float consoleWidth = width / 3f;
        table.setSize(consoleWidth, consoleHeight);

        // Posiciona a tabela na parte inferior da tela
        table.setPosition(0, 0);
    }

    public void toggle() {
        visible = !visible;
        if (visible) {
            Gdx.input.setInputProcessor(stage);
            stage.setKeyboardFocus(textField);
        } else {
            Gdx.input.setInputProcessor(null);
        }
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private Skin createBasicSkin() {
        Skin skin = new Skin();

        // Font
        BitmapFont font = new BitmapFont();
        skin.add("default", font);

        // Crie uma textura branca de 1x1 e adicione ao skin
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        // Crie um drawable com fundo preto translúcido
        Drawable translucentBackground = skin.newDrawable("white", new Color(0, 0, 0, 0.5f));

        // TextField style
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = translucentBackground;
        textFieldStyle.cursor = skin.newDrawable("white", Color.WHITE);
        textFieldStyle.selection = skin.newDrawable("white", Color.LIGHT_GRAY);
        skin.add("default", textFieldStyle);

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // ScrollPane style
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = translucentBackground; // Define o fundo translúcido
        skin.add("default", scrollPaneStyle);

        return skin;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
        System.out.println("CommandExecutor setado no DevConsole: " + (commandExecutor != null));
    }
}