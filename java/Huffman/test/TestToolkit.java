import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Assert;

import huffman.Tree;

/**
 * Klasse mit (statischen) (Hilfs-)methoden für JUnit-Tests. Die Klasse
 * beinhaltet unter anderem Methoden zum Vergleichen von Dateien und zum
 * Ermitteln des Stack-Trace einer Exception als Zeichenkette.
 * 
 * @author kar, mhe
 */
public final class TestToolkit {

    /**
     * Bestimmt, ob die erzeugten Graphvizbeschreibungen in eine Grafik
     * konvertiert und anschließend angezeigt werden sollen.
     * 
     * Sollte auf false gesetzt werden, falls "dot" (graphviz) oder "display"
     * nicht installiert ist.
     */
    private static final boolean CONVERT_AND_DISPLAY_GRAPH = true;

    /**
     * Dateiendung der anzuzeigenden Dateien.
     */
    private static final String PIC_EXTENSION = ".png";

    /**
     * Dateiendung der zu vergleichenden Dateien.
     */
    private static final String GRAPH_EXTENSION = ".dot";

    /**
     * Verzeichnis, in das die Dateien geschrieben werden sollen.
     */
    private static final String OUTPUT_DIR = "./test/results/";

    /**
     * Verzeichnis mit den Vergleichsdateien.
     */
    private static final String EXPECTED_DIR = "./test/expected/";

    /**
     * Privater Konstruktor, um das Erzeugen eines default-Konstruktors zu
     * verhindern. Diese Klasse soll nicht instanzierbar sein, da sie nur
     * statische Hilfsfunktionen enthält, die zum Testen benötigt werden.
     */
    private TestToolkit() {
        throw new AssertionError();
    }

