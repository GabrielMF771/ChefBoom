package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.entity.component.*;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.screen.GameScreen;
import br.com.gabriel.chefboom.screen.YouLoseScreen;
import br.com.gabriel.chefboom.world.World;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import br.com.gabriel.chefboom.world.LevelEnded;

public class ClientControllerSystem extends IteratingSystem {

    private ComponentMapper<ClientComponent> mClient;
    private ComponentMapper<SpriteComponent> mSprite;
    private ComponentMapper<RigidBodyComponent> mRigidBody;
    private ComponentMapper<CollidableComponent> mCollidable;
    private ComponentMapper<PlayerComponent> mPlayer;

    private boolean moveRight;
    private Texture explosion01;
    private Texture explosion02;


    private int clientsExplodedThisFrame = 0;

    private final World gameWorld;

    private final Music gameMusic = GameScreen.getGameMusic();

    private final Sound explosionSound = Assets.manager.get(Assets.explosionSound);

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
        explosion01 = Assets.manager.get(Assets.explosao1);
        explosion02 = Assets.manager.get(Assets.explosao2);
    }

    @Override
    protected void process(int entityId) {
        ClientComponent cClient = mClient.get(entityId);
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
            cRigidBody.velocity.x = cClient.walkSpeed;
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
            if (player != null && player.hp > 0) {
                // Só desconta vida para clientes que NÃO explodiram por atendimento
                int clientsThatHurt = 0;

                IntBag clients = getEntityIds();
                int[] ids = clients.getData();
                int size = clients.size();
                for (int i = 0; i < size; i++) {
                    int id = ids[i];
                    ClientComponent cClient = mClient.get(id);
                    if (cClient != null && cClient.isExploding && !cClient.explodedByServe) {
                        clientsThatHurt++;
                        LevelEnded.setClientesAtendidos((LevelEnded.getClientesAtendidos() + 1 ));
                        LevelEnded.setVidasRestantes(LevelEnded.getVidasRestantes()-1);
                    }
                }

                if (clientsThatHurt > 0) {
                    player.hp -= clientsThatHurt;
                    if (player.hp < 0) player.hp = 0;
                    if (player.hp == 0) {
                        gameMusic.stop();
                        ChefBoom.getInstance().setScreen(new YouLoseScreen());
                    }
                }
            }
        }
    }
}