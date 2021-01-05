import org.junit.Assert;
import org.junit.Test;

import coding.Huffman;
import huffman.Tree;

import java.io.File;
import java.io.IOException;

public class BeispielTest {
    /** Ordner mit den zu kodierenden Dateien */
    public static final String FILES = "test/files/";

    /** Ordner, in den die Zieldateien geschrieben werden */
    public static final String RESULTS = "test/results/";

    @Test
    public void eineTextdatei_filesize() throws IOException {
        new File("test/results/eineTextdatei.code").delete();
        Assert.assertEquals("encode: Anzahl an Bits", 80,
                Huffman.encode(FILES + "eineTextdatei", RESULTS + "eineTextdatei.code"));

        Assert.assertEquals("encode: Dateigröße des Ergebnisses", 2066,
                new File(RESULTS + "eineTextdatei.code").length());
        // 2048 Bytes Statistik + 4 Bytes Blockanzahl + 1 * 4 Bytes Blockgröße(n) +
        // 1 * 10 Bytes Code = 2066 Bytes
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

    @Test
    public void ab_decode() throws IOException {
        new File(RESULTS + "ab.code").delete();
        Huffman.encode(FILES + "ab", RESULTS + "ab.code", 1);
        new File(RESULTS + "ab").delete();
        Huffman.decode(RESULTS + "ab.code", RESULTS + "ab");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "ab",
                RESULTS + "ab");
    }

