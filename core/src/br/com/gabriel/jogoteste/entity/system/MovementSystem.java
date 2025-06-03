package br.com.gabriel.jogoteste.entity.system;

import br.com.gabriel.jogoteste.entity.component.CollidableComponent;
import br.com.gabriel.jogoteste.entity.component.RigidBodyComponent;
import br.com.gabriel.jogoteste.entity.component.TransformComponent;
import br.com.gabriel.jogoteste.world.World;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MovementSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> mTransform;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private ComponentMapper<CollidableComponent> mCollidable;

    private World world;

    private Array<Rectangle> tiles = new Array<Rectangle>();

    public MovementSystem(World world) {
        super(Aspect.all(TransformComponent.class, RigidBodyComponent.class));
        this.world = world;
    }

    @Override
    protected void process(int entityId) {
        TransformComponent cTransform = mTransform.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);

        float delta = super.world.getDelta();

        if(mCollidable.has(entityId)) {
            cCollidable.onGround = false;
            cCollidable.onCeiling = false;
            cCollidable.onLeftWall = false;
            cCollidable.onRightWall = false;

            Vector2 velocity = new Vector2(cRigidBody.velocity);

            velocity.scl(delta);

            Rectangle rectangle = cCollidable.collisionBox;
            rectangle.setPosition(cTransform.position);

            float startX, startY, endX, endY;

            if(velocity.x > 0){
                startX = endX = cTransform.position.x + rectangle.width + velocity.x;
            } else {
                startX = endX = cTransform.position.x + velocity.x;
            }

            startY = cTransform.position.y;
            endY = cTransform.position.y + rectangle.height;

            tiles.clear();
            world.getTilesRectangle(startX, startY, endX, endY, tiles);

            rectangle.x += velocity.x;

            for(Rectangle tile : tiles) {
                if(rectangle.overlaps(tile)) {
                    if(velocity.x > 0){
                        cCollidable.onRightWall = true;
                    } else if(velocity.x < 0) {
                        cCollidable.onLeftWall = true;
                    }

                    velocity.x = 0;
                    break;
                }
            }

            rectangle.x = cTransform.position.x;

            if(velocity.y > 0){
                startY = endY = cTransform.position.y + rectangle.height + velocity.y;
            } else {
                startY = endY = cTransform.position.y + velocity.y;
            }

            startX = cTransform.position.x;
            endX = cTransform.position.x + rectangle.width;

            tiles.clear();
            world.getTilesRectangle(startX, startY, endX, endY, tiles);

            rectangle.y += velocity.y;

            for(Rectangle tile : tiles) {
                if(rectangle.overlaps(tile)) {
                    if(velocity.y > 0){
                        cTransform.position.y = tile.y - rectangle.height;
                        cCollidable.onCeiling = true;
                    } else {
                        cTransform.position.y = tile.y + rectangle.width;
                        cCollidable.onGround = true;
                    }
                    velocity.y = 0;
                    break;
                }
            }

            cTransform.position.add(velocity);

            velocity.scl(1 / delta);

            cRigidBody.velocity.set(velocity);
        } else {
            cTransform.position.mulAdd(cRigidBody.velocity, delta);
        }


    }
}
