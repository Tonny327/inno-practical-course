package com.innowise;

import java.util.Objects;

public class LinkedList<T> {
    private Node<T> head;

    public void addFirst(T t){
        Node<T> firstNode = new Node<>(t);
        firstNode.next = head;
        head = firstNode;
    }

    public  void addLast(T t){
        if(head == null){
            head = new Node<>(t);
            return;
        }

        Node<T> currentNode = head;
        while(currentNode.next != null){
            currentNode = currentNode.next;
        }
        currentNode.next = new Node<>(t);
    }

    public void add(int index, T value){
        if (index < 0 || index > size()) throw new IndexOutOfBoundsException();
        if (index == 0) {
            addFirst(value);
            return;
        }

        Node<T> currentNode = head;
        for (int i = 0; i < index -1; i++) {
            currentNode = currentNode.next;
        }

        Node<T> newNode = new Node<>(value);
        newNode.next = currentNode.next;
        currentNode.next = newNode;
    }

    public int indexOf(T t){
        if (head == null) {
            return -1;
        }

        if (head.value == t){
            return 0;
        }

        Node<T> currentNode = head;
        int result = 0;

        while (currentNode.next != null){
            ++result;
            if(currentNode.next.value == t){
                return result;
            }
            currentNode = currentNode.next;
        }
        return -1;
    }

    public T get(int index) {
        if (index < 0) throw new IndexOutOfBoundsException();
        Node<T> currentNode = head;
        int i = 0;
        while (currentNode != null) {
            if (i == index) return currentNode.value;
            currentNode = currentNode.next;
            i++;
        }
        throw new IndexOutOfBoundsException();
    }

    public T getFirst(){
        if (head == null)throw new IllegalStateException("List is empty");
        return head.value;
    }

    public T getLast() {
        if (head == null) throw new IllegalStateException("List is empty");
        Node<T> currentNode = head;
        while (currentNode.next != null){
            currentNode = currentNode.next;
        }
        return currentNode.value;
    }

    public void removeByValue(T t) {
        if (head == null){
            return;
        }

        if(head.value ==t){
            head = head.next;
            return;
        }

        Node<T> currentNode = head;
        while (currentNode.next != null) {
            if (currentNode.next.value == t){
                currentNode.next = currentNode.next.next;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    public T remove(int index){
        if (index < 0 || index >= size()) throw new IndexOutOfBoundsException();
        if (index == 0) return removeFirst();

        Node<T> currentNode = head;
        for (int i = 0; i < index - 1; i++) {
            currentNode = currentNode.next;
        }

        T value = currentNode.next.value;
        currentNode.next = currentNode.next.next;
        return value;
    }

    public T removeFirst() {
        if (head == null) throw new IllegalStateException("List is empty");
        T value = head.value;
        head = head.next;
        return value;
    }

    public T removeLast() {
        if (head == null) throw new IllegalStateException("List is empty");
        if (head.next == null) {
            T value = head.value;
            head = null;
            return value;
        }

        Node<T> currentNode = head;
        while (currentNode.next.next != null){
            currentNode = currentNode.next;
        }
        T value = currentNode.next.value;
        currentNode.next = null;
        return value;
    }

    public int size() {
        int count = 0;
        Node<T> currentNode = head;
        while (currentNode != null) {
            count++;
            currentNode = currentNode.next;
        }
        return count;
    }

    @Override
    public String toString() {
        return "LinkedList{" +
                "head=" + head +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedList<?> that = (LinkedList<?>) o;
        return Objects.equals(head, that.head);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(head);
    }

    private static class Node<T>{
        private final T value;
        private Node<T> next;

        public Node(T value){
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", next=" + next +
                    '}';
        }
    }
}
