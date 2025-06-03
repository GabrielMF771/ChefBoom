package br.com.gabriel.jogoteste.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Rectangle;

public class CollidableComponent extends Component {

    public final Rectangle collisionBox = new Rectangle();

    // Tamanho original, sem escala
    public float baseWidth = 32;

    public float baseHeight = 32;

    // Verificações da colisão nas 4 direções
    public boolean onGround;

    public boolean onCeiling;

    public boolean onLeftWall;

    public boolean onRightWall;
}
