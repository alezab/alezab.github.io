# Obsługa wyjątków w Javie – try-catch vs throws vs throw new

## try-catch

Służy do przechwytywania i obsługi wyjątków w miejscu, gdzie mogą wystąpić.

Przykład:
```java
try {
    int x = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Błąd: dzielenie przez zero!");
}
```

## try-catch z warunkiem

Możesz użyć instrukcji warunkowej (`if`) wewnątrz bloku `try`, aby obsłużyć różne sytuacje, a wyjątek rzucić tylko, gdy warunek jest spełniony:

```java
try {
    if (x < 0) {
        throw new IllegalArgumentException("x nie może być ujemne");
    }
    // dalszy kod
} catch (IllegalArgumentException e) {
    System.out.println("Błąd: " + e.getMessage());
}
```

Możesz też mieć kilka różnych warunków i rzucać różne wyjątki w zależności od sytuacji:

```java
try {
    if (x < 0) {
        throw new IllegalArgumentException("x < 0");
    }
    if (x == 0) {
        throw new ArithmeticException("x == 0");
    }
    // dalszy kod
} catch (IllegalArgumentException e) {
    System.out.println("Nieprawidłowy argument: " + e.getMessage());
} catch (ArithmeticException e) {
    System.out.println("Błąd arytmetyczny: " + e.getMessage());
}
```

## throws

Służy do deklarowania, że metoda może zgłosić wyjątek, ale nie obsługuje go bezpośrednio. Przerzuca odpowiedzialność za obsługę wyjątku na wywołującego.

Przykład:
```java
public void readFile() throws IOException {
    // kod, który może rzucić IOException
}
```

## Tworzenie własnych wyjątków (Custom Exceptions)

Możesz utworzyć własną klasę wyjątku, dziedzicząc po klasie `Exception` lub `RuntimeException`:

```java
public class CustomException extends Exception {
    public CustomException(String message) {
        super(message);
    }
}
```

Użycie własnego wyjątku:

```java
if (warunek_bledu) {
    throw new CustomException("Customowy exception");
}
```

## throw new

Służy do ręcznego zgłaszania wyjątku.

Przykład:
```java
if (x < 0) {
    throw new IllegalArgumentException("x nie może być ujemne");
}
```

## Podsumowanie

- **try-catch** – obsługa wyjątku tu i teraz.
- **throws** – informacja, że metoda może rzucić wyjątek (obsługa gdzie indziej).
- **throw new** – ręczne zgłoszenie wyjątku.
