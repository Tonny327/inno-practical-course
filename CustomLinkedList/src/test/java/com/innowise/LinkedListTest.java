package com.innowise;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class LinkedListTest {

    @Test
    void testAddFirst(){
        LinkedList<String> list = new LinkedList<>();
        list.addFirst("C");
        list.addFirst("B");
        list.addFirst("A");
        assertEquals("A", list.getFirst());
        assertEquals(3, list.size());
    }

    @Test
    void testAddLast(){
        LinkedList<String> list = new LinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        assertEquals("C", list.getLast());
        assertEquals(3, list.size());
    }

    @Test
    void testAdd(){
        LinkedList<String> list = new LinkedList<>();
        list.addLast("A");
        list.addLast("C");
        list.add(1, "B");
        assertEquals("B", list.get(1));
        assertEquals(3, list.size());
    }

    @Test
    void testIndexOf(){
        LinkedList<String> list = new LinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        assertEquals(1, list.indexOf("B"));
        assertEquals(-1, list.indexOf("D"));
    }

    @Test
    void testGet(){
        LinkedList<String> list = new LinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    void testGetFirst(){
        LinkedList<String> list = new LinkedList<>();
        list.addFirst("C");
        list.addFirst("B");
        list.addFirst("A");
        assertEquals("A", list.getFirst());
        assertThrows(IllegalStateException.class, () -> new LinkedList<>().getFirst());
    }

    @Test
    void testGetLast(){
        LinkedList<String> list = new LinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        assertEquals("C", list.getLast());
        assertThrows(IllegalStateException.class, () -> new LinkedList<>().getLast());
    }

    @Test
    void testRemoveByValue(){
        LinkedList<String> list = new LinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        list.removeByValue("B");
        assertEquals(-1, list.indexOf("B"));
        assertEquals(2, list.size());
    }

    @Test
    void testRemove(){
        LinkedList<String> list = new LinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        String removed = list.remove(1);
        assertEquals("B", removed);
        assertEquals("C", list.get(1));
        assertEquals(2, list.size());
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(5));
    }

    @Test
    void testRemoveFirst(){
        LinkedList<String> list = new LinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        String removed = list.removeFirst();
        assertEquals("A", removed);
        assertEquals("B", list.getFirst());
        assertThrows(IllegalStateException.class, () -> new LinkedList<>().removeFirst());
    }

    @Test
    void testRemoveLast(){
        LinkedList<String> list = new LinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        String removed = list.removeLast();
        assertEquals("C", removed);
        assertEquals("B", list.getLast());
        assertThrows(IllegalStateException.class, () -> new LinkedList<>().removeLast());
    }

    @Test
    void testSize(){
        LinkedList<Integer> list = new LinkedList<>();
        assertEquals(0, list.size());
        list.addFirst(1);
        list.addLast(2);
        list.add(1,3);
        assertEquals(3, list.size());
        list.removeByValue(3);
        assertEquals(2, list.size());
    }

}
