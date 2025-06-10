package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.entity.component.CollidableComponent;
import br.com.gabriel.chefboom.entity.component.PlayerComponent;
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

public class PlayerControllerSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> mPlayer;

    private ComponentMapper<SpriteComponent> mSprite;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private ComponentMapper<CollidableComponent> mCollidable;

    private boolean moveRight;
    private boolean moveLeft;
    private boolean moveUp;
    private boolean moveDown;
    private boolean run;

    private Texture texFrente;
    private Texture texCostas;
    private Texture texDireita;
    private Texture texEsquerda;

    public PlayerControllerSystem() {
        super(Aspect.all(PlayerComponent.class, RigidBodyComponent.class, CollidableComponent.class));

        Gdx.input.setInputProcessor(new InputMultiplexer(new GameInputAdapter()));
    }

    @Override
    protected void initialize() {
        texFrente = Assets.manager.get(Assets.playerFrente);
        texCostas = Assets.manager.get(Assets.playerCostas);
        texDireita = Assets.manager.get(Assets.playerDireita);
        texEsquerda = Assets.manager.get(Assets.playerEsquerda);
    }

    @Override
    protected void process(int entityId) {
        PlayerComponent cPlayer = mPlayer.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        SpriteComponent cSprite = mSprite.get(entityId);

        /*
        if (cPlayer.invulnerableTime > 0f) {
            cPlayer.invulnerableTime -= world.getDelta();
            if (cPlayer.invulnerableTime < 0f) cPlayer.invulnerableTime = 0f;
        }
         */

        if (cPlayer.canWalk) {
            float speed = cPlayer.walkSpeed;
            if (run) speed += 70;

            boolean movingX = moveRight ^ moveLeft;
            boolean movingY = moveUp ^ moveDown;

            if (movingX && movingY) {
                speed *= 0.7071f;
                if (speed < 0) speed = 0;
            }

            // Eixo X
            if (moveRight && moveLeft) {
                cRigidBody.velocity.x = 0;
            } else if (moveRight) {
                cRigidBody.velocity.x = speed;
                cSprite.sprite.setTexture(texDireita);
            } else if (moveLeft) {
                cRigidBody.velocity.x = -speed;
                cSprite.sprite.setTexture(texEsquerda);
            } else {
                cRigidBody.velocity.x = 0;
            }

            // Eixo Y
            if (moveUp && moveDown) {
                cRigidBody.velocity.y = 0;
            } else if (moveUp) {
                cRigidBody.velocity.y = speed;
                cSprite.sprite.setTexture(texCostas);
            } else if (moveDown) {
                cRigidBody.velocity.y = -speed;
                cSprite.sprite.setTexture(texFrente);
            } else {
                cRigidBody.velocity.y = 0;
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

                case Input.Keys.LEFT:
                case Input.Keys.A:
                    moveLeft = true;
                    break;

                case Input.Keys.UP:
                case Input.Keys.W:
                    moveUp = true;
                    break;

                case Input.Keys.DOWN:
                case Input.Keys.S:
                    moveDown = true;
                    break;
                case Input.Keys.SHIFT_LEFT:
                case Input.Keys.SHIFT_RIGHT:
                    run = true;
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

                case Input.Keys.LEFT:
                case Input.Keys.A:
                    moveLeft = false;
                    break;

                case Input.Keys.UP:
                case Input.Keys.W:
                    moveUp = false;
                    break;

                case Input.Keys.DOWN:
                case Input.Keys.S:
                    moveDown = false;
                    break;
                case Input.Keys.SHIFT_LEFT:
                case Input.Keys.SHIFT_RIGHT:
                    run = false;
                    break;
            }
            return true;
        }
    }
}
