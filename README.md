# CustomLinkedList

A Java implementation of a doubly linked list supporting basic operations such as insertion, deletion, search, and indexed access.

## Project Structure

- `LinkedList.java` — core implementation of the doubly linked list
- `LinkedListTest.java` — unit tests written with JUnit 5
- `pom.xml` — Maven configuration file

## Key Features

- `addFirst(T value)` — insert at the beginning
- `addLast(T value)` — insert at the end
- `add(int index, T value)` — insert at a specific index
- `removeByValue(T value)` — remove by value
- `remove(int index)` — remove by index
- `get(int index)` — access by index
- `getFirst()` / `getLast()` — access first or last element
- `size()` — current size of the list
- `toString()` — readable string representation of the list

## Testing

To run the tests:

```bash
cd CustomLinkedList
mvn test
