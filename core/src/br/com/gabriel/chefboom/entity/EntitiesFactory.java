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

    public int createPlayer(World world, float x, float y) {
        int entity = world.create();

        TransformComponent cTransform = mTransform.create(entity);
        cTransform.position.set(x, y);

        Texture texture = Assets.manager.get(Assets.testeEsquerda);

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

        Texture texture = Assets.manager.get(Assets.testeDireita);

        SpriteComponent cSprite = mSprite.create(entityC);
        cSprite.sprite = new Sprite(texture);

        ClientComponent cClient = mClient.create(entityC);

        RigidBodyComponent cRigidBody = mRigidBody.create(entityC);

        CollidableComponent cCollidable = mCollidable.create(entityC);
        cCollidable.collisionBox.setSize(texture.getWidth(), texture.getHeight());
        cCollidable.collisionBox.setCenter(new Vector2(x, y));

        StateComponent<PlayerState> cState = mState.create(entityC);
        cState.state = new DefaultStateMachine<Entity, PlayerState>(world.getEntity(entityC), PlayerState.Idle);

        return entityC;
    }
}