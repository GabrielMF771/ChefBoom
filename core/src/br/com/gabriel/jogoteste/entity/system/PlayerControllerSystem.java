package br.com.gabriel.jogoteste.entity.system;

import br.com.gabriel.jogoteste.entity.component.CollidableComponent;
import br.com.gabriel.jogoteste.entity.component.PlayerComponent;
import br.com.gabriel.jogoteste.entity.component.RigidBodyComponent;
import br.com.gabriel.jogoteste.entity.component.SpriteComponent;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

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


    public PlayerControllerSystem() {
        super(Aspect.all(PlayerComponent.class, RigidBodyComponent.class, CollidableComponent.class));

        Gdx.input.setInputProcessor(new InputMultiplexer(new GameInputAdapter()));
    }

    @Override
    protected void process(int entityId) {
        PlayerComponent cPlayer = mPlayer.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        SpriteComponent cSprite = mSprite.get(entityId);

        if (cPlayer.canWalk) {
            float speed = cPlayer.walkSpeed;
            if (run) speed += 100;

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
                cSprite.sprite = new Sprite(new Texture("player/teste-direita.png"));
            } else if (moveLeft) {
                cRigidBody.velocity.x = -speed;
                cSprite.sprite = new Sprite(new Texture("player/teste-esquerda.png"));
            } else {
                cRigidBody.velocity.x = 0;
            }

            // Eixo Y
            if (moveUp && moveDown) {
                cRigidBody.velocity.y = 0;
            } else if (moveUp) {
                cRigidBody.velocity.y = speed;
                cSprite.sprite = new Sprite(new Texture("player/teste-costas.png"));
            } else if (moveDown) {
                cRigidBody.velocity.y = -speed;
                cSprite.sprite = new Sprite(new Texture("player/teste-frente.png"));
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
