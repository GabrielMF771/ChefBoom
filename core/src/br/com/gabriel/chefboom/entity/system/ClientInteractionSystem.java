package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.dictionary.Blocks;
import br.com.gabriel.chefboom.entity.component.*;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.util.ClientUtils;
import br.com.gabriel.chefboom.world.World;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class ClientInteractionSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> mPlayer;
    private ComponentMapper<TransformComponent> mTransform;
    private ComponentMapper<ClientComponent> mClient;
    private ComponentMapper<ItemComponent> mItem;
    private ComponentMapper<SpriteComponent> mSprite;

    private final World world;

    public ClientInteractionSystem(World world) {
        super(Aspect.all(PlayerComponent.class, TransformComponent.class));
        this.world = world;
    }

    @Override
    protected void process(int entityId) {
        PlayerComponent player = mPlayer.get(entityId);
        TransformComponent playerTransform = mTransform.get(entityId);

        if (Gdx.input.isKeyJustPressed(Input.Keys.E) && player.heldItemEntity != null) {
            int clientId = ClientUtils.findNearbyClient(
                    world.getArtemis(), mTransform, playerTransform.position
            );
            if (clientId != -1) {
                SpriteComponent itemSprite = mSprite.get(player.heldItemEntity);
                int itemTypeId;
                if (itemSprite.sprite.getTexture() == Assets.manager.get(Assets.apple)) {
                    itemTypeId = 0;
                } else if (itemSprite.sprite.getTexture() == Assets.manager.get(Assets.bread)) {
                    itemTypeId = 1;
                } else {
                    itemTypeId = -1; // tipo desconhecido
                }

                ClientComponent client = mClient.get(clientId);
                if (itemTypeId == client.wantedItemId) {
                    // Deleta o item do jogador

                    // TODO - TIRAR DO COMENTÁRIO QUANDO IMPLEMENTAR A LÓGICA DOS ITENS
                    //world.getArtemis().delete(player.heldItemEntity);
                    //player.heldItemEntity = null;

                    // Deleta o cliente que foi alimentado
                    world.getArtemis().delete(clientId);
                }
            }
        }
    }

    private int findNearbyClient(Vector2 pos) {
        IntBag clients = getClientEntities();
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

    private IntBag getClientEntities() {
        return getWorld().getAspectSubscriptionManager()
                .get(Aspect.all(ClientComponent.class, TransformComponent.class))
                .getEntities();
    }
}
