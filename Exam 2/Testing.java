import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.AfterClass;
import org.junit.Test;


public class Testing {
	
	private static int points = 0;
	
	@Test
	public void testFloor(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		assertEquals(null, b.floor(new Integer(42)));
		points += 2;
		b.insert(48);
		assertEquals(null, b.floor(new Integer(42)));
		points += 2;
		b.insert(22);
		assertEquals(new Integer(22), b.floor(new Integer(42)));
		points += 4;
		b = new BinarySearchTree<Integer>();
		b.insert(22);
		assertEquals(new Integer(22), b.floor(new Integer(22)));
		points += 3;
		b.insert(42);
		assertEquals(new Integer(22), b.floor(new Integer(22)));
		points += 3;
		b = new BinarySearchTree<Integer>();
		int size = 64; 
		int v = size / 2;
		int temp;
		while (v > 0) {
			temp = v;
			while (temp < size){
				b.insert(temp);
				temp += v;
				}
			v = v / 2;
		}
		assertEquals(new Integer(22), b.floor(new Integer(22)));
		points += 2;
		b = new BinarySearchTree<Integer>();
		for (int i = 1; i < 64; i++) b.insert(i);
		assertEquals(new Integer(22), b.floor(new Integer(22)));
		points += 2;
		b = new BinarySearchTree<Integer>();
		for (int i = 63; i >= 1; i--) b.insert(i);
		assertEquals(new Integer(22), b.floor(new Integer(22)));	
		points += 2;
	}
	
	
	@Test
	public void testClone(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		assertEquals("[]", b.toString());
		points += 1;
		int nums = 16; 
		for (int i = 0; i < nums; i++) {
		    b.insert(i);
		}
		BinarySearchTree<Integer> copy = b.clone();
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]", b.toString());
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]", copy.toString());
		points += 2;

		b.remove(8);
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15]", b.toString());
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]", copy.toString());
		points += 2;
		
		copy = b.clone();
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15]", copy.toString());
		points += 2;
		
		b = new BinarySearchTree<Integer>();
		for (int i = 0; i < nums; i++) {
		    b.insert(i);
		}
		copy.remove(8);
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]", b.toString());	
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15]", copy.toString());
		points += 2;
	
		b = new BinarySearchTree<Integer>();
		int size = 8; 
		int v = size / 2;
		int temp;
		while (v > 0) {
			temp = v;
			while (temp < size){
				b.insert(temp);
				temp += v;
				}
			v = v / 2;
		}
		copy = b.clone();
		Iterator<Integer> iter = b.preOrderiterator();
		LinkedList<Integer> vals = new LinkedList<Integer>();
		vals.add(4);vals.add(2);vals.add(1);vals.add(3);
		vals.add(6);vals.add(5);vals.add(7);
		for (Integer val : vals){
			assertTrue(iter.hasNext());
			assertEquals(val, iter.next());
		}
		assertFalse(iter.hasNext());
		assertEquals(2, copy.height());
		points += 11;
		
	}
	
	@Test
	public void testSort(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		for (int i = 1; i < 8; i++) {
		    b.insert(i);
		}
		BinarySearchTree<Integer> reverseSorted = b.sort(new Comparator<Integer>(){
			public int compare(Integer arg0, Integer arg1) {
				return arg1.compareTo(arg0);
			}});
		assertEquals("[1, 2, 3, 4, 5, 6, 7]", b.toString());
		assertEquals("[7, 6, 5, 4, 3, 2, 1]", reverseSorted.toString());
		points += 18;
		b = new BinarySearchTree<Integer>();
		for (int i = 1; i < 8; i++) {
		    b.insert(i);
		}
		BinarySearchTree<Integer> whoooaaw = b.sort(new Comparator<Integer>(){
			public int compare(Integer arg0, Integer arg1) {
				return 0;
			}});
		assertEquals("[1]", whoooaaw.toString());
		points += 2;
	}

	@AfterClass
	public static void testNothing(){
		System.out.println("Points: " + points);
	}

}