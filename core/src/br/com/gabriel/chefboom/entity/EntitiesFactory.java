package br.com.gabriel.chefboom.entity;

import br.com.gabriel.chefboom.entity.component.*;
import br.com.gabriel.chefboom.entity.state.PlayerState;
import br.com.gabriel.chefboom.resource.Assets;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class EntitiesFactory {

    private ComponentMapper<PlayerComponent> mPlayer;

    private ComponentMapper<SpriteComponent> mSprite;

    private ComponentMapper<TransformComponent> mTransform;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private ComponentMapper<CollidableComponent> mCollidable;

    private ComponentMapper<StateComponent> mState;

    private ComponentMapper<ClientComponent> mClient;

    private ComponentMapper<ItemComponent> mItem;

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
    public int createClient(World world, float x, float y) {
        int entityC = world.create();

        TransformComponent cTransform = mTransform.create(entityC);
        cTransform.position.set(x, y);
        cTransform.scaleX = 2f;
        cTransform.scaleY = 2f;

        Texture texture = Assets.manager.get(Assets.playerDireita);

        SpriteComponent cSprite = mSprite.create(entityC);
        cSprite.sprite = new Sprite(texture);

        ClientComponent cClient = mClient.create(entityC);

        // Total de tipos de itens disponíveis (ALTERAR DEPOIS, NAO PODE FICAR AQUI)
        int totalItens = 2;

        // Sorteio de itens
        cClient.wantedItemId = (int) (Math.random() * totalItens);

        RigidBodyComponent cRigidBody = mRigidBody.create(entityC);

        CollidableComponent cCollidable = mCollidable.create(entityC);
        cCollidable.collisionBox.setSize(texture.getWidth(), texture.getHeight());
        cCollidable.collisionBox.setCenter(new Vector2(x, y));

        StateComponent<PlayerState> cState = mState.create(entityC);
        cState.state = new DefaultStateMachine<Entity, PlayerState>(world.getEntity(entityC), PlayerState.Idle);

        return entityC;
    }

    public int createItem(com.artemis.World world, float x, float y, com.badlogic.gdx.graphics.Texture texture) {
        int entity = world.create();

        TransformComponent cTransform = mTransform.create(entity);
        cTransform.position.set(x, y);
        cTransform.scaleX = 2f;
        cTransform.scaleY = 2f;

        SpriteComponent cSprite = mSprite.create(entity);
        cSprite.sprite = new com.badlogic.gdx.graphics.g2d.Sprite(texture);

        ItemComponent cItem = mItem.create(entity);
        cItem.isHeld = false;

        return entity;
    }
}