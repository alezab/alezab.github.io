# Notatka: Projekt Genealogy – Java dla początkujących

## Użyte elementy języka Java

W projekcie genealogicznym wykorzystano wiele podstawowych i nieco bardziej zaawansowanych elementów języka Java. Oto najważniejsze z nich:

### 1. Klasy i obiekty
- Definiowanie własnych klas (`Person`, `Family`, `PlantUMLRunner`).
- Tworzenie obiektów za pomocą operatora `new`.
- Konstruktory (w tym z różną liczbą argumentów).

### 2. Pola i metody
- Prywatne pola (np. `private String firstName`).
- Publiczne metody (np. gettery, metody statyczne).
- Przeciążanie metod (np. różne wersje konstruktora).

### 3. Kolekcje
- Listy (`List<Person>`, np. `ArrayList`).
- Zbiory (`Set<Person>`, np. `HashSet`).
- Mapy (`Map<String, Person>`, `Map<String, List<Person>>`).

### 4. Praca z datami
- Klasa `LocalDate` do reprezentacji daty urodzenia i śmierci.
- Parsowanie dat z tekstu (`LocalDate.parse`).

### 5. Wyjątki
- Tworzenie własnych wyjątków (np. `NegativeLifespanException`).
- Rzucanie i obsługa wyjątków (`throw`, `try-catch`).

### 6. Interfejsy funkcyjne i wyrażenia lambda
- Użycie interfejsów funkcyjnych `Predicate<T>` i `Function<T, R>`.
- Przekazywanie funkcji jako argumentów do metod.

#### **Predicate**
`Predicate<T>` to interfejs funkcyjny reprezentujący funkcję przyjmującą jeden argument typu `T` i zwracającą wartość typu `boolean`. Używany jest do testowania warunków, np. filtrowania listy:

```java
Predicate<Person> isAlive = p -> p.getDeathDate() == null;
if (isAlive.test(person)) { ... }
```

#### **Function**
`Function<T, R>` to interfejs funkcyjny reprezentujący funkcję przyjmującą jeden argument typu `T` i zwracającą wynik typu `R`. Umożliwia przekształcanie wartości, np.:

```java
Function<String, String> toYellow = line -> line + " #Yellow";
String result = toYellow.apply("object \"Jan Kowalski\"");
```

### 7. Strumienie (Streams)
- Filtrowanie, sortowanie i przetwarzanie kolekcji za pomocą strumieni (`stream()`, `filter`, `map`, `sorted`, `collect`).

### 8. Serializacja
- Zapisywanie i odczytywanie obiektów do/z pliku binarnego (`ObjectOutputStream`, `ObjectInputStream`).

### 9. Praca z plikami
- Odczyt plików tekstowych (`BufferedReader`, `FileReader`).
- Zapis plików (`FileWriter`).

### 10. Wywołanie zewnętrznych procesów
- Uruchamianie programu PlantUML przez `ProcessBuilder`.

---

## Przydatne ogólne szablony (snippety) do podobnych zadań

### 1. Klasa z polami, konstruktorem i getterami
```java
public class Example {
    private String name;
    private int age;

    public Example(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
}
```

### 2. Wczytywanie pliku CSV do listy obiektów
```java
List<Example> list = new ArrayList<>();
try (BufferedReader reader = new BufferedReader(new FileReader("plik.csv"))) {
    String line = reader.readLine(); // pomiń nagłówek
    while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        list.add(new Example(parts[0], Integer.parseInt(parts[1])));
    }
}
```

### 3. Filtrowanie i sortowanie listy
```java
// Filtrowanie
List<Example> filtered = list.stream()
    .filter(e -> e.getAge() > 18)
    .collect(Collectors.toList());

// Sortowanie
list.sort(Comparator.comparing(Example::getAge));
```

### 4. Własny wyjątek
```java
public class MyException extends Exception {
    public MyException(String message) {
        super(message);
    }
}
```

### 5. Użycie Predicate i Function
```java
Predicate<Example> isAdult = e -> e.getAge() >= 18;
Function<Example, String> toStringFunc = e -> e.getName() + ", wiek: " + e.getAge();

for (Example e : list) {
    if (isAdult.test(e)) {
        System.out.println(toStringFunc.apply(e));
    }
}
```

### 6. Serializacja listy obiektów
```java
// Zapis
try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.bin"))) {
    out.writeObject(list);
}
// Odczyt
try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.bin"))) {
    List<Example> loaded = (List<Example>) in.readObject();
}
```

### 7. Wywołanie zewnętrznego programu (np. PlantUML)
```java
ProcessBuilder pb = new ProcessBuilder("java", "-jar", "plantuml.jar", "diagram.puml");
pb.inheritIO();
Process p = pb.start();
p.waitFor();
```

---

Te szablony mogą być punktem wyjścia do rozwiązywania podobnych zadań w przyszłości.

## Podsumowanie
Projekt genealogiczny to praktyczny przykład wykorzystania podstawowych i średniozaawansowanych elementów Javy: klas, kolekcji, wyjątków, pracy z plikami, datami oraz programowania funkcyjnego (Predicate, Function). Pozwala zrozumieć, jak łączyć różne narzędzia języka w celu rozwiązania realnego problemu.
