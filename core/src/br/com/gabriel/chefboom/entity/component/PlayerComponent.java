package br.com.gabriel.chefboom.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class PlayerComponent extends Component {

    public boolean canWalk = true;

    public float walkSpeed = 150f;

    public int hp = 3;

    public Vector2 position = new Vector2();

    public Integer heldItemEntity = null;

    public float invulnerableTime = 0f;
}
