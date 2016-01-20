import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.AfterClass;
import org.junit.Test;


public class Testing {
	
	private static int points = 0;

	
	@Test
	public void testContains(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		assertFalse(b.contains(3));
		points += 2;
		b.insert(3);
		assertTrue(b.contains(3));
		points += 3;
		assertFalse(b.contains(2));
		points += 2;
		assertFalse(b.contains(4));
		points += 1;
		for (int i = 1; i < 100; i++) b.insert(i);
		for (int i = 1; i < 100; i++) assertTrue(b.contains(i));
		points += 5;
		assertFalse(b.contains(100));
		points += 2;
	}
	
	@Test
	public void testContainsPerformance(){
		// This test code should run in less than a second. You will 
		// get the 5 points if your code satisfies this constraint.
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		int size = 1046576; 
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
		assertEquals(1046575, b.size());
		for (int i = 1; i < size; i++) assertTrue(b.contains(i));
		points += 5;
	}
	
	@Test
	public void testRemoveAll(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		// No rotations at all
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
		// Testing functionality
		assertEquals(7, b.size());
		Collection<Integer> c = new ArrayList<Integer>();
		c.add(1);
		assertTrue(b.removeAll(c));
		points += 1;
		assertFalse(b.removeAll(c));
		points += 1;
		c = new ArrayList<Integer>();
		c.add(7);
		c.add(3);
		c.add(5);
		assertTrue(b.removeAll(c));
		points += 3;
		assertEquals(3, b.size());
		points += 4;
		c = new ArrayList<Integer>();
		c.add(2);
		c.add(1);
		c.add(4);
		assertTrue(b.removeAll(c));	
		points += 3;
		assertEquals(1, b.size());	
		points += 3;
	}
	
	@Test
	public void testRemoveAllPerformance(){	
		// This test code should run in less than a second. You will 
		// get the 5 points if your code satisfies this constraint.
		ArrayList<Integer> c = new ArrayList<Integer>();
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		int size = 1046576; 
		int v = size / 2;
		int temp;
		while (v > 0) {
			temp = v;
			while (temp < size){
				b.insert(temp);
				c.add(temp);
				temp += v;
				}
			v = v / 2;
		}
		assertEquals(1046575, b.size());
		b.removeAll(c);
		assertEquals(0, b.size());
		points += 5;
	}
	
	@Test
	public void testRetainAll(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		// No rotations at all
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
		// Testing functionality
		assertEquals(7, b.size());
		Collection<Integer> c = new ArrayList<Integer>();
		c.add(1);
		assertTrue(b.retainAll(c));
		points += 2;
		assertEquals(1, b.size());
		points += 1;
		assertFalse(b.retainAll(c));
		points += 2;
		assertEquals(1, b.size());
		points += 1;
		b = new BinarySearchTree<Integer>();
		// No rotations at all
		size = 8; 
		v = size / 2;
		while (v > 0) {
			temp = v;
			while (temp < size){
				b.insert(temp);
				temp += v;
				}
			v = v / 2;
		}
		c = new ArrayList<Integer>();
		c.add(7);
		c.add(3);
		c.add(5);
		assertTrue(b.retainAll(c));
		points += 1;
		assertEquals(3, b.size());
		points += 4;
		c = new ArrayList<Integer>();
		c.add(5);
		c.add(2);
		c.add(3);
		assertTrue(b.retainAll(c));	
		points += 1;
		assertEquals(2, b.size());	
		points += 3;
	}
	
	@Test
	public void testRetainAllPerformance(){
		// This test code should run in less than three seconds. You will 
		// get the 5 points if your code satisfies this constraint.
		ArrayList<Integer> c = new ArrayList<Integer>();
		ArrayList<Integer> d = new ArrayList<Integer>();
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		int size = 1046576; 
		int v = size / 2;
		int temp;
		while (v > 0) {
			temp = v;
			while (temp < size){
				b.insert(temp);
				d.add(temp);
				temp += v;
				}
			v = v / 2;
		}
		assertEquals(1046575, b.size());
		b.retainAll(c);
		assertEquals(0, b.size());

		
		c = new ArrayList<Integer>();
		d = new ArrayList<Integer>();
		b = new BinarySearchTree<Integer>();
		size = 16384; 
		v = size / 2;
		while (v > 0) {
			temp = v;
			while (temp < size){
				b.insert(temp);
				d.add(temp);
				temp += v;
				}
			v = v / 2;
		}
		assertEquals(16383, b.size());
		b.retainAll(d);
		assertEquals(16383, b.size());
		points += 5;
	}

	@AfterClass
	public static void testNothing(){
		System.out.println("Points: " + points);
	}

}