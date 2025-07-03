package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.entity.component.*;
import br.com.gabriel.chefboom.resource.Assets;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerControllerSystem extends IteratingSystem {

    private ComponentMapper<PlayerComponent> mPlayer;
    private ComponentMapper<SpriteComponent> mSprite;
    private ComponentMapper<RigidBodyComponent> mRigidBody;
    private ComponentMapper<CollidableComponent> mCollidable;
    private ComponentMapper<StateComponent> mState;

    private AnimationController walkDownController;
    private AnimationController walkLeftController;
    private AnimationController walkRightController;
    private AnimationController walkUpController;

    private AnimationController idleDownController;
    private AnimationController idleLeftController;
    private AnimationController idleRightController;
    private AnimationController idleUpController;

    private final Sound dashSound = Assets.manager.get(Assets.dashSound);

    public PlayerControllerSystem() {
        super(Aspect.all(PlayerComponent.class, RigidBodyComponent.class, CollidableComponent.class));
    }

    @Override
    protected void initialize() {
        Texture texWalk = Assets.manager.get(Assets.playerWalk);
        Texture texIdle = Assets.manager.get(Assets.playerIdle);

        int frameCols = 6;
        int frameRows = 4;
        int frameWidth = 96 / frameCols;
        int frameHeight = 128 / frameRows;

        TextureRegion[][] walkTmp = TextureRegion.split(texWalk, frameWidth, frameHeight);

        walkRightController = new AnimationController(new Animation<TextureRegion>(0.12f, walkTmp[0]));
        walkUpController = new AnimationController(new Animation<TextureRegion>(0.12f, walkTmp[1]));
        walkLeftController = new AnimationController(new Animation<TextureRegion>(0.12f, walkTmp[2]));
        walkDownController = new AnimationController(new Animation<TextureRegion>(0.12f, walkTmp[3]));

        TextureRegion[][] idleTmp = TextureRegion.split(texIdle, frameWidth, frameHeight);

        idleRightController = new AnimationController(new Animation<TextureRegion>(0.12f, idleTmp[0]));
        idleUpController = new AnimationController(new Animation<TextureRegion>(0.12f, idleTmp[1]));
        idleLeftController = new AnimationController(new Animation<TextureRegion>(0.12f, idleTmp[2]));
        idleDownController = new AnimationController(new Animation<TextureRegion>(0.12f, idleTmp[3]));
    }

    @Override
    protected void process(int entityId) {
        // Se o console estiver visível, não faça nada.
        if (ChefBoom.getInstance().getDevConsole().isVisible()) {
            return;
        }

        PlayerComponent cPlayer = mPlayer.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        SpriteComponent cSprite = mSprite.get(entityId);
        StateComponent cState = mState.get(entityId);

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

        // Atualiza direção no StateComponent
        if (moveX > 0) cState.direction = StateComponent.Direction.RIGHT;
        else if (moveX < 0) cState.direction = StateComponent.Direction.LEFT;
        else if (moveY > 0) cState.direction = StateComponent.Direction.UP;
        else if (moveY < 0) cState.direction = StateComponent.Direction.DOWN;

        // Atualiza animação conforme movimento
        AnimationController currentController;
        switch (cState.direction) {
            case RIGHT: currentController = walkRightController; break;
            case LEFT: currentController = walkLeftController; break;
            case UP: currentController = walkUpController; break;
            case DOWN: default: currentController = walkDownController; break;
        }

        if (moveX != 0 || moveY != 0) {
            if (!currentController.isPlaying()) currentController.play();
            currentController.update(delta);
            cSprite.sprite.setRegion(currentController.getCurrentFrame());
        } else {
            AnimationController idleController;
            switch (cState.direction) {
                case RIGHT: idleController = idleRightController; break;
                case LEFT: idleController = idleLeftController; break;
                case UP: idleController = idleUpController; break;
                case DOWN: default: idleController = idleDownController; break;
            }
            if (!idleController.isPlaying()) idleController.play();
            idleController.update(delta);
            cSprite.sprite.setRegion(idleController.getCurrentFrame());
        }
    }
}