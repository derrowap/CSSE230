import java.util.ArrayList;
import java.util.Iterator;

/**
 * A Priority Queue data structure.
 * 
 * @author derrowap
 * 
 * @param <E>
 *            - the type of elements stored
 */
public class PriorityQueue<E extends Comparable<? super E>> extends
		ArrayList<E> {
	public PriorityQueue() {
		super();
	}

	/**
	 * Inserts the specified element into this priority queue.
	 * 
	 * @param e
	 *            - the element to add
	 * 
	 * @return true if inserted successfully
	 * 
	 * @throws NullPointerException
	 *             - if the specified element is null
	 */
	public boolean add(E e) {
		if (e == null)
			throw new NullPointerException();
		super.add(e);
		this.addBalance(super.size() - 1);
		return true;
	}

	/**
	 * Removes all of the elements from this priority queue. The queue will be
	 * empty after this call returns.
	 */
	public void clear() {
		super.clear();
	}

	/**
	 * Returns true if this queue contains the specified element.
	 * 
	 * @param o
	 *            - object to be checked for containment in this queue
	 * 
	 * @return true if this queue contains the specified element
	 */
	public boolean contains(Object o) {
		return super.contains(o);
	}

	/**
	 * Returns an iterator over the elements in this queue.
	 * 
	 * @return an iterator over the elements in this queue
	 */
	public Iterator<E> iterator() {
		return super.iterator();
	}

	/**
	 * Inserts the specified element into this priority queue.
	 * 
	 * @param e
	 *            - the element to add
	 * 
	 * @return true if specified element was added
	 * 
	 * @throws NullPointerException
	 *             - if the specified element is null
	 */
	public boolean offer(E e) {
		return this.add(e);
	}

	/**
	 * Retrieves, but does not remove, the head of this queue, or returns null
	 * if this queue is empty.
	 * 
	 * @return the head of this queue, or null if this queue is empty
	 */
	public E peek() {
		if (this.isEmpty())
			return null;
		return this.get(0);
	}

	/**
	 * Retrieves and removes the head of this queue, or returns null if this
	 * queue is empty.
	 * 
	 * @return the head of this queue, or null if this queue is empty
	 */
	public E poll() {
		if (super.isEmpty())
			return null;
		E e = this.peek();
		this.remove(e);
		return e;
	}

	/**
	 * Removes a single instance of the specified element from this queue, if it
	 * is present.
	 * 
	 * @param o
	 *            - element to be removed from this queue, if present
	 * 
	 * @return true if this queue changed as a result of the call
	 */
	public boolean remove(Object o) {
		int e = super.indexOf(o);
		if (e == -1)
			return false;
		int lastIndex = super.size() - 1;
		if (e != lastIndex) {
			super.set(e, super.get(lastIndex));
			super.remove(lastIndex);
			this.removeBalance(e);
		} else
			super.remove(e);
		return true;
	}

	/**
	 * Returns the number of elements in this priority queue.
	 * 
	 * @return the number of elements in this priority queue
	 */
	public int size() {
		return super.size();
	}

	/**
	 * Returns an array containing all of the elements in this queue.
	 * 
	 * @return an array containing all of the elements in this queue
	 */
	public Object[] toArray() {
		return super.toArray();
	}

	/**
	 * Returns an array containing all of the elements in this queue.
	 * 
	 * @param a
	 *            - the array into which the elements of the queue are to be
	 *            stored, if it is big enough; otherwise, a new array of the
	 *            same runtime type is allocated for this purpose
	 * 
	 * @return an array containing all of the elements in this queue
	 */
	public <T> T[] toArray(T[] a) {
		return super.toArray(a);
	}

	/**
	 * If the element at the specified index is smaller than the parent of that
	 * element, they swap places. Recursively is called all the way up the tree
	 * until it no longer needs to swap.
	 * 
	 * @param index
	 *            - index of element to check for balancing
	 */
	private void addBalance(int index) {
		E end = super.get(index);
		E swap = super.get((index - 1) / 2);
		if (swap.compareTo(end) > 0) { // Swap elements
			super.set((index - 1) / 2, end);
			super.set(index, swap);
			this.addBalance((index - 1) / 2);
		} // Done
	}

	/**
	 * Recursively iterates down the tree, checking if the element at the
	 * specified index is larger than one of its children, if so swaps places
	 * with the smallest child.
	 * 
	 * @param index
	 *            - index of element to check for balancing
	 */
	private void removeBalance(int index) {
		int smallest = this.smallestChild(index);
		if (smallest == -1)
			return;
		E parent = super.get(index);
		E child = super.get(smallest);
		if (child.compareTo(parent) < 0) { // Swap elements
			super.set(index, child);
			super.set(smallest, parent);
			this.removeBalance(smallest);
		} // Done
	}

	/**
	 * Returns the smallest child's index of the element specified by the given
	 * index. Returns -1 if there are no children to the parent element.
	 * 
	 * @param index
	 *            - the parent element to compare the smallest children with
	 * 
	 * @return index of the smallest child
	 */
	private int smallestChild(int index) {
		E leftChild = (index * 2 + 1) < super.size() ? super.get(index * 2 + 1)
				: null;
		E rightChild = (index * 2 + 2) < super.size() ? super
				.get(index * 2 + 2) : null;
		if (rightChild == null) {
			if (leftChild != null)
				return index * 2 + 1; // leftChild is smallest
			return -1; // both children are null
		}
		if (leftChild == null)
			return index * 2 + 2; // rightChild is smallest
		if (leftChild.compareTo(rightChild) <= 0)
			return index * 2 + 1; // leftChild is smallest
		return index * 2 + 2; // rightChild is smallest
	}

}
