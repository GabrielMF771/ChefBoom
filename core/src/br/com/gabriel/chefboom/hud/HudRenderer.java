package br.com.gabriel.chefboom.hud;

import br.com.gabriel.chefboom.world.World;
import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.entity.component.ClientComponent;
import br.com.gabriel.chefboom.entity.system.OrderSystem;
import br.com.gabriel.chefboom.resource.Assets;
import com.artemis.ComponentMapper;
import com.artemis.Aspect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;

public class HudRenderer {
    private final ComponentMapper<ClientComponent> mClient;
    private final Texture hudImageTexture;
    private final OrderSystem orderSystem;
    private final World artemisWorld;

    // Título da fase
    private static final float LEVEL_MESSAGE_DURATION = 5f;
    private boolean levelMessageActive = true;
    private String levelMessage = "";
    private BitmapFont font;

    public HudRenderer(OrderSystem orderSystem, World artemisWorld, ComponentMapper<ClientComponent> mClient) {
        this.hudImageTexture = Assets.manager.get(Assets.hudBackground);
        this.orderSystem = orderSystem;
        this.artemisWorld = artemisWorld;
        this.mClient = mClient;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        float hudHeight = 3 * Block.TILE_SIZE;
        float hudWidth = camera.viewportWidth;

        batch.draw(hudImageTexture, 0, camera.viewportHeight - hudHeight, hudWidth, hudHeight);

        int maxSlots = 3;
        float itemSize = 2 * Block.TILE_SIZE;

        Array<OrderSystem.Order> orders = orderSystem.getActiveOrders();

        for (int i = 0; i < maxSlots; i++) {
            float slotWidth = hudWidth / maxSlots;
            float x = i * slotWidth + (slotWidth - itemSize) / 2f;
            float y = camera.viewportHeight - hudHeight / 2f - itemSize / 2f;

            OrderSystem.Order orderForSlot = null;
            for (OrderSystem.Order order : orders) {
                int clientId = findClientIdByOrder(order, i);

                if (clientId != -1) {
                    ClientComponent client = mClient.get(clientId);
                    if (client != null && client.queueId == i && client.inQueue) {
                        orderForSlot = order;
                        break;
                    }
                }
            }

            if (orderForSlot != null) {
                Texture itemTexture;
                switch (orderForSlot.wantedItemId) {
                    case 0:
                        itemTexture = Assets.manager.get(Assets.burguer);
                        break;
                    case 1:
                        itemTexture = Assets.manager.get(Assets.fries);
                        break;
                    default:
                        itemTexture = Assets.manager.get(Assets.soda);
                        break;
                }
                batch.draw(itemTexture, x, y, itemSize, itemSize);
            } else {
                batch.setColor(1, 1, 1, 1);
            }
        }

        // Vidas

        Texture heartTexture = Assets.manager.get(Assets.heart);
        int playerId = artemisWorld.getPlayer();
        com.artemis.ComponentMapper<br.com.gabriel.chefboom.entity.component.PlayerComponent> mPlayer =
                artemisWorld.getArtemis().getMapper(br.com.gabriel.chefboom.entity.component.PlayerComponent.class);
        br.com.gabriel.chefboom.entity.component.PlayerComponent player = mPlayer.get(playerId);

        int maxHearts = 3;
        float heartSize = Block.TILE_SIZE * 1.5f;
        float heartPadding = Block.TILE_SIZE * 0.2f;
        float baseX = heartPadding;
        float baseY = heartPadding;

        for (int i = 0; i < maxHearts; i++) {
            float x = baseX + i * (heartSize + heartPadding);
            float y = baseY;
            if (player != null && i < player.hp) {
                batch.draw(heartTexture, x, y, heartSize, heartSize);
            } else {
                // Desenha coração "vazio"
                batch.setColor(1, 1, 1, 0.3f);
                batch.draw(heartTexture, x, y, heartSize, heartSize);
                batch.setColor(1, 1, 1, 1);
            }
        }

        // Mensagem de nível
        if (levelMessageActive) {
            font.getData().setScale(2f);
            font.setColor(1, 1, 1, 1);

            com.badlogic.gdx.graphics.g2d.GlyphLayout layout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(font, levelMessage);
            float x = (camera.viewportWidth - layout.width) / 2f;
            float y = camera.viewportHeight - hudHeight - 15;

            font.draw(batch, levelMessage, x, y);
        }
    }

    // Procura o clientId correspondente ao pedido
    private int findClientIdByOrder(OrderSystem.Order order, int queueId) {
        com.artemis.utils.IntBag clients = artemisWorld.getArtemis()
                .getAspectSubscriptionManager()
                .get(Aspect.all(ClientComponent.class))
                .getEntities();
        int[] ids = clients.getData();
        int size = clients.size();
        for (int i = 0; i < size; i++) {
            int id = ids[i];
            ClientComponent client = mClient.get(id);
            if (client != null && client.wantedItemId == order.wantedItemId
                    && client.queueId == queueId && client.inQueue) { // Check if client is at the counter
                return id;
            }
        }
        return -1;
    }

    public void showLevelMessage(int level) {
        if(level == 3){
            levelMessage = "Nível Infinito";
        } else {
            levelMessage = "Nível: " + (level + 1);
        }

        levelMessageActive = true;
    }
}