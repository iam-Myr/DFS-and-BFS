import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/* A problem object holds useful information about a problem like its 
	 * initial stage, its goal stage and number of blocks. Based on the problem 2d array,
	 * it can make logical assumptions about the positions of blocks. It can also perform
	 * a move on an instance of the puzzle.
	 */

public class Problem {
	
	private int numBlocks;
	private char[] blocks;
	private int[][] init;
	private int[][] goal;	
	
	public Problem(int numBlocks, char[] names, int[][] init, int[][] goal) {
		this.numBlocks = numBlocks;
		this.blocks = names;
		this.init = init;
		this.goal = goal;
	}
	
	/*A move means that the row of the block about to be moved becomes 0
	 * so it's no longer on top of another block, and then the element in [top][bottom] becomes 1
	 * where top = the row of the block to be moved and bottom = the column of the target block
	*/
	public int[][] move(char block, char location, int[][] snapshot) {
		int[][] puzzle = new int[numBlocks][numBlocks];
		
		for(int i=0; i<numBlocks; i++)
			  for(int j=0; j<numBlocks; j++)
			   puzzle[i][j] = snapshot[i][j];
		
		for (int i = 0; i < numBlocks; i++) puzzle[block - 65][i] = 0; 
		if (!(location == 't')) 
			puzzle[block - 65][location - 65] = 1; //The character A is equal to 65 in java, so its index is easily calculated
		return puzzle;
	}
	
	/* List of conditions of verifier:
	 * Block to be moved exists
	 * If the location is the table, it's not already on the table
	*/
	public boolean isMovePossible(char block, char location, int[][] puzzle) {
		int pos =  block - 65;
		
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] == block) return true;
		}
		return false;
	}
		
	public boolean isClear(char block, int[][] puzzle) {
		int index = block - 65;
		
		for (int i = 0; i < numBlocks; i++) {
			if (puzzle[i][index] != 0) return false;
		}
		return true;
	}
	
	public boolean isOnTable(char block, int[][] puzzle ) {
		int index = block - 65;

		for (int j = 0; j < numBlocks; j++) {
			if (puzzle[index][j] != 0) return false;
		}
		return true;
	}
	
	public boolean isOnBlock(char top, char bottom, int[][] puzzle) {
		if  (puzzle[top - 65][bottom - 65] == 1) return true;;
		return false;
	}

	public int getNumBlocks() {
		return numBlocks;
	}
	
	//Finds the blocks that are clear, and therefore able to be moved
	public Queue<Character> findClear(int[][] puzzle){
		Queue<Character> moves = new LinkedList<Character>();
		
		for (Character c : blocks) {
			if (isClear(c,puzzle)) moves.add(c);
		}
		moves.add('t');
		
		return moves;
	}
	
	public boolean isSolution(int[][] puzzle) {
		for (int i = 0; i < numBlocks; i++) {
			for (int j = 0; j < numBlocks; j++) {
				if (puzzle[i][j] != goal[i][j]) return false;
			}
		}
		return true;
	}
	
	public char onTopOf(char block, int[][] puzzle) {
		for (int j = 0; j < numBlocks; j++) {
			if (puzzle[block - 65][j] == 1) {
				return (char)(j + 65);
			}
		}
		return 't';
	}

	public int[][] getInit() {
		return init;
	}
}
