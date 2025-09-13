package com.innowise.linkedlist;

import java.util.Objects;

/**
 * A custom implementation of a doubly linked list. Supports insertion, deletion, search, and
 * indexed access. This list maintains references to both head and tail nodes, allowing efficient
 * operations at both ends.
 *
 * @param <T> the type of elements stored in the list
 */

public class LinkedList<T> {

  private Node<T> head;
  private Node<T> tail;
  private int size = 0;

  /**
   * Inserts the specified value at the beginning of the list.
   *
   * @param value the value to insert
   */
  public void addFirst(T value) {
    Node<T> newNode = new Node<>(value);
    newNode.next = head;
    if (head != null) {
      head.prev = newNode;
    } else {
      tail = newNode;
    }
    head = newNode;
    size++;
  }

  /**
   * Inserts the specified value at the end of the list.
   *
   * @param value the value to insert
   */
  public void addLast(T value) {
    Node<T> newNode = new Node<>(value);
    if (tail == null) {
      head = tail = newNode;
    } else {
      tail.next = newNode;
      newNode.prev = tail;
      tail = newNode;
    }
    size++;
  }

  /**
   * Inserts the specified value at the given index.
   *
   * @param index the position to insert at
   * @param value the value to insert
   * @throws IndexOutOfBoundsException if index is out of range
   */
  public void add(int index, T value) {
    if (index < 0 || index > size) {
      throw new IndexOutOfBoundsException();
    }
    if (index == 0) {
      addFirst(value);
      return;
    }
    if (index == size) {
      addLast(value);
      return;
    }

    Node<T> nodeAfter;
    if (index < size / 2) {
      nodeAfter = head;
      for (int i = 0; i < index; i++) {
        nodeAfter = nodeAfter.next;
      }
    } else {
      nodeAfter = tail;
      for (int i = size - 1; i > index; i--) {
        nodeAfter = nodeAfter.prev;
      }
    }

    Node<T> newNode = new Node<>(value);
    Node<T> nodeBefore = nodeAfter.prev;

    newNode.prev = nodeBefore;
    newNode.next = nodeAfter;

    nodeBefore.next = newNode;
    nodeAfter.prev = newNode;
    size++;
  }

  /**
   * Returns the value at the specified index.
   *
   * @param index the position to retrieve
   * @return the value at the given index
   * @throws IndexOutOfBoundsException if index is out of range
   */
  public T get(int index) {
    if (index < 0 || index >= size()) {
      throw new IndexOutOfBoundsException();
    }
    Node<T> currentNode;
    if (index < size / 2) {
      currentNode = head;
      for (int i = 0; i < index; i++) {
        currentNode = currentNode.next;
      }
    } else {
      currentNode = tail;
      for (int i = size - 1; i > index; i--) {
        currentNode = currentNode.prev;
      }
    }
    return currentNode.value;
  }

  /**
   * Returns the first element in the list.
   *
   * @return the first value
   * @throws IllegalStateException if the list is empty
   */
  public T getFirst() {
    if (head == null) {
      throw new IllegalStateException("List is empty");
    }
    return head.value;
  }

  /**
   * Returns the last element in the list.
   *
   * @return the last value
   * @throws IllegalStateException if the list is empty
   */
  public T getLast() {
    if (tail == null) {
      throw new IllegalStateException("List is empty");
    }
    return tail.value;
  }

  /**
   * Removes the element at the specified index.
   *
   * @param index the position to remove
   * @return the removed value
   * @throws IndexOutOfBoundsException if index is out of range
   */
  public T remove(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException();
    }
    if (index == 0) {
      return removeFirst();
    }
    if (index == size - 1) {
      return removeLast();
    }

    Node<T> currentNode;
    if (index < size / 2) {
      currentNode = head;
      for (int i = 0; i < index; i++) {
        currentNode = currentNode.next;
      }
    } else {
      currentNode = tail;
      for (int i = size - 1; i > index; i--) {
        currentNode = currentNode.prev;
      }
    }

    Node<T> previousNode = currentNode.prev;
    Node<T> nextNode = currentNode.next;

    previousNode.next = nextNode;
    nextNode.prev = previousNode;

    currentNode.next = null;
    currentNode.prev = null;
    size--;

    return currentNode.value;
  }

  /**
   * Removes and returns the first element in the list.
   *
   * @return the removed value
   * @throws IllegalStateException if the list is empty
   */
  public T removeFirst() {
    if (head == null) {
      throw new IllegalStateException("List is empty");
    }
    T value = head.value;
    Node<T> nodeToRemove = head;
    head = head.next;
    if (head != null) {
      head.prev = null;
    } else {
      tail = null;
    }

    nodeToRemove.next = null;
    nodeToRemove.prev = null;
    size--;
    return value;
  }

  /**
   * Removes and returns the last element in the list.
   *
   * @return the removed value
   * @throws IllegalStateException if the list is empty
   */
  public T removeLast() {
    if (tail == null) {
      throw new IllegalStateException("List is empty");
    }
    T value = tail.value;
    Node<T> nodeToRemove = tail;
    tail = tail.prev;
    if (tail != null) {
      tail.next = null;
    } else {
      head = null;
    }

    nodeToRemove.next = null;
    nodeToRemove.prev = null;
    size--;
    return value;
  }

  /**
   * Returns the number of elements in the list.
   *
   * @return the size of the list
   */
  public int size() {
    return size;
  }

  /**
   * Compares this list to another for equality based on element values.
   *
   * @param o the object to compare with
   * @return true if both lists contain the same elements in order
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LinkedList<?> that)) {
      return false;
    }

    Node<T> nodeA = this.head;
    Node<?> nodeB = that.head;

    while (nodeA != null && nodeB != null) {
      if (!Objects.equals(nodeA.value, nodeB.value)) {
        return false;
      }
      nodeA = nodeA.next;
      nodeB = nodeB.next;
    }

    return nodeA == null && nodeB == null;
  }

  /**
   * Returns a hash code based on the list's contents.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    int result = 1;
    Node<T> currentNode = head;
    while (currentNode != null) {
      result = 31 * result + Objects.hashCode(currentNode.value);
      currentNode = currentNode.next;
    }
    return result;
  }

  /**
   * Returns a string representation of the list.
   *
   * @return a readable format of the list contents
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("LinkedList{");
    Node<T> currentNode = head;
    while (currentNode != null) {
      builder.append(currentNode.value);
      if (currentNode.next != null) {
        builder.append(" <-> ");
      }
      currentNode = currentNode.next;
    }
    builder.append("}");
    return builder.toString();
  }

  /**
   * Internal node class used to store list elements and links.
   *
   * @param <T> the type of value stored
   */
  private static class Node<T> {

    /**
     * The value stored in this node.
     */
    private final T value;
    /**
     * Reference to the next node.
     */
    private Node<T> next;
    /**
     * Reference to the previous node.
     */
    private Node<T> prev;

    public Node(T value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

  }
}
