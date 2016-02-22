import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * This class implements a Binary Search Tree.
 *
 * @author wollowsk.
 *         Created January 5, 2011.
 */

public class BinarySearchTree<T extends Comparable<? super T>> implements
		Iterable<T> {

	private BinaryNode root;
	private int size;

	public BinarySearchTree() {
		root = null;
		size = 0;
	}

	/**
	 * This method determines whether a BST is even. We define an even BST as a
	 * tree in which
	 * all of the "outside" nodes are divisible by two. For example the
	 * following tree is even:
	 * 6
	 * 4 8
	 * 2 5 7 10
	 * The "outside" nodes are 2, 4, 6, 8, and 10. The "inside" nodes are 5 and
	 * 7.
	 * This method runs in linear time.
	 * 
	 * @return This method returns true if the BST is even and false otherwise.
	 */
	public boolean evenTree() {
		if (this.root == null)
			return true;
		if ((int) this.root.element % 2 != 0)
			return false;
		BinaryNode temp = this.root;
		while (temp.leftChild != null) {
			if ((int) temp.leftChild.element % 2 != 0)
				return false;
			temp = temp.leftChild;
		}
		temp = this.root;
		while (temp.rightChild != null) {
			if ((int) temp.rightChild.element % 2 != 0)
				return false;
			temp = temp.rightChild;
		}

		return true;
	}

	/**
	 * This method determines the difference between the length of the longest
	 * path
	 * from the root to a null node and the shortest one. The length is the
	 * number of nodes
	 * visited. For maximum credit, this method visits each node only once.
	 * 
	 * @return This method returns the difference between the longest and
	 *         shortest paths
	 *         in a BST.
	 */
	public int lopSided() {
		if (this.root == null)
			return 0;
		int[] result = this.root.height(false);
		return result[0] - result[1];
	}

	public boolean insert(T element) {
		if (element == null) {
			throw new NullPointerException(
					"Cannot insert null element into binary search tree");
		} else if (root == null) {
			root = new BinaryNode(element);
			size++;
			return true;
		} else {
			return root.insert(element);
		}
	}

	public String toString() {
		if (root == null)
			return "";
		return root.toString();
	}

	public String simpleToString() {
		if (root == null)
			return "";
		return root.simpleToString();
	}

	public Iterator<T> iterator() {
		return new PreOrderIterator();
	}

	private class BinaryNode {

		private T element;
		private BinaryNode leftChild;
		private BinaryNode rightChild;

		public BinaryNode(T e) {
			element = e;
			leftChild = rightChild = null;
		}

		/*
		 * heights[0] is max height
		 * heights[1] is min height
		 */
		public int[] height(boolean reachedNull) {
			int leftHeight = 0, rightHeight = 0;
			int[] output = { 0, 0 };
			int[] left = new int[2], right = new int[2];
			if (!reachedNull)
				output[1]++;
			if (this.leftChild == null) {
				leftHeight = 1;
				reachedNull = true;
			} else {
				left = this.leftChild.height(reachedNull);
				leftHeight = left[0] + 1;
			}
			if (this.rightChild == null) {
				rightHeight = 1;
				reachedNull = true;
			} else {
				right = this.rightChild.height(reachedNull);
				rightHeight = right[0] + 1;
			}
			if (left != null) {
				if (right != null) {
					output[1] += (left[1] < right[1]) ? left[1] : right[1];
				} else {
					output[1] += left[1];
				}
			} else if (right != null) {
				output[1] += right[1];
			}
			if (leftHeight >= rightHeight) {
				output[0] = leftHeight;
				return output;
			}
			output[0] = rightHeight;
			return output;
		}

		public boolean insert(T element2) {
			if (element.compareTo(element2) > 0) {
				if (leftChild == null) {
					leftChild = new BinaryNode(element2);
					size++;
					return true;
				} else {
					return leftChild.insert(element2);
				}
			} else {
				if (rightChild == null) {
					rightChild = new BinaryNode(element2);
					size++;
					return true;
				} else {
					return rightChild.insert(element2);
				}
			}
		}

		public BinaryNode remove(T e) {
			if (e.compareTo(element) < 0) {
				if (leftChild != null)
					leftChild = leftChild.remove(e);
				return this;
			}
			if (e.compareTo(element) > 0) {
				if (rightChild != null)
					rightChild = rightChild.remove(e);
				return this;
			}
			if (leftChild == null && rightChild == null) {
				return null;
			}
			if (leftChild == null) {
				return rightChild;
			}
			if (rightChild == null) {
				return leftChild;
			}
			element = leftChild.largest();
			leftChild = leftChild.remove(element);
			return this;
		}

		public T largest() {
			if (rightChild != null)
				return rightChild.largest();
			return element;
		}

		public String toString() {
			String s = "[" + element + " "
					+ ((leftChild == null) ? null : leftChild.element) + " "
					+ ((rightChild == null) ? null : rightChild.element)
					+ "]\n";
			if (leftChild != null) {
				s += leftChild.toString();
			}
			if (rightChild != null) {
				s += rightChild.toString();
			}
			return s;
		}

		public String simpleToString() {
			String s = element.toString();
			if (leftChild != null) {
				s += leftChild.simpleToString();
			}
			if (rightChild != null) {
				s += rightChild.simpleToString();
			}
			return s;
		}

		public ArrayList<BinaryNode> getChildren() {
			ArrayList<BinaryNode> temp = new ArrayList<BinaryNode>();
			if (leftChild != null)
				temp.add(leftChild);
			if (rightChild != null)
				temp.add(rightChild);
			return temp;
		}
	}

	private class PreOrderIterator implements Iterator<T> {
		private ArrayList<BinaryNode> nodes;

		public PreOrderIterator() {
			nodes = new ArrayList<BinaryNode>();
			if (root != null) {
				nodes.add(root);
			}
		}

		public boolean hasNext() {
			return !nodes.isEmpty();
		}

		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			BinaryNode temp = nodes.remove(0);
			nodes.addAll(0, temp.getChildren());
			return temp.element;
		}

		public void remove() {
		}

	}

}
