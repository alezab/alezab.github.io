# Wykład 7. Programowanie funkcyjne w Javie

## Strumienie (Streams)
Strumienie (`Stream`) to sekwencje danych, które umożliwiają przetwarzanie zbiorów w sposób deklaratywny (np. filtrowanie, mapowanie, redukcja). Pozwalają na operacje na kolekcjach bez konieczności pisania pętli.

Przykład:
```java
List<String> names = Arrays.asList("Ala", "Ola", "Ela");
names.stream()
    .filter(name -> name.startsWith("A"))
    .forEach(System.out::println);
```

## Funktory
W Javie nie ma bezpośredniego pojęcia funktora jak w C++, ale można je rozumieć jako obiekty, które można wywołać jak funkcje (np. poprzez interfejsy funkcyjne i lambdy).

## Interfejsy funkcyjne
Interfejs funkcyjny to interfejs z jedną metodą abstrakcyjną. Przykłady: `Runnable`, `Comparator`, `Function<T,R>`, `Predicate<T>`. Umożliwiają przekazywanie zachowań jako parametrów.

Przykład:
```java
@FunctionalInterface
interface MyFunction {
    void execute();
}
```

## Funkcje lambda
Funkcje lambda to skrócona forma zapisu implementacji interfejsów funkcyjnych. Pozwalają na przekazywanie funkcji jako argumentów.

Przykład:
```java
MyFunction f = () -> System.out.println("Hello!");
f.execute();
```

---

# Wykład 8. Programowanie generyczne w Javie

## Typ Object
`Object` to bazowy typ wszystkich klas w Javie. Pozwala na przechowywanie dowolnych obiektów, ale wymaga rzutowania przy pobieraniu.

Przykład:
```java
Object obj = "tekst";
String s = (String) obj;
```

## Szablony klas i metod (Generyki)
Generyki umożliwiają tworzenie klas i metod operujących na różnych typach, bez utraty bezpieczeństwa typów.

### Klasa generyczna:
```java
class Box<T> {
    private T value;
    public void set(T value) { this.value = value; }
    public T get() { return value; }
}
Box<Integer> intBox = new Box<>();
intBox.set(123);
```

### Metoda generyczna:
```java
public <T> void printArray(T[] array) {
    for (T elem : array) {
        System.out.println(elem);
    }
}
```

**Podsumowanie:**
- Programowanie funkcyjne w Javie opiera się na strumieniach, interfejsach funkcyjnych i lambdach.
- Programowanie generyczne pozwala pisać uniwersalne klasy/metody bez utraty bezpieczeństwa typów.
