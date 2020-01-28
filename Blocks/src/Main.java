import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		
		String meth = args[0]; 
		String input = args[1];
		String output = args[2];
		
		int method = methodToInt(meth); //1 is depth, 2 is breadth
		if (method == 0) System.exit(0);
	
		Parser parser = new Parser();
		
		Problem problem = parser.read("Data/" + input);
		int[][] snapshot = new int[problem.getNumBlocks()][problem.getNumBlocks()];	

		long start = System.currentTimeMillis();
		TreeNode solution = search(problem, method);
		long elapsedTime = System.currentTimeMillis() - start;
		
		System.out.println("The solution is:");
		solution.printSnapshot();
		try {
			writeSolution(solution,output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Time passed: " + elapsedTime/1000F + " seconds");
	}
	
	/* The search function is the same of all search algorithms, since the only thing that changes is
	 * the order of the nodes in the frontier
	 */
	private static TreeNode search(Problem problem, int method) {
		
		TreeNode node = new TreeNode(problem.getInit());
		Collection frontier = new LinkedList();
		
		switch (method) {
		case 1: frontier = new Stack(); break;
		case 2: frontier = new LinkedList(); break;
		}
		
		frontier.add(node);
		while(!frontier.isEmpty()) {
			node = removeFromFrontier(frontier, method);
			if(problem.isSolution(node.getSnapshot())) return node;
			findChildren(node, problem, frontier, method);	
		}
		
		return node;
	}
	
	/* The DFS frontier is a Stack, and the BFS is a Queue. This function
	 * makes sure that the correct remove method is used for each structure
	 * 
	 */
	
	private static TreeNode removeFromFrontier(Collection frontier, int method) {
		TreeNode node = new TreeNode();
		
		switch (method) {
		case 1:	node = ((Stack<TreeNode>) frontier).pop(); return node; 
		case 2: node = ((Queue<TreeNode>) frontier).poll();return node;
		}
		return node;
		
	}
	
	/* The function starts by making a list with the clear blocks of a puzzle instance.
	 * Next, it picks the first 2 blocks of the queue and checks if a move is possible with them.
	 * If it is, it creates a new child node for the current node and checks if there is a loop
	 * If there is, the child gets deleted, or if there isn't it gets added to the frontier
	 * The top and bottom blocks that were used are added to the bottom of the queue
	 * and new are picked, until all possible combinations have been exhausted
	 * Input: A Node
	 * Output: The same Node with children
	 */
	private static void findChildren(TreeNode node, Problem problem, Collection frontier, int method) {
		Queue<Character> clear = problem.findClear(node.getSnapshot());
		char top, bottom, from;
		TreeNode child;
		
		for (int i = 0; i < clear.size(); i++) {
			top = clear.poll();
			if (top == node.getTop()) {     
				clear.add(top);
				top = clear.poll();
			}
			for (int j = 0; j < clear.size(); j++) {
				bottom = clear.poll();
				if (problem.isMovePossible(top, bottom, node.getSnapshot())) {
					from = problem.onTopOf(top, node.getSnapshot());
					child = new TreeNode(top, bottom, from,
							problem.move(top, bottom, node.getSnapshot()), node);
					if (!isLoop(child)) { 
						
						switch (method) {
						case 1: frontier.add(child); break;
						case 2: frontier.add(child); break;
						}
					}
					else node.removeChild(child);
				}
				clear.add(bottom);
			}
			clear.add(top);
		}
	}//Editors note: while I'm writing this, I realized I could have switched the top and bottom for a second move
	
	
	/*
	 * Checks whether the current child node is the same as one of its previous nodes, therefore being a loop
	 */
	private static boolean isLoop(TreeNode node) {
		boolean loop = true;
		TreeNode parentNode = node;
		int[][] child = node.getSnapshot();
		int[][] parent;
		
		while(!parentNode.isRoot()) {
			parentNode = parentNode.getParent(); 
			parent = parentNode.getSnapshot();
			loop = true;
			for (int i = 0; i < child.length; i++) {
				for (int j = 0; j < child.length; j++) {
				if (parent[i][j] != child[i][j]) loop = false;
				}
			}
			if(loop) return loop;
		}
		return loop;
	}
	
	public static void writeSolution(TreeNode node, String filename) throws IOException {
		FileWriter fw = new FileWriter(filename);
		TreeNode parent = node;
		int move = 1;
		Stack<TreeNode> parents = new Stack(); 
		
		while (!parent.isRoot()) {
			parents.add(parent);
			parent = parent.getParent();
		}
		
		while (!parents.isEmpty()) {
			parent = parents.pop();
			char top = parent.getTop();
			String from = parent.getFrom();
			if (from.equals("t")) from = "table";
			String bottom = String.valueOf(parent.getBottom());
			if (bottom.equals("t")) bottom = "table";
			fw.write(move+ ". " + "(" + top + ", " + from + ", " + bottom + ")\r\n");
			move++;
		}
		fw.close();
	}
	
	public static int methodToInt(String meth) {
		if (meth.equals("depth")) return 1;
		if (meth.equals("breadth")) return 2;
		else return 0;
		
	}
}
