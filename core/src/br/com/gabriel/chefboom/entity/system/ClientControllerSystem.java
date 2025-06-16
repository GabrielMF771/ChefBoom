package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.entity.component.*;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.screen.GameScreen;
import br.com.gabriel.chefboom.screen.MenuScreen;
import br.com.gabriel.chefboom.screen.NextLevelScreen;
import br.com.gabriel.chefboom.screen.YouLoseScreen;
import br.com.gabriel.chefboom.world.World;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import static br.com.gabriel.chefboom.resource.Assets.explosao2;


public class ClientControllerSystem extends IteratingSystem {

    private ComponentMapper<ClientComponent> mClient;
    private ComponentMapper<SpriteComponent> mSprite;
    private ComponentMapper<RigidBodyComponent> mRigidBody;
    private ComponentMapper<CollidableComponent> mCollidable;
    private ComponentMapper<PlayerComponent> mPlayer;

    private boolean moveRight;
    private Texture texDireita;
    private Texture explosion01;
    private Texture explosion02;

    private int clientsExplodedThisFrame = 0;

    private final World gameWorld;

    private Music gameMusic = GameScreen.getGameMusic();

    private Sound explosionSound = Assets.manager.get(Assets.explosionSound);

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
        explosion01 = Assets.manager.get(Assets.explosao1);
        explosion02 = Assets.manager.get(Assets.explosao2);
    }

    @Override
    protected void process(int entityId) {
        ClientComponent cClient = mClient.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        SpriteComponent cSprite = mSprite.get(entityId);

        // Lógica da animação de explosão
        if (cClient.isExploding) {
            cClient.explosionTimer += world.getDelta();

            // Após 0.2s, a animação termina e o cliente é removido
            if (cClient.explosionTimer >= 0.2f) {
                clientsExplodedThisFrame++; // Dano ao jogador é contado aqui
                getWorld().delete(entityId);

                // Após 0.1s, muda para o segundo frame da explosão
            } else if (cClient.explosionTimer >= 0.1f) {
                cSprite.sprite.setTexture(explosion02);
            }

            return;
        }


        if (cClient.canWalk) {
            float speed = cClient.walkSpeed;
            cRigidBody.velocity.x = speed;
            cSprite.sprite.setTexture(texDireita);
        }

        if (cClient.inQueue) {
            cClient.timeLeft -= world.getDelta();
            if (cClient.timeLeft < 0) cClient.timeLeft = 0;
            if (cClient.timeLeft <= 0) {
                // Inicia a explosão
                cClient.isExploding = true;
                cSprite.sprite.setTexture(explosion01);
                cRigidBody.velocity.x = 0;
                explosionSound.play(Config.EFFECTS_VOLUME);

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
                    gameMusic.stop();
                    ChefBoom.getInstance().setScreen(new YouLoseScreen());

                    // TODO - Fazer a lógica de detectar se o nivel foi completado
                    // Já fiz a lógica de passar de nível, mas não está implementada
                    //ChefBoom.getInstance().setScreen(new NextLevelScreen());
                }
            }
        }
    }
}