    @Test
    public void ab_decode2T() throws IOException {
        new File(RESULTS + "ab2T.code").delete();
        Huffman.encode(FILES + "ab", RESULTS + "ab2T.code", 2);
        new File(RESULTS + "ab2T").delete();
        Huffman.decode(RESULTS + "ab2T.code", RESULTS + "ab2T");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "ab",
                RESULTS + "ab2T");
    }

    @Test
    public void ab_decode3T() throws IOException {
        new File(RESULTS + "ab3T.code").delete();
        Huffman.encode(FILES + "ab", RESULTS + "ab3T.code", 3);
        new File(RESULTS + "ab3T").delete();
        Huffman.decode(RESULTS + "ab3T.code", RESULTS + "ab3T");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "ab",
                RESULTS + "ab3T");
    }

    @Test
    public void abc_decode() throws IOException {
        new File(RESULTS + "abc.code").delete();
        Huffman.encode(FILES + "abc", RESULTS + "abc.code", 1);
        new File(RESULTS + "abc").delete();
        Huffman.decode(RESULTS + "abc.code", RESULTS + "abc");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "abc",
                RESULTS + "abc");
    }

    @Test
    public void abc_decode2T() throws IOException {
        new File(RESULTS + "abc2T.code").delete();
        Huffman.encode(FILES + "abc", RESULTS + "abc2T.code", 2);
        new File(RESULTS + "abc2T").delete();
        Huffman.decode(RESULTS + "abc2T.code", RESULTS + "abc2T");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "abc",
                RESULTS + "abc2T");
    }

    @Test
    public void abc_decode3T() throws IOException {
        new File(RESULTS + "abc3T.code").delete();
        Huffman.encode(FILES + "abc", RESULTS + "abc3T.code", 3);
        new File(RESULTS + "abc3T").delete();
        Huffman.decode(RESULTS + "abc3T.code", RESULTS + "abc3T");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "abc",
                RESULTS + "abc3T");
    }

    @Test
    public void abcd_decode() throws IOException {
        new File(RESULTS + "abcd.code").delete();
        Huffman.encode(FILES + "abcd", RESULTS + "abcd.code", 1);
        new File(RESULTS + "abcd").delete();
        Huffman.decode(RESULTS + "abcd.code", RESULTS + "abcd");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "abcd",
                RESULTS + "abcd");
    }

    @Test
    public void abcd_decode2T() throws IOException {
        new File(RESULTS + "abcd2T.code").delete();
        Huffman.encode(FILES + "abcd", RESULTS + "abcd2T.code", 2);
        new File(RESULTS + "abcd2T").delete();
        Huffman.decode(RESULTS + "abcd2T.code", RESULTS + "abcd2T");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "abcd",
                RESULTS + "abcd2T");
    }
     @Test
     public void abcd_decode3T() throws IOException {
     new File(RESULTS + "abcd3T.code").delete();
     Huffman.encode(FILES + "abcd", RESULTS + "abcd3T.code", 3);
     new File(RESULTS + "abcd3T").delete();
     Huffman.decode(RESULTS + "abcd3T.code", RESULTS + "abcd3T");
     TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "abcd",
     RESULTS + "abcd3T");
     }

    @Test
    public void binary_decode() throws IOException {
        new File(RESULTS + "binary.code").delete();
        Huffman.encode(FILES + "binary", RESULTS + "binary.code", 1);
        new File(RESULTS + "binary").delete();
        Huffman.decode(RESULTS + "binary.code", RESULTS + "binary");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "binary",
                RESULTS + "binary");
    }

     @Test
     public void german_decode() throws IOException {
     new File(RESULTS + "german.code").delete();
     Huffman.encode(FILES + "german", RESULTS + "german.code", 1);
     new File(RESULTS + "german").delete();
     Huffman.decode(RESULTS + "german.code", RESULTS + "german");
     TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "german",
     RESULTS + "german");
     }

     @Test
     public void german_Aalquappen_decode() throws IOException {
     new File(RESULTS + "german_Aalquappen.code").delete();
     Huffman.encode(FILES + "german_Aalquappen", RESULTS + "german_Aalquappen.code", 1);
     new File(RESULTS + "german_Aalquappen").delete();
     Huffman.decode(RESULTS + "german_Aalquappen.code", RESULTS + "german_Aalquappen");
     TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "german_Aalquappen",
     RESULTS + "german_Aalquappen");
     }
     
     @Test
     public void german_Aalfangverbote_decode() throws IOException {
         test_encode_decode("01-german_Aalfangverbote");
     }
     
     @Test
     public void minify2() throws IOException {
         test_encode_decode("02-vorne-weg");
     }
     public void test_encode_decode(String prefix) throws IOException {
         String original = FILES + prefix;
         String encoded = RESULTS + prefix + ".code";
         String decoded = RESULTS + prefix;
         
         new File(encoded).delete();
         Huffman.encode(original, encoded, 1);
         new File(decoded).delete();
         Huffman.decode(encoded, decoded);
         TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", original,
                 decoded);
     }
     
     
     @Test
     //Geht Auch nicht ist dieselbe groesse wie die groesste germanDatei die funktioniert
     public void random_1015b_decode() throws IOException {
         new File(RESULTS + "random_1015b.code").delete();
         Huffman.encode(FILES + "random_1015b", RESULTS + "random_1015b.code", 1);
         new File(RESULTS + "random_1015b").delete();
         Huffman.decode(RESULTS + "random_1015b.code", RESULTS + "random_1015b");
         TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "random_1015b",
                 RESULTS + "random_1015b");
     }
     
     @Test
     public void random_40b_decode() throws IOException {
         new File(RESULTS + "random_40b.code").delete();
         Huffman.encode(FILES + "random_40b", RESULTS + "random_40b.code", 1);
         new File(RESULTS + "random_40b").delete();
         Huffman.decode(RESULTS + "random_40b.code", RESULTS + "random_40b");
         TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "random_40b",
                 RESULTS + "random_40b");
     }
     
     @Test
     public void random_20b_decode() throws IOException {
         new File(RESULTS + "random_20b.code").delete();
         Huffman.encode(FILES + "random_20b", RESULTS + "random_20b.code", 1);
         new File(RESULTS + "random_20b").delete();
         Huffman.decode(RESULTS + "random_20b.code", RESULTS + "random_20b");
         TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "random_20b",
                 RESULTS + "random_20b");
     }
     
     @Test
     public void german_Aalfaengers_decode() throws IOException {
         new File(RESULTS + "german_Aalfaengers.code").delete();
         Huffman.encode(FILES + "german_Aalfaengers", RESULTS + "german_Aalfaengers.code", 1);
         new File(RESULTS + "german_Aalfaengers").delete();
         Huffman.decode(RESULTS + "german_Aalfaengers.code", RESULTS + "german_Aalfaengers");
         TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "german_Aalfaengers",
                 RESULTS + "german_Aalfaengers");
     }
     
     @Test
     public void german_Aalfaengerin_decode() throws IOException {
         new File(RESULTS + "german_Aalfaengerin.code").delete();
         Huffman.encode(FILES + "german_Aalfaengerin", RESULTS + "german_Aalfaengerin.code", 1);
         new File(RESULTS + "german_Aalfaengerin").delete();
         Huffman.decode(RESULTS + "german_Aalfaengerin.code", RESULTS + "german_Aalfaengerin");
         TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "german_Aalfaengerin",
                 RESULTS + "german_Aalfaengerin");
     }
     
     @Test
     public void german_Aalfang_decode() throws IOException {
         new File(RESULTS + "german_Aalfang.code").delete();
         Huffman.encode(FILES + "german_Aalfang", RESULTS + "german_Aalfang.code", 1);
         new File(RESULTS + "german_Aalfang").delete();
         Huffman.decode(RESULTS + "german_Aalfang.code", RESULTS + "german_Aalfang");
         TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "german_Aalfang",
                 RESULTS + "german_Aalfang");
     }

    @Test
    public void german_Aalbüschelwels_decode() throws IOException {
        new File(RESULTS + "german_Aalbüschelwels.code").delete();
        Huffman.encode(FILES + "german_Aalbüschelwels", RESULTS + "german_Aalbüschelwels.code", 1);
        new File(RESULTS + "german_Aalbüschelwels").delete();
        Huffman.decode(RESULTS + "german_Aalbüschelwels.code", RESULTS + "german_Aalbüschelwels");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "german_Aalbüschelwels",
                RESULTS + "german_Aalbüschelwels");
    }
    
    @Test
    public void german_Aalbueschelwelse_decode() throws IOException {
        new File(RESULTS + "german_Aalbueschelwelse.code").delete();
        Huffman.encode(FILES + "german_Aalbueschelwelse", RESULTS + "german_Aalbueschelwelse.code", 1);
        new File(RESULTS + "german_Aalbueschelwelse").delete();
        Huffman.decode(RESULTS + "german_Aalbueschelwelse.code", RESULTS + "german_Aalbueschelwelse");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "german_Aalbueschelwelse",
                RESULTS + "german_Aalbueschelwelse");
    }

    @Test
    public void german_Aalbueschelwelsen_Aalquappen_decode() throws IOException {
        new File(RESULTS + "german_Aalbueschelwelsen_Aalquappen.code").delete();
        Huffman.encode(FILES + "german_Aalbueschelwelsen_Aalquappen", RESULTS + "german_Aalbueschelwelsen_Aalquappen.code", 1);
        new File(RESULTS + "german_Aalbueschelwelsen_Aalquappen").delete();
        Huffman.decode(RESULTS + "german_Aalbueschelwelsen_Aalquappen.code", RESULTS + "german_Aalbueschelwelsen_Aalquappen");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "german_Aalbueschelwelsen_Aalquappen",
                RESULTS + "german_Aalbueschelwelsen_Aalquappen");
    }

    @Test
    public void eineTextdatei_decode_2Threads() throws IOException {
        new File(RESULTS + "eineTextdatei2T.code").delete();
        Assert.assertEquals("encode: Anzahl an Bits", 80,
                Huffman.encode(FILES + "eineTextdatei", RESULTS + "eineTextdatei2T.code", 2));

        new File(RESULTS + "eineTextdatei2T").delete();
        Huffman.decode(RESULTS + "eineTextdatei2T.code", RESULTS + "eineTextdatei2T");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "eineTextdatei",
                RESULTS + "eineTextdatei2T");
    }

    @Test
    public void eineTextdatei_decode_3Threads() throws IOException {
        new File(RESULTS + "eineTextdatei3T.code").delete();
        Assert.assertEquals("encode: Anzahl an Bits", 80,
                Huffman.encode(FILES + "eineTextdatei", RESULTS + "eineTextdatei3T.code", 3));

        new File(RESULTS + "eineTextdatei3T").delete();
        Huffman.decode(RESULTS + "eineTextdatei3T.code", RESULTS + "eineTextdatei3T");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "eineTextdatei",
                RESULTS + "eineTextdatei3T");
    }

    @Test
    public void empty() throws IOException { // leere Datei
        new File(RESULTS + "empty.code").delete();
        Assert.assertEquals("encode: Anzahl an Bits", 0,
                Huffman.encode(FILES + "empty", RESULTS + "empty.code"));

        Assert.assertEquals("encode: Dateigröße des Ergebnisses", 2056,
                new File(RESULTS + "empty.code").length());
        // 2048 Bytes Statistik + 4 Bytes Blockanzahl + 1 * 4 Bytes Blockgröße(n) +
        // 1 * 0 Bytes Code = 2056 Bytes

        new File(RESULTS + "empty").delete();
        Huffman.decode(RESULTS + "empty.code", RESULTS + "empty");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "empty",
                RESULTS + "empty");
    }

    @Test
    public void aaa() throws IOException { // 3 gleiche Symbole
        new File(RESULTS + "aaa.code").delete();
        Assert.assertEquals("encode: Anzahl an Bits", 0,
                Huffman.encode(FILES + "aaa", RESULTS + "aaa.code"));

        Assert.assertEquals("encode: Dateigröße des Ergebnisses", 2056,
                new File(RESULTS + "aaa.code").length());
        // 2048 Bytes Statistik + 4 Bytes Blockanzahl + 1 * 4 Bytes Blockgröße(n) +
        // 1 * 0 Bytes Code = 2056 Bytes

        new File(RESULTS + "aaa").delete();
        Huffman.decode(RESULTS + "aaa.code", RESULTS + "aaa");
        TestToolkit.assertFilesAreEqual("decode: Ausgabedatei ist falsch", FILES + "aaa",
                RESULTS + "aaa");
    }

    @Test
    public void example_toGraphviz() {
        Tree tree = Tree.merge(Tree.fromByte(1, 1),
                Tree.merge(Tree.fromByte(2, 2), Tree.fromByte(3, 3)));

        Assert.assertEquals("digraph G {\n_ [label=\"(6)\"]\n"
                + "_ -> _0 [label=\"0\"]\n_ -> _1 [label=\"1\"]\n"
                + "_0 [label=\"1 (1)\"]\n_1 [label=\"(5)\"]\n"
                + "_1 -> _10 [label=\"0\"]\n_1 -> _11 [label=\"1\"]\n"
                + "_10 [label=\"2 (2)\"]\n_11 [label=\"3 (3)\"]\n}\n", tree.toGraphviz());
    }

