import java.util.ArrayList;

/* A node structure that is used for the search tree. Each node contains 
 * the move that was performed (top, from bottom) as well as that instance of the puzzle.
 * 
 */

public class TreeNode {
	private int[][] snapshot;
	private TreeNode parent;
	private ArrayList<TreeNode> children;
	private boolean root;
	private char top, from , bottom;
	int h, g, f;
	
	public TreeNode(int[][] snapshot) {
		this.parent = null;
		this.snapshot = snapshot;
		this.children = new ArrayList();
		root = true;
	}

	public TreeNode(char top, char bottom, char from, int[][] snapshot, TreeNode parent) {
		this.snapshot = snapshot;
		this.parent = parent;
		this.children = new ArrayList();
		this.top = top;
		this.from = from;
		this.bottom = bottom;
		parent.addChild(this);
		root = false;
	}

	public TreeNode() {
	}

	public TreeNode getParent() {
		return parent;
	}

	public ArrayList<TreeNode> getChildren() {
		return children;
	}
	
	public void addChild(TreeNode child) {
		children.add(child);
	}
	
	public int[][] getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(int[][] snapshot) {
		this.snapshot = snapshot;
	}
	
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	
	public void removeChild(TreeNode child) {
		children.remove(child);
		child.setParent(null);
	}

	public char getTop() {
		return top;
	}

	public char getBottom() {
		return bottom;
	}

	public void printSnapshot() {
		
		System.out.println("Parent: " + parent);
		for (int i = 0; i < snapshot.length; i++)
		   {
		      for (int j = 0; j < snapshot.length; j++)
		      {
		         System.out.printf("%5d ", snapshot[i][j]);
		      }
		      System.out.println();
		   }
		System.out.println("------------------------------------------------------");
	}
	
	public void printMoves() {
		System.out.print("Move: " + "(" + top + ", " + from + ", " + bottom + ")");
	}
	
	public boolean isRoot() { //There was a problem with (parent =! null) so I had to add this
		if (root) return true;
		return false;
	}

	public String getFrom() {
		return String.valueOf(from);
	}
	
	
}
