package br.com.gabriel.jogoteste.entity.system;

import br.com.gabriel.jogoteste.entity.component.CollidableComponent;
import br.com.gabriel.jogoteste.entity.component.RigidBodyComponent;
import br.com.gabriel.jogoteste.entity.component.TransformComponent;
import br.com.gabriel.jogoteste.world.World;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.awt.*;

public class CollisionDebugSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> mTransform;

    private ComponentMapper<RigidBodyComponent> mRigidBody;

    private ComponentMapper<CollidableComponent> mCollidable;

    Camera camera;

    ShapeRenderer shapeRenderer;

    World gameWorld;

    public CollisionDebugSystem(Camera camera, World world) {
        super(Aspect.all(TransformComponent.class, RigidBodyComponent.class, CollidableComponent.class));
        this.camera = camera;
        gameWorld = world;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    protected void begin() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.YELLOW);

        Rectangle rectangle;
        for (int x = 0; x < gameWorld.getWidth(); x++){
            for (int y = 0; y < gameWorld.getHeight(); y++){
                rectangle = gameWorld.getTileRectangle(x,y);

                if(rectangle != null){
                    shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                }
            }
        }
    }

    @Override
    protected void process(int entityId) {
        TransformComponent cTransform = mTransform.get(entityId);
        RigidBodyComponent cRigidBody = mRigidBody.get(entityId);
        CollidableComponent cCollidable = mCollidable.get(entityId);

        Vector2 min = cCollidable.collisionBox.getPosition(new Vector2());
        Vector2 max = cCollidable.collisionBox.getSize(new Vector2()).add(min);
        Vector2 size = cCollidable.collisionBox.getSize(new Vector2());

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(min.x, min.y, size.x, size.y);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.line(cTransform.position.x, cTransform.position.y, cTransform.position.x + cRigidBody.velocity.x, cTransform.position.y + cRigidBody.velocity.y);

        shapeRenderer.setColor(Color.RED);

        if(cCollidable.onGround){
            shapeRenderer.line(min.x, min.y, max.x, min.y);
        }
        if(cCollidable.onCeiling){
            shapeRenderer.line(min.x, max.y, max.x, max.y);
        }
        if(cCollidable.onLeftWall){
            shapeRenderer.line(min.x, min.y, min.x, max.y);
        }
        if(cCollidable.onRightWall){
            shapeRenderer.line(max.x, min.y, max.x, max.y);
        }
    }

    @Override
    protected void end() {
        shapeRenderer.end();
    }

    @Override
    protected void dispose() {
        shapeRenderer.dispose();
    }
}
