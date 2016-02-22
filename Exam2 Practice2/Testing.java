import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.Test;

public class Testing {
		
	private static int points = 0;
	
	@Test
	public void testRange(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		assertEquals(new Integer(-1), b.range());
		points += 1;
		b.insert(3);
		assertEquals(new Integer(0), b.range());
		points += 1;
		b.insert(2);
		b.insert(5);
		b.insert(8);
		b.insert(7);
		b.insert(6);
		assertEquals(new Integer(6), b.range());
		points += 9;
		b.insert(-5);
		assertEquals(new Integer(13), b.range());
		points += 2;
		b = new BinarySearchTree<Integer>();
		b.insert(-5);
		b.insert(-2);
		assertEquals(new Integer(3), b.range());
		points += 2;
	}
	
	@Test
	public void testkthLargest(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		try {
			b.kthElement(1);
			fail("Did not throw IllegalArgumentException");
		} catch (Exception e){
			if (!(e instanceof IllegalArgumentException)) {
				fail("Did not throw IllegalArgumentException");				
			}
		}
		points += 1;
		b.insert(3);
		assertEquals(new Integer(3), b.kthElement(1));
		points += 2;
		b.insert(2);
		b.insert(5);
		b.insert(1);
		b.insert(4);
		b.insert(6);
		b.insert(7);
		for (int i = 1; i < 8; i++){
			assertEquals(new Integer(i), b.kthElement(i));
		}
		points += 15;
		try {
			b.kthElement(0);
			fail("Did not throw IllegalArgumentException");
		} catch (Exception e){
			if (!(e instanceof IllegalArgumentException)) {
				fail("Did not throw IllegalArgumentException");				
			}
		}
		try {
			b.kthElement(-3);
			fail("Did not throw IllegalArgumentException");
		} catch (Exception e){
			if (!(e instanceof IllegalArgumentException)) {
				fail("Did not throw IllegalArgumentException");				
			}
		}
		points += 1;
		try {
			b.kthElement(8);
			fail("Did not throw IllegalArgumentException");
		} catch (Exception e){
			if (!(e instanceof IllegalArgumentException)) {
				fail("Did not throw IllegalArgumentException");				
			}
		}
		try {
			b.kthElement(10);
			fail("Did not throw IllegalArgumentException");
		} catch (Exception e){
			if (!(e instanceof IllegalArgumentException)) {
				fail("Did not throw IllegalArgumentException");				
			}
		}
		points += 1;
	}
	
	@Test
	public void testMakeComplete(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		b.makeComplete();
		Iterator<Integer> i = b.iterator();
		assertFalse(i.hasNext());
		points += 2;
		
		b = new BinarySearchTree<Integer>();
		b.insert(5);
		b.makeComplete();
		i = b.iterator();
		assertTrue(i.hasNext());
		assertEquals(new Integer(5), i.next());
		assertFalse(i.hasNext());
		points += 3;
		
		b = new BinarySearchTree<Integer>();
		b.insert(5);
		b.insert(10);
		b.makeComplete();
		i = b.iterator();
		assertTrue(i.hasNext());
		assertEquals(new Integer(5), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(5), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(10), i.next());
		assertFalse(i.hasNext());
		points += 3;
		
		b = new BinarySearchTree<Integer>();
		b.insert(10);
		b.insert(5);
		b.insert(15);
		b.makeComplete();
		i = b.iterator();
		assertTrue(i.hasNext());
		assertEquals(new Integer(5), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(10), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(15), i.next());
		assertFalse(i.hasNext());
		points += 4;
		
		b = new BinarySearchTree<Integer>();
		b.insert(10);
		b.insert(5);
		b.insert(2);
		b.insert(15);
		b.makeComplete();
		i = b.iterator();
		assertTrue(i.hasNext());
		assertEquals(new Integer(2), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(5), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(5), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(10), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(15), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(15), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(15), i.next());
		assertFalse(i.hasNext());
		points += 4;
		
		b = new BinarySearchTree<Integer>();
		b.insert(10);
		b.insert(5);
		b.insert(2);
		b.insert(15);
		b.insert(17);
		b.makeComplete();
		i = b.iterator();
		assertTrue(i.hasNext());
		assertEquals(new Integer(2), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(5), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(5), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(10), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(15), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(15), i.next());
		assertTrue(i.hasNext());
		assertEquals(new Integer(17), i.next());
		assertFalse(i.hasNext());
		points += 4;

		b = new BinarySearchTree<Integer>();
		b.insert(10);
		b.insert(15);
		b.insert(20);
		b.insert(25);
		b.insert(30);
		b.makeComplete();
		i = b.iterator();
		for (int k = 0; k < 16; k++) {
			assertTrue(i.hasNext());
			assertEquals(new Integer(10), i.next());	
		}
		for (int k = 0; k < 8; k++) {
			assertTrue(i.hasNext());
			assertEquals(new Integer(15), i.next());	
		}
		for (int k = 0; k < 4; k++) {
			assertTrue(i.hasNext());
			assertEquals(new Integer(20), i.next());	
		}
		assertTrue(i.hasNext());
		assertEquals(new Integer(25), i.next());	
		assertTrue(i.hasNext());
		assertEquals(new Integer(25), i.next());	
		assertTrue(i.hasNext());
		assertEquals(new Integer(30), i.next());	
		assertFalse(i.hasNext());
		points += 4;
	}
	
	@AfterClass
	public static void testNothing(){
		System.out.println(points);
	}
}

