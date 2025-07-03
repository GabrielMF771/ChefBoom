package br.com.gabriel.chefboom.entity.component;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;

public class StateComponent<S extends State<Entity>> extends Component {
    public StateMachine<Entity, S> state;

    public Direction direction = Direction.DOWN;

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
