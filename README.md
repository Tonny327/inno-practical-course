# CustomLinkedList

Реализация двусвязного списка на Java с поддержкой базовых операций вставки, удаления, поиска и доступа по индексу. Проект создан в рамках практического курса Innowise.

## Структура проекта

- `LinkedList.java` — основная реализация двусвязного списка
- `LinkedListTest.java` — unit-тесты на JUnit 5
- `pom.xml` — конфигурация Maven

## Основные возможности

- `addFirst(T value)` — вставка в начало
- `addLast(T value)` — вставка в конец
- `add(int index, T value)` — вставка по индексу
- `removeByValue(T value)` — удаление по значению
- `remove(int index)` — удаление по индексу
- `get(int index)` — доступ по индексу
- `getFirst()` / `getLast()` — доступ к краям
- `size()` — текущий размер списка
- `toString()` — читаемый вывод структуры

## Тестирование

Для запуска тестов:

```bash
cd CustomLinkedList
mvn test
