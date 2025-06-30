# Enumy w Javie

## Co to jest enum?

Enum (typ wyliczeniowy) to specjalny typ w Javie, który reprezentuje zbiór stałych, nazwanych wartości.

## Przykład deklaracji enuma

```java
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
```

## Użycie enuma

```java
Day today = Day.MONDAY;

if (today == Day.MONDAY) {
    System.out.println("Dzisiaj jest poniedziałek!");
}
```

## Enum w nagłówku metody (jako argument)

```java
public void printDay(Day day) {
    System.out.println("Wybrany dzień: " + day);
}
```

Wywołanie:
```java
printDay(Day.FRIDAY);
```

## Enum jako typ zwracany przez metodę

```java
public Day getNextDay(Day day) {
    switch(day) {
        case MONDAY: return Day.TUESDAY;
        // ...pozostałe przypadki...
        case SUNDAY: return Day.MONDAY;
        default: throw new IllegalArgumentException();
    }
}
```

## Enum z polami i metodami

```java
public enum Color {
    RED("#FF0000"),
    GREEN("#00FF00"),
    BLUE("#0000FF");

    private final String hex;

    Color(String hex) {
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }
}
```

Użycie:
```java
Color c = Color.RED;
System.out.println(c.getHex()); // "#FF0000"
```

## Iterowanie po wartościach enuma

```java
for (Day d : Day.values()) {
    System.out.println(d);
}
```