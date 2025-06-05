package br.com.gabriel.chefboom.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class ClientComponent extends Component {

    public boolean canWalk = true;

    public float walkSpeed = 100;

    public Vector2 position = new Vector2();
}
