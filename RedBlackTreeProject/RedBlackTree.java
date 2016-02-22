import java.awt.Color;
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
	 * Removes the given element from this RedBlackTree. This represents Step 1
	 * and Step 4 of the algorithm.
	 * 
	 * @throws IllegalArgumentException
	 *             - if element given is null
	 * 
	 * @return true if element was removed
	 */
	protected boolean remove(T element) {
		if (element == null)
			throw new IllegalArgumentException();
		if (this.root == null)
			return false;

		if (this.root.hasTwoBlackChildren()) {
			this.root.setRed();
		}
		int beforeSize = this.size;
		if (!this.root.hasTwoBlackChildren()
				|| (this.root.leftChild == null && this.root.rightChild == null)
				|| (this.root.element.equals(element))) {
			this.root = this.root.removeStep2B(element, null);
		} else {
			if (this.root.element.compareTo(element) > 0) {
				this.root = this.root.leftChild.removeStep2(element, this.root);
			} else {
				this.root = this.root.rightChild
						.removeStep2(element, this.root);
			}
		}
		if (this.root != null)
			this.root.setBlack();
		if (beforeSize != this.size) {
			this.modifications++;
			return true;
		}
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
		 * Case2A of the top-down removal algorithm:
		 * This is the main case.
		 * P - this
		 * X - this.leftChild or this.rightChild
		 * T - opposite child of X
		 * ""X and T are BLACK, P is RED""
		 * 
		 * 1) If X has 2 black children, go to step 2A.
		 * 2) If X has at least one red child, go to step 2B.
		 * 
		 * @param element
		 * @return
		 */
		private BinaryNode removeStep2(T element, BinaryNode parent) {
			if (this.hasTwoBlackChildren())
				parent = this.removeStep2A(element, parent);
			else if (this.hasRedLeftChild() || this.hasRedRightChild())
				parent = this.removeStep2B(element, parent);
			return parent;
		}

		/**
		 * Case2A of the top-down removal algorithm:
		 * ""X has two BLACK children""
		 * 
		 * In this case, we consider T:
		 * 2A1. T has 2 black Children
		 * 2A2. T’s inside child is red
		 * 2A3. T’s outside child is red
		 * 2A4. If both of T’s children are red, we can do either 2A2 or 2A3.
		 * The test cases assume you do case 2A3.
		 * 
		 * @param elemenet
		 *            - element to be deleted
		 * @return parent of this subtree
		 */
		private BinaryNode removeStep2A(T element, BinaryNode parent) {
			BinaryNode T = (parent.leftChild != null && parent.leftChild.element
					.equals(this.element)) ? parent.rightChild
					: parent.leftChild;
			if (T.hasTwoBlackChildren())
				return this.removeStep2A1(element, parent);
			if (parent.leftChild != null
					&& parent.leftChild.element.equals(T.element)) {
				if (T.hasRedLeftChild())
					return this.removeStep2A3(element, parent);
				if (T.hasRedRightChild())
					return this.removeStep2A2(element, parent);
			} else {
				if (T.hasRedRightChild())
					return this.removeStep2A3(element, parent);
				if (T.hasRedLeftChild())
					return this.removeStep2A2(element, parent);
			}
			return parent;
		}

		/**
		 * Case2A1 of the top-down removal algorithm:
		 * ""X and T have 2 BLACK children""
		 * 
		 * 1) Recolor X, P and T
		 * 2) If X is the node to be deleted, go to step 3
		 * else move down the tree and go back to step 2
		 * 
		 * @param element
		 *            - element to be deleted
		 * @return parent of this subtree
		 */
		private BinaryNode removeStep2A1(T element, BinaryNode parent) {
			parent.setBlack();
			parent.leftChild.setRed();
			parent.rightChild.setRed();
			if (parent.leftChild != null
					&& parent.leftChild.element.equals(this.element)) {
				if (this.element.equals(element))
					parent = parent.leftChild.removeStep3(element, parent);
				else if (this.element.compareTo(element) > 0) {
					if (parent.leftChild.leftChild == null)
						return parent;
					parent.leftChild = parent.leftChild.leftChild.removeStep2(
							element, parent.leftChild);
				} else {
					if (parent.leftChild.rightChild == null)
						return parent;
					parent.leftChild = parent.leftChild.rightChild.removeStep2(
							element, parent.leftChild);
				}
			} else if (parent.rightChild != null
					&& parent.rightChild.element.equals(this.element)) {
				if (this.element.equals(element))
					parent = parent.rightChild.removeStep3(element, parent);
				else if (this.element.compareTo(element) > 0) {
					if (parent.rightChild.leftChild == null)
						return parent;
					parent.rightChild = parent.rightChild.leftChild
							.removeStep2(element, parent.rightChild);
				} else {
					if (parent.rightChild.rightChild == null)
						return parent;
					parent.rightChild = parent.rightChild.rightChild
							.removeStep2(element, parent.rightChild);
				}
			}
			return parent;
		}

		/**
		 * Case2A2 of the top-down removal algorithm:
		 * ""X has 2 BLACK children & L is RED""
		 * 
		 * 1) Perform a double rotation on P, T, and L.
		 * 2) Recolor X and P
		 * 3) If X is the node to be deleted, go to step 3
		 * else move down the tree and go back to step 2
		 * 
		 * @param element
		 *            - element to be deleted
		 * @return parent of this subtree
		 */
		private BinaryNode removeStep2A2(T element, BinaryNode parent) {
			if (parent.leftChild.element.equals(this.element)) {
				parent = parent.rightLeftRotation();
				parent.leftChild.setBlack();
				parent.leftChild.leftChild.setRed();
				if (this.element.equals(element))
					parent.leftChild = parent.leftChild.leftChild.removeStep3(
							element, parent.leftChild);
				else {
					if (this.element.compareTo(element) > 0) {
						if (parent.leftChild.leftChild.leftChild == null)
							return parent;
						parent.leftChild.leftChild = parent.leftChild.leftChild.leftChild
								.removeStep2(element,
										parent.leftChild.leftChild);
					} else {
						if (parent.leftChild.leftChild.rightChild == null)
							return parent;
						parent.leftChild.leftChild = parent.leftChild.leftChild.rightChild
								.removeStep2(element,
										parent.leftChild.leftChild);
					}
				}
			} else if (parent.rightChild.element.equals(this.element)) {
				parent = parent.leftRightRotation();
				parent.rightChild.setBlack();
				parent.rightChild.rightChild.setRed();
				if (this.element.equals(element))
					parent.rightChild = parent.rightChild.rightChild
							.removeStep3(element, parent.rightChild);
				else {
					if (this.element.compareTo(element) > 0) {
						if (parent.rightChild.rightChild.leftChild == null)
							return parent;
						parent.rightChild.rightChild = parent.rightChild.rightChild.leftChild
								.removeStep2(element,
										parent.rightChild.rightChild);
					} else {
						if (parent.rightChild.rightChild.rightChild == null)
							return parent;
						parent.rightChild.rightChild = parent.rightChild.rightChild.rightChild
								.removeStep2(element,
										parent.rightChild.rightChild);
					}
				}
			}
			return parent;
		}

		/**
		 * Case 2A3 of the top-down removal algorithm:
		 * ""X has 2 BLACK children & R is RED""
		 * 
		 * 1) Perform a single rotation on P, T, and R.
		 * 2) Recolor X, P, T and R
		 * 3) If X is the node to be deleted, go to step 3
		 * else move down the tree and go back to step 2
		 * 
		 * @param element
		 *            - element to be deleted
		 * @return parent of this subtree
		 */
		private BinaryNode removeStep2A3(T element, BinaryNode parent) {
			if (parent.leftChild.element.equals(this.element)) {
				parent = parent.leftRotation();
				parent.rightChild.setBlack();
				parent.setRed();
				parent.leftChild.setBlack();
				parent.leftChild.leftChild.setRed();
				if (this.element.equals(element))
					parent.leftChild = parent.leftChild.leftChild.removeStep3(
							element, parent.leftChild);
				else {
					if (this.element.compareTo(element) > 0) {
						if (parent.leftChild.leftChild.leftChild == null)
							return parent;
						parent.leftChild.leftChild = parent.leftChild.leftChild.leftChild
								.removeStep2(element,
										parent.leftChild.leftChild);
					} else {
						if (parent.leftChild.leftChild.rightChild == null)
							return parent;
						parent.leftChild.leftChild = parent.leftChild.leftChild.rightChild
								.removeStep2(element,
										parent.leftChild.leftChild);
					}
				}
			} else if (parent.rightChild.element.equals(this.element)) {
				parent = parent.rightRotation();
				parent.leftChild.setBlack();
				parent.setRed();
				parent.rightChild.setBlack();
				parent.rightChild.rightChild.setRed();
				if (this.element.equals(element))
					parent.rightChild = parent.rightChild.rightChild
							.removeStep3(element, parent.rightChild);
				else {
					if (this.element.compareTo(element) > 0) {
						if (parent.rightChild.rightChild.leftChild == null)
							return parent;
						parent.rightChild.rightChild = parent.rightChild.rightChild.leftChild
								.removeStep2(element,
										parent.rightChild.rightChild);
					} else {
						if (parent.rightChild.rightChild.rightChild == null)
							return parent;
						parent.rightChild.rightChild = parent.rightChild.rightChild.rightChild
								.removeStep2(element,
										parent.rightChild.rightChild);
					}
				}
			}
			return parent;
		}

		/**
		 * Case 2B of the top-down removal algorithm:
		 * ""X has at least one RED child""
		 * 
		 * 1) If X is the node to be deleted, go to step 3
		 * 2) Else move down the tree. The new node is either red or black.
		 * 3) If it is red, go to step 2B1
		 * 4) If it is black go to step 2B2.
		 * 
		 * @param element
		 *            - element to be deleted
		 * @return parent of this subtree
		 */
		private BinaryNode removeStep2B(T element, BinaryNode parent) {
			if (this.element.equals(element))
				parent = this.removeStep3(element, parent);
			else if (parent == null) {
				if (this.element.compareTo(element) > 0) {
					if (this.leftChild == null)
						return this;
					if (this.leftChild.color == Color.RED)
						parent = this.leftChild.removeStep2B1(element, this);
					else
						parent = this.leftChild.removeStep2B2(element, this);
				} else {
					if (this.rightChild == null)
						return this;
					if (this.rightChild.color == Color.RED)
						parent = this.rightChild.removeStep2B1(element, this);
					else
						parent = this.rightChild.removeStep2B2(element, this);
				}
			} else if (parent.leftChild != null
					&& parent.leftChild.element.equals(this.element)) {
				if (this.element.compareTo(element) > 0) {
					if (parent.leftChild.leftChild == null)
						return parent;
					if (parent.leftChild.leftChild.color == Color.RED) {
						parent.leftChild = parent.leftChild.leftChild
								.removeStep2B1(element, parent.leftChild);
					} else {
						parent.leftChild = parent.leftChild.leftChild
								.removeStep2B2(element, parent.leftChild);
					}
				} else {
					if (parent.leftChild.rightChild == null)
						return parent;
					if (parent.leftChild.rightChild.color == Color.RED) {
						parent.leftChild = parent.leftChild.rightChild
								.removeStep2B1(element, parent.leftChild);
					} else {
						parent.leftChild = parent.leftChild.rightChild
								.removeStep2B2(element, parent.leftChild);
					}
				}
			} else if (parent.rightChild != null
					&& parent.rightChild.element.equals(this.element)) {
				if (this.element.compareTo(element) > 0) {
					if (parent.rightChild.leftChild == null)
						return parent;
					if (parent.rightChild.leftChild.color == Color.RED) {
						parent.rightChild = parent.rightChild.leftChild
								.removeStep2B1(element, parent.rightChild);
					} else {
						parent.rightChild = parent.rightChild.leftChild
								.removeStep2B2(element, parent.rightChild);
					}
				} else {
					if (parent.rightChild.rightChild == null)
						return parent;
					if (parent.rightChild.rightChild.color == Color.RED) {
						parent.rightChild = parent.rightChild.rightChild
								.removeStep2B1(element, parent.rightChild);
					} else {
						parent.rightChild = parent.rightChild.rightChild
								.removeStep2B2(element, parent.rightChild);
					}
				}
			}
			return parent;
		}

		/**
		 * Case 2B1 of the top-down removal algorithm:
		 * ""X is RED""
		 * 
		 * If X is to be deleted, go to step 3 else move down the tree again and
		 * go back to step 2.
		 * 
		 * @param element
		 *            - element to be deleted
		 * @return parent of this subtree
		 */
		private BinaryNode removeStep2B1(T element, BinaryNode parent) {
			if (this.element.equals(element))
				parent = this.removeStep3(element, parent);
			else if (parent.leftChild != null
					&& parent.leftChild.element.equals(this.element)) {
				if (this.element.compareTo(element) > 0) {
					if (parent.leftChild.leftChild == null)
						return parent;
					parent.leftChild = parent.leftChild.leftChild.removeStep2(
							element, parent.leftChild);
				} else {
					if (parent.leftChild.rightChild == null)
						return parent;
					parent.leftChild = parent.leftChild.rightChild.removeStep2(
							element, parent.leftChild);
				}
			} else if (parent.rightChild != null
					&& parent.rightChild.element.equals(this.element)) {
				if (this.element.compareTo(element) > 0) {
					if (parent.rightChild.leftChild == null)
						return parent;
					parent.rightChild = parent.rightChild.leftChild
							.removeStep2(element, parent.rightChild);
				} else {
					if (parent.rightChild.rightChild == null)
						return parent;
					parent.rightChild = parent.rightChild.rightChild
							.removeStep2(element, parent.rightChild);
				}
			}
			return parent;
		}

		/**
		 * Case 2B2 of the top-down removal algorithm:
		 * ""The new X is BLACK""
		 * 
		 * 1) Rotate T around P
		 * 2) Recolor P and T
		 * 3) Go back to step 2 (We will check again whether X is the node to be
		 * removed. It won’t reduce to this case.)
		 * 
		 * @param element
		 *            - element to be deleted
		 * @return parent of this subtree
		 */
		private BinaryNode removeStep2B2(T element, BinaryNode parent) {
			if (parent.leftChild.element.equals(this.element)) {
				parent = parent.leftRotation();
				parent.setBlack();
				parent.leftChild.setRed();
				parent.leftChild = parent.leftChild.leftChild.removeStep2(
						element, parent.leftChild);
			} else {
				parent = parent.rightRotation();
				parent.setBlack();
				parent.rightChild.setRed();
				parent.rightChild = parent.rightChild.rightChild.removeStep2(
						element, parent.rightChild);
			}
			return parent;
		}

		/**
		 * Case 3 of the top-down removal algorithm:
		 * 
		 * a. If X has two children, obtain the largest value V of the left
		 * sub-tree. If X is red, copy V to X, move X to the left child and go
		 * to step 2 with value V. If X is black, store V in a temporary
		 * variable, do Not move X and go to step 2b with V. When finished with
		 * the removal, place V into X.
		 * 
		 * b. If X is a leaf, it must be red and can be safely deleted. Do so
		 * and go to step 4.
		 * 
		 * c. If X has a single child, then X may be red or black. It is black
		 * only in case 2B. In this case, X has exactly one red child. Color
		 * that child black and remove X. Then go to step 4
		 * 
		 * @param element
		 *            - element to be deleted
		 * @return parent of this subtree
		 */
		private BinaryNode removeStep3(T element, BinaryNode parent) {
			RedBlackTree.this.size--;

			/* X is a leaf node */
			if (this.rightChild == null && this.leftChild == null) {
				if (parent == null)
					return null;
				if (parent.leftChild != null
						&& parent.leftChild.element.equals(this.element))
					parent.leftChild = null;
				else if (parent.rightChild != null
						&& parent.rightChild.element.equals(this.element))
					parent.rightChild = null;
				return parent;
			}

			/* X has two non-null children */
			if (this.leftChild != null && this.rightChild != null) {
				BinaryNode max = this.leftChild.findMaxNode();
				if (this.color == Color.RED) {
					RedBlackTree.this.size++;
					if (parent == null) {
						System.out.println("this: " + this.element + " max: " + max.element);
						this.element = max.element;
						return this.leftChild.removeStep2(max.element, this);
						// parent.element = max.element;
					} else if (parent.leftChild != null
							&& parent.leftChild.element.equals(this.element)) {
						parent.leftChild.element = max.element;
						parent.leftChild = parent.leftChild.leftChild
								.removeStep2(max.element, parent.leftChild);
						// parent.leftChild.element = max.element;
					} else {
						parent.rightChild.element = max.element;
						parent.rightChild = parent.rightChild.leftChild
								.removeStep2(max.element, parent.rightChild);
						// parent.rightChild.element = max.element;
					}
				} else {
					parent = this.removeStep2B(max.element, parent);
					if (parent.element.equals(this.element))
						parent.element = max.element;
					else if (parent.leftChild != null
							&& parent.leftChild.element.equals(this.element))
						parent.leftChild.element = max.element;
					else if (parent.rightChild != null
							&& parent.rightChild.element.equals(this.element))
						parent.rightChild.element = max.element;
				}
				return parent;
			}

			/* X has a single right child */
			if (this.rightChild != null) {
				if (parent == null) {
					return this.rightChild;
				}
				if (parent.leftChild != null
						&& parent.leftChild.element.equals(this.element)) {
					parent.leftChild = this.rightChild;
					parent.leftChild.setBlack();
				} else if (parent.rightChild != null
						&& parent.rightChild.element.equals(this.element)) {
					parent.rightChild = this.rightChild;
					parent.rightChild.setBlack();
				}
				return parent;
			}

			/* X has a single left child */
			if (this.leftChild != null) {
				if (parent == null) {
					return this.leftChild;
				}
				if (parent.leftChild != null
						&& parent.leftChild.element.equals(this.element)) {
					parent.leftChild = this.leftChild;
					parent.leftChild.setBlack();
				} else if (parent.rightChild != null
						&& parent.rightChild.element.equals(this.element)) {
					parent.rightChild = this.leftChild;
					parent.rightChild.setBlack();
				}
			}
			return parent;
		}

		/**
		 * Checks to see if this has two, non-null BLACK children.
		 * 
		 * @return true if this has two, non-null BLACK children
		 */
		private boolean hasTwoBlackChildren() {
			if ((this.leftChild != null && this.rightChild != null
					&& this.leftChild.color == Color.BLACK && this.rightChild.color == Color.BLACK)
					|| (this.leftChild == null && this.rightChild == null))
				return true;
			return false;
		}

		/**
		 * Checks to see if this has a non-null RED left child.
		 * 
		 * @return true if this has a non-null RED left child
		 */
		private boolean hasRedLeftChild() {
			if (this.leftChild != null && this.leftChild.color == Color.RED)
				return true;
			return false;
		}

		/**
		 * Checks to see if this has a non-null RED right child.
		 * 
		 * @return true if this has a non-null RED right child
		 */
		private boolean hasRedRightChild() {
			if (this.rightChild != null && this.rightChild.color == Color.RED)
				return true;
			return false;
		}

		/**
		 * If the tree needs to be rotated, it will balance the tree, then
		 * returns the top of this new tree.
		 * 
		 * @return top of rotated tree
		 */
		private BinaryNode detectRotation(BinaryNode parent,
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
		 * Finds the largest node in this tree.
		 * 
		 * @return largest node in this tree
		 */
		private BinaryNode findMaxNode() {
			BinaryNode output = this;
			while (output.rightChild != null)
				output = output.rightChild;
			return output;
		}

		/**
		 * Sets this BinaryNode's color to BLACK.
		 */
		private void setBlack() {
			this.color = Color.BLACK;
		}

		/**
		 * Sets this BinaryNode's color to RED.
		 */
		private void setRed() {
			this.color = Color.RED;
		}

		/**
		 * Sets the color of both children to BLACK and the color of this
		 * BinaryNode to RED.
		 */
		private void swapColors() {
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
