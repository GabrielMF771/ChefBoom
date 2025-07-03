package br.com.gabriel.chefboom.entity.system;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationController {
    private Animation<TextureRegion> animation;
    private float stateTime = 0f;
    private boolean playing = false;

    public AnimationController(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    public void play() {
        playing = true;
        stateTime = 0f;
    }

    public void stop() {
        playing = false;
        stateTime = 0f;
    }

    public void update(float delta) {
        if (playing) {
            stateTime += delta;
            if (animation.isAnimationFinished(stateTime)) {
                playing = false;
            }
        }
    }

    public TextureRegion getCurrentFrame() {
        return animation.getKeyFrame(stateTime, true);
    }

    public boolean isPlaying() {
        return playing;
    }
}