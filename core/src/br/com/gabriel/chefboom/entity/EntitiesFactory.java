package br.com.gabriel.chefboom.entity;

import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.entity.component.*;
import br.com.gabriel.chefboom.entity.state.PlayerState;
import br.com.gabriel.chefboom.entity.system.ItemSystem;
import br.com.gabriel.chefboom.resource.Assets;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

@Wire
public class EntitiesFactory {

    private ComponentMapper<PlayerComponent> mPlayer;

    private ComponentMapper<SpriteComponent> mSprite;

    private ComponentMapper<TransformComponent> mTransform;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private ComponentMapper<CollidableComponent> mCollidable;

    private ComponentMapper<StateComponent> mState;

    private ComponentMapper<ClientComponent> mClient;

    private ComponentMapper<ItemComponent> mItem;

    private ComponentMapper<InteractiveBlock> mInteractiveBlock;

    public void setWorld(World world) {
        mPlayer = world.getMapper(PlayerComponent.class);
        mSprite = world.getMapper(SpriteComponent.class);
        mTransform = world.getMapper(TransformComponent.class);
        mRigidBody = world.getMapper(RigidBodyComponent.class);
        mCollidable = world.getMapper(CollidableComponent.class);
        mState = world.getMapper(StateComponent.class);
        mClient = world.getMapper(ClientComponent.class);
        mItem = world.getMapper(ItemComponent.class);
        mInteractiveBlock = world.getMapper(InteractiveBlock.class);
    }

    public int createPlayer(World world, float x, float y) {
        int entity = world.create();

        TransformComponent cTransform = mTransform.create(entity);
        cTransform.position.set(x, y);
        cTransform.scaleX = 2f;
        cTransform.scaleY = 2f;

        Texture texture = Assets.manager.get(Assets.playerEsquerda);

        SpriteComponent cSprite = mSprite.create(entity);
        cSprite.sprite = new Sprite(texture);

        PlayerComponent cPlayer = mPlayer.create(entity);

        RigidBodyComponent cRigidBody = mRigidBody.create(entity);

        CollidableComponent cCollidable = mCollidable.create(entity);
        cCollidable.collisionBox.setSize(texture.getWidth(), texture.getHeight());
        cCollidable.collisionBox.setCenter(new Vector2(x, y));

        StateComponent<PlayerState> cState = mState.create(entity);
        cState.state = new DefaultStateMachine<Entity, PlayerState>(world.getEntity(entity), PlayerState.Idle);

        return entity;
    }
    public int createClient(World world, float x, float y, int queueId) {
        int entity = world.create();

        TransformComponent cTransform = mTransform.create(entity);
        cTransform.position.set(x, y);
        cTransform.scaleX = 2f;
        cTransform.scaleY = 2f;

        // Array com as texturas dos clientes
        AssetDescriptor<Texture>[] clientTextures = new AssetDescriptor[] {
                Assets.cliente1,
                Assets.cliente2,
                Assets.cliente3,
                Assets.cliente4,
                Assets.cliente5,
                Assets.cliente6
        };

        // Escolhe uma textura aleatória
        Random random = new Random();
        AssetDescriptor<Texture> randomTextureDescriptor = clientTextures[random.nextInt(clientTextures.length)];
        Texture texture = Assets.manager.get(randomTextureDescriptor);


        SpriteComponent cSprite = mSprite.create(entity);
        cSprite.sprite = new Sprite(texture);

        ClientComponent cClient = mClient.create(entity);
        cClient.queueId = queueId;

        // TODO - Todo item novo que adicionar, deve ser adicionado aqui
        // Define as chances para cada item
        double[] chances = {0.5, 0.3, 0.2}; // 50% burguer, 30% fries, 20% soda
        cClient.wantedItemId = ItemSystem.randomItemByProbability(chances);

        RigidBodyComponent cRigidBody = mRigidBody.create(entity);

        CollidableComponent cCollidable = mCollidable.create(entity);
        cCollidable.collisionBox.setSize(texture.getWidth(), texture.getHeight());
        cCollidable.collisionBox.setCenter(new Vector2(x, y));

        return entity;
    }

    public int createItem(World world, float x, float y, Texture texture) {
        int entity = world.create();

        // Garante que os ComponentMappers estão inicializados
        if (mTransform == null || mSprite == null || mItem == null) {
            setWorld(world); // Inicializa os mappers se eles forem nulos
            if (mTransform == null || mSprite == null || mItem == null) {
                throw new IllegalStateException("ComponentMappers não foram inicializados corretamente.");
            }
        }

        TransformComponent cTransform = mTransform.create(entity);
        cTransform.position.set(x, y);
        cTransform.scaleX = 2f;
        cTransform.scaleY = 2f;

        SpriteComponent cSprite = mSprite.create(entity);
        cSprite.sprite = new com.badlogic.gdx.graphics.g2d.Sprite(texture);

        ItemComponent cItem = mItem.create(entity);
        cItem.isHeld = false;

        if(ChefBoom.DEBUG){
            Gdx.app.log("EntitiesFactory", "Item criado na posição: " + x / Block.TILE_SIZE + ", " + y / Block.TILE_SIZE);
        }

        return entity;
    }

    public int createInteractiveBlock(World world, float x, float y, InteractiveBlock.Type type, float TimeLeft ,Texture texture) {
        int entity = world.create();

        TransformComponent cTransform = mTransform.create(entity);
        cTransform.position.set(x, y);
        cTransform.scaleX = 1f;
        cTransform.scaleY = 1f;

        SpriteComponent cSprite = mSprite.create(entity);
        cSprite.sprite = new com.badlogic.gdx.graphics.g2d.Sprite(texture);

        CollidableComponent cCollidable = mCollidable.create(entity);
        cCollidable.collisionBox.setSize(texture.getWidth(), texture.getHeight());
        cCollidable.collisionBox.setCenter(new Vector2(x, y));

        InteractiveBlock cInteractiveBlock = mInteractiveBlock.create(entity);
        cInteractiveBlock.type = type;

        cInteractiveBlock.timeLeft = TimeLeft;

        return entity;
    }
}