# Klasy abstrakcyjne w Javie

## Definicja
Klasa abstrakcyjna to klasa, której nie można zainicjalizować (nie można utworzyć jej obiektu). Służy jako baza dla innych klas. Może zawierać metody abstrakcyjne (bez implementacji) oraz metody z implementacją.

## Deklaracja klasy abstrakcyjnej
Aby zadeklarować klasę abstrakcyjną, używamy słowa kluczowego `abstract`:

```java
public abstract class Animal {
    public abstract void makeSound(); // metoda abstrakcyjna
    public void sleep() {
        System.out.println("Sleeping...");
    }
}
```

## Metoda abstrakcyjna – brak implementacji

Metoda abstrakcyjna w klasie abstrakcyjnej nie zawiera implementacji, czyli nie ma ciała (nie piszemy `{ ... }`). Zamiast tego kończymy deklarację średnikiem:

```java
public abstract void makeSound(); // poprawnie – bez ciała
```

Próba dodania ciała do metody abstrakcyjnej spowoduje błąd kompilacji.

Metody nieabstrakcyjne (zwykłe) w klasie abstrakcyjnej mogą mieć normalną implementację:

```java
public void sleep() {
    System.out.println("Sleeping...");
}
```

## Dziedziczenie po klasie abstrakcyjnej
Klasa dziedzicząca po klasie abstrakcyjnej musi zaimplementować wszystkie jej metody abstrakcyjne:

```java
public class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }
}
```

## Cechy klas abstrakcyjnych
- Nie można utworzyć obiektu klasy abstrakcyjnej: `new Animal()` – błąd kompilacji.
- Mogą zawierać zarówno metody abstrakcyjne, jak i zwykłe.
- Mogą mieć pola i konstruktory.
- Służą do definiowania wspólnego interfejsu dla klas pochodnych.

## Czy klasa abstrakcyjna może mieć nieabstrakcyjne metody?

Tak, klasa abstrakcyjna może zawierać zarówno metody abstrakcyjne (bez implementacji), jak i metody nieabstrakcyjne (z implementacją). Dzięki temu można w niej umieścić wspólne zachowania dla wszystkich klas pochodnych.

Przykład:
```java
public abstract class Shape {
    public abstract double area(); // metoda abstrakcyjna
    public void printInfo() {      // metoda nieabstrakcyjna
        System.out.println("To jest figura.");
    }
}
```

## Przykład użycia

```java
Animal myDog = new Dog();
myDog.makeSound(); // Woof!
myDog.sleep();     // Sleeping...
```
