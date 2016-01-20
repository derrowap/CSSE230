import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;


/**
 * An AVL tree object.
 * 
 * @author derrowap
 *
 * @param <T>
 */
public class AVLTree<T extends Comparable<? super T>> implements Iterable<T> {
	protected BinaryNode root;
	private int size;
	private int modifcations;
	private int rotations;

	/**
	 * Constructs an AVLTree that initializes root to NULL.
	 */
	protected AVLTree() {
		this.root = null;
		this.modifcations = 0;
		this.size = 0;
		this.rotations = 0;
	}

	/**
	 * Returns TRUE if the tree is empty and FALSE otherwise.
	 *
	 * @return boolean if AVLTree is empty
	 */
	protected boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * Returns the height of the tree. If the tree is empty, return -1.
	 * 
	 * @return height of AVLTree
	 */
	protected int height() {
		if (this.root == null)
			return -1;
		return this.root.height;
	}

	/**
	 * Returns the number of elements in the tree;
	 * 
	 * @return size of AVLTree
	 */
	protected int size() {
		return this.size;
	}

	/**
	 * Returns a String containing the elements of the tree. The elements of the
	 * tree are to be returned in a bracketed list and are to be separated by
	 * commas. The elements are to appear in-order.
	 * 
	 * @return in-order String of AVLTree
	 */
	public String toString() {
		return this.toArrayList().toString();
	}

	/**
	 * Places every element in this BinaryTree into an ArrayList<T> in-order
	 * 
	 * @return ArrayList<T> of binary nodes
	 */
	protected ArrayList<T> toArrayList() {
		Iterator<T> i = this.iterator();
		ArrayList<T> output = new ArrayList<>();
		while (i.hasNext())
			output.add(i.next());
		return output;
	}

	/**
	 * Returns an array of the elements in-order by first constructing an array
	 * list that then gets converted to an array
	 * 
	 * @return array of elements in AVLTree in-order
	 */
	protected Object[] toArray() {
		return this.toArrayList().toArray();
	}

	/**
	 * Returns a lazy pre-order iterator object that
	 * iterates over elements of type T.
	 * 
	 * @return a lazy pre-order iterator of the AVLTree
	 */
	public Iterator<T> iterator() {
		return new LazyPreOrderIterator();
	}

	/**
	 * returns a lazy in-order iterator object that
	 * iterates over elements of type T.
	 * 
	 * @return a lazy in-order iterator of the AVLTree
	 */
	protected Iterator<T> inOrderIterator() {
		return new LazyInOrderIterator();
	}

	/**
	 * Inserts a given element into the proper location in Binary Tree.
	 * If successful, returns TRUE, else FALSE.
	 * 
	 * @return boolean indicating success
	 */
	protected boolean insert(T o) {
		if (o == null)
			throw new IllegalArgumentException("Can't insert null");
		if (this.root == null) {
			this.root = new BinaryNode(o);
			this.size++;
			this.modifcations++;
			return true;
		}
		int beforeSize = this.size;
		this.root = this.root.getInsert(o);
		if (this.size == beforeSize) {
			return false;
		}
		this.modifcations++;
		return true;
	}

	/**
	 * Receives an element of the parameterized Comparable type T and returns
	 * TRUE if the element was successfully removed from the Binary Tree, else
	 * FALSE.
	 * 
	 * @return boolean indicating success
	 */
	protected boolean remove(T element) {
		// didn't pass a value to remove
		if (element == null)
			throw new IllegalArgumentException();
		if (this.root == null)
			return false; 	// tree has no elements
		// remove element
		int beforeSize = this.size;
		this.root = this.root.getRemove(element);
		if (this.size < beforeSize) {
			this.modifcations++;
			return true;
		}
		return false;
	}

