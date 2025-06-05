package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.entity.component.CollidableComponent;
import br.com.gabriel.chefboom.entity.component.ClientComponent;
import br.com.gabriel.chefboom.entity.component.RigidBodyComponent;
import br.com.gabriel.chefboom.entity.component.SpriteComponent;
import br.com.gabriel.chefboom.resource.Assets;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
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

        Gdx.input.setInputProcessor(new InputMultiplexer(new GameInputAdapter()));
    }

    @Override
    protected void initialize() {
        texDireita = Assets.manager.get(Assets.testeDireita);
    }

    @Override
    protected void process(int entityId) {
        ClientComponent cPlayer = mPlayer.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        SpriteComponent cSprite = mSprite.get(entityId);

        if (cPlayer.canWalk) {
            float speed = cPlayer.walkSpeed;

            // Eixo X - ELE SÓ ANDA PRA DIREITA
           if (moveRight) {
                cRigidBody.velocity.x = speed;
                cSprite.sprite.setTexture(texDireita);
            } else {
                cRigidBody.velocity.x = 0;
            }


        }
    }

    private class GameInputAdapter extends InputAdapter {

        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Input.Keys.RIGHT:
                case Input.Keys.D:
                    moveRight = true;
                    break;
            }
            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            switch (keycode) {
                case Input.Keys.RIGHT:
                case Input.Keys.D:
                    moveRight = false;
                    break;

            }
            return true;
        }
    }
}
