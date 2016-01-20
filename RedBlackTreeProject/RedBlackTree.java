import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * A RedBlackTree object.
 * 
 * @author derrowap
 *
 * @param <T>
 */
public class RedBlackTree<T extends Comparable<? super T>> implements
		Iterable<RedBlackTree.BinaryNode> {

	/**
	 * A color enum to be used to set color of BinaryNodes in RedBlackTree.
	 * 
	 * @author derrowap
	 *
	 */
	public enum Color {
		BLACK, RED
	}

	protected BinaryNode root;
	private int size;
	private int modifications;
	private int rotations;

	// MAX # of rotations in Tree: (height + 1) / 4

	/**
	 * Constructs a RedBlackTree that initializes root to NULL.
	 */
	public RedBlackTree() {
		this.root = null;
		this.size = 0;
		this.modifications = 0;
		this.rotations = 0;
	}

	/**
	 * Returns the number of rotations done on the tree. Double rotations count
	 * as two.
	 * 
	 * @return number of rotations
	 */
	public int getRotationCount() {
		return this.rotations;
	}

	/**
	 * Returns the number of elements in the tree
	 * 
	 * @return size of RedBlackTree
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Returns TRUE if the tree is empty and FALSE otherwise.
	 *
	 * @return boolean if RedBlackTree is empty
	 */
	protected boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * Returns the height of the tree. If the tree is empty, return -1.
	 * 
	 * @return height of RedBlackTree
	 */
	protected int height() {
		if (this.isEmpty())
			return -1;
		return this.root.getHeight();
	}

	/**
	 * Inserts a given element into the proper location in RedBlackTree.
	 * If successful, returns TRUE, else FALSE.
	 * 
	 * @return boolean indicating success
	 */
	protected boolean insert(T o) {
		if (o == null)
			throw new IllegalArgumentException("Can't insert null");
		if (this.root == null) { // Empty Tree
			this.root = new BinaryNode(o);
			this.root.setBlack();
			this.size++;
			return true;
		}
		int beforeSize = this.size;
		this.root = this.root.getInsert(o);
		this.root.setBlack();
		if (beforeSize != this.size) {
			this.modifications++;
			return true;
		}
		return false;
	}

	/**
	 * Not yet implemented
	 * 
	 * @return false
	 */
	protected boolean remove(T o) {

		return false;
	}

	/**
	 * Returns a lazy pre-order iterator object that iterates over objects of
	 * type RedBlackTree.BinaryNode
	 * 
	 * @return a lazy pre-order iterator of the RedBlackTree
	 */
	public Iterator<RedBlackTree.BinaryNode> iterator() {
		return new LazyPreOrderIterator();
	}

	/**
	 * A BinaryNode object.
	 * 
	 * @author derrowap
	 *
	 */
	protected class BinaryNode {
		private T element;
		private BinaryNode leftChild;
		private BinaryNode rightChild;
		private Color color;

		/**
		 * Constructs a BinaryNode initializing children
		 * to NULL and height to be 0.
		 * 
		 * @param e
		 *            - the element of this BinaryNode
		 */
		protected BinaryNode(T e) {
			this.element = e;
			this.leftChild = null;
			this.rightChild = null;
			this.color = Color.RED;
		}

		/**
		 * Returns the element of this BinaryNode.
		 * 
		 * @return this element
		 */
		protected T getElement() {
			return this.element;
		}

		/**
		 * Returns the left child of this BinaryNode
		 * 
		 * @return leftCild BinaryNode
		 */
		protected BinaryNode getLeftChild() {
			return this.leftChild;
		}

		/**
		 * Returns the right child of this BinaryNode
		 * 
		 * @return rightChild BinaryNode
		 */
		protected BinaryNode getRightChild() {
			return this.rightChild;
		}

		/**
		 * Computes the height of this BinaryNode by returning the greatest
		 * value between the height on the left side and the height on the right
		 * side plus one for the root's child.
		 * 
		 * @return height of BinaryNode
		 */
		public int getHeight() {
			int leftHeight = this.leftChild == null ? 0 : this.leftChild
					.getHeight() + 1;
			int rightHeight = this.rightChild == null ? 0 : this.rightChild
					.getHeight() + 1;
			if (leftHeight > rightHeight)
				return leftHeight;
			return rightHeight;
		}

		/**
		 * Returns the color of this BinaryNode
		 * 
		 * @return color of BinaryNode
		 */
		protected Color getColor() {
			return this.color;
		}

		/**
		 * Uses iteration to insert an element in the proper location of the
		 * RedBlackTree. This method uses top-down insertion, correcting the
		 * tree as it iterates down and immediately returns when at the bottom
		 * with nothing else to do.
		 * 
		 * @param o
		 *            - the element to insert
		 * @return root of tree with inserted node
		 */
		private BinaryNode getInsert(T o) {
			BinaryNode localRoot = this, parent = null, grandParent = null, greatGrandParent = null;
			BinaryNode current = localRoot;
			while (true) {
				if (current == null) { // insert new node
					current = new BinaryNode(o);
					RedBlackTree.this.size++;
					if (current.element.compareTo(parent.element) > 0)
						parent.rightChild = current;
					else
						parent.leftChild = current;
					if (grandParent == null) // parent is root node
						return parent;
					if (parent.color == Color.RED) {
						BinaryNode temp = current.detectRotation(parent,
								grandParent);
						if (greatGrandParent == null)
							return temp;
						if (greatGrandParent.leftChild.element == grandParent.element)
							greatGrandParent.leftChild = temp;
						else if (greatGrandParent.rightChild.element == grandParent.element)
							greatGrandParent.rightChild = temp;
					}
					return localRoot;
				}
				/* Check for swapping colors */
				if (current.leftChild != null && current.rightChild != null
						&& current.leftChild.color == Color.RED
						&& current.rightChild.color == Color.RED) {
					current.swapColors();
					if (parent != null && parent.color == Color.RED) {
						BinaryNode temp = current.detectRotation(parent,
								grandParent);
						if (temp.element == current.element) {
							/* double rotation occured */
							if (greatGrandParent != null) {
								if (greatGrandParent.leftChild.element == grandParent.element) {
									grandParent = temp;
									greatGrandParent.leftChild = grandParent;
								} else {
									grandParent = temp;
									greatGrandParent.rightChild = grandParent;
								}
							} else {
								grandParent = temp;
								localRoot = grandParent;
							}
							if (grandParent.rightChild.element == parent.element) {
								/* rightLeftRotation occured */
								parent = grandParent.rightChild;
								current = parent.leftChild;
							} else { /* leftRightRotation occured */
								parent = grandParent.leftChild;
								current = parent.rightChild;
							}
						} else { /* single rotation occured */
							parent = temp;
							if (parent.leftChild.element == current.element) // rightRotation
								current = parent.leftChild;
							else
								// leftRotation
								current = parent.rightChild;
							if (greatGrandParent == null)
								localRoot = parent;
							else if (greatGrandParent.leftChild.element == grandParent.element) {
								grandParent = greatGrandParent;
								grandParent.leftChild = parent;
							} else if (greatGrandParent.rightChild.element == grandParent.element) {
								grandParent = greatGrandParent;
								grandParent.rightChild = parent;
							}
						}
					}
				}
				/* Determine which subtree to go to next */
				int compared = o.compareTo(current.element);
				if (compared == 0)
					return localRoot; // element already in tree
				greatGrandParent = grandParent;
				grandParent = parent;
				parent = current;
				if (compared < 0) /* Go to left subtree */
					current = current.leftChild;
				if (compared > 0) /* Go to right subtree */
					current = current.rightChild;
			}
		}

		/**
		 * If the tree needs to be rotated, it will balance the tree, then
		 * returns the top of this new tree.
		 * 
		 * @return top of rotated tree
		 */
		protected BinaryNode detectRotation(BinaryNode parent,
				BinaryNode grandParent) {
			BinaryNode output = this;
			if (grandParent.leftChild != null
					&& grandParent.leftChild.element == parent.element) {
				if (parent.leftChild != null
						&& parent.leftChild.element == this.element)
					output = grandParent.rightRotation();
				else
					output = grandParent.leftRightRotation();
			} else {
				if (parent.rightChild != null
						&& parent.rightChild.element == this.element)
					output = grandParent.leftRotation();
				else
					output = grandParent.rightLeftRotation();
			}
			output.setBlack();
			output.leftChild.setRed();
			output.rightChild.setRed();
			return output;
		}

		/**
		 * Executes a left rotation on this node.
		 * 
		 * @return the new root node of a left rotated tree
		 */
		private BinaryNode leftRotation() {
			BinaryNode output = this.rightChild;
			BinaryNode temp = null;
			if (this.rightChild.leftChild != null)
				temp = this.rightChild.leftChild;
			this.rightChild = null;
			output.leftChild = this;
			if (temp != null)
				output.leftChild.rightChild = temp;
			RedBlackTree.this.rotations++;
			return output;
		}

		/**
		 * Executes a right rotation on this node.
		 * 
		 * @return the new root node of a right rotated tree
		 */
		private BinaryNode rightRotation() {
			BinaryNode output = this.leftChild;
			BinaryNode temp = null;
			if (this.leftChild.rightChild != null)
				temp = this.leftChild.rightChild;
			this.leftChild = null;
			output.rightChild = this;
			if (temp != null)
				output.rightChild.leftChild = temp;
			RedBlackTree.this.rotations++;
			return output;
		}

		/**
		 * Executes a double rotation on this node by first doing a left
		 * rotaion on this left child, and then a right rotation on this node.
		 * 
		 * @return new root node of double rotated tree
		 */
		private BinaryNode leftRightRotation() {
			this.leftChild = this.leftChild.leftRotation();
			return this.rightRotation();
		}

		/**
		 * Executes a double rotation on this node by first doing a right
		 * rotation on this right child, and then a left rotation on this node.
		 * 
		 * @return new root node of double rotated tree
		 */
		private BinaryNode rightLeftRotation() {
			this.rightChild = this.rightChild.rightRotation();
			return this.leftRotation();
		}

		/**
		 * Sets this BinaryNode's color to BLACK.
		 */
		protected void setBlack() {
			this.color = Color.BLACK;
		}

		/**
		 * Sets this BinaryNode's color to RED.
		 */
		protected void setRed() {
			this.color = Color.RED;
		}

		/**
		 * Sets the color of both children to BLACK and the color of this
		 * BinaryNode to RED.
		 */
		protected void swapColors() {
			this.color = Color.RED;
			this.leftChild.color = Color.BLACK;
			this.rightChild.color = Color.BLACK;
		}
	}

	/**
	 * A lazy in-order iterator object.
	 * 
	 * @author derrowap
	 *
	 */
	protected class LazyInOrderIterator implements
			Iterator<RedBlackTree.BinaryNode> {
		Stack<BinaryNode> stack = new Stack<>();
		BinaryNode lastNode;
		int modifications;

		/**
		 * Constructs a lazy in-order iterator object.
		 */
		private LazyInOrderIterator() {
			this.modifications = RedBlackTree.this.modifications;
			this.lastNode = RedBlackTree.this.root;
			// avoid null pointer exception
			if (this.lastNode != null) {
				// add all nodes on the left side to the stack
				while (this.lastNode.leftChild != null) {
					this.stack.push(this.lastNode);
					this.lastNode = this.lastNode.leftChild;
				}
				// pushes left-most element on the stack
				this.stack.push(this.lastNode);
				this.lastNode = null;
			}
		}

		/**
		 * Checks if there exists a next element in the iterator. If there is,
		 * return TRUE, else FALSE.
		 * 
		 * @return boolean if there is a next value
		 */
		public boolean hasNext() {
			return !this.stack.empty();
		}

		/**
		 * Finds the next element in the iterator in an in-order arrangement
		 * throws a NoSuchElementException if there is no next element
		 * 
		 * 1. Pop top of stack.
		 * 2. Push popped node's right child to stack.
		 * 3. Push all of right child's left children to the stack.
		 * 4. Return popped node.
		 * 
		 * @return next object in tree
		 */
		public RedBlackTree.BinaryNode next() {
			if (!hasNext())
				throw new NoSuchElementException();
			// Pop top of stack
			this.lastNode = this.stack.pop();
			// Push popped node's right child to stack.
			if (this.lastNode.rightChild != null) {
				this.stack.push(this.lastNode.rightChild);
				// Push all of right child's left children to the stack.
				BinaryNode temp = this.lastNode.rightChild.leftChild;
				while (temp != null) {
					this.stack.push(temp);
					temp = temp.leftChild;
				}
			}
			// Return popped node
			return this.lastNode;
		}

		/**
		 * Attempts to remove the last element that was returned from this
		 * iterator.
		 * 
		 * Throws an IllegalStateException if there was never a last element
		 * returned or if it was already removed.
		 * 
		 * Throws a ConcurrentModificationException if the tree was modified
		 * outside of the iterator after this iterator was already constructed.
		 */
		public void remove() {
			if (this.lastNode == null)
				throw new IllegalStateException();
			if (this.modifications != RedBlackTree.this.modifications)
				throw new ConcurrentModificationException();
			if (RedBlackTree.this.remove(this.lastNode.element)) {
				this.modifications++;
				this.lastNode = null;
			}
		}
	}

	/**
	 * A lazy pre-order iterator object.
	 * 
	 * @author derrowap
	 *
	 */
	protected class LazyPreOrderIterator implements
			Iterator<RedBlackTree.BinaryNode> {
		Stack<BinaryNode> stack;
		BinaryNode lastNode;
		int modifications;

		/**
		 * Constructs a lazy pre-order iterator object.
		 */
		private LazyPreOrderIterator() {
			this.stack = new Stack<>();
			this.modifications = RedBlackTree.this.modifications;
			this.lastNode = null;
			if (RedBlackTree.this.root != null)
				this.stack.push(RedBlackTree.this.root);
		}

		/**
		 * Checks if there exists a next element in the iterator. If there is,
		 * return TRUE, else FALSE.
		 * 
		 * @return boolean if there is a next value
		 */
		public boolean hasNext() {
			return !this.stack.empty();
		}

		/**
		 * Finds the next element in the iterator in a pre-order arrangement.
		 * Throws a NoSuchElementException if there is no next element.
		 * 
		 * 1. Pop top of stack.
		 * 2. Push popped node's right child to stack.
		 * 3. Push popped node's left child to stack.
		 * 4. Return popped node.
		 * 
		 * @return next object in tree
		 */
		public RedBlackTree.BinaryNode next() {
			if (!hasNext())
				throw new NoSuchElementException();
			this.lastNode = this.stack.pop();
			if (this.lastNode.rightChild != null)
				this.stack.push(this.lastNode.rightChild);
			if (this.lastNode.leftChild != null)
				this.stack.push(this.lastNode.leftChild);
			return this.lastNode;
		}

		/**
		 * Attempts to remove the last element that was returned from this
		 * iterator.
		 * 
		 * Throws an IllegalStateException if there was never a last element
		 * returned or if it was already removed.
		 * 
		 * Throws a ConcurrentModificationException if the tree was modified
		 * outside of the iterator after this iterator was already constructed.
		 */
		public void remove() {
			if (this.lastNode == null)
				throw new IllegalStateException();
			if (this.modifications != RedBlackTree.this.modifications)
				throw new ConcurrentModificationException();
			if (RedBlackTree.this.remove(this.lastNode.element)) {
				this.modifications++;
				this.lastNode = null;
			}
		}
	}

}
