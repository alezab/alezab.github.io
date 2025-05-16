# Java – Tworzenie obiektów i wywoływanie metod

## Tworzenie obiektów

Aby utworzyć obiekt klasy w Javie, używamy słowa kluczowego `new`:

```java
NazwaKlasy nazwaObiektu = new NazwaKlasy();
```

Przykład:
```java
Car myCar = new Car();
```

## Wywoływanie metod instancyjnych (normalnych)

Metody instancyjne wywołujemy na obiekcie:

```java
nazwaObiektu.nazwaMetody();
```

Przykład:
```java
myCar.startEngine();
```

## Wywoływanie metod statycznych

Metody statyczne wywołujemy na klasie (bez tworzenia obiektu):

```java
NazwaKlasy.nazwaMetodyStatycznej();
```

Przykład:
```java
Math.abs(-5);
```

Można też wywołać metodę statyczną przez obiekt, ale nie jest to zalecane:
```java
myCar.staticMethod();
```

## Podsumowanie

- **Obiekt**: `NazwaKlasy nazwa = new NazwaKlasy();`
- **Metoda instancyjna**: `nazwaObiektu.metoda();`
- **Metoda statyczna**: `NazwaKlasy.metodaStatyczna();`
