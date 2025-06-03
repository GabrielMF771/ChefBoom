package br.com.gabriel.jogoteste.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

//CLASSE SÓ PRA GUARDAR UMA VARIAVEL DO TIPO SPRITE DO PROPRIO LIBGDX
public class SpriteComponent extends Component {
    //VAR TIPO SPRITE
    public Sprite sprite;

    public boolean flipX;
    public boolean flipY;
}