    /**
     * Wandelt eine dot-Datei in eine png-Datei um.
     * 
     * @param dotFilename Dateiname der .dot Datei
     * @param pngFilename Dateiname der .png Datei
     * @return true, wenn alles geklappt hat
     */
    public static boolean graphvizDotToPng(String dotFilename,
            String pngFilename) {
        final Runtime runtime = Runtime.getRuntime();
        Process proc;
        try {
            proc = runtime.exec(new String[] { "dot", "-Tpng",
                    "-o" + pngFilename, dotFilename });
            final int exitValue = proc.waitFor();
            return exitValue == 0;
        } catch (IOException | InterruptedException e) {
            System.err.println("Fehler beim Aufruf von dot.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Schreibt die Graphviz-Repräsentation des übergebenen Baumes in die Datei
     * filename.
     * 
     * @param tree der Baum
     * @param filename (Dateipfad +) Dateiname
     */
    public static void writeGraphvizFile(Tree tree, String filename)
            throws IOException {
        FileWriter f = null;
        try {
            f = new FileWriter(filename);
            f.write(tree.toGraphviz());
        } finally {
            if (f != null) {
                f.close();
            }
        }
    }

    /**
     * Schreibt die Graphviz-Repräsentation des übergebenen Baumes ins
     * entsprechende Verzeichnis in die Datei mit dem übergebenen Namen und
     * überprüft, ob sie der erwarteten Graphviz-Datei entspricht.
     * 
     * @param tree der zu testende Baum
     * @param filename Dateiname der Vergleichsdatei im Ordner EXPECTED_DIR
     *            (ohne Endung)
     */
    public static void assertDotEquals(Tree tree, String filename)
            throws IOException {
        assertDotEquals("Die Datei sieht nicht wie erwartet aus!\n", tree,
                filename);
    }

    /**
     * Schreibt die Graphviz-Repräsentation des übergebenen Baumes ins
     * entsprechende Verzeichnis in die Datei mit dem übergebenen Namen und
     * überprüft, ob sie der erwarteten Graphviz-Datei entspricht.
     * 
     * @param message zusätzliche Beschreibung der potentiellen Fehlermeldung
     * @param tree der zu testende Baum
     * @param filename Dateiname der Vergleichsdatei im Ordner EXPECTED_DIR
     *            (ohne Endung)
     */
    public static void assertDotEquals(String message, Tree tree,
            String filename) throws IOException {

        // Ausgabeverzeichnis anlegen, falls noch nicht vorhanden
        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                System.err.println("Das Verzeichnis " + OUTPUT_DIR
                        + " konnte nicht erstellt werden!");
            }
        }

        final String outFilename = OUTPUT_DIR + filename + ".out";
        final String expFilename = EXPECTED_DIR + filename + ".exp";

        writeGraphvizFile(tree, outFilename + GRAPH_EXTENSION);

        assertTrue(
                message + " Erwartet:" + expFilename + GRAPH_EXTENSION + "\n"
                        + " Ergebnis:" + outFilename + GRAPH_EXTENSION,
                dotFilesAreEqual(expFilename, outFilename));
    }

    /**
     * Ermittelt, ob der Inhalt der Dot-Dateien fileName1 und fileName2
     * identisch ist. Zeigt die entsprechenden Graphen an, wenn
     * CONVERT_AND_DISPLAY_GRAPH auf true steht.
     * 
     * @param fileName1 erste Datei ohne Endung.
     * @param fileName2 zweite Datei ohne Endung.
     * 
     * @return boolscher Wert, der angibt, ob der Inhalt der Dateien fileName1
     *         und fileName2 identisch ist.
     * 
     * @pre Dateiname duerfen keine Leerzeichen enthalten.
     */
    public static boolean dotFilesAreEqual(String fileName1, String fileName2) {
        final String fileName1Dot = fileName1 + GRAPH_EXTENSION;
        final String fileName2Dot = fileName2 + GRAPH_EXTENSION;
        final String fileName1Png = fileName1 + PIC_EXTENSION;
        final String fileName2Png = fileName2 + PIC_EXTENSION;

        if (filesAreEqual(fileName1Dot, fileName2Dot)) {
            return true;
        } else if (CONVERT_AND_DISPLAY_GRAPH) {
            // .dot Dateien in .png Dateien rendern
            graphvizDotToPng(fileName1Dot, fileName1Png);
            graphvizDotToPng(fileName2Dot, fileName2Png);

            // beide Bilder mit "display" getrennt anzeigen:
            try {
                Runtime runtime = Runtime.getRuntime();
                runtime.exec(new String[] { "display", fileName1Png });
                runtime.exec(new String[] { "display", fileName2Png });
            } catch (IOException e) {
                System.err.println("Fehler beim der Ausführung von display.");
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * Ermittelt, ob der Inhalt der Dateien fileName1 und fileName2
     * identisch ist.
     * 
     * @param fileName1 erste Datei
     * @param fileName2 zweite Datei
     * 
     * @return boolscher Wert, der angibt, ob der Inhalt der Dateien fileName1
     *         und fileName2 identisch ist.
     * 
     * @pre Dateiname duerfen keine Leerzeichen enthalten.
     */
    public static boolean filesAreEqual(String fileName1, String fileName2) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(new String[] { "diff", "--brief",
                    fileName1, fileName2 });
            final int exitValue = proc.waitFor();

            switch (exitValue) {
            case 0: // Dateien sind gleich
                return true;

            case 1: // Dateien sind unterschiedlich
                return false;

            default: // Fehler beim Aufruf von "diff"
                System.err.println("Fehler beim Aufruf von diff."
                        + " Sind die Dateien vorhanden?\n" + fileName1 + "\n"
                        + fileName2 + "\n");
                return false;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Fehler beim der Ausführung von diff.");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Prüft mit JUnit, ob der Inhalt der Dateien fileName1 und fileName2
     * identisch ist.
     * 
     * @param message zusätzliche Beschreibung der potentiellen Fehlermeldung
     * @param fileName1 erste Datei
     * @param fileName2 zweite Datei
     * 
     * @pre Dateiname duerfen keine Leerzeichen enthalten.
     */
    public static void assertFilesAreEqual(String message, String fileName1, String fileName2) {
        Assert.assertTrue(message, filesAreEqual(fileName1, fileName2));
    }
    
    /**
     * Prüft mit JUnit, ob der Inhalt der Dateien fileName1 und fileName2
     * identisch ist.
     * 
     * @param fileName1 erste Datei
     * @param fileName2 zweite Datei
     * 
     * @pre Dateiname duerfen keine Leerzeichen enthalten.
     */
    public static void assertFilesAreEqual(String fileName1, String fileName2) {
        assertFilesAreEqual("Dateien " + fileName1 + " und " + fileName2 + " sind unterschiedlich",
                fileName1, fileName2);
    }
}

