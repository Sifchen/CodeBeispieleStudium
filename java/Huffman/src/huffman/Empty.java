package huffman;

import java.util.Iterator;
import coding.Bit;
import coding.BitChain;

/**
 * Die Klasse Empty, welche einen leeren Baum darstellt, also einen ohne Blätter und Knoten.
 * 
 * @author Daniel Pigotow
 *
 */
class Empty extends Tree {

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int getByte() {
        throw new IllegalArgumentException();
    }

    @Override
    public long getCount() {
        return 0;
    }

    @Override
    protected void toGraphviz(StringBuilder sb, String prefix) {
    }

    @Override
    protected int translateByte(Iterator<Bit> iterator) {
        throw new IllegalArgumentException();
    }


    @Override
    protected void toCodetable(BitChain bitChain, BitChain[] codetable) {
    }

}
