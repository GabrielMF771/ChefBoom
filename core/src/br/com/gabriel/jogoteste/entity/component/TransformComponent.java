package br.com.gabriel.jogoteste.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import br.com.gabriel.jogoteste.entity.component.TransformComponent;

public class TransformComponent extends Component {

    //INFORMÇÃES DE TRANSFORMÇÃO

    public final Vector2 position = new Vector2();

    public final Vector2 origin = new Vector2();

    public boolean originCenter = false;

    public float rotation = 0;

    //ESCALA DO PLAYER - SE QUISER AUMENTAR O TAMANHO SO MEXER AQUI
    public float scaleX = 2;

    public float scaleY = 2;
}
