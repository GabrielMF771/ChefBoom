package br.com.gabriel.chefboom.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent extends Component {

    //INFORMÇÃES DE TRANSFORMÇÃO

    public final Vector2 position = new Vector2();

    public final Vector2 origin = new Vector2();

    public boolean originCenter = false;

    public float rotation = 0;

    //ESCALA DO PLAYER - SE QUISER AUMENTAR O TAMANHO SO MEXER AQUI
    public float scaleX = 1;

    public float scaleY = 1;
}
