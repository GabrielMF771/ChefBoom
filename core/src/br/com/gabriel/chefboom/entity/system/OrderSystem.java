package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.entity.component.ClientComponent;
import br.com.gabriel.chefboom.entity.component.TransformComponent;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;

public class OrderSystem extends IteratingSystem {

    private ComponentMapper<ClientComponent> mClient;
    private ComponentMapper<TransformComponent> mTransform;

    private final Array<Order> activeOrders = new Array<>();

    public static class Order {
        public int wantedItemId;
        public float x, y;

        public Order(int wantedItemId, float x, float y) {
            this.wantedItemId = wantedItemId;
            this.x = x;
            this.y = y;
        }
    }

    public OrderSystem() {
        super(Aspect.all(ClientComponent.class, TransformComponent.class));
    }

    @Override
    protected void begin() {
        activeOrders.clear();
    }

    @Override
    protected void process(int entityId) {
        ClientComponent client = mClient.get(entityId);
        TransformComponent transform = mTransform.get(entityId);

        // Só adiciona clientes ativos (por exemplo, que ainda não foram atendidos)
        if (client != null && transform != null) {
            activeOrders.add(new Order(client.wantedItemId, transform.position.x, transform.position.y));
        }
    }

    public Array<Order> getActiveOrders() {
        return activeOrders;
    }
}