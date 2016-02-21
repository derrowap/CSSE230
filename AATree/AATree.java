import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class AATree<T extends Comparable<? super T>> implements
		Iterable<AATree.BinaryNode> {
	protected BinaryNode root;
	private int size;
	protected int rotationCount;
	private BinaryNode last;
	private BinaryNode deleted;

	public AATree() {
		this.root = null;
		this.size = 0;
		this.rotationCount = 0;
	}

	protected boolean insert(T e) {
		if (e == null)
			return false;
		if (this.root == null) {
			this.root = new BinaryNode(e);
			this.size++;
			return true;
		}
		int beforeSize = this.size;
		this.root = this.root.insert(e);
		if (this.size == beforeSize++)
			return true;
		return false;
	}

	protected boolean remove(T e) {
		if (e == null || this.root == null)
			return false;
		int beforeSize = this.size;
		this.deleted = null;
		this.root = this.root.remove(e);
		if (this.size == beforeSize--)
			return true;
		return false;
	}

	public Iterator<AATree.BinaryNode> iterator() {
		return new AATreeIterator();
	}

	protected ArrayList<Object> toArrayList() {
		Iterator<AATree.BinaryNode> i = this.iterator();
		ArrayList<Object> output = new ArrayList<>();
		while (i.hasNext())
			output.add(i.next());
		return output;
	}

	protected int size() {
		return this.size;
	}

	public class BinaryNode {
		protected T element;
		protected BinaryNode leftChild, rightChild;
		protected int level;

		public BinaryNode(T e) {
			this.element = e;
			this.leftChild = this.rightChild = null;
			this.level = 1;
		}

		protected T getElement() {
			return this.element;
		}

		protected int getLevel() {
			return this.level;
		}

		protected BinaryNode getLeftChild() {
			return this.leftChild;
		}

		protected BinaryNode getRightChild() {
			return this.rightChild;
		}

		private BinaryNode insert(T e) {
			BinaryNode output = this;
			int compared = this.element.compareTo(e);
			if (compared == 0)
				return this; // Duplicate element
			else if (compared < 0) {
				if (this.rightChild == null) {
					this.rightChild = new BinaryNode(e);
					AATree.this.size++;
				} else {
					this.rightChild = this.rightChild.insert(e);
				}
			} else if (compared > 0) {
				if (this.leftChild == null) {
					this.leftChild = new BinaryNode(e);
					AATree.this.size++;
				} else {
					this.leftChild = this.leftChild.insert(e);
				}
			}
			output = this.skew();
			return output.split();
		}

		private BinaryNode remove(T e) {
			BinaryNode output = this;
			int compared = this.element.compareTo(e);
			if(compared < 0)
				output.rightChild = output.rightChild.remove(e);
			else if(compared > 0)
				output.leftChild = output.leftChild.remove(e);
			else {
				if(output.leftChild == null && output.rightChild == null)
					return null;
				BinaryNode parent = output;
				BinaryNode replacement = null;
				if (output.leftChild != null) {
					replacement = output.leftChild;
					while(replacement.rightChild != null) {
						parent = replacement;
						replacement = replacement.rightChild;
					}
				} else if (output.rightChild != null) {
					replacement = output.rightChild;
					while(replacement.leftChild != null) {
						parent = replacement;
						replacement = replacement.leftChild;
					}
				}
				output.element = replacement.element;
				if(parent.leftChild == replacement)
					parent.leftChild = null;
				else
					parent.rightChild = null;
			}
			
			int minLevel = output.leftChild != null ? output.leftChild.level : output.level;
			minLevel = output.rightChild != null && output.rightChild.level + 1 < minLevel ? output.rightChild.level + 1: minLevel;
			if(minLevel < output.level){
				output.level = minLevel;
				if(output.rightChild != null && minLevel < output.rightChild.level)
					output.rightChild.level = minLevel;
			}
			
			output = output.skew();
			if (output.rightChild != null)
				output.rightChild = output.rightChild.skew();
			if (output.rightChild.rightChild != null)
				output.rightChild.rightChild = output.rightChild.rightChild
						.skew();
			output = output.split();
			if (output.rightChild != null)
				output.rightChild = output.rightChild.split();
			return output;
			
			
			
			
//			BinaryNode output = this;
//			int compared = this.element.compareTo(e);
//			/* Search down the tree and set pointers last and deleted */
//			AATree.this.last = this;
//			if (compared > 0 && output.leftChild != null) {
//				// if (this.leftChild == null)
//				// return output;
//				// if (output.leftChild != null)
//				output.leftChild = output.leftChild.remove(e);
//			} else if (compared < 0 && output.rightChild != null) {
//				// if (this.rightChild == null)
//				// return output;
//				AATree.this.deleted = this;
//				// if (output.rightChild != null)
//				output.rightChild = output.rightChild.remove(e);
//			}
//			/* At the bottom of the tree we remove the element (if present) */
//			if (output.element.equals(AATree.this.last.element)) {
//				if (AATree.this.deleted == null || !e.equals(AATree.this.deleted.element))
//					return output; // Item not found
//				AATree.this.deleted.element = output.element;
//				output = output.rightChild;
//				AATree.this.size--;
//				/* On the way back, we rebalance */
//			} else if ((output.leftChild != null && output.leftChild.level < output.level - 1)
//					|| (output.rightChild != null && output.rightChild.level < output.level - 1)) {
//				if (output.rightChild.level > output.level - 1) {
//					output.rightChild.level = output.level;
//				}
//				output = output.skew();
//				if (output.rightChild != null)
//					output.rightChild = output.rightChild.skew();
//				if (output.rightChild.rightChild != null)
//					output.rightChild.rightChild = output.rightChild.rightChild
//							.skew();
//				output = output.split();
//				if (output.rightChild != null)
//					output.rightChild = output.rightChild.split();
//			}
//			return output;
		}

		private BinaryNode skew() {
			BinaryNode output = this;
			if (this.leftChild != null && this.leftChild.level == this.level) {
				AATree.this.rotationCount++;
				output = this.leftChild;
				this.leftChild = output.rightChild;
				output.rightChild = this;
			}
			return output;
		}

		private BinaryNode split() {
			BinaryNode output = this;
			if (this.rightChild != null && this.rightChild.rightChild != null
					&& this.rightChild.rightChild.level == this.level) {
				AATree.this.rotationCount++;
				output = this.rightChild;
				this.rightChild = output.leftChild;
				output.leftChild = this;
				output.level++;
			}
			return output;
		}
	}

	private class AATreeIterator implements Iterator<AATree.BinaryNode> {
		Stack<BinaryNode> stack;
		BinaryNode lastNode;

		private AATreeIterator() {
			this.stack = new Stack<>();
			this.lastNode = null;
			if (AATree.this.root != null)
				this.stack.push(AATree.this.root);
		}

		public boolean hasNext() {
			return !this.stack.empty();
		}

		public BinaryNode next() {
			if (!hasNext())
				throw new NoSuchElementException();
			this.lastNode = this.stack.pop();
			if (this.lastNode.rightChild != null)
				this.stack.push(this.lastNode.rightChild);
			if (this.lastNode.leftChild != null)
				this.stack.push(this.lastNode.leftChild);
			return this.lastNode;
		}
	}

}
