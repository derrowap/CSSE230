import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import org.junit.AfterClass;
import org.junit.Test;

public class Testing {
		
	private static int points = 0;
	
	@Test
	public void testEvenTree(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		assertTrue(b.evenTree());
		b.insert(3);
		assertFalse(b.evenTree());
		
		b = new BinarySearchTree<Integer>();
		b.insert(10);
		assertTrue(b.evenTree());
		
		b.insert(20);
		b.insert(13);
		assertTrue(b.evenTree());

		b.insert(6);
		assertTrue(b.evenTree());
		b.insert(7);
		assertTrue(b.evenTree());
		b.insert(41);
		assertFalse(b.evenTree());	
		b.insert(42);
		assertFalse(b.evenTree());	
		points += 26;
	}
	
	
	@Test
	public void testLopSided(){
		BinarySearchTree<Integer> b = new BinarySearchTree<Integer>();
		assertEquals(0, b.lopSided());
		b.insert(10);
		assertEquals(0, b.lopSided());
		b.insert(20);
		assertEquals(1, b.lopSided());
		b.insert(5);
		assertEquals(0, b.lopSided());		
		b.insert(30);
		b.insert(25);
		b.insert(22);
		b.insert(21);
		assertEquals(4, b.lopSided());
		b.insert(7);
		b.insert(9);
		assertEquals(4, b.lopSided());
		b.insert(3);
		b.insert(15);
		b.insert(35);
		assertEquals(3, b.lopSided());
		points += 26;
	}

	@AfterClass
	public static void testNothing(){
		System.out.println(points);
	}
}

