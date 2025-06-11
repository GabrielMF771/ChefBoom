package br.com.gabriel.chefboom.entity.component;

import com.artemis.Component;

public class InteractiveBlock extends Component {
    public enum Type {
        PLATE,
        TRASH,
    }

    public Type type;
}
