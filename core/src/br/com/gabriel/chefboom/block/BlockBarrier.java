package br.com.gabriel.chefboom.block;

public class BlockBarrier extends Block {

    public BlockBarrier() {
        super(null);
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
