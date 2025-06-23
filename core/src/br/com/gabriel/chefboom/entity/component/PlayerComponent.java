package br.com.gabriel.chefboom.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class PlayerComponent extends Component {

    public int playerId = -1;

    public boolean canWalk = true;

    public float walkSpeed = 150f;

    //TODO - SE MEXER NA VIDA, TEM Q MEXER NO PARAMETRO DA LEVELENDED (VidasRestantes)
    public int hp = 3;

    public Vector2 position = new Vector2();

    public Integer heldItemEntity = null;

    public boolean isDashing = false;
    public float dashCooldown = 0f;
    public float dashTimeLeft = 0f;
    public float dashDirX = 0;
    public float dashDirY = 0;
    public float dashSpeed = 300f; // velocidade do dash
    public float dashTime = 0.18f; // duração do dash em segundos
    public float getDashCooldownTime = 1f; // Tempo de cooldown do dash
}
