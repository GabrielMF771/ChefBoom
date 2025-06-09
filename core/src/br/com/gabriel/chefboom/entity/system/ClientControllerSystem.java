package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.entity.component.CollidableComponent;
import br.com.gabriel.chefboom.entity.component.ClientComponent;
import br.com.gabriel.chefboom.entity.component.RigidBodyComponent;
import br.com.gabriel.chefboom.entity.component.SpriteComponent;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.screen.MenuScreen;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;

public class ClientControllerSystem extends IteratingSystem {

    private ComponentMapper<ClientComponent> mPlayer;

    private ComponentMapper<SpriteComponent> mSprite;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private ComponentMapper<CollidableComponent> mCollidable;

    private boolean moveRight;

    private Texture texDireita;

    public ClientControllerSystem() {
        super(Aspect.all(ClientComponent.class, RigidBodyComponent.class, CollidableComponent.class));
    }

    @Override
    protected void initialize() {
        texDireita = Assets.manager.get(Assets.playerDireita);
    }

    @Override
    protected void process(int entityId) {
        ClientComponent cPlayer = mPlayer.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        SpriteComponent cSprite = mSprite.get(entityId);

        if (cPlayer.canWalk) {
            float speed = cPlayer.walkSpeed;

            // Move sempre para a direita
            cRigidBody.velocity.x = speed;
            cSprite.sprite.setTexture(texDireita);
        }

        if (cPlayer.inQueue) {
            cPlayer.timeLeft -= world.getDelta();
            if (cPlayer.timeLeft < 0) cPlayer.timeLeft = 0;
            if (cPlayer.timeLeft <= 0) {
                ChefBoom.getInstance().setScreen(new MenuScreen());
            }
        }
    }
}
