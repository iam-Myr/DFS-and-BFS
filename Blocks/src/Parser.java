import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* A simple parser that reads files in this exact format
 * 
 * (define (problem BLOCKS-4-0)
(:domain BLOCKS)
(:objects D B A C ) 
(:INIT (CLEAR C) (CLEAR A) (CLEAR B) (CLEAR D) (ONTABLE C) (ONTABLE A)
 (ONTABLE B) (ONTABLE D) (HANDEMPTY))
(:goal (AND (ON D C) (ON C B) (ON B A)))
)
 * 
 * and creates a problem object.
 * Input: file location
 * Output: Problem object
 */

public class Parser {

	public Problem read(String file) {
		Problem problem;
		
		BufferedReader reader;
		
		int stage = 0;
		char[] objects = null;
		ArrayList<String> init = new ArrayList<String>();
		ArrayList<String> goal = new ArrayList<String>();
		
		try {
			reader = new BufferedReader(new FileReader(
					file));
			String line = reader.readLine();
		
			while (!line.equals(")")) {
				
				if (line.contains("objects")) stage = 1;
				else if (line.contains("INIT")) stage = 2;
				else if (line.contains("goal"))stage = 3;
				
				switch (stage) {
	            case 1: line = line.replaceAll("\\:objects ", "");
	            		line = line.replaceAll("\\)", "");
	            		line = line.replaceAll("\\(", "");
	            		line = line.replaceAll(" ", "");
	            		objects = line.toCharArray();
	            		Arrays.sort(objects);
	                     break;
	            case 2: line = line.replaceAll(" \\(", "");
						line = line.replaceAll("\\(:INIT", "");
						line = line.replaceAll("HANDEMPTY\\)", "");
						List<String> temp = new ArrayList<String>(Arrays.asList(line.split("\\)")));
						init.addAll(temp);
	                     break;
	            case 3: line = line.replaceAll("\\)", "");
						line = line.replaceAll("\\(:goal \\(AND \\(", "");
						temp = new ArrayList<String>(Arrays.asList(line.split("\\(")));
						goal.addAll(temp);
	                    break;
	            default: break;
	        }
				
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		problem = new Problem(objects.length, objects, descriptionToArray(init, objects), descriptionToArray(goal, objects));
		return problem;
		
	}
	
	/* -Array idea by George Gasdrogkas dai17xxx -
	 * This function makes a representation of the block world as a 2d array. 
	 * The rows and columns both represent the blocks: A = 1, B = 2...
	 * So the element in (1,2) is AB and it can be either 0 or 1.
	 * 1 means that the row block is on top of the column block, in the example 
	 * if (1,2) = 1, that means that A is on top of B. 
	 * There are 2 useful observations that can be made with that
	 * mechanic in mind:
	 * - If a letter has only 0 in its row, it means that it's clear
	 * - If a letter has only 0 in its column, it means that it's on the table
	 * Input: String list of descriptions, array of objects
	 * Output: 2d array
	*/
	public int[][] descriptionToArray(List<String> list, char[] objects) {
		int size = objects.length;
		int[][] array = new int[size][size];
		
		for (String str : list) {	
			if (str.contains("ON ")) {
				int top = str.charAt(3) - 65;
				int bottom = str.charAt(5) - 65;
				array[top][bottom] = 1;
			}
		}
		
		for (int i = 0; i < size; i++)
		   {
		      for (int j = 0; j < size; j++)
		      {
		         System.out.printf("%5d ", array[i][j]);
		      }
		      System.out.println();
		   }
		System.out.println("------------------------------------------------------");
		return array;
	}
}
