package br.com.gabriel.jogoteste.block;


import com.badlogic.gdx.graphics.Texture;

public class Block {
    //DEFINE O TAMANHO DOS TILES DO MUNDO
    public static final int TILE_SIZE = 32;

    public final Texture texture;
    //CONSTRUTOR RECEBE A TEXTURA
    public Block(Texture texture) {
        this.texture = texture;
    }

}