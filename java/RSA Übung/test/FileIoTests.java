
import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.NoSuchFileException;

import org.junit.Test;

import rsa.FileIO;

    /**
     * JUnit-Testklasse für FileIO mit einigen Tests.
     *
     * @author Torben Fritsch, Paul Moeller-Friedrich
     *
     */

public class FileIoTests {
        @Test
        public void test_bigIntegerToBytes(){
            BigInteger biggie = new BigInteger("300");
            assertArrayEquals(new byte[]{0,1, 44} ,FileIO.bigIntegerToBytes(biggie, 3));
        }

        @Test
        public void test_readBigIntegers() throws IOException {
            final String path = "test/expected/BigInteger.txt";
            BigInteger[] ones = new BigInteger[]{new BigInteger("97"), new BigInteger("98"), new BigInteger("99")};
            BigInteger[] threepels = new BigInteger[] {new BigInteger(new byte[] {97,98,99})};
            assertArrayEquals(ones, FileIO.readBigIntegers(1, path));
            assertArrayEquals(threepels, FileIO.readBigIntegers(3, path));
        }

        @Test
        public void test_readBytes() throws IOException {
            assertArrayEquals(new byte[] {97,98,99},FileIO.readBytes("test/expected/BigInteger.txt"));
        }

        @Test(expected = AssertionError.class)
        public void test_readBytes_emptyFile() throws IOException {
            assertArrayEquals(new byte[] {97, 98, 99}, FileIO.readBytes("test/expected/empty.txt"));
        }

        @Test
        public void test_writeBytes() throws IOException, InterruptedException {
            FileIO.writeBytes(new byte[] {97,98,99},"test/results/out.txt");
            TestToolkit.filesAreEqual("test/results/out.txt", "test/expected/BigInteger.txt");
        }

        @Test(expected = NoSuchFileException.class)
        public void test_writeBytes_wrongPath() throws IOException, InterruptedException {
            FileIO.writeBytes(new byte[] {97,98,99},"wrong/path/out.txt");
            TestToolkit.filesAreEqual("wrong/path/out.txt", "test/expected/BigInteger.txt");
        }
        @Test
        public void test_writeBytes_fileExists() throws IOException, InterruptedException {
            FileIO.writeBytes(new byte[] {97,98,99},"test/expected/hallowelt2.txt");
            TestToolkit.filesAreEqual("test/expected/hallowelt2.txt", "test/expected/BigInteger.txt");
        }

}
