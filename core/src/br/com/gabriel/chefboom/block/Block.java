package br.com.gabriel.chefboom.block;


import br.com.gabriel.chefboom.world.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Block {
    //DEFINE O TAMANHO DOS TILES DO MUNDO
    public static final int TILE_SIZE = 32;

    public final Texture texture;

    //CONSTRUTOR RECEBE A TEXTURA
    public Block(Texture texture) {
        this.texture = texture;
    }

    public boolean isSolid(){
        return true;
    }

    public Rectangle getTileRectangle(World world, int x, int y) {
        Rectangle rectangle = null;

        if(isSolid()){
            rectangle = new Rectangle(World.mapToWorld(x), World.mapToWorld(y), Block.TILE_SIZE, Block.TILE_SIZE);
        }

        return rectangle;
    }
}