package br.com.gabriel.chefboom.entity.system;

import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.entity.component.ClientComponent;
import br.com.gabriel.chefboom.entity.component.InteractiveBlock;
import br.com.gabriel.chefboom.entity.component.SpriteComponent;
import br.com.gabriel.chefboom.entity.component.TransformComponent;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;

// TODO - TIRAR DEPOIS
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public class SpriteRenderSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> mTransform;
    private ComponentMapper<SpriteComponent> mSprite;
    private ComponentMapper<ClientComponent> mClient;
    private ComponentMapper<InteractiveBlock> mInteractiveBlock;

    OrthographicCamera camera;
    SpriteBatch batch;

    // TODO - TIRAR DEPOIS
    private BitmapFont timerFont;

    public SpriteRenderSystem(OrthographicCamera camera) {
        super(Aspect.all(TransformComponent.class).one(SpriteComponent.class, InteractiveBlock.class));
        this.camera = camera;
        batch = new SpriteBatch();
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    // TODO - TIRAR DEPOIS
    @Override
    protected void initialize() {
        // Carrega a fonte FreeType
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 26;
        parameter.color = com.badlogic.gdx.graphics.Color.WHITE;
        timerFont = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    protected void process(int entityId) {
        TransformComponent cTransform = mTransform.get(entityId);
        SpriteComponent cSprite = mSprite.get(entityId);

        if (cSprite != null && cSprite.sprite != null) {
            Sprite sprite = cSprite.sprite;

            if (cTransform.originCenter) {
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
        }

        // Timer dos clientes
        if (mClient.has(entityId)) {
            ClientComponent cClient = mClient.get(entityId);
            if (cClient.inQueue) {
                float blockCenterX = Math.round(cTransform.position.x / Block.TILE_SIZE) * Block.TILE_SIZE;
                float blockCenterY = Math.round(cTransform.position.y / Block.TILE_SIZE) * Block.TILE_SIZE + Block.TILE_SIZE;
                String timeText = String.valueOf((int)Math.ceil(cClient.timeLeft));

                // Exemplo: muda a cor do texto se o tempo estiver acabando
                if (cClient.timeLeft < 3f) {
                    timerFont.setColor(1, 0, 0, 1); // vermelho
                } else if (cClient.timeLeft < 7f) {
                    timerFont.setColor(1, 1, 0, 1); // amarelo
                } else {
                    timerFont.setColor(0, 0, 0, 1); // preto
                }

                timerFont.draw(batch, timeText, blockCenterX, blockCenterY + Block.TILE_SIZE);
                timerFont.setColor(0, 0, 0, 1); // reset cor
            }
        }

        // Timer dos blocos interativos
        if (mInteractiveBlock.has(entityId)) {
            InteractiveBlock cBlock = mInteractiveBlock.get(entityId);
            if (cBlock.timerActive && cBlock.timeLeft > 0f) {
                float blockCenterX = Math.round(cTransform.position.x / Block.TILE_SIZE) * Block.TILE_SIZE;
                float blockCenterY = Math.round(cTransform.position.y / Block.TILE_SIZE) * Block.TILE_SIZE + Block.TILE_SIZE;
                String timeText = String.valueOf((int)Math.ceil(cBlock.timeLeft));

                timerFont.draw(batch, timeText, blockCenterX, blockCenterY + Block.TILE_SIZE);
                timerFont.setColor(0, 0, 0, 1);
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
        if (timerFont != null) timerFont.dispose();
    }
}
