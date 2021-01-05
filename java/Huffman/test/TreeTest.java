import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import coding.Bit;
import coding.BitChain;
import coding.Huffman;
import huffman.Forest;
import huffman.Statistics;
import huffman.Tree;

public class TreeTest {

    /** Ordner mit den zu kodierenden Dateien */
    public static final String FILES = "test/files/";

    /** Ordner, in den die Zieldateien geschrieben werden */
    public static final String RESULTS = "test/results/";

    @Test
    public void toCodetable_aFewChars() throws IOException {
        String input = "aaaaabbccccddd"; // a:5, b:2, c:4, d:3
        byte[] inputData = input.getBytes();
        Statistics statisticInput = Statistics.analyse(inputData);
        Forest startForest = statisticInput.toForest();
        Tree tree = startForest.toTree();
        BitChain[] codetable = tree.toCodetable();

        BitChain chainA = new BitChain();
        chainA.append(Bit.ZERO);
        Assert.assertEquals(chainA, codetable[97]);

        BitChain chainB = new BitChain();
        chainB.append(Bit.ONE);
        chainB.append(Bit.ONE);
        chainB.append(Bit.ZERO);
        Assert.assertEquals(chainB, codetable[98]);

        BitChain chainC = new BitChain();
        chainC.append(Bit.ONE);
        chainC.append(Bit.ZERO);
        Assert.assertEquals(chainC, codetable[99]);

        BitChain chainD = new BitChain();
        chainD.append(Bit.ONE);
        chainD.append(Bit.ONE);
        chainD.append(Bit.ONE);
        Assert.assertEquals(chainD, codetable[100]);

        for (int i = 0; i < 97; i++) {
            Assert.assertEquals(0, codetable[i].length());
        }
        for (int i = 101; i < 256; i++) {
            Assert.assertEquals(0, codetable[i].length());
        }
    }

    @Test
    public void toGraphviz_exampleAssignment() {
        String input = "aaaaabbccccddd"; // a:5, b:2, c:4, d:3
        byte[] inputData = input.getBytes();
        Statistics statisticInput = Statistics.analyse(inputData);
        Forest startForest = statisticInput.toForest();
        Tree tree = startForest.toTree();
        String result = tree.toGraphviz();
        System.out.print(result);
    }

    @Test
    public void toGraphviz_eineTestDatei() throws IOException {
        byte[] inputData = Files.readAllBytes(Paths.get(FILES + "eineTextdatei"));
        Statistics statisticInput = Statistics.analyse(inputData);
        Forest startForest = statisticInput.toForest();
        Tree tree = startForest.toTree();
        String result = tree.toGraphviz();
        System.out.print(result);
    }

    @Test
    public void eineTextdatei_decode() throws IOException {
        new File(RESULTS + "eineTextdatei.code").delete();
        Assert.assertEquals("encode: Anzahl an Bits", 80,
                Huffman.encode(FILES + "eineTextdatei", RESULTS + "eineTextdatei.code"));

        new File(RESULTS + "eineTextdatei").delete();
        Huffman.decode(RESULTS + "eineTextdatei.code", RESULTS + "eineTextdatei");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "eineTextdatei",
                RESULTS + "eineTextdatei");
    }

}
