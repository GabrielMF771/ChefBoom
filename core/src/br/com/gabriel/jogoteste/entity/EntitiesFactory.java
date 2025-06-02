package br.com.gabriel.jogoteste.entity;

import br.com.gabriel.jogoteste.entity.component.*;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class EntitiesFactory {

    private ComponentMapper<PlayerComponent> mPlayer;

    private ComponentMapper<SpriteComponent> mSprite;

    private ComponentMapper<TransformComponent> mTransform;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private ComponentMapper<CollidableComponent> mCollidable;

    public int createPlayer(World world, float x, float y) {
        int entity = world.create();

        TransformComponent cTransform = mTransform.create(entity);
        cTransform.position.set(x, y);

        RigidBodyComponent cRigidBody = mRigidBody.create(entity);

        SpriteComponent cSprite = mSprite.create(entity);
        cSprite.sprite = new Sprite(new Texture("player/teste-frente.png"));

        PlayerComponent cPlayer = mPlayer.create(entity);

        CollidableComponent cCollidable = mCollidable.create(entity);

        return entity;
    }
}
