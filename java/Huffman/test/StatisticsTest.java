import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import huffman.Statistics;

public class StatisticsTest {

	@Test
	public void testAnalyse_validBytesMANUALTEST() {

		byte[] input = { 12, 123, 23, 11, 32, 11 };
		Statistics result = Statistics.analyse(input);
		System.out.print(result.toString());
	}

	@Test
	public void testAnalyse_charsToBytesMANUALTEST() {

		String input = "aabbc def";
		Statistics result = Statistics.analyse(input.getBytes());
		System.out.print(result.toString());
	}

	@Test
	public void testWriteToFile_validBytesMANUALTEST() throws IOException {

		byte[] input = { 12, 123, 23, 11, 32, 11 };
		Statistics result = Statistics.analyse(input);

		FileOutputStream fileOut = new FileOutputStream("test/writeToFile_validBytesMANUALTEST");
		DataOutputStream dataOut = new DataOutputStream(fileOut);
		result.writeToFile(dataOut);
	}

	@Test
	public void readFromFile_validBytesMAUNALTEST() throws IOException {
		FileInputStream fileIn = new FileInputStream("test/writeToFile_validBytesMANUALTEST");
		DataInputStream dataIn = new DataInputStream(fileIn);
		Statistics result = Statistics.readFromFile(dataIn);
		System.out.print(result.toString());
		// System.out.print(result.sum());

	}

	@Test
	public void testWriteToFileANDreadFromFile_validNegativeBytesMAUNALTEST() throws IOException {

		byte[] inputData = { -128, -127, -128, -126, -128, -127 };
		Statistics statisticInput = Statistics.analyse(inputData);

		FileOutputStream fileOut = new FileOutputStream("test/writeToFileANDreadFromFile_validNegativeBytesMAUNALTEST");
		DataOutputStream dataOut = new DataOutputStream(fileOut);
		statisticInput.writeToFile(dataOut);

		FileInputStream fileIn = new FileInputStream("test/writeToFileANDreadFromFile_validNegativeBytesMAUNALTEST");
		DataInputStream dataIn = new DataInputStream(fileIn);
		Statistics result = Statistics.readFromFile(dataIn);
		System.out.print(result.toString());

	}
	
	
}
