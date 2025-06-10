package br.com.gabriel.chefboom.hud;

import br.com.gabriel.chefboom.world.World;
import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.entity.component.ClientComponent;
import br.com.gabriel.chefboom.entity.system.OrderSystem;
import br.com.gabriel.chefboom.resource.Assets;
import com.artemis.ComponentMapper;
import com.artemis.Aspect;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class HudRenderer {
    private final Texture hudImageTexture;
    private final OrderSystem orderSystem;
    private final World artemisWorld;
    private final ComponentMapper<ClientComponent> mClient;

    public HudRenderer(OrderSystem orderSystem, World artemisWorld, ComponentMapper<ClientComponent> mClient) {
        this.hudImageTexture = Assets.manager.get(Assets.hudbackground);
        this.orderSystem = orderSystem;
        this.artemisWorld = artemisWorld;
        this.mClient = mClient;
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
                Texture itemTexture = orderForSlot.wantedItemId == 0
                        ? Assets.manager.get(Assets.apple)
                        : Assets.manager.get(Assets.bread);
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
}