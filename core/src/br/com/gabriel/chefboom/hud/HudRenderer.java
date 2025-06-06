package br.com.gabriel.chefboom.hud;

import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.resource.Assets;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HudRenderer {
    private final Texture hudImageTexture;

    public HudRenderer() {
        this.hudImageTexture = Assets.manager.get(Assets.hudbackground);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        float imageHeight = 3 * Block.TILE_SIZE;
        float imageWidth = camera.viewportWidth;
        batch.draw(hudImageTexture, 0, camera.viewportHeight - imageHeight, imageWidth, imageHeight);


    }
}