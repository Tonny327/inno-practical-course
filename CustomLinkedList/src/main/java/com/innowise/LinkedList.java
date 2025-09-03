package com.innowise;

import java.util.Objects;

public class LinkedList<T> {
    private Node<T> head;
    private Node<T> tail;

    public void addFirst(T value){
        Node<T> newNode = new Node<>(value);
        newNode.next = head;
        if(head != null){
            head.prev = newNode;
        } else{
            tail = newNode;
        }
        head = newNode;
    }

    public  void addLast(T value){
        Node<T> newNode = new Node<>(value);
        if (tail == null){
            head = tail = newNode;
            return;
        }
        tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
    }

    public void add(int index, T value){
        if (index < 0 || index > size()) throw new IndexOutOfBoundsException();
        if (index == 0) {
            addFirst(value);
            return;
        }
        if (index == size()){
            addLast(value);
            return;
        }

        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }

        Node<T> newNode = new Node<>(value);
        Node<T> previousNode = currentNode.prev;

        newNode.prev = previousNode;
        newNode.next = currentNode;

        previousNode.next = newNode;
        currentNode.prev = newNode;
    }

    public int indexOf(T value){
        Node<T> currentNode = head;
        int index = 0;
        while(currentNode != null){
            if (Objects.equals(currentNode.value, value)){
                return index;
            }
            currentNode = currentNode.next;
            index++;
        }
        return -1;
    }

    public T get(int index) {
        if (index < 0 || index >= size()) throw new IndexOutOfBoundsException();
        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }
        return currentNode.value;
    }

    public T getFirst(){
        if (head == null)throw new IllegalStateException("List is empty");
        return head.value;
    }

    public T getLast() {
        if (tail == null) throw new IllegalStateException("List is empty");
        return tail.value;
    }

    public void removeByValue(T value) {
        Node<T> currentNode = head;
        while (currentNode != null){
            if (Objects.equals(currentNode.value, value)){
                Node<T> previousNode = currentNode.prev;
                Node<T> nextNode = currentNode.next;
                if (previousNode != null){
                    previousNode.next = nextNode;
                }else{
                    head = nextNode;
                }
                if (nextNode != null){
                    nextNode.prev = previousNode;
                }else{
                    tail = previousNode;
                }
                return;
            }
            currentNode = currentNode.next;
        }
    }

    public T remove(int index){
        if (index < 0 || index >= size()) throw new IndexOutOfBoundsException();
        if (index == 0) return removeFirst();
        if (index == size() -1) return removeLast();

        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }

        Node<T> previousNode = currentNode.prev;
        Node<T> nextNode = currentNode.next;

        previousNode.next = nextNode;
        nextNode.prev = previousNode;

        return currentNode.value;
    }

    public T removeFirst() {
        if (head == null) throw new IllegalStateException("List is empty");
        T value = head.value;
        head = head.next;
        if (head != null) {
            head.prev = null;
        }else{
            tail = null;
        }
        return value;
    }

    public T removeLast() {
        if (tail == null) throw new IllegalStateException("List is empty");
        T value = tail.value;
        tail = tail.prev;
        if (tail != null){
            tail.next = null;
        }else{
            head = null;
        }
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkedList<?> that)) return false;

        Node<T> nodeA = this.head;
        Node<?> nodeB = that.head;

        while (nodeA != null && nodeB != null) {
            if (!Objects.equals(nodeA.value, nodeB.value)) return false;
            nodeA = nodeA.next;
            nodeB = nodeB.next;
        }

        return nodeA == null && nodeB == null;
    }

    @Override
    public int hashCode() {
        int result =1;
        Node<T> currentNode = head;
        while (currentNode != null){
            result = 31 * result + Objects.hashCode(currentNode.value);
            currentNode = currentNode.next;
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("LinkedList{");
        Node<T> currentNode = head;
        while (currentNode != null){
            builder.append(currentNode.value);
            if (currentNode.next != null){
                builder. append(" <-> ");
            }
            currentNode = currentNode.next;
        }
        builder.append("}");
        return builder.toString();
    }

    private static class Node<T>{
        private final T value;
        private Node<T> next;
        private Node<T> prev;

        public Node(T value){
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

    }
}
