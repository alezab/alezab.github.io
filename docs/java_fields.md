# Modyfikatory dostępu do pól w Javie

## Typy modyfikatorów dostępu

- **private** – pole dostępne tylko w tej samej klasie.
- **protected** – pole dostępne w tej samej klasie, w klasach dziedziczących oraz w tym samym pakiecie.
- **public** – pole dostępne z dowolnego miejsca.
- **(default)** – brak modyfikatora, pole dostępne tylko w tym samym pakiecie.

## Przykłady

```java
public class Person {
    private String name;      // tylko w tej klasie
    protected int age;        // w tej klasie, klasach dziedziczących i pakiecie
    public String address;    // wszędzie
    double salary;            // (default) tylko w pakiecie
}
```

## Czy pola w klasie abstrakcyjnej muszą być `protected`?

Nie, pola w klasie abstrakcyjnej mogą mieć dowolny modyfikator dostępu (`private`, `protected`, `public`, (default)). Wybór zależy od tego, czy chcesz, aby były widoczne w podklasach lub poza pakietem. Najczęściej stosuje się `protected` dla pól, które mają być dostępne w podklasach, ale nie jest to wymagane.

Przykład:
```java
public abstract class Animal {
    private String name;       // tylko w tej klasie
    protected int age;         // w podklasach i pakiecie
    public String type;        // wszędzie
}
```

## Podsumowanie

| Modyfikator  | Ta sama klasa | Ten sam pakiet | Podklasa | Inne pakiety |
|--------------|:-------------:|:--------------:|:--------:|:------------:|
| private      |      ✔        |                |          |              |
| (default)    |      ✔        |      ✔         |          |              |
| protected    |      ✔        |      ✔         |    ✔     |              |
| public       |      ✔        |      ✔         |    ✔     |      ✔       |