//     @Test
//     public void time_measure_100mb() throws IOException {
//     new File(RESULTS + "big_file_100mb").delete();
//     new File(RESULTS + "big_file_100mb.code").delete();
//     // Beispiel zur Messung der Laufzeit
//     // Die Datei "big_file" muss im files-Ordner angelegt oder der Aufruf angepasst werden.
//     long timeStart = System.currentTimeMillis();
//     Huffman.encode(FILES + "big_file_100mb", RESULTS + "big_file_100mb.code", 4);
//     Huffman.decode(RESULTS + "big_file_100mb.code", RESULTS + "big_file_100mb");
//     System.out.println("Dauer: " + (System.currentTimeMillis() - timeStart) + " ms");
//    
//     }

     @Test
     public void time_measure_AndENCODE_DECODE2mb() throws IOException {
     new File(RESULTS + "big_file_2mb").delete();
     new File(RESULTS + "big_file_2mb.code").delete();
     // Beispiel zur Messung der Laufzeit
     // Die Datei "big_file" muss im files-Ordner angelegt oder der Aufruf angepasst werden.
     long timeStart = System.currentTimeMillis();
     Huffman.encode(FILES + "big_file_2mb", RESULTS + "big_file_2mb.code", 4);
     Huffman.decode(RESULTS + "big_file_2mb.code", RESULTS + "big_file_2mb");
     System.out.println("Dauer: " + (System.currentTimeMillis() - timeStart) + " ms");
     TestToolkit.assertFilesAreEqual("decode/encode: Ausgabedatei ist falsch",
     FILES + "big_file_2mb", RESULTS + "big_file_2mb");
     }

     @Test
     public void time_measureAndENCODE_DECODE4T() throws IOException {
     new File(RESULTS + "big_file_1mb").delete();
     new File(RESULTS + "big_file_1mb.code").delete();
     // Beispiel zur Messung der Laufzeit
     // Die Datei "big_file" muss im files-Ordner angelegt oder der Aufruf angepasst werden.
     long timeStart = System.currentTimeMillis();
     Huffman.encode(FILES + "big_file_1mb", RESULTS + "big_file_1mb.code", 4);
     Huffman.decode(RESULTS + "big_file_1mb.code", RESULTS + "big_file_1mb");
     System.out.println("Dauer: " + (System.currentTimeMillis() - timeStart) + " ms");
     TestToolkit.assertFilesAreEqual("decode/encode: Ausgabedatei ist falsch",
     FILES + "big_file_1mb", RESULTS + "big_file_1mb");
     }

}
