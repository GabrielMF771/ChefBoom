package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.entity.component.*;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.screen.MenuScreen;
import br.com.gabriel.chefboom.screen.YouLoseScreen;
import br.com.gabriel.chefboom.world.World;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;

public class ClientControllerSystem extends IteratingSystem {

    private ComponentMapper<ClientComponent> mClient;
    private ComponentMapper<SpriteComponent> mSprite;
    private ComponentMapper<RigidBodyComponent> mRigidBody;
    private ComponentMapper<CollidableComponent> mCollidable;
    private ComponentMapper<PlayerComponent> mPlayer;

    private boolean moveRight;
    private Texture texDireita;

    private int clientsExplodedThisFrame = 0;

    private final World gameWorld;

    public ClientControllerSystem(World gameWorld) {
        super(Aspect.all(ClientComponent.class, RigidBodyComponent.class, CollidableComponent.class));
        this.gameWorld = gameWorld;
    }

    @Override
    protected void begin() {
        clientsExplodedThisFrame = 0;
    }

    @Override
    protected void initialize() {
        texDireita = Assets.manager.get(Assets.playerDireita);
    }

    @Override
    protected void process(int entityId) {
        ClientComponent cClient = mClient.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        SpriteComponent cSprite = mSprite.get(entityId);

        if (cClient.canWalk) {
            float speed = cClient.walkSpeed;
            cRigidBody.velocity.x = speed;
            cSprite.sprite.setTexture(texDireita);
        }

        if (cClient.inQueue) {
            cClient.timeLeft -= world.getDelta();
            if (cClient.timeLeft < 0) cClient.timeLeft = 0;
            if (cClient.timeLeft <= 0) {
                // TODO - Fazer o cliente explodir
                clientsExplodedThisFrame++;
                getWorld().delete(entityId);
                //System.out.println("CLIENTE FOI EMBORA");
            }
        }
    }

    @Override
    protected void end() {
        if (clientsExplodedThisFrame > 0) {
            int playerId = gameWorld.getPlayer();
            PlayerComponent player = mPlayer.get(playerId);
            if (player != null && player.hp > 0 /*&& player.invulnerableTime <= 0*/) {
                player.hp -= clientsExplodedThisFrame;
                if (player.hp < 0) player.hp = 0;
                //player.invulnerableTime = 1.0f;
                if (player.hp == 0) {
                    ChefBoom.getInstance().setScreen(new YouLoseScreen());
                }
            }
        }
    }
}