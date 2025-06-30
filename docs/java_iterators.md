<!-- ...existing code... -->

## Integer.compare vs manual return 1/-1/0 in Java iterators/comparators

Przy implementacji metod porównujących (np. w Comparatorach lub metodach sortujących w iteratorach), można spotkać dwa podejścia:

### 1. Manualne zwracanie 1, -1, 0

```java
@Override
public int compare(Integer a, Integer b) {
    if (a < b) return -1;
    if (a > b) return 1;
    return 0;
}
```

### 2. Użycie Integer.compare

```java
@Override
public int compare(Integer a, Integer b) {
    return Integer.compare(a, b);
}
```

### Różnice i zalety

- `Integer.compare(a, b)` jest krótsze, czytelniejsze i mniej podatne na błędy.
- Manualne porównania mogą prowadzić do błędów, np. przy porównywaniu wartości granicznych (np. przepełnienie typu int).
- `Integer.compare` poprawnie obsługuje wszystkie przypadki, także wartości skrajne.

**Rekomendacja:**  
Zawsze używaj `Integer.compare` (lub odpowiednich metod dla innych typów prymitywnych), gdy implementujesz metody porównujące w iteratorach, comparatorach itp.

## Gdzie wykorzystujemy funkcje compare

Funkcje porównujące (`compare`, np. z Comparatora) są szeroko wykorzystywane w Javie, m.in. w:

- **Sortowaniu kolekcji**  
  Metody takie jak `Collections.sort(List, Comparator)` lub `List.sort(Comparator)` używają funkcji compare do ustalania kolejności elementów.

- **Strukturach danych opartych o porządek**  
  Struktury takie jak `TreeSet`, `TreeMap` wymagają Comparatora lub Comparable do utrzymania uporządkowania elementów.

- **Algorytmach wyszukiwania i przetwarzania**  
  Metody takie jak `Collections.binarySearch(List, key, Comparator)` wykorzystują compare do porównywania elementów podczas wyszukiwania binarnego.

- **Priorytetowe kolejki**  
  Klasa `PriorityQueue` może korzystać z Comparatora do ustalania priorytetów elementów.

**Podsumowanie:**  
Funkcje compare są kluczowe wszędzie tam, gdzie potrzebne jest porządkowanie, sortowanie lub porównywanie elementów w kolekcjach i strukturach danych.

<!-- ...existing code... -->