import java.io.IOException;

/**
 * Klasse mit einer (statischen) Methode zum Vergleichen von Dateien.
 * 
 * Der Dateivergleich funktioniert nativ nur unter Linux.
 * 
 * @author kar, mhe
 */
public class TestToolkit {

    /**
     * Ermittelt, ob der Inhalt der Dateien fileName1 und fileName2 identisch
     * ist.
     * 
     * @param fileName1 erste Datei.
     * @param fileName2 zweite Datei.
     * 
     * @return boolscher Wert, der angibt, ob der Inhalt der Dateien fileName1
     *         und fileName2 identisch ist.
     * 
     * @throws InterruptedException Externer Fehler
     * @throws IOException Dateifehler
     */
    public static boolean filesAreEqual(String fileName1, String fileName2)
            throws InterruptedException, IOException {
        final Runtime runtime = Runtime.getRuntime();
        final Process proc =
                runtime.exec("diff  --brief " + fileName1 + " " + fileName2);
        final int exitValue = proc.waitFor();
        return exitValue == 0;
    }


}
