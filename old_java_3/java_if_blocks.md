# Instrukcja if w Javie – sprawdzanie przedziałów, AND, OR

## Sprawdzanie czy wartość jest w przedziale

```java
int x = 7;
if (x >= 5 && x <= 10) {
    System.out.println("x jest w przedziale 5-10");
}
```
- `&&` (AND) – oba warunki muszą być spełnione.

## Sprawdzanie czy wartość jest poza przedziałem

```java
if (x < 5 || x > 10) {
    System.out.println("x jest poza przedziałem 5-10");
}
```
- `||` (OR) – wystarczy, że jeden z warunków jest spełniony.

## Przykład z negacją

```java
if (!(x >= 5 && x <= 10)) {
    System.out.println("x jest poza przedziałem 5-10");
}
```

## Łączenie wielu warunków

```java
if ((x > 0 && x < 100) || x == 200) {
    System.out.println("x jest w przedziale 1-99 lub równe 200");
}
```
