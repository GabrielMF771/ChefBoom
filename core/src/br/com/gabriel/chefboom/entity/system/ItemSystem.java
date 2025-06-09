package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.block.BlockBarrier;
import br.com.gabriel.chefboom.entity.component.*;
import br.com.gabriel.chefboom.world.World;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import br.com.gabriel.chefboom.util.ClientUtils;

public class ItemSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> mPlayer;
    private ComponentMapper<TransformComponent> mTransform;
    private ComponentMapper<ItemComponent> mItem;
    private ComponentMapper<SpriteComponent> mSprite;

    private final World world;

    public ItemSystem(World world) {
        super(Aspect.all(PlayerComponent.class, TransformComponent.class));
        this.world = world;
    }

    private int findNearbyClient(Vector2 pos) {
        com.artemis.utils.IntBag clients = getWorld().getAspectSubscriptionManager()
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

    @Override
    protected void process(int entityId) {
        PlayerComponent player = mPlayer.get(entityId);
        TransformComponent playerTransform = mTransform.get(entityId);
        SpriteComponent playerSprite = mSprite.get(entityId);

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (player.heldItemEntity == null) {
                Vector2 frontBlock = getBlockInFront(playerTransform, playerSprite);
                int x = World.worldToMap(frontBlock.x);
                int y = World.worldToMap(frontBlock.y);

                int itemId = findNearbyItem(frontBlock);
                if (itemId != -1) {
                    player.heldItemEntity = itemId;
                    mItem.get(itemId).isHeld = true;
                }
            } else {
                // Colocar item no bloco à frente, se não houver cliente e não houver item no bloco
                int clientId = ClientUtils.findNearbyClient(
                        world.getArtemis(), mTransform, playerTransform.position
                );
                if (clientId == -1) {
                    Vector2 placePos = getBlockInFront(playerTransform, playerSprite);
                    int x = World.worldToMap(placePos.x);
                    int y = World.worldToMap(placePos.y);

                    if (world.isValid(x, y) && !isItemOnBlock(x, y)) {
                        Block block = world.getBlock(x, y, World.FG);
                        if (block instanceof BlockBarrier) {
                            TransformComponent itemTransform = mTransform.get(player.heldItemEntity);
                            itemTransform.position.set(x * Block.TILE_SIZE, y * Block.TILE_SIZE);
                            mItem.get(player.heldItemEntity).isHeld = false;
                            player.heldItemEntity = null;
                        }
                    }
                }
            }
        }

        // Mantém o item acima do jogador se estiver segurando
        if (player.heldItemEntity != null) {
            TransformComponent itemTransform = mTransform.get(player.heldItemEntity);
            itemTransform.position.set(
                    playerTransform.position.x,
                    playerTransform.position.y + Block.TILE_SIZE
            );
        }
    }

    private int findNearbyItem(Vector2 playerPos) {
        com.artemis.utils.IntBag entities = getItemEntities();
        int[] ids = entities.getData();
        int size = entities.size();

        for (int i = 0; i < size; i++) {
            int itemId = ids[i];
            TransformComponent itemTransform = mTransform.get(itemId);
            ItemComponent item = mItem.get(itemId);
            if (!item.isHeld && playerPos.dst(itemTransform.position) < Block.TILE_SIZE * 1.5f) {
                return itemId;
            }
        }
        return -1;
    }

    // Retorna a posição do bloco à frente do jogador, centralizada no grid
    private Vector2 getBlockInFront(TransformComponent playerTransform, SpriteComponent playerSprite) {
        float x = playerTransform.position.x;
        float y = playerTransform.position.y;

        int mapX = World.worldToMap(x);
        int mapY = World.worldToMap(y);

        String texturePath = playerSprite.sprite.getTexture().toString();

        if (texturePath.contains("frente")) { // olhando para baixo
            mapY -= 1;
        } else if (texturePath.contains("costas")) { // olhando para cima
            mapY += 1;
        } else if (texturePath.contains("direita")) { // olhando para direita
            mapX += 1;
        } else if (texturePath.contains("esquerda")) { // olhando para esquerda
            mapX -= 1;
        } else {
            mapY -= 1; // padrão: para baixo
        }

        // Retorna a posição centralizada do bloco alvo
        return new Vector2(mapX * Block.TILE_SIZE, mapY * Block.TILE_SIZE);
    }

    private int findItemOnBlock(int x, int y) {
        com.artemis.utils.IntBag entities = getItemEntities();
        int[] ids = entities.getData();
        int size = entities.size();

        for (int i = 0; i < size; i++) {
            int itemId = ids[i];
            TransformComponent itemTransform = mTransform.get(itemId);
            ItemComponent item = mItem.get(itemId);
            if (!item.isHeld) {
                int itemX = World.worldToMap(itemTransform.position.x);
                int itemY = World.worldToMap(itemTransform.position.y);
                if (itemX == x && itemY == y) {
                    return itemId;
                }
            }
        }
        return -1;
    }

    private boolean isItemOnBlock(int x, int y) {
        com.artemis.utils.IntBag entities = getItemEntities();
        int[] ids = entities.getData();
        int size = entities.size();

        for (int i = 0; i < size; i++) {
            int itemId = ids[i];
            TransformComponent itemTransform = mTransform.get(itemId);
            ItemComponent item = mItem.get(itemId);
            if (!item.isHeld) {
                int itemX = World.worldToMap(itemTransform.position.x);
                int itemY = World.worldToMap(itemTransform.position.y);
                if (itemX == x && itemY == y) {
                    return true;
                }
            }
        }
        return false;
    }

    private com.artemis.utils.IntBag getItemEntities() {
        return getWorld().getAspectSubscriptionManager()
                .get(Aspect.all(ItemComponent.class, TransformComponent.class))
                .getEntities();
    }
}