	/**
	 * Returns the number of rotations needed on the AVLTree in order for it to
	 * be a balanced tree.
	 * 
	 * @return number of rotations
	 */
	protected int getRotationCount() {
		return this.rotations;
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
		private int height;

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
			this.height = 0;
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
		 * Returns the left child of this BinaryNode.
		 * 
		 * @return leftChild BinaryNode
		 */
		protected BinaryNode getLeftChild() {
			return this.leftChild;
		}

		/**
		 * Returns the right child of this BinaryNode.
		 * 
		 * @return rightChild BinaryNode
		 */
		protected BinaryNode getRightChild() {
			return this.rightChild;
		}

		/**
		 * Returns the height of this BinaryNode.
		 * 
		 * @return height
		 */
		protected int getHeight() {
			return this.height;
		}

		/**
		 * Inserts an element into the proper location in the tree.
		 * 
		 * 1. Compare insertElement with node.
		 * 2. Go to left child if insertElement is less than.
		 * 3. Go to right child if insertElement is greater than.
		 * 4. When the proper child is null, insert element.
		 * 5. If insertElement is found as a duplicate, return false.
		 * 
		 * @param o
		 *            - the element to insert
		 * 
		 * @return root of tree with inserted node
		 */
		private BinaryNode getInsert(T o) {
			int insertCompare = o.compareTo(this.element);
			BinaryNode output = this;
			if (insertCompare < 0) {
				// insert in the left subtree
				if (this.leftChild == null) {
					this.leftChild = new BinaryNode(o);
					AVLTree.this.size++;
				} else {
					output.leftChild = this.leftChild.getInsert(o);
					output = output.getRotation();	// check for rotations
				}
			} else if (insertCompare > 0) {
				// insert in the right subtree
				if (this.rightChild == null) {
					this.rightChild = new BinaryNode(o);
					AVLTree.this.size++;
				} else {
					output.rightChild = this.rightChild.getInsert(o);
					output = output.getRotation();	// check for rotations
				}
			} else
				return output;
			output.adjustHeight(); 	// Update height
			return output;
		}

		/**
		 * Recursively finds the element to remove in the AVLTree, and then
		 * removes it. Checks for any rotations and updates the heights and
		 * sizes.
		 * 
		 * @return BinaryNode that was removed
		 */
		private BinaryNode getRemove(T e) {
			BinaryNode output = this;
			if (output.element.compareTo(e) < 0) {
				// go to the right
				if (output.rightChild != null)
					output.rightChild = output.rightChild.getRemove(e);
				else
					return output;
			} else if (output.element.compareTo(e) > 0) {
				// go to the left
				if (output.leftChild != null)
					output.leftChild = output.leftChild.getRemove(e);
				else
					return output;
			} else {	// this is the element to remove
				// Element to remove has no children
				if (output.leftChild == null && output.rightChild == null) {
					AVLTree.this.size--;
					return null;
				}
				// Element to remove has leftChild
				else if (output.rightChild == null) {
					AVLTree.this.size--;
					output = output.leftChild;
				}
				// Element to remove has rightChild
				else if (output.leftChild == null) {
					AVLTree.this.size--;
					output = output.rightChild;
				}
				// Element has two children
				else if (output.leftChild != null && output.rightChild != null) {
					output.element = output.leftChild.findMax().element;
					int beforeSize = AVLTree.this.size;
					output.leftChild = output.leftChild
							.getRemove(output.element);
					if (beforeSize != AVLTree.this.size + 1)
						System.out.println("The size didn't change!");
				}
			}
			output = output.getRotation(); 	// Check for rotations
			output.adjustHeight(); 			// Update Height
			return output;
		}

		/**
		 * Iterates down the right side of the tree
		 * to return the largest BinaryNode.
		 * 
		 * @return max BinaryNode
		 */
		private BinaryNode findMax() {
			BinaryNode max = this;
			while (max.rightChild != null)
				max = max.rightChild;
			return max;
		}

		/**
		 * Finds if the tree needs to be rotated and will balance the tree, then
		 * gets the root of the new tree.
		 * 
		 * @return root of rotated node
		 */
		private BinaryNode getRotation() {
			if (this.leftChild == null && this.rightChild == null)
				return this; // No rotations needed
			if (this.leftChild == null) {
				if (this.rightChild.height > 0) {
					if (this.rightChild.rightChild == null) {
						return rightLeftRotation(); // Right subtree is too high
					}
					return leftRotation();	// Right subtree is too high
				}
				return this; // No rotations needed
			}
			if (this.rightChild == null) {
				if (this.leftChild.height > 0) {
					if (this.leftChild.leftChild == null) {
						return leftRightRotation(); // Left subtree is too high
					}
					return rightRotation(); // Left subtree is too high
				}
				return this; // No rotations needed
			}
			if (this.rightChild.height - this.leftChild.height > 1) {
				return leftRotation();	// Right subtree is too high
			}
			if (this.leftChild.height - this.rightChild.height > 1) {
				return rightRotation();	// Left subtree is too high
			}
			return this; // No rotations needed
		}

		/**
		 * Adjusts this height by comparing the heights of the left and right
		 * subtrees.
		 * 
		 * @return height of this node
		 */
		private void adjustHeight() {
			int leftHeight = this.leftChild == null ? 0
					: this.leftChild.height + 1;
			int rightHeight = this.rightChild == null ? 0
					: this.rightChild.height + 1;
			this.height = leftHeight > rightHeight ? leftHeight : rightHeight;
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
			AVLTree.this.rotations++;
			output.leftChild.adjustHeight();
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
			AVLTree.this.rotations++;
			output.rightChild.adjustHeight();
			return output;
		}

		/**
		 * Executes a double rotation on this node by first doinf a left
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
	}

	/**
	 * A lazy in-order iterator object.
	 * 
	 * @author derrowap
	 *
	 */
	protected class LazyInOrderIterator implements Iterator<T> {
		Stack<BinaryNode> stack = new Stack<>();
		BinaryNode lastNode;
		int modifications;

		/**
		 * Constructs a lazy in-order iterator object.
		 */
		private LazyInOrderIterator() {
			this.modifications = AVLTree.this.modifcations;
			this.lastNode = AVLTree.this.root;
			// avoid null pointer exception
			if (AVLTree.this.root != null) {
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
		public T next() {
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
			return this.lastNode.element;
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
			if (this.modifications != AVLTree.this.modifcations)
				throw new ConcurrentModificationException();
			if (AVLTree.this.remove(this.lastNode.element)) {
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
	protected class LazyPreOrderIterator implements Iterator<T> {
		Stack<BinaryNode> stack;
		BinaryNode lastNode;
		int modifications;

		/**
		 * Constructs a lazy pre-order iterator object.
		 */
		private LazyPreOrderIterator() {
			this.stack = new Stack<>();
			this.modifications = AVLTree.this.modifcations;
			this.lastNode = null;
			if (AVLTree.this.root != null)
				this.stack.push(AVLTree.this.root);
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
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			this.lastNode = this.stack.pop();
			if (this.lastNode.rightChild != null)
				this.stack.push(this.lastNode.rightChild);
			if (this.lastNode.leftChild != null)
				this.stack.push(this.lastNode.leftChild);
			return this.lastNode.element;
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
			if (this.modifications != AVLTree.this.modifcations)
				throw new ConcurrentModificationException();
			if (AVLTree.this.remove(this.lastNode.element)) {
				this.modifications++;
				this.lastNode = null;
			}
		}
	}
}