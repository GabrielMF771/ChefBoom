package br.com.gabriel.chefboom.console;

import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.entity.EntitiesFactory;
import br.com.gabriel.chefboom.entity.component.ItemComponent;
import br.com.gabriel.chefboom.entity.component.PlayerComponent;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.screen.GameScreen;
import br.com.gabriel.chefboom.world.World;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class CommandExecutor {

    private final Map<String, Command> commands = new HashMap<>();
    private final ChefBoom game;
    private final ComponentMapper<PlayerComponent> mPlayer;
    private final ComponentMapper<ItemComponent> mItem;

    public CommandExecutor(ChefBoom game, ComponentMapper<PlayerComponent> mPlayer, ComponentMapper<ItemComponent> mItem) {
        this.game = game;
        this.mPlayer = mPlayer;
        this.mItem = mItem;
        registerCommands();
    }

    private void registerCommands() {
        commands.put("help", this::listCommands);
        commands.put("toggle_fps", this::toggleFps);
        commands.put("give", this::giveItem);
        commands.put("spawn_client", this::spawnClient);
        commands.put("toggle_multiplayer", this::toggleMultiplayer);
        commands.put("debug", this::toggleDebug);
    }

    public String execute(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String[] parts = input.trim().split("\\s+");
        String commandName = parts[0].toLowerCase();
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        Command command = commands.get(commandName);
        if (command != null) {
            try {
                return command.execute(args);
            } catch (Exception e) {
                return "Erro ao executar comando '" + commandName + "': " + e.getMessage();
            }
        }
        return "Comando desconhecido: '" + commandName + "'. Digite 'help' para ver a lista de comandos.";
    }

    private String listCommands(String[] args) {
        StringJoiner joiner = new StringJoiner(", ");
        commands.keySet().stream().sorted().forEach(joiner::add);
        return "Comandos disponiveis: " + joiner;
    }

    private String toggleFps(String[] args) {
        game.toggleShowFps();
        return "Exibicao de FPS " + (game.isShowingFps() ? "ativada." : "desativada.");
    }

    private String toggleMultiplayer(String[] args) {
        Config.TWO_PLAYERS = !Config.TWO_PLAYERS;
        String status = Config.TWO_PLAYERS ? "ativado" : "desativado";
        return "Modo 2 jogadores " + status + ". Reinicie o jogo para aplicar a mudanca.";
    }

    private String giveItem(String[] args) {
        if (args.length < 1) {
            return "Uso: give <item_name>. Itens: burguer, fries, soda.";
        }

        GameScreen gameScreen = game.getGameScreen();
        World world = gameScreen.getWorld();

        String itemName = args[0].toLowerCase();
        AssetDescriptor<Texture> itemAsset;

        switch (itemName) {
            case "burguer":
                itemAsset = Assets.burguer;
                break;
            case "fries":
                itemAsset = Assets.fries;
                break;
            case "soda":
                itemAsset = Assets.soda;
                break;
            default:
                return "Item desconhecido: '" + itemName + "'.";
        }

        if (!Assets.manager.isLoaded(itemAsset.fileName)) {
            return "Textura do item '" + itemName + "' nao carregada.";
        }
        Texture texture = Assets.manager.get(itemAsset);

        EntitiesFactory entitiesFactory = new EntitiesFactory();
        entitiesFactory.setWorld(world.getArtemis());

        int playerEntityId = world.getPlayer();
        if (playerEntityId == -1) {
            return "Jogador não encontrado.";
        }

        PlayerComponent cPlayer = mPlayer.get(playerEntityId);

        if (cPlayer.heldItemEntity != null) {
            world.getArtemis().delete(cPlayer.heldItemEntity);
        }

        int itemEntity = entitiesFactory.createItem(world.getArtemis(), 0, 0, texture);

        // Define o item como segurado
        ItemComponent cItem = mItem.get(itemEntity);
        cItem.isHeld = true;

        // Atribui o novo item ao jogador
        cPlayer.heldItemEntity = itemEntity;

        return "Item '" + itemName + "' criado.";
    }

    private String spawnClient(String[] args) {
        GameScreen gameScreen = game.getGameScreen();
        if (gameScreen == null) {
            return "Comando disponivel apenas durante o jogo.";
        }

        if (args.length != 1) {
            return "Uso: spawn_client <queue_id>";
        }

        try {
            int queueId = Integer.parseInt(args[0]);
            com.artemis.World artemisWorld = gameScreen.getWorld().getArtemis();
            EntitiesFactory entitiesFactory = new EntitiesFactory();
            artemisWorld.inject(entitiesFactory);

            int y;
            switch (queueId) {
                case 0:
                    y = 9;
                    break;
                case 1:
                    y = 6;
                    break;
                case 2:
                    y = 3;
                    break;
                default:
                    return "Fila invalida. Use 0, 1, ou 2.";
            }

            entitiesFactory.createClient(artemisWorld, -2 * Block.TILE_SIZE, y * Block.TILE_SIZE, queueId, true, 'r');
            return "Cliente spawnado na fila " + queueId;

        } catch (NumberFormatException e) {
            return "ID da fila invalido. Deve ser um numero.";
        } catch (Exception e) {
            return "Erro ao spawnar cliente: " + e.getMessage();
        }
    }

    private String toggleDebug(String[] args) {
        ChefBoom.DEBUG = !ChefBoom.DEBUG;
        return "Modo debug " + (ChefBoom.DEBUG ? "ativado." : "desativado.");
    }
}