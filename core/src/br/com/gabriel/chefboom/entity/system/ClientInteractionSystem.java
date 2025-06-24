package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.entity.component.*;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.util.ClientUtils;
import br.com.gabriel.chefboom.world.ClienteAtendidoThread;
import br.com.gabriel.chefboom.world.LevelEnded;
import br.com.gabriel.chefboom.world.World;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

public class ClientInteractionSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> mPlayer;
    private ComponentMapper<TransformComponent> mTransform;
    private ComponentMapper<ClientComponent> mClient;
    private ComponentMapper<ItemComponent> mItem;
    private ComponentMapper<SpriteComponent> mSprite;

    private final World world;

    private final Sound explosionSound = Assets.manager.get(Assets.explosionSound);

    private final Sound wrongSound = Assets.manager.get(Assets.wrongSound);

    public ClientInteractionSystem(World world) {
        super(Aspect.all(PlayerComponent.class, TransformComponent.class));
        this.world = world;
    }

    @Override
    protected void process(int entityId) {
        PlayerComponent cPlayer = mPlayer.get(entityId);
        TransformComponent cTransform = mTransform.get(entityId);

        boolean interactPressed = false;

        if (cPlayer.playerId == 0) {
            interactPressed = Gdx.input.isKeyJustPressed(Input.Keys.E);
        } else if (Config.TWO_PLAYERS && cPlayer.playerId == 1) {
            interactPressed = Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
        }

        if (interactPressed && cPlayer.heldItemEntity != null) {
            int clientId = ClientUtils.findNearbyClient(
                    world.getArtemis(), mTransform, cTransform.position
            );
            if (clientId != -1) {
                SpriteComponent itemSprite = mSprite.get(cPlayer.heldItemEntity);
                int itemTypeId;
                // TODO - Todo item novo que adicionar, deve ser adicionado aqui
                if (itemSprite.sprite.getTexture() == Assets.manager.get(Assets.burguer)) {
                    itemTypeId = 0;
                } else if (itemSprite.sprite.getTexture() == Assets.manager.get(Assets.fries)) {
                    itemTypeId = 1;
                } else if (itemSprite.sprite.getTexture() == Assets.manager.get(Assets.soda)) {
                    itemTypeId = 2;
                } else if (itemSprite.sprite.getTexture() == Assets.manager.get(Assets.donuts)) {
                        itemTypeId = 3;
                } else {
                    itemTypeId = -1; // tipo desconhecido
                }

                ClientComponent client = mClient.get(clientId);
                if (itemTypeId == client.wantedItemId) {
                    // Deleta o item do jogador
                    world.getArtemis().delete(cPlayer.heldItemEntity);
                    cPlayer.heldItemEntity = null;

                    // Lógica de explosão do cliente ao receber o item correto
                    client.isExploding = true;
                    client.inQueue = false; // Cliente não está mais na fila
                    client.explosionTimer = 0f; // Reseta o timer de explosão
                    client.explodedByServe = true; // Marca que o cliente explodiu por receber o item correto

                    //AUMENTA A QUANTIDADE DE CLIENTES ATENDIDOS
                    LevelEnded.setClientesAtendidos((LevelEnded.getClientesAtendidos() + 1 ));
                    ClienteAtendidoThread.notificarClienteAtendido();


                    // Troca o sprite para o primeiro frame da explosão
                    SpriteComponent clientSprite = mSprite.get(clientId);
                    if (clientSprite != null) {
                        clientSprite.sprite.setTexture(Assets.manager.get(Assets.explosao1));
                    }

                    explosionSound.play(Config.EFFECTS_VOLUME);
                } else {
                    wrongSound.play(Config.EFFECTS_VOLUME);
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
