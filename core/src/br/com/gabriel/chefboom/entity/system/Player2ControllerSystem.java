package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.entity.component.*;
import br.com.gabriel.chefboom.resource.Assets;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Player2ControllerSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent2> mPlayer;

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

    private final Sound dashSound = Assets.manager.get(Assets.dashSound);

    // Dash
    private boolean isDashing = false;
    private float dashTimeLeft = 0f;
    private final float DASH_TIME = 0.18f; // duração do dash em segundos
    private final float DASH_SPEED = 380f; // velocidade do dash
    private float dashDirX = 0;
    private float dashDirY = 0;
    private float dashCooldown = 0f; // Tempo de recarga do dash
    private final float DASH_COOLDOWN_TIME = 1f; // Tempo de cooldown do dash

    public Player2ControllerSystem() {
        super(Aspect.all(PlayerComponent2.class, RigidBodyComponent.class, CollidableComponent.class));

        Gdx.input.setInputProcessor(new InputMultiplexer(new Player2ControllerSystem.GameInputAdapter()));
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
        PlayerComponent2 cPlayer = mPlayer.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        SpriteComponent cSprite = mSprite.get(entityId);

        float delta = world.getDelta();

        // Atualização do cooldown
        if (dashCooldown > 0f) {
            dashCooldown -= delta;
            if (dashCooldown < 0f)
                dashCooldown = 0f;
        }

        if (isDashing) {
            dashTimeLeft -= delta;
            if (dashTimeLeft <= 0) {
                isDashing = false;
                dashCooldown = DASH_COOLDOWN_TIME; // Inicia cooldown ao terminar dash
                cRigidBody.velocity.x = 0;
                cRigidBody.velocity.y = 0;
            } else {
                cRigidBody.velocity.x = dashDirX * DASH_SPEED;
                cRigidBody.velocity.y = dashDirY * DASH_SPEED;
            }
            return;
        }


        if (cPlayer.canWalk) {
            float speed = cPlayer.walkSpeed;
            //if (run) speed += 70;

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
                    moveRight = true;
                    break;

                case Input.Keys.LEFT:
                    moveLeft = true;
                    break;

                case Input.Keys.UP:
                    moveUp = true;
                    break;

                case Input.Keys.DOWN:
                    moveDown = true;
                    break;

                case Input.Keys.SHIFT_RIGHT:
                    //run = true;
                    if (!isDashing && dashCooldown == 0f) {
                        // Toca o som do dash
                        dashSound.play(Config.EFFECTS_VOLUME - 0.15f);

                        // Calcula a direção atual
                        dashDirX = 0;
                        dashDirY = 0;

                        if (moveRight) dashDirX += 1;
                        if (moveLeft) dashDirX -= 1;
                        if (moveUp) dashDirY += 1;
                        if (moveDown) dashDirY -= 1;

                        // Evita o dash sem direção
                        if (dashDirX == 0 && dashDirY == 0) dashDirY = -1;

                        // Normaliza o vetor para diagonal não ser mais rápido
                        float len = (float)Math.sqrt(dashDirX * dashDirX + dashDirY * dashDirY);
                        dashDirX /= len;
                        dashDirY /= len;

                        isDashing = true;
                        dashTimeLeft = DASH_TIME;
                    }
                    break;
            }
            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            switch (keycode) {
                case Input.Keys.RIGHT:
                    moveRight = false;
                    break;

                case Input.Keys.LEFT:
                    moveLeft = false;
                    break;

                case Input.Keys.UP:
                    moveUp = false;
                    break;

                case Input.Keys.DOWN:
                    moveDown = false;
                    break;

                case Input.Keys.SHIFT_RIGHT:
                    //run = false;
                    break;
            }
            return true;
        }
    }
}


