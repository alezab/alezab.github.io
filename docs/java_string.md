<!-- ...existing code... -->

## Formatowanie wartości procentowych w Java

Aby sformatować liczby jako procenty w Javie, można użyć klasy `String.format` lub `NumberFormat`.

### Przykład z String.format

```java
double value = 0.2567;
String percent = String.format("%.2f%%", value * 100); // "25.67%"
```

- `%.2f` – liczba z dwoma miejscami po przecinku
- `%%` – znak procenta

### Przykład z NumberFormat

```java
import java.text.NumberFormat;

double value = 0.2567;
NumberFormat percentFormat = NumberFormat.getPercentInstance();
percentFormat.setMinimumFractionDigits(2);
String percent = percentFormat.format(value); // "25,67%"
```

- `getPercentInstance()` – zwraca format dla procentów
- `setMinimumFractionDigits(2)` – ustawia liczbę miejsc po przecinku

## Formatowanie wartości procentowych dla różnych typów

### int

```java
int value = 25;
String percent = String.format("%d%%", value); // "25%"
```

### float

```java
float value = 0.2567f;
String percent = String.format("%.2f%%", value * 100); // "25.67%"
```

### double

```java
double value = 0.2567;
String percent = String.format("%.2f%%", value * 100); // "25.67%"
```

### String (gdy wartość jest w Stringu)

```java
String value = "25.67";
String percent = value + "%"; // "25.67%"
```

### Użycie NumberFormat dla float/double

```java
import java.text.NumberFormat;

double value = 0.2567;
NumberFormat percentFormat = NumberFormat.getPercentInstance();
percentFormat.setMinimumFractionDigits(2);
String percent = percentFormat.format(value); // "25,67%"
```

## Formatowanie procentów z uwzględnieniem Locale

Możesz użyć `Locale` w `String.format` oraz `NumberFormat`, aby dostosować formatowanie do konkretnego języka/kraju.

### String.format z Locale

```java
import java.util.Locale;

double value = 0.2567;
String percent = String.format(Locale.GERMANY, "%.2f%%", value * 100); // "25,67%"
String percentUS = String.format(Locale.US, "%.2f%%", value * 100);    // "25.67%"
```

### NumberFormat z Locale

```java
import java.text.NumberFormat;
import java.util.Locale;

double value = 0.2567;
NumberFormat percentFormatPL = NumberFormat.getPercentInstance(new Locale("pl", "PL"));
percentFormatPL.setMinimumFractionDigits(2);
String percentPL = percentFormatPL.format(value); // "25,67%"

NumberFormat percentFormatUS = NumberFormat.getPercentInstance(Locale.US);
percentFormatUS.setMinimumFractionDigits(2);
String percentUS = percentFormatUS.format(value); // "25.67%"
```

- Locale wpływa na separator dziesiętny i wygląd liczby.

<!-- ...existing code... -->