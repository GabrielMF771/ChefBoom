package br.com.gabriel.jogoteste.dictionary;

import br.com.gabriel.jogoteste.block.Block;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.IntMap;

public class Blocks {
    //CRIANDO O INTMAP - ELE É TIPO UMA LISTA QUE TEM VARIOS IDS SEGUIDOS, CADA ID CORRESPONDE
    // A UM BLOCO - TIPO UM ARMAZEM DE IDS
    public static final IntMap<Block> REGISTRY = new IntMap<Block>();

    //REGISTRO DOS BLOCOS E SEUS IDS
    public static final int AIR_ID = 0;

    public static final Block AIR;
    public static final Block GRASS;


    //RETORNA O BLOCO APOS INSERIR O ID
    // (AMBAS FUÇÕES SÓ FUNCIONAM SE O BLOCO ESTIVER REGISTRADO)
    public static Block getBlockById(int id) {
        return REGISTRY.get(id);
    }

    //RETORNA O ID APOS INSERIR O BLOCO
    // (AMBAS FUÇÕES SÓ FUNCIONAM SE O BLOCO ESTIVER REGISTRADO)
    public static int getIdByBlock(Block block) {
        return REGISTRY.findKey(block, true, AIR_ID);
    }

    //REGISTRA OS BLOCOS NO INTMAP
    private static Block register(int id, Block block) {
        REGISTRY.put(id, block);
        return block;
    }

    static {
        //CHAMANDO A FUNÇÃO Q REGISTRA COLOCANDO OS PARAMETROS E TEXTURA DO BLOCO
        AIR = register(AIR_ID, new Block(null));
        GRASS = register(1, new Block(new Texture("grass.png")));
    }
}
