import org.junit.Assert;
import org.junit.Test;

import coding.Bit;
import coding.BitChain;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class BitChainTest {
    @Test
    public void appendBitChain_test() {
        //aufbauen von zwei Bitketten
        BitChain chain = new BitChain();
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);

        BitChain other = new BitChain();
        other.append(Bit.ONE);
        other.append(Bit.ZERO);
        other.append(Bit.ZERO);
        other.append(Bit.ONE);

        //anhaengen einer Bitkette
        chain.append(other);
        Assert.assertEquals("1101001",chain.toString());
    }

    @Test
    public void copyBitChain_test() {
        //aufbauen von Bitkette
        BitChain chain = new BitChain();
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);

        //kopieren der Bitkette
        BitChain copy = chain.copy();
        Assert.assertEquals("1101",copy.toString());
    }

    @Test
    public void lengthBitChain_test() {
        //aufbauen von Bitkette
        BitChain chain = new BitChain();
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);

        //laenge der Bitkette pruefen
        Assert.assertEquals(4, chain.length());
    }

    @Test
    public void toArrayBitChain_test() {
        //aufbauen von Bitkette
        BitChain chain = new BitChain();
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);

        //array anlegen
        Bit[] correctArray = {Bit.ONE, Bit.ONE, Bit.ZERO, Bit.ONE};
        Bit[] bitChainArray = chain.toArray();
        //arrays vergleichen
        Assert.assertArrayEquals(correctArray, bitChainArray);
    }

    @Test
    public void writeTo_test() throws IOException {
        //aufbauen von Bitkette
        BitChain chain = new BitChain();
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);

        FileOutputStream out = new FileOutputStream("test/result");
        chain.writeTo(out);
    }

    @Test
    public void writeToMoreBits_test() throws IOException {
        //aufbauen von Bitkette
        BitChain chain = new BitChain();
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);

        FileOutputStream out = new FileOutputStream("test/result");
        chain.writeTo(out);
    }
    
    @Test
    public void writeToMoreBits_test2() throws IOException {
        //aufbauen von Bitkette
        BitChain chain = new BitChain();
        chain.append(Bit.ZERO);
        chain.append(Bit.ZERO);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);

        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        chain.writeTo(out);
        
        byte[] array = out.toByteArray();
        
        System.out.println(Arrays.toString(array));
    }

    @Test
    public void writeToNotEnoughBits_test() throws IOException {
        //aufbauen von Bitkette
        BitChain chain = new BitChain();
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);

        FileOutputStream out = new FileOutputStream("test/empty");
        chain.writeTo(out);
    }

    @Test
    public void writeALotOfBits_test() throws IOException {
        //aufbauen von Bitkette
        BitChain chain = new BitChain();
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ONE);
        chain.append(Bit.ZERO);
        chain.append(Bit.ONE);
        chain.append(chain);
        chain.append(chain);
        chain.append(chain);
        System.out.print(chain.toString());


        FileOutputStream out = new FileOutputStream("test/aLotOfBits");
        chain.writeTo(out);
    }
}
