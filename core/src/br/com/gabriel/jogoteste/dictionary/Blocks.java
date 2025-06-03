package br.com.gabriel.jogoteste.dictionary;

import br.com.gabriel.jogoteste.block.Block;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.IntMap;

public class Blocks {
    //CRIANDO O INTMAP - ELE É TIPO UMA LISTA QUE TEM VARIOS IDS SEGUIDOS, CADA ID CORRESPONDE
    // A UM BLOCO
    public static final IntMap<Block> REGISTRY = new IntMap<Block>();

    //REGISTRO DOS BLOCOS E SEUS IDS
    public static final int AIR_ID = 0;

    public static final Block AIR;
    public static final Block GRASS;
    public static final Block STONE;


    public static Block getBlockById(int id) {
        return REGISTRY.get(id);
    }

    public static int getIdByBlock(Block block) {
        return REGISTRY.findKey(block, true, AIR_ID);
    }

    //REGISTRA OS BLOCOS NO INTMAP
    private static Block register(int id, Block block) {
        REGISTRY.put(id, block);
        return block;
    }

    static {
        //CHAMANDO A FUNÇÃO Q REGISTRA
        AIR = register(AIR_ID, new Block(null));
        GRASS = register(1, new Block(new Texture("grass.png")));
        STONE = register(2, new Block(new Texture("stone.png")));
    }
}
