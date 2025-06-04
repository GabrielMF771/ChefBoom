package br.com.gabriel.chefboom.dictionary;

import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.block.BlockAir;
import br.com.gabriel.chefboom.resource.Assets;
import com.badlogic.gdx.utils.IntMap;

public class Blocks {
    //CRIANDO O INTMAP - ELE É TIPO UMA LISTA QUE TEM VARIOS IDS SEGUIDOS, CADA ID CORRESPONDE
    // A UM BLOCO - TIPO UM ARMAZEM DE IDS
    public static final IntMap<Block> REGISTRY = new IntMap<Block>();

    //REGISTRO DOS BLOCOS E SEUS IDS
    public static final int AIR_ID = 0;

    public static final Block AIR;
    public static final Block BARRIER;
    public static final Block GROUND1;
    public static final Block GROUND2;


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
        AIR = register(AIR_ID, new BlockAir());
        BARRIER = register(1, new Block(Assets.manager.get(Assets.barrier)));
        GROUND1 = register(2, new Block(Assets.manager.get(Assets.ground1)));
        GROUND2 = register(3, new Block(Assets.manager.get(Assets.ground2)));
    }
}
