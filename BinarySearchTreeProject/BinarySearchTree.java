import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BinarySearchTree<T extends Comparable<? super T>> implements
		Iterable<T> {
	private BinaryNode root;
	protected int size;
	protected int modifcations = 0;

	// Initialization of BinarySearchTree
	public BinarySearchTree() {
		this.root = null;
	}

	/*
	 * Returns TRUE if the tree is empty and FALSE otherwise
	 */
	public boolean isEmpty() {
		return this.root == null;
	}

	/*
	 * Returns the height of the tree. If the tree is empty, return -1
	 */
	public int height() {
		if (this.isEmpty())
			return -1;
		return this.root.getHeight();
	}

	/*
	 * Returns the number of elements in the tree;
	 */
	public int size() {
		return this.size;
	}

	/*
	 * Returns a String containing the elements of the tree. The elements of the
	 * tree are to be returned in a bracketed list and are to be separated by
	 * commas. The elements are to appear in order
	 */
	@Override
	public String toString() {
		return this.toArrayList().toString();
	}

	/*
	 * Places every element in this BinaryTree into an ArrayList<T> in-order
	 * 
	 * @return ArrayList<T> of binary nodes
	 */
	public ArrayList<T> toArrayList() {
		Iterator<T> i = this.iterator();
		ArrayList<T> output = new ArrayList<>();
		while (i.hasNext())
			output.add(i.next());
		return output;
	}

	/*
	 * returns an array of the elements in-order
	 */
	public Object[] toArray() {
		return this.toArrayList().toArray();
	}

	/*
	 * returns a lazy in-order iterator object throws a NoSuchElementException
	 * if the root is empty
	 */
	@Override
	public Iterator<T> iterator() {
		return new LazyInOrderIterator(this.root);
	}

	/*
	 * returns a lazy pre-order iterator object throws a NoSuchElementException
	 * if the root is empty
	 */
	public Iterator<T> preOrderIterator() {
		return new LazyPreOrderIterator(this.root);
	}

	/*
	 * Inserts a given element into the proper location in Binary Tree
	 * 
	 * @return boolean indicating if it inserted into tree or not
	 */
	public boolean insert(T o) {
		if (o == null)
			throw new IllegalArgumentException("Can't insert null");
		if (this.root == null) {
			this.root = new BinaryNode(o);
			this.size++;
			this.modifcations++;
			return true;
		}
		if (this.root.getInsert(o)) {
			this.modifcations++;
			return true;
		}
		return false;
	}

	/*
	 * Receives an element of the parameterized Comparable type T and returns
	 * true if the element was successfully removed from the Binary Tree.
	 * 
	 * @return TRUE if removed, FALSE if not removed
	 */
	public boolean remove(T element) {
		if (element == null)
			throw new IllegalArgumentException();
		MyBoolean b = new MyBoolean();
		if (this.root == null) {
			// tree has no elements
			return false;
		}
		this.root = this.root.getRemove(b, element);
		if (b.getValue())
			this.modifcations++;
		return b.getValue();
	}

	protected class BinaryNode {
		protected T element;
		protected BinaryNode leftChild;
		protected BinaryNode rightChild;

		public BinaryNode(T e) {
			this.element = e;
			this.leftChild = null;
			this.rightChild = null;
		}

		/*
		 * Computes the height of this BinaryNode by returning the greatest
		 * value between the height on the left side and the height on the right
		 * side.
		 * 
		 * @return int of height of BinaryNode
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

		/*
		 * 1. Compare insertElement with node.
		 * 2. Go to left child if insertElement is less than.
		 * 3. Go to right child if insertElement is greater than.
		 * 4. When the proper child is null, insert element.
		 * 5. If insertElement is found as a duplicate, return false.
		 */
		public boolean getInsert(T o) {
			if (o.compareTo(this.element) < 0) {

				// o is less than the current element in the binary tree
				if (this.leftChild != null)
					return this.leftChild.getInsert(o);

				// add to tree
				this.leftChild = new BinaryNode(o);
				BinarySearchTree.this.size++;
				return true;
			}

			if (o.compareTo(this.element) > 0) {

				// o is greater than the current element in the binary tree
				if (this.rightChild != null)
					return this.rightChild.getInsert(o);

				// add to tree
				this.rightChild = new BinaryNode(o);
				BinarySearchTree.this.size++;
				return true;

			}

			// o is a duplicate and the element is already in the binary tree
			return false;
		}

		/*
		 * 1. Has no children:
		 * set reference = null
		 * 2. Has one child:
		 * set reference = child
		 * 3. Has two children:
		 * find largest element in left subtree of element to remove
		 * set element to remove = largest element
		 * if largest element has a left child:
		 * call remove on left subtree of duplicate(largest element)
		 * sets largest element's old reference equal to left child
		 */
		public BinaryNode getRemove(MyBoolean b, T e) {
			if (this.element.compareTo(e) < 0) {
				// go to the right
				if (this.rightChild != null)
					this.rightChild = this.rightChild.getRemove(b, e);
				else
					return null;
			}

			else if (this.element.compareTo(e) > 0) {
				// go to the left
				if (this.leftChild != null)
					this.leftChild = this.leftChild.getRemove(b, e);
				else
					return null;
			}

			else {
				// this is the element to remove

				// Element to remove has no children
				if (this.leftChild == null && this.rightChild == null) {
					b.setValue(true);
					BinarySearchTree.this.size--;
					return null;
				}

				// Element to remove has leftChild
				else if (this.rightChild == null) {
					b.setValue(true);
					BinarySearchTree.this.size--;
					return this.leftChild;
				}

				// Element to remove has rightChild
				else if (this.leftChild == null) {
					b.setValue(true);
					BinarySearchTree.this.size--;
					return this.rightChild;
				}

				// Element has two children
				else if (this.leftChild != null && this.rightChild != null) {
					this.element = this.leftChild.findMax().element;
					this.leftChild = this.leftChild.getRemove(b, this.element);
				}
			}
			return this;
		}

		public BinaryNode findMax() {
			BinaryNode max = this;
			while (max.rightChild != null) {
				max = max.rightChild;
			}
			return max;
		}
	}

	public class LazyInOrderIterator implements Iterator<T> {
		Stack<BinaryNode> stack = new Stack<>();
		BinaryNode lastNode;
		int modifications = 0;

		// Initialization of InOrderIterator
		public LazyInOrderIterator(BinaryNode node) {
			this.modifications = BinarySearchTree.this.modifcations;
			this.lastNode = null;
			// avoid null pointer exception
			if (node != null) {
				// add all nodes on the left side to the stack
				while (node.leftChild != null) {
					this.stack.push(node);
					node = node.leftChild;
				}
				// pushes left-most element on the stack
				this.stack.push(node);
			}
		}

		/*
		 * Checks if there exists a next element in the iterator. If there is,
		 * return TRUE, else FALSE.
		 * 
		 * @return boolean if there is a next value
		 */
		@Override
		public boolean hasNext() {
			return !this.stack.empty();
		}

		/*
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
		@Override
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

		@Override
		public void remove() {
			if (this.lastNode == null)
				throw new IllegalStateException();
			if (this.modifications != BinarySearchTree.this.modifcations)
				throw new ConcurrentModificationException();
			if (BinarySearchTree.this.remove(this.lastNode.element)) {
				this.modifications++;
				this.lastNode = null;
			}
		}
	}

	public class LazyPreOrderIterator implements Iterator<T> {
		Stack<BinaryNode> stack = new Stack<>();
		BinaryNode lastNode;
		int modifications = 0;

		// Initialization of PreOrderIterator
		public LazyPreOrderIterator(BinaryNode node) {
			this.modifications = BinarySearchTree.this.modifcations;
			this.lastNode = null;

			if (node != null)
				this.stack.push(node);
		}

		/*
		 * Checks if there exists a next element in the iterator. If there is,
		 * return TRUE, else FALSE.
		 * 
		 * @return boolean if there is a next value
		 */
		@Override
		public boolean hasNext() {
			return !this.stack.empty();
		}

		/*
		 * Finds the next element in the iterator in a pre-order arrangement
		 * throws a NoSuchElementException if there is no next element
		 * 
		 * 1. Pop top of stack.
		 * 2. Push popped node's right child to stack.
		 * 3. Push popped node's left child to stack.
		 * 4. Return popped node.
		 * 
		 * @return next object in tree
		 */
		@Override
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

		@Override
		public void remove() {
			if (this.lastNode == null)
				throw new IllegalStateException();
			if (this.modifications != BinarySearchTree.this.modifcations)
				throw new ConcurrentModificationException();
			if (BinarySearchTree.this.remove(this.lastNode.element)) {
				this.modifications++;
				this.lastNode = null;
			}
		}

	}

	public class MyBoolean {
		private boolean value = false;

		public boolean getValue() {
			return this.value;
		}

		public void setValue(boolean b) {
			this.value = b;
		}
	}
}