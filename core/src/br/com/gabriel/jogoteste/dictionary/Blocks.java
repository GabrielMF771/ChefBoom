package br.com.gabriel.jogoteste.dictionary;

import br.com.gabriel.jogoteste.block.Block;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.IntMap;

public class Blocks {

    public static final IntMap<Block> REGISTRY = new IntMap<Block>();

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

    private static Block register(int id, Block block) {
        REGISTRY.put(id, block);
        return block;
    }

    static {
        AIR = register(AIR_ID, new Block(null));
        GRASS = register(1, new Block(new Texture("grass.png")));
        STONE = register(2, new Block(new Texture("stone.png")));
    }
}
