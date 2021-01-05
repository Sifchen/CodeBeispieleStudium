package coding;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import huffman.Forest;
import huffman.Statistics;
import huffman.Tree;

/**
 * Stellt Methoden für das Kodieren und Dekodieren von Dateien mit Hilfe der Huffman-Kodierung zur
 * Verfügung.
 * 
 * @author kar, mhe, Daniel Pigotow
 */
public class Huffman {

    /**
     * Hilfsmethode die die Threads in einem Array joint
     * 
     * @param threads das Array der zu Joinenden Threads
     */
    private static void joinThreads(Thread[] threads) {
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                // not supposed to happen
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Kodiert die übergebene Quellatei in die übergebene Zieldatei. Dabei werden so viele Blöcke
     * erzeugt, wie vom Aufrufer angegeben.
     * 
     * @param srcPath Pfad zur Quelldatei, darf nicht null sein
     * @param dstPath Pfad zur Zieldatei, darf nicht null sein
     * @param blockCount Anzahl der zu erzeugenden Blöcke, muss &ge; 1 sein
     * @return Anzahl der Bits der Kodierung (ohne Padding-Bits)
     * @throws IOException Fehler beim Lesen oder Schreiben der Dateien
     */
    public static long encode(String srcPath, String dstPath, int blockCount) throws IOException {
        if (srcPath == null) {
            throw new IllegalArgumentException("srcPath darf nicht null sein");
        }
        if (dstPath == null) {
            throw new IllegalArgumentException("dstPath darf nicht null sein");
        }
        if (blockCount < 1) {
            throw new IllegalArgumentException("blockCount muss >= 1 sein");
        }
        byte[] inputBytes = Files.readAllBytes(Paths.get(srcPath));

        long amountBitsEncoded = 0;

        // Statistik erstellen
        Statistics statistic = Statistics.analyse(inputBytes);
        // Wald erstellen
        Forest forest = statistic.toForest();
        // Huffmanbaum erstellen
        Tree codingTree = forest.toTree();
        // Kodierungstabelle erstellen
        BitChain[] codetable = codingTree.toCodetable();
        // Anlegen von Array mit einzelnen encoderTasks
        EncoderTask[] encoderTasks = new EncoderTask[blockCount];
        // Anlegen von Array mit einzelenen Threads
        Thread[] threads = new Thread[blockCount];
        // Laenge von Datei holen fuer Formel
        long statisticSum = statistic.sum();
        int currStartIdx = 0;

        for (int i = 0; i < blockCount; i++) {
            // Aufteilen nach Formel
            int currBlockSize =
                    (int) (statisticSum / blockCount + (i < (statisticSum % blockCount) ? 1 : 0));
            // EncoderTask erstellen und Array ablegen
            encoderTasks[i] = new EncoderTask(codetable, inputBytes, currStartIdx, currBlockSize);
            // Startindex nun erhoehen
            currStartIdx += currBlockSize;
            // Aus der Task ein Thread erzeugen
            threads[i] = new Thread(encoderTasks[i]);
            // Thread starten
            threads[i].start();
        }
        // Ueber alle Threads laufen und Joinen
        joinThreads(threads);

        // In Datei Schreiben
        DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(dstPath));
        // Schreibe Statistik
        statistic.writeToFile(outputStream);
        // Schreibe Anzahl der Bloecke
        outputStream.writeInt(blockCount);
        // Schreibe die einzelnen Blockgroessen
        for (int i = 0; i < encoderTasks.length; i++) {
            outputStream.writeInt(encoderTasks[i].getResult().length);
        }
        // Schreibe Codierte Zeichen
        for (int i = 0; i < encoderTasks.length; i++) {
            outputStream.write(encoderTasks[i].getResult());
            amountBitsEncoded += encoderTasks[i].getBitsInResult();
        }
        outputStream.close();
        return amountBitsEncoded;
    }

    /**
     * Kodiert die übergebene Datei mit einem Block.
     * 
     * @param srcPath Pfad zur Quelldatei, darf nicht null sein
     * @param dstPath Pfad zur Zieldatei, darf nicht null sein
     * @return Anzahl der Bits der Kodierung (ohne Padding-Bits)
     * @throws IOException Fehler beim Lesen oder Schreiben der Dateien
     */
    public static long encode(String srcPath, String dstPath) throws IOException {
        return encode(srcPath, dstPath, 1);
    }

    /**
     * Dekodiert die übergebene Quelldatei in die übergebene Zieldatei.
     * 
     * @param srcPath Pfad zur Quelldatei, darf nicht null sein
     * @param dstPath Pfad zur Zieldatei, darf nicht null sein
     * @throws IOException Fehler beim Lesen oder Schreiben der Dateien
     * @throws RuntimeException Fehler beim Lesen oder Schreiben der Dateien
     */
    public static void decode(String srcPath, String dstPath) throws IOException {
        if (srcPath == null) {
            throw new IllegalArgumentException("srcPath darf nicht null sein");
        }
        if (dstPath == null) {
            throw new IllegalArgumentException("dstPath darf nicht null sein");
        }

        // Stream oeffnen um aus Datei zu lesen
        DataInputStream inputStream = new DataInputStream(new FileInputStream(srcPath));
        // Statistik aus Datei lesen
        Statistics statistic = Statistics.readFromFile(inputStream);
        // Laenge von Datei holen fuer Formel
        long statisticSum = statistic.sum();
        // Wald erstellen
        Forest forest = statistic.toForest();
        // Huffmanbaum erstellen
        Tree codingTree = forest.toTree();

        // Anzahl der Bloecke bei der Kodierung
        int blockCount = inputStream.readInt();
        // Groessen der einzelnen Bloecke
        int[] blockSizes = new int[blockCount];
        // Lesen der Bloeckgroessen
        for (int i = 0; i < blockCount; i++) {
            blockSizes[i] = inputStream.readInt();
        }
        // Array fuer DecoderTasks anlegen
        DecoderTask[] decoderTasks = new DecoderTask[blockCount];
        // Array fuer Threads anlegen
        Thread[] threads = new Thread[blockCount];

        for (int i = 0; i < blockCount; i++) {

            // Soviele bytes sollen in dem Ergebniss enthalten sein
            int currBlockSize =
                    (int) (statisticSum / blockCount + (i < (statisticSum % blockCount) ? 1 : 0));

            // Array anlegen in das die codierten Bytes geschieben werden sollen
            byte[] byteBlock = new byte[blockSizes[i]];
            // Lese byteblock.lenth bytes aus dem Stream in das array ein
            inputStream.readFully(byteBlock);
            // Erzeuge aus dem Array einen Iterator
            BitIterator byteBlockIterator = new BitIterator(new ByteArrayInputStream(byteBlock));
            // Mach die DecoderTask
            decoderTasks[i] = new DecoderTask(codingTree, byteBlockIterator, currBlockSize);
            // Erzeuge einen Thread aus dem Task
            threads[i] = new Thread(decoderTasks[i]);
            // Starte den Thread
            threads[i].start();
        }

        // Ueber alle Threads laufen und Joinen
        joinThreads(threads);

        // In Datei Schreiben
        DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(dstPath));

        for (int i = 0; i < decoderTasks.length; i++) {
            outputStream.write(decoderTasks[i].getResult());
        }

        outputStream.close();

    }

}
