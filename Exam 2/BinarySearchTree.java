import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BinarySearchTree<T extends Comparable<? super T>> implements
		Iterable<T> {
	public BinaryNode root;
	private int size;
	private int modCount;

	public BinarySearchTree() {
		root = null;
		size = 0;
		modCount = 0;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public int height() {
		if (root == null)
			return -1;
		return root.height();
	}

	public int size() {
		return size;
	}

	public boolean insert(T element) {
		if (element == null)
			throw new IllegalArgumentException("Attempting to insert null.");
		if (root == null) {
			root = new BinaryNode(element);
			size++;
			modCount++;
			return true;
		}
		return root.insert(element);
	}

	public boolean remove(T element) {
		if (element == null)
			throw new IllegalArgumentException("Attempting to insert null.");
		if (root == null) {
			return false;
		}
		MyBoolean b = new MyBoolean();
		root = root.remove(element, b);
		return b.getValue();
	}

	/**
	 * Returns the greatest element in this tree less than or equal to
	 * the given element, or null if there is no such element.
	 * 
	 * @param e
	 *            - the value to match
	 * @return the greatest element less than or equal to e, or null
	 *         if there is no such element
	 */
	public T floor(T e) {
		// TODO: implement this method
		ArrayList<T> t = this.toArrayList();
		T previous = null;
		for (int i = 0; i < t.size(); i++) {
			if (t.get(i) == e)
				return t.get(i);
			if (t.get(i).compareTo(e) > 0)
				return previous;
			previous = t.get(i);
		}
		return previous;

	}

	/**
	 * Returns a shallow copy of this BinarySearchTree instance. (The
	 * elements themselves are not copied.)
	 * 
	 * @return a clone of this BinarySearchTree instance
	 */
	public BinarySearchTree<T> clone() {
		// TODO: implement this method
		BinarySearchTree<T> b = new BinarySearchTree<>();
		if (this.root == null)
			return b;
		Iterator<T> t =this.preOrderiterator();
		while(t.hasNext()) {
			b.insert(t.next());
		}
		return b;
	}

	/**
	 * Sorts this list according to the order induced by the specified
	 * Comparator.
	 * Notice that you may assume that the Comparator c has a compareTo method
	 * as follows:
	 * int compare(T o1, T o2). You invoke it as follows: c.compare(o1, o2).
	 * 
	 * @param c
	 *            - the Comparator used to compare list elements.
	 * @return a new instance of the tree using the ordering as specified by the
	 *         Comparator.
	 */
	public BinarySearchTree<T> sort(Comparator<? super T> c) {
		// TODO: implement this method
		BinarySearchTree<T> b = new BinarySearchTree<>();
		if (this.root == null)
			return b;
		b.root = new BinaryNode(this.root.element);
		Iterator<T> t = this.preOrderiterator();
		t.next(); // root
		while (t.hasNext()) {
			b.root._insert(t.next(), c);
		}
		return b;
	}

	public Iterator<T> iterator() {
		return new InOrderIterator();
	}

	public Iterator<T> preOrderiterator() {
		return new PreOrderIterator();
	}

	public ArrayList<T> toArrayList() {
		ArrayList<T> a = new ArrayList<T>();
		Iterator<T> i = iterator();
		while (i.hasNext())
			a.add(i.next());
		return a;
	}

	public String toString() {
		return toArrayList().toString();
	}

	public class BinaryNode {
		private T element;
		private BinaryNode leftChild;
		private BinaryNode rightChild;

		public BinaryNode(T element) {
			this.element = element;
			this.leftChild = null;
			this.rightChild = null;
		}

		public boolean _insert(T e, Comparator<? super T> c) {
			int v = c.compare(e, this.element);
			if (v == -1) {
				if (leftChild != null)
					return leftChild._insert(e, c);
				leftChild = new BinaryNode(e);
				return true;
			}
			if (v == 1) {
				if (rightChild != null)
					return rightChild._insert(e, c);
				rightChild = new BinaryNode(e);
				return true;
			}
			return false;
		}

		public int height() {
			int lHeight = -1;
			int rHeight = -1;
			if (leftChild != null)
				lHeight = leftChild.height();
			if (rightChild != null)
				rHeight = rightChild.height();
			return (lHeight > rHeight ? lHeight : rHeight) + 1;
		}

		public boolean insert(T element) {
			int v = element.compareTo(this.element);
			if (v == -1) {
				if (leftChild != null)
					return leftChild.insert(element);
				leftChild = new BinaryNode(element);
				size++;
				modCount++;
				return true;
			}
			if (v == 1) {
				if (rightChild != null)
					return rightChild.insert(element);
				rightChild = new BinaryNode(element);
				size++;
				modCount++;
				return true;
			}
			return false;
		}

		public BinaryNode remove(T element, MyBoolean b) {
			int v = element.compareTo(this.element);
			if (v == -1) {
				if (leftChild != null) {
					leftChild = leftChild.remove(element, b);
				} else {
					b.setFalse();
				}
				return this;
			}
			if (v == 1) {
				if (rightChild != null) {
					rightChild = rightChild.remove(element, b);
				} else {
					b.setFalse();
				}
				return this;
			}
			if (leftChild == null && rightChild == null) {
				size--;
				modCount++;
				return null;
			}
			if (leftChild == null) {
				size--;
				modCount++;
				return rightChild;
			}
			if (rightChild == null) {
				size--;
				modCount++;
				return leftChild;
			}
			this.element = leftChild.getLargest();
			leftChild = leftChild.remove(this.element, b);
			return this;
		}

		private T getLargest() {
			if (rightChild != null)
				return rightChild.getLargest();
			return element;
		}
	}

	private class InOrderIterator implements Iterator<T> {
		private ArrayList<T> a;
		private int position;
		private T lastOneReturned;

		public InOrderIterator() {
			a = new ArrayList<T>();
			position = 0;
			lastOneReturned = null;
			if (root != null)
				fillArrayList(root);
		}

		private void fillArrayList(BinaryNode n) {
			if (n.leftChild != null)
				fillArrayList(n.leftChild);
			a.add(n.element);
			if (n.rightChild != null)
				fillArrayList(n.rightChild);
		}

		public boolean hasNext() {
			return position < a.size();
		}

		public T next() {
			if (!hasNext())
				throw new NoSuchElementException("Go to Purdue!");
			lastOneReturned = a.get(position++);
			return lastOneReturned;
		}

		public void remove() {
			if (lastOneReturned == null)
				throw new IllegalStateException();
			BinarySearchTree.this.remove(lastOneReturned);
			lastOneReturned = null;
		}

	}

	private class PreOrderIterator implements Iterator<T> {
		private Stack<BinaryNode> nodes;

		public PreOrderIterator() {
			nodes = new Stack<BinaryNode>();
			if (root != null) {
				nodes.push(root);
			}
		}

		public boolean hasNext() {
			return !nodes.isEmpty();
		}

		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			BinaryNode temp = nodes.pop();
			if (temp.rightChild != null)
				nodes.push(temp.rightChild);
			if (temp.leftChild != null)
				nodes.push(temp.leftChild);
			return temp.element;
		}

		public void remove() {
			// TODO Auto-generated method stub
		}

	}

	private class MyBoolean {
		private boolean value = true;

		public boolean getValue() {
			return value;
		}

		public void setFalse() {
			value = false;
		}
	}

}
