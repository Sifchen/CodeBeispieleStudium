package coding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
/**
 * Klasse für eine nebenläufige Kodierung mit Threads.
 * 
 * @author kar, mhe, Daniel Pigotow
 */
public class EncoderTask implements Runnable {
    private final BitChain[] codetable;
    private final byte[] bytes;
    private final int startIdx;
    private final int numOfBytes;

    /**
     * Das Ergebnis der Kodierung.
     */
    private byte[] result;

    /**
     * Die Anzahl der Bits im Ergebnis der Kodierung. Padding-Bits werden dabei nicht mitgezählt.
     */
    private long bitsInResult = 0;

    /**
     * Konstruktor.
     * 
     * @param codetable Kodierungstabelle, darf nicht null sein. Wird in dieser Klasse nicht
     *            verändert.
     * @param bytes Zu kodierende Bytes, darf nicht null sein. Wird in dieser Klasse nicht
     *            verändert.
     * @param startIdx Index des ersten zu kodierenden Bytes im übergebenen Array, muss &ge; 0 sein
     * @param numOfBytes Anzahl der zu kodierenden Bytes, muss &ge; 0 sein
     */
    public EncoderTask(BitChain[] codetable, byte[] bytes, int startIdx, int numOfBytes) {
        if (codetable == null) {
            throw new IllegalArgumentException("codetable darf nicht null sein");
        }
        if (bytes == null) {
            throw new IllegalArgumentException("bytes darf nicht null sein");
        }
        if (startIdx < 0) {
            throw new IllegalArgumentException("startIdx muss >= 0 sein");
        }
        if (numOfBytes < 0) {
            throw new IllegalArgumentException("blockLength muss >= 0 sein");
        }

        this.codetable = codetable;
        this.bytes = bytes;
        this.startIdx = startIdx;
        this.numOfBytes = numOfBytes;
    }

    @Override
    public void run() {
        try {

            ByteArrayOutputStream output = new ByteArrayOutputStream(this.numOfBytes);
            BitChain remainingBits = new BitChain();
            int paddingBits = 0;

            for (int i = this.startIdx; i < this.startIdx + this.numOfBytes; i++) {

                BitChain curr = this.codetable[Byte.toUnsignedInt(bytes[i])];
                

                remainingBits.append(curr);
                if (remainingBits.length() >= Byte.SIZE) {
                    remainingBits.writeTo(output);
                }

            }

            while (remainingBits.length() % Byte.SIZE != 0) {
                remainingBits.append(Bit.ZERO);
                paddingBits++;
            }
            while (remainingBits.length() != 0) {
                remainingBits.writeTo(output);
            }

            this.result = output.toByteArray();
            this.bitsInResult = result.length * Byte.SIZE - paddingBits;

        } catch (IOException e) {
            // Umwandlung in RuntimeException, da run keine IOException werfen darf
            throw new RuntimeException(e);
        }
    }

    /**
     * Liefert das Ergebnis der Kodierung.
     * 
     * Darf erst aufgerufen werden, wenn der umschließende Thread beendet ist.
     * 
     * @return Referenz auf das Ergebnis der Kodierung
     */
    public byte[] getResult() {
        return result;
    }

    /**
     * Liefert die Anzahl der Bits im Ergebnis der Kodierung. Padding-Bits werden dabei nicht
     * mitgezählt.
     * 
     * Darf erst aufgerufen werden, wenn der umschließende Thread beendet ist.
     * 
     * @return Anzahl der Bits im Ergebnis der Kodierung
     */
    public long getBitsInResult() {
        return bitsInResult;
    }
}
