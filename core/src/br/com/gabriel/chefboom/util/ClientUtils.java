package br.com.gabriel.chefboom.util;

import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.entity.component.ClientComponent;
import br.com.gabriel.chefboom.entity.component.TransformComponent;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.Aspect;
import com.badlogic.gdx.math.Vector2;

public class ClientUtils {
    public static int findNearbyClient(
            World artemisWorld,
            ComponentMapper<TransformComponent> mTransform,
            Vector2 pos
    ) {
        com.artemis.utils.IntBag clients = artemisWorld.getAspectSubscriptionManager()
                .get(Aspect.all(ClientComponent.class, TransformComponent.class))
                .getEntities();
        int[] ids = clients.getData();
        int size = clients.size();

        for (int i = 0; i < size; i++) {
            int clientId = ids[i];
            TransformComponent clientTransform = mTransform.get(clientId);

            float dx = Math.abs(pos.x - clientTransform.position.x);
            float dy = Math.abs(pos.y - clientTransform.position.y);

            if (dx <= 2 * Block.TILE_SIZE && dy <= 1.5f * Block.TILE_SIZE) {
                return clientId;
            }
        }
        return -1;
    }
}