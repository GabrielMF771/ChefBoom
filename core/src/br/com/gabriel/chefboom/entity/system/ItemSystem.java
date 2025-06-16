package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.entity.EntitiesFactory;
import br.com.gabriel.chefboom.entity.component.*;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.world.World;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import br.com.gabriel.chefboom.util.ClientUtils;

public class ItemSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> mPlayer;
    private ComponentMapper<TransformComponent> mTransform;
    private ComponentMapper<ItemComponent> mItem;
    private ComponentMapper<SpriteComponent> mSprite;
    private ComponentMapper<InteractiveBlock> mInteractiveBlock;

    private final World world;
    private final EntitiesFactory entitiesFactory;

    private Sound readySound = Assets.manager.get(Assets.readySound);

    public ItemSystem(World world, EntitiesFactory entitiesFactory) {
        super(Aspect.all(PlayerComponent.class, TransformComponent.class));
        this.world = world;
        this.entitiesFactory = entitiesFactory;
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
        // Decrementa o timer apenas dos blocos ativos
        com.artemis.utils.IntBag entities = getWorld().getAspectSubscriptionManager()
                .get(Aspect.all(TransformComponent.class, InteractiveBlock.class))
                .getEntities();
        int[] ids = entities.getData();
        int size = entities.size();
        float delta = Gdx.graphics.getDeltaTime();

        for (int i = 0; i < size; i++) {
            InteractiveBlock block = mInteractiveBlock.get(ids[i]);
            if (block != null && block.timerActive && block.timeLeft > 0f) {
                block.timeLeft -= delta;
                if (block.timeLeft < 0f) block.timeLeft = 0f;

                // Quando chega a zero, gera o item e reseta/desativa o timer
                if (block.timeLeft == 0f) {
                    int x = World.worldToMap(mTransform.get(ids[i]).position.x);
                    int y = World.worldToMap(mTransform.get(ids[i]).position.y);
                    if (block.type == InteractiveBlock.Type.GRILL) {
                        createItemOnBlock(Assets.burguer.fileName, x, y);
                        readySound.play(Config.EFFECTS_VOLUME);
                        block.timeLeft = World.GRILLTIME;
                    } else if (block.type == InteractiveBlock.Type.SODAMACHINE) {
                        createItemOnBlock(Assets.soda.fileName, x, y);
                        readySound.play(Config.EFFECTS_VOLUME);
                        block.timeLeft = World.SODATIME;
                    } else if (block.type == InteractiveBlock.Type.FRIESMACHINE) {
                        createItemOnBlock(Assets.fries.fileName, x, y);
                        readySound.play(Config.EFFECTS_VOLUME);
                        block.timeLeft = World.FRIESTIME;
                    }
                    block.timerActive = false;
                }
            }
        }

        PlayerComponent cPlayer = mPlayer.get(entityId);
        TransformComponent cTransform = mTransform.get(entityId);
        SpriteComponent cSprite = mSprite.get(entityId);

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (cPlayer.heldItemEntity == null) {
                Vector2 frontBlock = getBlockInFront(cTransform, cSprite);
                int x = World.worldToMap(frontBlock.x);
                int y = World.worldToMap(frontBlock.y);

                boolean isGrill = isInteractiveBlockAt(x, y, InteractiveBlock.Type.GRILL);
                boolean isSodaMachine = isInteractiveBlockAt(x, y, InteractiveBlock.Type.SODAMACHINE);
                boolean isFriesMachine = isInteractiveBlockAt(x, y, InteractiveBlock.Type.FRIESMACHINE);

                // Busca o bloco interativo à frente para acessar o timer
                com.artemis.utils.IntBag blockEntities = getWorld().getAspectSubscriptionManager()
                        .get(Aspect.all(TransformComponent.class, InteractiveBlock.class))
                        .getEntities();
                int[] blockIds = blockEntities.getData();
                int blockSize = blockEntities.size();
                InteractiveBlock frontInteractiveBlock = null;
                for (int i = 0; i < blockSize; i++) {
                    int eid = blockIds[i];
                    TransformComponent t = mTransform.get(eid);
                    int bx = World.worldToMap(t.position.x);
                    int by = World.worldToMap(t.position.y);
                    if (bx == x && by == y) {
                        frontInteractiveBlock = mInteractiveBlock.get(eid);
                        break;
                    }
                }

                if (frontInteractiveBlock != null) {
                    if ((isGrill || isSodaMachine || isFriesMachine) && !isItemOnBlock(x, y)) {
                        if (!frontInteractiveBlock.timerActive) {
                            frontInteractiveBlock.timerActive = true;
                            if (frontInteractiveBlock.timeLeft == 0f) {
                                frontInteractiveBlock.timeLeft = 5f;
                            }
                        }
                    } else {
                        // Tenta pegar o item do bloco à frente
                        int itemId = findNearbyItem(cTransform, cSprite);
                        if (itemId != -1) {
                            cPlayer.heldItemEntity = itemId;
                            mItem.get(itemId).isHeld = true;
                        }
                    }
                }

            } else {
                // Colocar item no bloco à frente, se não houver cliente e não houver item no bloco
                int clientId = ClientUtils.findNearbyClient(
                        world.getArtemis(), mTransform, cTransform.position
                );
                if (clientId == -1) {
                    Vector2 placePos = getBlockInFront(cTransform, cSprite);
                    int x = World.worldToMap(placePos.x);
                    int y = World.worldToMap(placePos.y);

                    boolean isPlate = isInteractiveBlockAt(x, y, InteractiveBlock.Type.PLATE);
                    boolean isTrash = isInteractiveBlockAt(x, y, InteractiveBlock.Type.TRASH);

                    if (world.isValid(x, y) && !isItemOnBlock(x, y)) {
                        if (isPlate) {
                            TransformComponent itemTransform = mTransform.get(cPlayer.heldItemEntity);
                            itemTransform.position.set(x * Block.TILE_SIZE, y * Block.TILE_SIZE);
                            mItem.get(cPlayer.heldItemEntity).isHeld = false;
                            cPlayer.heldItemEntity = null;
                        } else if (isTrash) {
                            world.getArtemis().delete(cPlayer.heldItemEntity);
                            cPlayer.heldItemEntity = null;
                        }
                    }
                }
            }
        }

        // Mantém o item acima do jogador se estiver segurando
        if (cPlayer.heldItemEntity != null) {
            TransformComponent itemTransform = mTransform.get(cPlayer.heldItemEntity);
            itemTransform.position.set(
                    cTransform.position.x,
                    cTransform.position.y + Block.TILE_SIZE
            );
        }
    }

    private int findNearbyItem(TransformComponent playerTransform, SpriteComponent playerSprite) {
        // Calcula a posição centralizada e deslocada à frente do jogador
        float centerX = playerTransform.position.x + Block.TILE_SIZE / 2f;
        float centerY = playerTransform.position.y + Block.TILE_SIZE / 2f;
        float offset = Block.TILE_SIZE * 0.6f;

        float targetX = centerX;
        float targetY = centerY;

        String texturePath = playerSprite.sprite.getTexture().toString();

        if (texturePath.contains("frente")) { // olhando para baixo
            targetY -= offset;
        } else if (texturePath.contains("costas")) { // olhando para cima
            targetY += offset;
        } else if (texturePath.contains("direita")) { // olhando para direita
            targetX += offset;
        } else if (texturePath.contains("esquerda")) { // olhando para esquerda
            targetX -= offset;
        } else {
            targetY -= offset; // padrão: para baixo
        }

        // Procura item exatamente no bloco à frente
        int x = World.worldToMap(targetX);
        int y = World.worldToMap(targetY);

        return findItemOnBlock(x, y);
    }

    // Retorna a posição do bloco à frente do jogador, centralizada no grid
    private Vector2 getBlockInFront(TransformComponent playerTransform, SpriteComponent playerSprite) {
        float centerX = playerTransform.position.x + Block.TILE_SIZE / 2f;
        float centerY = playerTransform.position.y + Block.TILE_SIZE / 2f;

        float offset = Block.TILE_SIZE * 0.6f; // deslocamento para garantir que pegue o bloco à frente

        float targetX = centerX;
        float targetY = centerY;

        String texturePath = playerSprite.sprite.getTexture().toString();

        if (texturePath.contains("frente")) { // olhando para baixo
            targetY -= offset;
        } else if (texturePath.contains("costas")) { // olhando para cima
            targetY += offset;
        } else if (texturePath.contains("direita")) { // olhando para direita
            targetX += offset;
        } else if (texturePath.contains("esquerda")) { // olhando para esquerda
            targetX -= offset;
        } else {
            targetY -= offset; // padrão: para baixo
        }

        int mapX = World.worldToMap(targetX);
        int mapY = World.worldToMap(targetY);

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

    public static int randomItemByProbability(double[] chances) {
        double total = 0;
        for (double c : chances) total += c;
        double r = Math.random() * total;
        double acumulado = 0;
        for (int i = 0; i < chances.length; i++) {
            acumulado += chances[i];
            if (r < acumulado) return i;
        }
        return chances.length - 1;
    }

    private com.artemis.utils.IntBag getItemEntities() {
        return getWorld().getAspectSubscriptionManager()
                .get(Aspect.all(ItemComponent.class, TransformComponent.class))
                .getEntities();
    }

    private boolean isInteractiveBlockAt(int x, int y, InteractiveBlock.Type type) {
        com.artemis.utils.IntBag entities = getWorld().getAspectSubscriptionManager()
                .get(Aspect.all(TransformComponent.class, InteractiveBlock.class))
                .getEntities();
        int[] ids = entities.getData();
        int size = entities.size();

        for (int i = 0; i < size; i++) {
            int entityId = ids[i];
            TransformComponent transform = mTransform.get(entityId);
            InteractiveBlock interactiveBlock = mInteractiveBlock.get(entityId);
            int blockX = World.worldToMap(transform.position.x);
            int blockY = World.worldToMap(transform.position.y);
            if (blockX == x && blockY == y && interactiveBlock != null && interactiveBlock.type == type) {
                return true;
            }
        }
        return false;
    }

    private int createItemOnBlock(String assetPath, int x, int y) {
        com.badlogic.gdx.graphics.Texture texture = Assets.manager.get(assetPath);
        int itemId = entitiesFactory.createItem(
                world.getArtemis(),
                x * Block.TILE_SIZE,
                y * Block.TILE_SIZE,
                texture
        );
        mItem.get(itemId).isHeld = false;
        return itemId;
    }
}