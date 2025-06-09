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

        // Desenha o fundo da HUD
        //batch.draw(hudImageTexture, 0, camera.viewportHeight - hudHeight, hudWidth, hudHeight);

        int maxSlots = 3;
        float itemSize = 2 * Block.TILE_SIZE;

        // IDs dos clientes fixos
        int[] clients = artemisWorld.getClients();

        Array<OrderSystem.Order> orders = orderSystem.getActiveOrders();

        for (int i = 0; i < maxSlots; i++) {
            float slotWidth = hudWidth / maxSlots;
            float x = i * slotWidth + (slotWidth - itemSize) / 2f;
            float y = camera.viewportHeight - hudHeight / 2f - itemSize / 2f;

            // Procura o pedido do cliente correspondente ao slot
            OrderSystem.Order orderForSlot = null;
            for (OrderSystem.Order order : orders) {
                if (i < clients.length) {
                    // Verifica se o pedido pertence ao cliente deste slot
                    ClientComponent client = mClient.get(clients[i]);
                    if (client != null && client.wantedItemId == order.wantedItemId) {
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
                // Desenhe o slot vazio (exemplo: um fundo cinza claro)
                batch.setColor(0.8f, 0.8f, 0.8f, 1f);
                batch.setColor(1f, 1f, 1f, 1f);
            }
        }
    }

    // Procura o clientId correspondente ao pedido (ajuste conforme sua lógica de identificação)
    private int findClientIdByOrder(OrderSystem.Order order) {
        com.artemis.utils.IntBag clients = artemisWorld.getArtemis()
                .getAspectSubscriptionManager()
                .get(Aspect.all(ClientComponent.class))
                .getEntities();
        int[] ids = clients.getData();
        int size = clients.size();
        for (int i = 0; i < size; i++) {
            int id = ids[i];
            ClientComponent client = mClient.get(id);
            if (client != null && client.wantedItemId == order.wantedItemId) {
                return id;
            }
        }
        return -1;
    }
}