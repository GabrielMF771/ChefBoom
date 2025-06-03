package br.com.gabriel.jogoteste.entity.component;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;

public class StateComponent<S extends State<Entity>> extends Component {
    public StateMachine<Entity, S> state;


}
