# Deep, Shallow i Lazy Copy (z przykładami i referencją)
- **Shallow copy**: kopiuje tylko referencje do obiektów, więc zmiana w jednym obiekcie wpływa na drugi, jeśli zawiera referencje do tych samych obiektów.
```java
List<Person> original = new ArrayList<>();
List<Person> shallowCopy = new ArrayList<>(original); // tylko referencje
```
- **Deep copy**: tworzy nowe instancje wszystkich obiektów, więc zmiany w kopii nie wpływają na oryginał. Wymaga jawnego kopiowania pól złożonych.
```java
List<Person> deepCopy = original.stream()
    .map(person -> new Person(person)) // zakładając konstruktor kopiujący
    .collect(Collectors.toList());
```
- **Lazy copy**: kopia tworzona dopiero przy modyfikacji (np. CopyOnWriteArrayList), oszczędza pamięć i czas przy niezmiennych kolekcjach.

# Builder i Decorator
- **Builder**: wzorzec projektowy pozwalający na stopniowe budowanie złożonych obiektów poprzez wywoływanie metod pośrednich, poprawia czytelność kodu i unika złożonych konstruktorów.
```java
Person p = new Person.Builder().name("Jan").age(30).build();
```
- **Decorator**: wzorzec umożliwiający dynamiczne dodawanie nowych funkcjonalności do obiektów bez zmiany ich kodu źródłowego, poprzez opakowanie ich w dodatkowe klasy.
```java
InputStream in = new BufferedInputStream(new FileInputStream("plik.txt"));
```

# Custom Exception i jak wywołać
Tworzenie własnych wyjątków pozwala na lepszą kontrolę nad obsługą błędów w aplikacji.
```java
public class CustomException extends Exception {
    public CustomException(String message) {
        super(message);
    }
}

if (warunek_bledu) {
    throw new CustomException("Customowy exception");
}
```

# Scanner - pobieranie informacji od usera
Scanner pozwala na łatwe pobieranie danych wejściowych od użytkownika z konsoli.
```java
Scanner scanner = new Scanner(System.in);
String name = scanner.nextLine();
int age = scanner.nextInt();
```

# fromBinaryFile, ObjectInputStream
Służy do odczytu obiektów z pliku binarnego (serializacja/deserializacja).
```java
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("plik.bin"));
MyObject obj = (MyObject) ois.readObject();
ois.close();
```

# ChronoUnit.DAYS.between
Pozwala obliczyć różnicę w dniach (lub innych jednostkach czasu) między dwiema datami.
```java
long days = ChronoUnit.DAYS.between(birthDate, deathDate);
```

# Komparatory zwykłe i implements Comparable
- **Comparable**: interfejs do naturalnego porównywania obiektów (np. sortowanie po jednym polu).
```java
class Person implements Comparable<Person> {
    public int compareTo(Person other) {
        return this.age - other.age;
    }
}
```
- **Comparator**: umożliwia tworzenie różnych sposobów porównywania obiektów, np. po kilku polach lub w odwrotnej kolejności.
```java
Collections.sort(list, new Comparator<Person>() {
    public int compare(Person p1, Person p2) {
        return Long.compare(p2.getLifespan(), p1.getLifespan());
    }
});
```

# Java test, apply i Predicate
- **Predicate**: interfejs funkcyjny do testowania warunków (zwraca boolean).
- **Function**: interfejs funkcyjny do przekształcania wartości.
```java
Predicate<String> isEmpty = String::isEmpty;
boolean result = isEmpty.test("");
Function<Integer, Integer> square = x -> x * x;
int y = square.apply(5);
```

# Single way linked list implementation (generic)
Jednokierunkowa lista wiązana z użyciem typów generycznych.
```java
class Node<T> {
    T data;
    Node<T> next;
    // konstruktor, gettery, settery
}
```

# Interface vs Abstract Class
- **Interface**: zawiera tylko sygnatury metod (od Javy 8 mogą być domyślne implementacje i metody statyczne), nie może mieć stanu (pól instancyjnych).
- **Abstract class**: może mieć zarówno metody abstrakcyjne, jak i zaimplementowane, może mieć pola i konstruktor.
- Klasa może dziedziczyć tylko po jednej klasie abstrakcyjnej, ale implementować wiele interfejsów.
- Interfejsy służą do definiowania kontraktów, klasy abstrakcyjne do dziedziczenia wspólnej logiki.

# Generyczne programowanie, getClass, hashCode, clone, rzutowanie obiektów
- **Generyki**: pozwalają pisać klasy/metody operujące na różnych typach bez utraty bezpieczeństwa typów.
- **getClass()**: zwraca klasę obiektu w czasie wykonania.
- **hashCode()**: zwraca kod skrótu obiektu, używany np. w HashMap.
- **clone()**: tworzy kopię obiektu (wymaga implementacji Cloneable).
- **Rzutowanie**: zmiana typu referencji, wymaga jawnego castowania.
```java
T obj = (T) someObject; // rzutowanie
obj.getClass();
obj.hashCode();
obj.clone();
```

# Iterator, Spliterator
- **Iterator**: pozwala przechodzić po elementach kolekcji w sposób sekwencyjny.
- **Spliterator**: umożliwia podział kolekcji na części do przetwarzania równoległego (np. w streamach).
```java
Iterator<String> it = list.iterator();
while(it.hasNext()) { ... }

Spliterator<String> spl = list.spliterator();
spl.forEachRemaining(System.out::println);
```

# Serializable
Interfejs `Serializable` umożliwia zapisanie stanu obiektu do strumienia (np. pliku) i późniejsze jego odczytanie (serializacja/deserializacja). Nie posiada metod, jest tzw. interfejsem znacznikowym.
```java
class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    // pola, konstruktory itd.
}
```
Aby obiekt mógł być zapisany do pliku przez `ObjectOutputStream`, musi implementować ten interfejs.

# Przydatne rzeczy:
```java
String[] lineParts = line.split(",");
str.toLowerCase().contains("abc");
str.isEmpty();
DateTimeFormatter.ofPattern("dd.MM.yyyy");
LocalDate, LocalTime
map.get(key), map.put(key, value), map.containsKey(key)
date1.isBefore(date2), date1.isAfter(date2)
```
Shift+F6  
Ctrl Alt Shift L  

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Hello {
    public static void main(String a[]) {

        Comparator<String> com = new Comparator<String>() {
            public int compare(String i, String j)
            {
                if(i.length() < j.length()) 
                    return 1;
                else 
                    return -1;
            }
        };
        List<String> names = new ArrayList<>();
        names.add("Navin");
        names.add("Navin Reddy");
        names.add("Telusko");
        names.add("Navin Telusko");

        Collections.sort(names, com);

        System.out.println(names);

    }
}
```

