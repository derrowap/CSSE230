import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.PriorityQueue;

import org.junit.AfterClass;
import org.junit.Test;

public class Testing {
		
	private static int points = 0;
	
	@Test
	public void testToPriorityQueue(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		PriorityQueue<Integer> h = b.toPriorityQueue();
		assertTrue(h.isEmpty());
		b.insert(10);
		h = b.toPriorityQueue();
		assertEquals(1, h.size());
		assertTrue(h.contains(10));
		for (int i = 11; i < 16; i++) b.insert(i);
		for (int i = 9; i > 5; i--) b.insert(i);
		h = b.toPriorityQueue();
		assertEquals(10, h.size());
		points += 10;
	}
	
	
	@Test
	public void testReverse(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		b.reverse();
		b.insert(5);
		b.reverse();
		Iterator<Integer> i = b.iterator();
		assertTrue(i.hasNext());
		assertEquals(new Integer(5), i.next());	
		b.insert(6);
		b.reverse();
		i = b.iterator();
		assertTrue(i.hasNext());
		assertEquals(new Integer(6), i.next());	
		assertTrue(i.hasNext());
		assertEquals(new Integer(5), i.next());	
		b = new BinarySearchTree<Integer>();
		b.insert(5);
		b.insert(6);
		b.insert(3);
		b.reverse();
		i = b.iterator();
		assertTrue(i.hasNext());
		assertEquals(new Integer(6), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(5), i.next());	
		assertTrue(i.hasNext());
		assertEquals(new Integer(3), i.next());	
		
		b = new BinarySearchTree<Integer>();
		b.insert(5);
		b.insert(7);
		b.insert(6);
		b.insert(3);
		b.insert(4);
		b.insert(2);
		b.reverse();
		i = b.iterator();
		assertTrue(i.hasNext());
		assertEquals(new Integer(7), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(6), i.next());	
		assertTrue(i.hasNext());
		assertEquals(new Integer(5), i.next());	
		assertTrue(i.hasNext());
		assertEquals(new Integer(4), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(3), i.next());	
		assertTrue(i.hasNext());
		assertEquals(new Integer(2), i.next());	
		b.reverse();
		i = b.iterator();
		assertTrue(i.hasNext());
		assertEquals(new Integer(2), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(3), i.next());	
		assertTrue(i.hasNext());
		assertEquals(new Integer(4), i.next());	
		assertTrue(i.hasNext());
		assertEquals(new Integer(5), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(6), i.next());	
		assertTrue(i.hasNext());
		assertEquals(new Integer(7), i.next());	

		points += 15;
	}
	
	@Test
	public void testformsAV(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		assertTrue(b.formsAV());
		b.insert(10);
		assertTrue(b.formsAV());
		b.insert(15);
		assertFalse(b.formsAV());
		b.insert(20);
		assertFalse(b.formsAV());	
		b.insert(7);
		assertFalse(b.formsAV());
		b.insert(2);
		assertTrue(b.formsAV());
		b.insert(3);
		assertFalse(b.formsAV());
		b.insert(18);
		assertFalse(b.formsAV());
	
		b = new BinarySearchTree<Integer>();
		b.insert(50);
		for (int i = 51; i < 100; i++) b.insert(i);
		for (int i = 49; i > 0; i--) b.insert(i);
		assertTrue(b.formsAV());
		
		points += 30;
	}
	

	
	@AfterClass
	public static void testNothing(){
		System.out.println(points);
	}
	
	
}

