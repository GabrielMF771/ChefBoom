package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.Config;
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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class PlayerControllerSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> mPlayer;
    private ComponentMapper<SpriteComponent> mSprite;
    private ComponentMapper<RigidBodyComponent> mRigidBody;
    private ComponentMapper<CollidableComponent> mCollidable;

    private Texture texFrente;
    private Texture texCostas;
    private Texture texDireita;
    private Texture texEsquerda;

    private final Sound dashSound = Assets.manager.get(Assets.dashSound);

    public PlayerControllerSystem() {
        super(Aspect.all(PlayerComponent.class, RigidBodyComponent.class, CollidableComponent.class));
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

        float delta = world.getDelta();

        // Atualização do cooldown do dash
        if (cPlayer.dashCooldown > 0f) {
            cPlayer.dashCooldown -= delta;
            if (cPlayer.dashCooldown < 0f) cPlayer.dashCooldown = 0f;
        }

        // Dash em andamento
        if (cPlayer.isDashing) {
            cPlayer.dashTimeLeft -= delta;
            cRigidBody.velocity.x = cPlayer.dashDirX * cPlayer.dashSpeed;
            cRigidBody.velocity.y = cPlayer.dashDirY * cPlayer.dashSpeed;
            if (cPlayer.dashTimeLeft <= 0f) {
                cPlayer.isDashing = false;
                cRigidBody.velocity.x = 0;
                cRigidBody.velocity.y = 0;
            }
            return;
        }

        // Movimentação normal
        float moveX = 0, moveY = 0;
        boolean dashPressed = false;

        // Player 1 (WASD)
        if (cPlayer.playerId == 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) moveY = 1;
            if (Gdx.input.isKeyPressed(Input.Keys.S)) moveY = -1;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) moveX = -1;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) moveX = 1;
            dashPressed = Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT);
        }
        // Player 2 (Setas)
        else if (Config.TWO_PLAYERS && cPlayer.playerId == 1) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveY = 1;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveY = -1;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveX = -1;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveX = 1;
            dashPressed = Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT);
        }

        // Normaliza para não correr mais na diagonal
        if (moveX != 0 && moveY != 0) {
            moveX *= 0.7071f;
            moveY *= 0.7071f;
        }

        float speed = cPlayer.walkSpeed;
        cRigidBody.velocity.x = moveX * speed;
        cRigidBody.velocity.y = moveY * speed;

        // Dash
        if (dashPressed && cPlayer.dashCooldown == 0f && (moveX != 0 || moveY != 0)) {
            cPlayer.isDashing = true;
            cPlayer.dashTimeLeft = cPlayer.dashTime;
            cPlayer.dashDirX = moveX;
            cPlayer.dashDirY = moveY;
            cPlayer.dashCooldown = cPlayer.getDashCooldownTime;
            dashSound.play(Config.EFFECTS_VOLUME);
        }

        // Troca sprite conforme direção
        if (moveX > 0) cSprite.sprite.setTexture(texDireita);
        else if (moveX < 0) cSprite.sprite.setTexture(texEsquerda);
        else if (moveY > 0) cSprite.sprite.setTexture(texCostas);
        else if (moveY < 0) cSprite.sprite.setTexture(texFrente);
    }
}