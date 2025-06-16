package br.com.gabriel.chefboom.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class ClientComponent extends Component {

    public boolean canWalk = true;

    public float walkSpeed = 75f;

    public int wantedItemId = -1;

    public boolean inQueue = false;

    public Vector2 position = new Vector2();

    public float timeLeft = 35f; // Tempo em segundos

    public int queueId = -1; // ID do cliente na fila, -1 se não estiver na fila
}
