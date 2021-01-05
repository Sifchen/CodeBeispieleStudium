import java.io.IOException;

import org.junit.Test;

import huffman.Forest;
import huffman.Statistics;
import huffman.Tree;

public class ForestTests {
//	@Test
//	public void toForest_aFewCharsMAUNALTEST() throws IOException {
//		String input = "aaaaabbccccddd"; // a:5, b:2, c:4, d:3
//		byte[] inputData = input.getBytes();
//		Statistics statisticInput = Statistics.analyse(inputData);
//		Forest startForest = statisticInput.toForest();
//		System.out.println("Start" + startForest.toString());
//		startForest.getForest().poll();
//		System.out.println("After 1 poll" + startForest.toString());
//		startForest.getForest().poll();
//		System.out.println("After 2 polls" + startForest.toString());
//		
//		// System.out.print(result.sum());
//
//	}
	
	@Test
	public void toForest_aFewCharsSameOccsMAUNALTEST() throws IOException {
		String input = "aabbccdeee"; // a:2, b:2, c:2, d:1 e:3
		byte[] inputData = input.getBytes();
		Statistics statisticInput = Statistics.analyse(inputData);
		Forest startForest = statisticInput.toForest();
		System.out.println(startForest.toString());
	}
	
	@Test
	public void toTree_aFewCharsDifferentOccsMAUNALTEST() throws IOException {
		String input = "aaaaabbccccddd"; // // a:5, b:2, c:4, d:3
		byte[] inputData = input.getBytes();
		Statistics statisticInput = Statistics.analyse(inputData);
		Forest startForest = statisticInput.toForest();
		System.out.println(startForest.toString());
		Tree result = startForest.toTree();
		System.out.println(result.toString());
	}
	
	@Test
	public void toTree_minimalCharsMAUNALTEST() throws IOException {
		String input = "ab"; // // a:1, b:1
		byte[] inputData = input.getBytes();
		Statistics statisticInput = Statistics.analyse(inputData);
		Forest startForest = statisticInput.toForest();
		System.out.println(startForest.toString());
		Tree result = startForest.toTree();
		System.out.println(result.toString());
	}
}
