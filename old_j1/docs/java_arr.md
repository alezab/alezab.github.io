<!-- ...existing code... -->

### Wyjaśnienie: public void add(Person... people)

Słowo kluczowe `...` (trzy kropki) w deklaracji metody oznacza tzw. **varargs** (zmienną liczbę argumentów).  
Dzięki temu metoda `add` może przyjmować dowolną liczbę argumentów typu `Person` (w tym zero).

Przykład użycia:
```java
add(person1);
add(person1, person2, person3);
add(); // wywołanie bez argumentów też jest poprawne
```

Wewnątrz metody `people` jest traktowane jak tablica (`Person[]`).

```java
public void add(Person... people) {
    for (Person p : people) {
        // operacje na każdym obiekcie Person
    }
}
```
<!-- ...existing code... -->