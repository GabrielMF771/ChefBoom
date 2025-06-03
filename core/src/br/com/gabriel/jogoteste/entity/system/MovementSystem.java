package br.com.gabriel.jogoteste.entity.system;

import br.com.gabriel.jogoteste.block.Block;
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
    private Vector2 velocity = new Vector2();

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

        if(cRigidBody.isKinematic) {
            if(delta == 0) return;

            if(mCollidable.has(entityId)) {
                cCollidable.onGround = false;
                cCollidable.onCeiling = false;
                cCollidable.onLeftWall = false;
                cCollidable.onRightWall = false;

                velocity.set(cRigidBody.velocity);

                velocity.scl(delta);

                Rectangle rectangle = cCollidable.collisionBox;

                rectangle.setSize(cTransform.scaleX * (Block.TILE_SIZE / 2), cTransform.scaleY * (Block.TILE_SIZE / 2));

                rectangle.setPosition(cTransform.position);

                float startX, startY, endX, endY;

                // EIXO Y
                if (velocity.y > 0) {
                    startY = rectangle.y + rectangle.height;
                    endY = rectangle.y + rectangle.height + velocity.y;
                } else {
                    startY = rectangle.y + velocity.y;
                    endY = rectangle.y;
                }

                startX = rectangle.x;
                endX = rectangle.x + rectangle.width;

                world.getTilesRectangle(startX, startY, endX, endY, tiles);

                for (int i = 0; i < Math.abs(velocity.y); i++) {
                    boolean found = false;

                    float oldY = rectangle.y;

                    rectangle.y += Math.signum(velocity.y);

                    for (Rectangle tile : tiles) {
                        if (rectangle.overlaps(tile)) {
                            if (velocity.y > 0) {
                                cCollidable.onCeiling = true;
                            } else {
                                cCollidable.onGround = true;
                            }

                            found = true;
                            break;
                        }
                    }

                    if(found) {
                        rectangle.y = oldY;
                        velocity.y = 0;
                        break;
                    }
                }

                // EIXO X

                if (velocity.x > 0) {
                    startX = rectangle.x + rectangle.width;
                    endX = rectangle.x + rectangle.width + velocity.x;
                } else {
                    startX = rectangle.x + velocity.x;
                    endX = rectangle.x;
                }

                startY = rectangle.y;
                endY = rectangle.y + rectangle.height;

                world.getTilesRectangle(startX, startY, endX, endY, tiles);

                for (int i = 0; i < Math.abs(velocity.x); i++) {
                    boolean found = false;

                    float oldX = rectangle.x;

                    rectangle.x += Math.signum(velocity.x);

                    for (Rectangle tile : tiles) {
                        if (rectangle.overlaps(tile)) {
                            if (velocity.x > 0) {
                                cCollidable.onRightWall = true;
                            } else if (velocity.x < 0) {
                                cCollidable.onLeftWall = true;
                            }

                            found = true;
                            break;
                        }
                    }

                    if(found) {
                        rectangle.x = oldX;
                        velocity.x = 0;
                        break;
                    }
                }

                cTransform.position.set(rectangle.x, rectangle.y);

                velocity.scl(1 / delta);

                cRigidBody.velocity.set(velocity);
            } else {
                cTransform.position.mulAdd(cRigidBody.velocity, delta);
            }
        }
    }
}
