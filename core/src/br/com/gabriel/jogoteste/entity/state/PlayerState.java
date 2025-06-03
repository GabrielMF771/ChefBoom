package br.com.gabriel.jogoteste.entity.state;

import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum PlayerState implements State<Entity> {
    Idle {
        @Override
        public void update(Entity entity) {
            super.update(entity);
        }
    },
    Walk {
        @Override
        public void update(Entity entity) {
            super.update(entity);
        }
    };


    @Override
    public void enter(Entity entity) {

    }

    @Override
    public void update(Entity entity) {

    }

    @Override
    public void exit(Entity entity) {

    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }
}
