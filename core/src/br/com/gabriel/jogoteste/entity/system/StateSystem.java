package br.com.gabriel.jogoteste.entity.system;

import br.com.gabriel.jogoteste.entity.component.StateComponent;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class StateSystem extends IteratingSystem {

    private ComponentMapper<StateComponent> mState;

    public StateSystem() {
        super(Aspect.all(StateComponent.class));
    }

    @Override
    protected void process(int entityId) {
        mState.get(entityId).state.update();
    }
}
