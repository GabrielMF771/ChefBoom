package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.entity.EntitiesFactory;
import br.com.gabriel.chefboom.entity.component.InteractiveBlock;
import br.com.gabriel.chefboom.entity.component.TransformComponent;
import br.com.gabriel.chefboom.entity.component.ItemComponent;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.world.World;
import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.audio.Sound;

public class BlockTimerSystem extends BaseSystem {

    private ComponentMapper<TransformComponent> mTransform;
    private ComponentMapper<InteractiveBlock> mInteractiveBlock;
    private ComponentMapper<ItemComponent> mItem;

    private final World world;
    private final EntitiesFactory entitiesFactory;
    private final Sound readySound = Assets.manager.get(Assets.readySound);

    public BlockTimerSystem(World world) {
        this.world = world;
        this.entitiesFactory = new EntitiesFactory();
    }

    @Override
    protected void processSystem() {
        com.artemis.utils.IntBag entities = world.getArtemis().getAspectSubscriptionManager()
                .get(Aspect.all(TransformComponent.class, InteractiveBlock.class))
                .getEntities();
        int[] ids = entities.getData();
        int size = entities.size();
        float delta = world.getArtemis().getDelta();

        for (int i = 0; i < size; i++) {
            InteractiveBlock block = mInteractiveBlock.get(ids[i]);
            if (block != null && block.timerActive && block.timeLeft > 0f) {
                block.timeLeft -= delta;
                if (block.timeLeft < 0f) block.timeLeft = 0f;

                if (block.timeLeft == 0f) {
                    TransformComponent t = mTransform.get(ids[i]);
                    int x = World.worldToMap(t.position.x);
                    int y = World.worldToMap(t.position.y);

                    // Só cria o item se não houver item no bloco
                    if (!isItemOnBlock(x, y)) {
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
                        } else if (block.type == InteractiveBlock.Type.DONUTSMACHINE) {
                            createItemOnBlock(Assets.donuts.fileName, x, y);
                            readySound.play(Config.EFFECTS_VOLUME);
                            block.timeLeft = World.DONUTSTIME;
                        }
                    }
                    block.timerActive = false;
                }
            }
        }
    }

    private boolean isItemOnBlock(int x, int y) {
        com.artemis.utils.IntBag entities = world.getArtemis().getAspectSubscriptionManager()
                .get(Aspect.all(ItemComponent.class, TransformComponent.class))
                .getEntities();
        int[] ids = entities.getData();
        int size = entities.size();

        for (int i = 0; i < size; i++) {
            TransformComponent itemTransform = mTransform.get(ids[i]);
            ItemComponent item = mItem.get(ids[i]);
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

    private void createItemOnBlock(String assetPath, int x, int y) {
        com.badlogic.gdx.graphics.Texture texture = Assets.manager.get(assetPath);
        int itemId = entitiesFactory.createItem(
                world.getArtemis(),
                x * br.com.gabriel.chefboom.block.Block.TILE_SIZE,
                y * br.com.gabriel.chefboom.block.Block.TILE_SIZE,
                texture
        );
        mItem.get(itemId).isHeld = false;
    }
}