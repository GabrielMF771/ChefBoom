package br.com.gabriel.chefboom.entity.state;

import br.com.gabriel.chefboom.entity.component.CollidableComponent;
import br.com.gabriel.chefboom.entity.component.RigidBodyComponent;
import br.com.gabriel.chefboom.entity.component.StateComponent;
import br.com.gabriel.chefboom.entity.component.TransformComponent;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum PlayerState implements State<Entity> {
    Idle {
        @Override
        public void update(Entity entity) {
            super.update(entity);

            TransformComponent cTransform = mTransform.get(entity);
            RigidBodyComponent cRigidBody = mRigidBody.get(entity);
            CollidableComponent cCollidable = mCollidable.get(entity);
            StateComponent cState = mState.get(entity);

            if(cRigidBody.velocity.x != 0 || cRigidBody.velocity.y != 0){
                cState.state.changeState(Walk);
            }

        }
    },
    Walk {
        @Override
        public void update(Entity entity) {
            super.update(entity);

            TransformComponent cTransform = mTransform.get(entity);
            RigidBodyComponent cRigidBody = mRigidBody.get(entity);
            CollidableComponent cCollidable = mCollidable.get(entity);
            StateComponent cState = mState.get(entity);

            if(cRigidBody.velocity.x == 0 && cRigidBody.velocity.y == 0) {
                cState.state.changeState(Idle);
            }
        }
    };

    protected ComponentMapper<TransformComponent> mTransform;
    protected ComponentMapper<RigidBodyComponent> mRigidBody;
    protected ComponentMapper<CollidableComponent> mCollidable;
    protected ComponentMapper<StateComponent> mState;

    private boolean alreadyInit;

    @Override
    public void enter(Entity entity) {
        init(entity);

        /*
        if(ChefBoom.DEBUG){
            Gdx.app.log("PlayerState", this.toString());
        }
        */
    }

    @Override
    public void update(Entity entity) {
        init(entity);

    }

    @Override
    public void exit(Entity entity) {

    }

    public void init(Entity entity) {
        if(!alreadyInit) {
            entity.getWorld().inject(this);
            alreadyInit = true;
        }
    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }
}
