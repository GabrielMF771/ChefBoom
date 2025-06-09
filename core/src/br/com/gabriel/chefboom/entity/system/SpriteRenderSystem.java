package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.entity.component.ClientComponent;
import br.com.gabriel.chefboom.entity.component.SpriteComponent;
import br.com.gabriel.chefboom.resource.Assets;
import br.com.gabriel.chefboom.entity.component.TransformComponent;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class SpriteRenderSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> mTransform;
    private ComponentMapper<SpriteComponent> mSprite;
    private ComponentMapper<ClientComponent> mClient;

    OrthographicCamera camera;
    SpriteBatch batch;

    public SpriteRenderSystem(OrthographicCamera camera) {
        super(Aspect.all(TransformComponent.class, SpriteComponent.class));
        this.camera = camera;
        batch = new SpriteBatch();
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    @Override
    protected void process(int entityId) {
        TransformComponent cTransform = mTransform.get(entityId);
        SpriteComponent cSprite = mSprite.get(entityId);
        ClientComponent cClient = mClient.get(entityId);

        Sprite sprite = cSprite.sprite;

        if(cTransform.originCenter) {
            sprite.setOriginCenter();
        } else {
            sprite.setOrigin(cTransform.origin.x, cTransform.origin.y);
        }

        sprite.setScale(cTransform.scaleX, cTransform.scaleY);

        sprite.setRotation(cTransform.rotation);

        sprite.setPosition(cTransform.position.x, cTransform.position.y);

        batch.draw(
                sprite.getTexture(),
                sprite.getX() - sprite.getOriginX(),
                sprite.getY() - sprite.getOriginY(),
                sprite.getOriginX(),
                sprite.getOriginY(),
                sprite.getWidth(),
                sprite.getHeight(),
                sprite.getScaleX(),
                sprite.getScaleY(),
                sprite.getRotation(),
                sprite.getRegionX(),
                sprite.getRegionY(),
                sprite.getRegionWidth(),
                sprite.getRegionHeight(),
                cSprite.flipX,
                cSprite.flipY
        );

        if (mClient.has(entityId)) {
            cClient = mClient.get(entityId);
            if (cClient.wantedItemId != -1 && cClient.inQueue) {
                Texture itemTexture = cClient.wantedItemId == 0
                        ? Assets.manager.get(Assets.apple)
                        : Assets.manager.get(Assets.bread);

                // Centraliza o item no centro do bloco acima da cabeça do NPC
                float blockCenterX = Math.round(cTransform.position.x / Block.TILE_SIZE) * Block.TILE_SIZE;
                float blockCenterY = Math.round(cTransform.position.y / Block.TILE_SIZE) * Block.TILE_SIZE + Block.TILE_SIZE;

                float itemX = blockCenterX + (Block.TILE_SIZE - Block.TILE_SIZE) / 2f;
                float itemY = blockCenterY;

                batch.draw(itemTexture, itemX, itemY, Block.TILE_SIZE, Block.TILE_SIZE);
            }
        }
    }

    @Override
    protected void end() {
        batch.end();
    }

    @Override
    protected void dispose() {
        batch.dispose();
    }
}
