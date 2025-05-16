# Zadanie: Klasa abstrakcyjna Clock

## Treść polecenia

Napisz klasę abstrakcyjną `Clock` przechowującą wewnętrznie stan zegara, posiadającą publiczne metody:
- `setCurrentTime`, która ustawia czas zegara na bieżącą godzinę zgodnie z zegarem systemowym.
- `setTime`, która przyjmuje trzy zmienne całkowite - godzinę, minutę i sekundę i ustawia zgodnie z nią czas zegara. Jeżeli dane nie są poprawne w kontekście zegara 24-godzinnego, należy rzucić wyjątek `IllegalArgumentException` i opisać przyczynę w wiadomości tego wyjątku (która ze zmiennych nie mieści się w jakim zakresie).
- `toString`, która zwraca napis zawierający godzinę w formacie hh:mm:ss.

Sposób przechowywania czasu w zegarze zaproponuj samodzielnie.

---

## Implementacja klasy Clock (z użyciem LocalTime i DateTimeFormatter)

```java
// filepath: src\Clock.java
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class Clock {
    protected LocalTime time;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private City city;

    public Clock(City city) {
        this.city = city;
        this.time = null;
    }

    protected void onTimeChanged() {
        // domyślnie nic nie robi, do nadpisania w AnalogClock
    }

    public void setCurrentTime() {
        this.time = LocalTime.now();
        onTimeChanged();
    }

    public void setTime(int hour, int minute, int second) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Godzina (" + hour + ") musi być w zakresie 0-23.");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minuta (" + minute + ") musi być w zakresie 0-59.");
        }
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException("Sekunda (" + second + ") musi być w zakresie 0-59.");
        }
        this.time = LocalTime.of(hour, minute, second);
        onTimeChanged();
    }

    /**
     * Ustawia nowe miasto i przelicza czas na czas strefowy nowego miasta.
     */
    public void setCity(City newCity) {
        if (this.city != null && this.time != null) {
            int oldTz = this.city.getSummerTimeZone();
            int newTz = newCity.getSummerTimeZone();
            int diff = newTz - oldTz;
            this.time = this.time.plusHours(diff);
        }
        this.city = newCity;
    }

    public City getCity() {
        return city;
    }

    @Override
    public String toString() {
        return time == null ? "Brak ustawionego czasu" : time.format(FORMATTER);
    }
}
```

---

## Przykład użycia klasy Clock w klasie Main (Krok 1)

```java
// filepath: src\Main.java
public class Main {
    public static void main(String[] args) {
        Clock clock = new Clock() {};
        clock.setCurrentTime();
        System.out.println("Aktualny czas: " + clock);

        try {
            clock.setTime(15, 30, 45);
            System.out.println("Ustawiony czas: " + clock);
            clock.setTime(25, 0, 0); // Błąd!
        } catch (IllegalArgumentException e) {
            System.out.println("Błąd: " + e.getMessage());
        }

        // Przykład użycia City.parseFile
        String path = "src\\strefy.csv";
        var cityMap = City.parseFile(path);
        System.out.println("Wczytane miasta:");
        for (var entry : cityMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
```

---

## Krok 2. Klasa DigitalClock z trybem 24/12-godzinnym

Napisz klasę `DigitalClock`, dziedziczącą po `Clock`. W klasie `DigitalClock` utwórz publiczny typ wyliczeniowy pozwalający na rozróżnienie między zegarem 24-godzinnym i 12-godzinnym. Dodaj argument tego typu do konstruktora.  
Jeżeli ustawiony jest tryb 24-godzinny, metoda `toString` powinna wywołać metodę `toString` klasy nadrzędnej. W trybie 12-godzinnym napis powinien ograniczać się do 12 godzin z dopiskiem AM lub PM i nie poprzedzać jednocyfrowej godziny zerem.

---

## Implementacja klasy DigitalClock

```java
// filepath: src\DigitalClock.java
import java.time.format.DateTimeFormatter;

public class DigitalClock extends Clock {
    public enum Mode {
        H24, H12
    }

    private final Mode mode;
    private static final DateTimeFormatter FORMATTER_12H = DateTimeFormatter.ofPattern("h:mm:ss a");

    public DigitalClock(Mode mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        if (time == null) return "Brak ustawionego czasu";
        if (mode == Mode.H24) {
            return super.toString();
        } else {
            return time.format(FORMATTER_12H);
        }
    }
}
```

---

## Przykład użycia klasy DigitalClock w klasie MainDigitalClock (Krok 2)

```java
// filepath: src\MainDigitalClock.java
public class Main {
    public static void main(String[] args) {
        DigitalClock clock24 = new DigitalClock(DigitalClock.Mode.H24);
        clock24.setTime(9, 5, 3);
        System.out.println("Tryb 24h: " + clock24);

        DigitalClock clock12 = new DigitalClock(DigitalClock.Mode.H12);
        clock12.setTime(9, 5, 3);
        System.out.println("Tryb 12h: " + clock12);

        clock12.setTime(15, 30, 45);
        System.out.println("Tryb 12h (popołudnie): " + clock12);

        clock12.setTime(0, 0, 0);
        System.out.println("Tryb 12h (północ): " + clock12);
    }
}
```

---

## Krok 3. Klasa City i wczytywanie danych z pliku CSV

Zapoznaj się z plikiem `strefy.csv` dołączonym do treści zadania. Plik zawiera letnie strefy czasowe wybranych miast oraz ich współrzędne geograficzne wyrażone stopniami kątowymi.

Napisz klasę `City` zawierającą wszystkie dane z pojedynczego wiersza pliku. Napisz w niej metody statyczne:
- prywatną `parseLine`, przyjmującą pojedynczą linię pliku i zwracającą obiekt `City`,
- publiczną `parseFile`, przyjmującą ścieżkę do tego pliku i zwracającą mapę zapełnioną danymi z pliku, w której kluczem jest nazwa miasta, a wartością obiekt `City`.

W razie potrzeby, do każdej z danych wczytanych do obiektu `City` można stworzyć publiczny akcesor. Wywołaj metodę `parseFile` przekazując jej ścieżkę do przykładowego pliku.

---

## Implementacja klasy City

```java
// filepath: src\City.java
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalTime;

public class City {
    private final String name;
    private final int summerTimeZone;
    private final String latitude;
    private final String longitude;

    public City(String name, int summerTimeZone, String latitude, String longitude) {
        this.name = name;
        this.summerTimeZone = summerTimeZone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() { return name; }
    public int getSummerTimeZone() { return summerTimeZone; }
    public String getLatitude() { return latitude; }
    public String getLongitude() { return longitude; }

    private static City parseLine(String line) {
        String[] parts = line.split(",", 4);
        if (parts.length < 4) throw new IllegalArgumentException("Nieprawidłowy format linii: " + line);
        String name = parts[0].trim();
        int tz = Integer.parseInt(parts[1].trim());
        String latitude = parts[2].trim();
        String longitude = parts[3].trim();
        return new City(name, tz, latitude, longitude);
    }

    public static Map<String, City> parseFile(String path) throws IOException {
        Map<String, City> map = new LinkedHashMap<>();
        List<String> lines = Files.readAllLines(Paths.get(path));
        for (int i = 1; i < lines.size(); i++) { // pomijamy nagłówek
            String line = lines.get(i).trim();
            if (!line.isEmpty()) {
                City city = parseLine(line);
                map.put(city.getName(), city);
            }
        }
        return map;
    }

    /**
     * Zwraca czas miejscowy obliczony na podstawie długości geograficznej.
     * @param zoneTime czas zgodny ze strefą czasową miasta
     * @return czas miejscowy (LocalTime)
     */
    public LocalTime localMeanTime(LocalTime zoneTime) {
        double longitudeDeg = parseLongitudeToDegrees(this.longitude);
        // Przesunięcie godzinowe: -180 -> -12h, 0 -> 0h, 180 -> +12h
        double offsetHours = longitudeDeg / 15.0;
        int secondsOffset = (int)Math.round(offsetHours * 3600);
        return zoneTime.plusSeconds(secondsOffset);
    }

    /**
     * Parsuje długość geograficzną w formacie "21.0122 E" lub "77.0428 W" na stopnie (ujemne dla W).
     */
    private static double parseLongitudeToDegrees(String lon) {
        String[] parts = lon.trim().split("\\s+");
        double value = Double.parseDouble(parts[0].replace(',', '.'));
        if (parts.length > 1 && (parts[1].equalsIgnoreCase("W") || parts[1].equalsIgnoreCase("W."))) {
            value = -value;
        }
        return value;
    }

    @Override
    public String toString() {
        return name + " [strefa: " + summerTimeZone + ", lat: " + latitude + ", lon: " + longitude + "]";
    }

    /**
     * Komparator porównujący miasta według bezwzględnej różnicy między czasem miejscowym a strefowym.
     * Miasta z największą różnicą są na początku.
     */
    public static int worstTimezoneFit(City a, City b) {
        int diffA = Math.abs(a.getLocalMeanTimeOffsetSeconds());
        int diffB = Math.abs(b.getLocalMeanTimeOffsetSeconds());
        // Największa różnica - pierwsze
        return Integer.compare(diffB, diffA);
    }

    /**
     * Zwraca różnicę (w sekundach) między czasem miejscowym a strefowym.
     */
    public int getLocalMeanTimeOffsetSeconds() {
        double longitudeDeg = parseLongitudeToDegrees(this.longitude);
        double offsetHours = longitudeDeg / 15.0;
        int secondsOffset = (int)Math.round(offsetHours * 3600);
        int strefaSeconds = this.summerTimeZone * 3600;
        return secondsOffset - strefaSeconds;
    }

    /**
     * Generuje pliki SVG zegarów analogowych dla listy miast.
     * @param cities lista miast
     * @param clock obiekt AnalogClock (będzie zmieniał miasto i czas)
     */
    public static void generateAnalogClocksSvg(List<City> cities, AnalogClock clock) throws Exception {
        String dirName = clock.toString();
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        for (City city : cities) {
            clock.setCity(city);
            clock.setCurrentTime();
            String fileName = dirName + File.separator + city.getName().replaceAll("[^a-zA-Z0-9_\\-]", "_") + ".svg";
            clock.toSvg(fileName);
        }
    }
}
```

---

## Przykład użycia klasy City w klasie Main (Krok 3)

```java
// filepath: src\\Main.java
public class Main {
    public static void main(String[] args) throws Exception {
        String path = "src\\strefy.csv";
        var cityMap = City.parseFile(path);
        System.out.println("Wczytane miasta:");
        for (var entry : cityMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
```

---

## Krok 4. Powiązanie Clock z miastem i zmiana czasu przy zmianie miasta

W klasie `Clock` dodaj prywatne pole `City`. Zmodyfikuj konstruktor tak, aby przyjmował referencję na obiekt `City` i ustawiał ją w tym polu.  
Dodaj publiczną metodę `setCity`, która przyjmie referencję na obiekt `City` i zastąpi w obiekcie `Clock` dotychczasową referencję na `City`.  
Zmiana miasta powinna spowodować zmianę wskazywanej godziny (np. zmiana Warszawy na Kijów powinna zwiększyć godzinę o 1 lub ewentualnie z 23 na 0).

---

## Implementacja zmian w klasie Clock

```java
// filepath: src\Clock.java
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class Clock {
    protected LocalTime time;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private City city;

    public Clock(City city) {
        this.city = city;
        this.time = null;
    }

    protected void onTimeChanged() {
        // domyślnie nic nie robi, do nadpisania w AnalogClock
    }

    public void setCurrentTime() {
        this.time = LocalTime.now();
        onTimeChanged();
    }

    public void setTime(int hour, int minute, int second) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Godzina (" + hour + ") musi być w zakresie 0-23.");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minuta (" + minute + ") musi być w zakresie 0-59.");
        }
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException("Sekunda (" + second + ") musi być w zakresie 0-59.");
        }
        this.time = LocalTime.of(hour, minute, second);
        onTimeChanged();
    }

    /**
     * Ustawia nowe miasto i przelicza czas na czas strefowy nowego miasta.
     */
    public void setCity(City newCity) {
        if (this.city != null && this.time != null) {
            int oldTz = this.city.getSummerTimeZone();
            int newTz = newCity.getSummerTimeZone();
            int diff = newTz - oldTz;
            this.time = this.time.plusHours(diff);
        }
        this.city = newCity;
    }

    public City getCity() {
        return city;
    }

    @Override
    public String toString() {
        return time == null ? "Brak ustawionego czasu" : time.format(FORMATTER);
    }
}
```

---

## Przykład użycia setCity w klasie Main (Krok 4)

```java
// filepath: src\Main.java
public class Main {
    public static void main(String[] args) throws Exception {
        var cityMap = City.parseFile("src\\strefy.csv");
        City warszawa = cityMap.get("Warszawa");
        City kijow = cityMap.get("Kijów");

        Clock clock = new Clock(warszawa) {};
        clock.setTime(22, 0, 0);
        System.out.println("Czas w Warszawie: " + clock + " (" + clock.getCity().getName() + ")");

        clock.setCity(kijow);
        System.out.println("Po zmianie miasta na Kijów: " + clock + " (" + clock.getCity().getName() + ")");
    }
}
```

---

## Krok 5. Obliczanie czasu miejscowego na podstawie długości geograficznej

W klasie `City` napisz publiczną metodę:
- `localMeanTime`, która przyjmie czas zgodny ze swoją strefą czasową i zwróci czas miejscowy obliczony na podstawie długości geograficznej.

Długość geograficzna przyjmuje wartości od -180 do +180 stopni, a odpowiadające jej przesunięcie godzinowe wartości od -12 do +12 godzin. Przesunięcie czasu zmienia się wprost proporcjonalnie do długości geograficznej.

Na przykład, jeżeli w Lublinie, według strefy czasowej, jest godzina 12:00:00, według położenia geograficznego, będzie to 11:30:16.

---

## Implementacja metody localMeanTime w klasie City

```java
// filepath: src\City.java
// ...existing code...
import java.time.LocalTime;

public class City {
    // ...existing code...

    /**
     * Zwraca czas miejscowy obliczony na podstawie długości geograficznej.
     * @param zoneTime czas zgodny ze strefą czasową miasta
     * @return czas miejscowy (LocalTime)
     */
    public LocalTime localMeanTime(LocalTime zoneTime) {
        double longitudeDeg = parseLongitudeToDegrees(this.longitude);
        // Przesunięcie godzinowe: -180 -> -12h, 0 -> 0h, 180 -> +12h
        double offsetHours = longitudeDeg / 15.0;
        int secondsOffset = (int)Math.round(offsetHours * 3600);
        return zoneTime.plusSeconds(secondsOffset);
    }

    /**
     * Parsuje długość geograficzną w formacie "21.0122 E" lub "77.0428 W" na stopnie (ujemne dla W).
     */
    private static double parseLongitudeToDegrees(String lon) {
        String[] parts = lon.trim().split("\\s+");
        double value = Double.parseDouble(parts[0].replace(',', '.'));
        if (parts.length > 1 && (parts[1].equalsIgnoreCase("W") || parts[1].equalsIgnoreCase("W."))) {
            value = -value;
        }
        return value;
    }

    // ...existing code...
}
```

---

## Przykład użycia localMeanTime w klasie Main (Krok 5)

```java
// filepath: src\Main.java
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) throws Exception {
        var cityMap = City.parseFile("src\\strefy.csv");
        City lublin = cityMap.get("Lublin");
        LocalTime zoneTime = LocalTime.of(12, 0, 0);
        LocalTime meanTime = lublin.localMeanTime(zoneTime);
        System.out.println("Czas strefowy w Lublinie: " + zoneTime);
        System.out.println("Czas miejscowy w Lublinie: " + meanTime);
    }
}
```

---

## Wyjaśnienie

- **localMeanTime**: Oblicza przesunięcie czasu na podstawie długości geograficznej. Każde 15 stopni długości to 1 godzina przesunięcia (3600 sekund).
- **Parsowanie długości**: Metoda `parseLongitudeToDegrees` zamienia długość geograficzną z formatu tekstowego na liczbę, uwzględniając kierunek (E/W).
- **Standardowe klasy Javy**: Używamy `LocalTime.plusSeconds`, co pozwala wygodnie dodać (lub odjąć) przesunięcie czasowe.

Dzięki temu można łatwo uzyskać dokładny czas miejscowy dla dowolnego miasta na podstawie jego położenia geograficznego.

---

## Krok 6. Sortowanie miast według największej różnicy między czasem miejscowym a strefowym

W klasie `City` napisz publiczną statyczną metodę:
- `worstTimezoneFit`, tak, aby mogła być użyta jako komparator dwóch miast.

Wynik sortowania przy użyciu tej metody powinien ustawić na początku kolekcji miasta, których różnica między czasem miejscowym a czasem wynikającym ze strefy czasowej jest największa.

---

## Implementacja metody worstTimezoneFit w klasie City

```java
// filepath: src\City.java
// ...existing code...
import java.util.Comparator;
import java.time.LocalTime;

public class City {
    // ...existing code...

    /**
     * Komparator porównujący miasta według bezwzględnej różnicy między czasem miejscowym a strefowym.
     * Miasta z największą różnicą są na początku.
     */
    public static int worstTimezoneFit(City a, City b) {
        int diffA = Math.abs(a.getLocalMeanTimeOffsetSeconds());
        int diffB = Math.abs(b.getLocalMeanTimeOffsetSeconds());
        // Największa różnica - pierwsze
        return Integer.compare(diffB, diffA);
    }

    /**
     * Zwraca różnicę (w sekundach) między czasem miejscowym a strefowym.
     */
    public int getLocalMeanTimeOffsetSeconds() {
        double longitudeDeg = parseLongitudeToDegrees(this.longitude);
        double offsetHours = longitudeDeg / 15.0;
        int secondsOffset = (int)Math.round(offsetHours * 3600);
        int strefaSeconds = this.summerTimeZone * 3600;
        return secondsOffset - strefaSeconds;
    }

    // ...existing code...
}
```

---

## Przykład użycia sortowania miast w klasie Main (Krok 6)

```java
// filepath: src\Main.java
// ...existing code...
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        var cityMap = City.parseFile("src\\strefy.csv");
        List<City> cities = new ArrayList<>(cityMap.values());
        cities.sort(City::worstTimezoneFit);

        System.out.println("Miasta posortowane wg największej różnicy czasu miejscowego i strefowego:");
        for (City city : cities) {
            System.out.println(city.getName());
        }
    }
}
```

---

## Wyjaśnienie

- **worstTimezoneFit**: Porównuje dwa miasta według bezwzględnej wartości różnicy między czasem miejscowym a strefowym (w sekundach).
- **getLocalMeanTimeOffsetSeconds**: Oblicza różnicę w sekundach między czasem miejscowym (na podstawie długości geograficznej) a czasem strefowym (na podstawie strefy czasowej).
- **Sortowanie**: Używamy metody referencyjnej `City::worstTimezoneFit` jako komparatora w sortowaniu listy miast.

Dzięki temu można łatwo znaleźć miasta, w których czas strefowy najbardziej odbiega od rzeczywistego czasu miejscowego.

---

## Krok 7. Klasa AnalogClock i generowanie tarczy zegara w SVG

Zapoznaj się z plikiem `zegar.svg` dołączonym do treści zadania.

Napisz klasę `AnalogClock`, dziedziczącą po `Clock`. Napisz w niej metodę:
- `toSvg`, która przyjmie ścieżkę do pliku i zapisze w nim kod SVG przedstawiający tarczę zegara (bez wskazówek).

---

## Implementacja klasy AnalogClock z metodą toSvg

```java
// filepath: src\AnalogClock.java
import java.io.FileWriter;
import java.io.IOException;

public class AnalogClock extends Clock {
    public AnalogClock(City city) {
        super(city);
    }

    /**
     * Zapisuje do pliku SVG tarczę zegara (bez wskazówek).
     * @param path ścieżka do pliku SVG
     */
    public void toSvg(String path) throws IOException {
        String svg = """
        <svg width="200" height="200" viewBox="-100 -100 200 200" xmlns="http://www.w3.org/2000/svg">
          <circle cx="0" cy="0" r="90" fill="none" stroke="black" stroke-width="2" />
          <g text-anchor="middle">
            <text x="0" y="-80" dy="6">12</text>
            <text x="80" y="0" dy="4">3</text>
            <text x="0" y="80" dy="6">6</text>
            <text x="-80" y="0" dy="4">9</text>
          </g>
        </svg>
        """;
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(svg);
        }
    }
}
```

---

## Przykład użycia klasy AnalogClock i metody toSvg w klasie Main (Krok 7)

```java
// filepath: src\Main.java
// ...existing code...
public class Main {
    public static void main(String[] args) throws Exception {
        // ...existing code...
        var cityMap = City.parseFile("src\\strefy.csv");
        City warszawa = cityMap.get("Warszawa");
        AnalogClock analogClock = new AnalogClock(warszawa);
        analogClock.toSvg("src\\tarcza_warszawa.svg");
        System.out.println("Tarcza zegara SVG została zapisana.");
    }
}
```

---

## Wyjaśnienie

- **AnalogClock**: Dziedziczy po `Clock` i reprezentuje zegar analogowy.
- **toSvg**: Zapisuje do pliku prostą tarczę zegara w formacie SVG (bez wskazówek). Kod SVG jest zgodny z przykładem z pliku `zegar.svg`.
- **FileWriter**: Używamy standardowej klasy Javy do zapisu tekstu do pliku.

Dzięki temu można łatwo wygenerować plik SVG z tarczą zegara dla dowolnego miasta.

---

## Krok 8. Abstrakcyjna klasa ClockHand do reprezentacji wskazówki zegara

Napisz klasę abstrakcyjną `ClockHand` reprezentującą wskazówkę zegara tarczowego. Klasa powinna posiadać publiczne, abstrakcyjne metody:
- `setTime`, która przyjmuje obiekt `LocalTime`,
- `toSvg`, która zwraca napis zawierający znacznik SVG wskazówki.

---

## Implementacja klasy ClockHand

```java
// filepath: src\ClockHand.java
import java.time.LocalTime;

public abstract class ClockHand {
    /**
     * Ustawia czas, na podstawie którego wskazówka powinna być ustawiona.
     * @param time obiekt LocalTime
     */
    public abstract void setTime(LocalTime time);

    /**
     * Zwraca znacznik SVG reprezentujący wskazówkę.
     * @return napis SVG
     */
    public abstract String toSvg();
}
```

---

## Wyjaśnienie

- **ClockHand**: Abstrakcyjna klasa bazowa dla wszystkich wskazówek zegara (godzinowej, minutowej, sekundowej).
- **setTime**: Pozwala ustawić czas, na podstawie którego wyliczana jest pozycja wskazówki.
- **toSvg**: Zwraca kod SVG reprezentujący wskazówkę, gotowy do wstawienia do pliku SVG.

Dzięki temu łatwo można rozszerzyć projekt o konkretne wskazówki, które będą generować odpowiedni kod SVG na podstawie ustawionego czasu.

---

## Krok 9. Klasa SecondHand – wskazówka sekundowa

Po klasie `ClockHand` podziedzicz klasę `SecondHand`. Powinna implementować metody klasy nadrzędnej:
- `setTime`, która ustawia kąt wskazówki dyskretnie (skokowo), na podstawie składowej sekund przekazanego czasu,
- `toSvg`, która zwraca znacznik SVG przedstawiający wskazówkę jako odcinek o określonej długości, grubości i kolorze, obróconą odpowiednią liczbę stopni, wynikającą z ustawionego metodą `setTime` czasu.

---

## Implementacja klasy SecondHand

```java
// filepath: src\SecondHand.java
import java.time.LocalTime;

public class SecondHand extends ClockHand {
    private int angleDeg = 0;

    @Override
    public void setTime(LocalTime time) {
        int seconds = time.getSecond();
        // Każda sekunda to 6 stopni (360/60)
        this.angleDeg = seconds * 6;
    }

    @Override
    public String toSvg() {
        // Długość wskazówki: 80, grubość: 1, kolor: czerwony
        return String.format(
            "<line x1=\"0\" y1=\"0\" x2=\"0\" y2=\"-80\" stroke=\"red\" stroke-width=\"1\" transform=\"rotate(%d)\" />",
            angleDeg
        );
    }
}
```

---

## Wyjaśnienie

- **SecondHand**: Dziedziczy po `ClockHand` i reprezentuje wskazówkę sekundową.
- **setTime**: Ustawia kąt wskazówki na podstawie liczby sekund (0 sekund = 0°, 15 sekund = 90°, 30 sekund = 180°, itd.).
- **toSvg**: Generuje znacznik SVG dla wskazówki sekundowej, ustawiając jej obrót zgodnie z kątem.

Dzięki temu można łatwo dodać wskazówkę sekundową do tarczy zegara SVG.

---

## Krok 10. Klasy HourHand i MinuteHand – wskazówki godzinowa i minutowa

Po klasie `ClockHand` podziedzicz jeszcze dwie klasy: `HourHand`, `MinuteHand` implementujące metody klasy nadrzędnej:
- `setTime`, która ustawia kąt wskazówki na podstawie czasu. Wskazówki powinny poruszać się ruchem ciągłym z dokładnością do jednej sekundy,
- `toSvg`, analogicznie jak w klasie `SecondHand`, tak, aby wskazówki wizualnie odróżniały się od siebie.

---

## Implementacja klasy HourHand

```java
// filepath: src\HourHand.java
import java.time.LocalTime;

public class HourHand extends ClockHand {
    private double angleDeg = 0.0;

    @Override
    public void setTime(LocalTime time) {
        int hour = time.getHour() % 12;
        int minute = time.getMinute();
        int second = time.getSecond();
        // Każda godzina to 30 stopni, każda minuta to 0.5 stopnia, każda sekunda to 0.5/60 stopnia
        this.angleDeg = hour * 30 + minute * 0.5 + second * (0.5 / 60);
    }

    @Override
    public String toSvg() {
        // Długość: 50, grubość: 4, kolor: czarny
        return String.format(
            "<line x1=\"0\" y1=\"0\" x2=\"0\" y2=\"-50\" stroke=\"black\" stroke-width=\"4\" transform=\"rotate(%.2f)\" />",
            angleDeg
        );
    }
}
```

---

## Implementacja klasy MinuteHand

```java
// filepath: src\MinuteHand.java
import java.time.LocalTime;

public class MinuteHand extends ClockHand {
    private double angleDeg = 0.0;

    @Override
    public void setTime(LocalTime time) {
        int minute = time.getMinute();
        int second = time.getSecond();
        // Każda minuta to 6 stopni, każda sekunda to 0.1 stopnia
        this.angleDeg = minute * 6 + second * 0.1;
    }

    @Override
    public String toSvg() {
        // Długość: 70, grubość: 2, kolor: niebieski
        return String.format(
            "<line x1=\"0\" y1=\"0\" x2=\"0\" y2=\"-70\" stroke=\"blue\" stroke-width=\"2\" transform=\"rotate(%.2f)\" />",
            angleDeg
        );
    }
}
```

---

## Wyjaśnienie

- **HourHand**: Kąt wyliczany jest z dokładnością do sekundy, co daje płynny ruch wskazówki godzinowej.
- **MinuteHand**: Kąt wyliczany jest z dokładnością do sekundy, co daje płynny ruch wskazówki minutowej.
- **toSvg**: Każda wskazówka ma inną długość, grubość i kolor, by były łatwo rozróżnialne na tarczy zegara SVG.

Dzięki temu można wygenerować pełny zegar analogowy z płynnie poruszającymi się wskazówkami.

---

## Krok 11. Wskazówki w AnalogClock i automatyczna aktualizacja położenia

W klasie `AnalogClock` umieść prywatną, polimorficzną, finalną listę wskazówek, zawierającą po jednej wskazówce każdej klasy (`HourHand`, `MinuteHand`, `SecondHand`).  
Zmodyfikuj metodę `toSvg`, aby oprócz tarczy, narysowane zostały także wskazówki.  
Niech ustawienie godziny zegara metodami z kroku 1. (`setCurrentTime`, `setTime`) powoduje zmianę położenia wskazówek.  
Rozwiązanie: dodaj do klasy `Clock` metodę chronioną `onTimeChanged()`, którą wywołują metody ustawiające czas. W `AnalogClock` nadpisz tę metodę i aktualizuj położenie wskazówek.

---

## Implementacja zmian w klasie AnalogClock

```java
// filepath: src\AnalogClock.java
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class AnalogClock extends Clock {
    private final List<ClockHand> hands;

    public AnalogClock(City city) {
        super(city);
        hands = Arrays.asList(new HourHand(), new MinuteHand(), new SecondHand());
    }

    @Override
    protected void onTimeChanged() {
        if (time != null) {
            for (ClockHand hand : hands) {
                hand.setTime(time);
            }
        }
    }

    /**
     * Zapisuje do pliku SVG tarczę zegara wraz ze wskazówkami.
     * @param path ścieżka do pliku SVG
     */
    @Override
    public void toSvg(String path) throws IOException {
        StringBuilder svg = new StringBuilder();
        svg.append("""
        <svg width="200" height="200" viewBox="-100 -100 200 200" xmlns="http://www.w3.org/2000/svg">
          <circle cx="0" cy="0" r="90" fill="none" stroke="black" stroke-width="2" />
          <g text-anchor="middle">
            <text x="0" y="-80" dy="6">12</text>
            <text x="80" y="0" dy="4">3</text>
            <text x="0" y="80" dy="6">6</text>
            <text x="-80" y="0" dy="4">9</text>
          </g>
        """);
        for (ClockHand hand : hands) {
            svg.append(hand.toSvg()).append("\n");
        }
        svg.append("</svg>");
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(svg.toString());
        }
    }
}
```

---

## Modyfikacja klasy Clock – dodanie onTimeChanged

```java
// filepath: src\Clock.java
// ...existing code...
public abstract class Clock {
    // ...existing code...

    protected void onTimeChanged() {
        // domyślnie nic nie robi, do nadpisania w AnalogClock
    }

    public void setCurrentTime() {
        this.time = LocalTime.now();
        onTimeChanged();
    }

    public void setTime(int hour, int minute, int second) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Godzina (" + hour + ") musi być w zakresie 0-23.");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minuta (" + minute + ") musi być w zakresie 0-59.");
        }
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException("Sekunda (" + second + ") musi być w zakresie 0-59.");
        }
        this.time = LocalTime.of(hour, minute, second);
        onTimeChanged();
    }

    // ...existing code...
}
```

---

## Wyjaśnienie

- **Lista hands**: Przechowuje wszystkie wskazówki (godzinowa, minutowa, sekundowa) jako obiekty polimorficzne.
- **onTimeChanged**: Chroniona metoda wywoływana po każdej zmianie czasu. W `AnalogClock` aktualizuje położenie wskazówek.
- **toSvg**: Rysuje tarczę oraz wszystkie wskazówki.
- **Brak nadpisywania setTime/setCurrentTime**: Dzięki `onTimeChanged` nie trzeba nadpisywać tych metod w podklasach.

To rozwiązanie jest elastyczne, czytelne i zgodne z zasadami OOP.

---

## Krok 12. Generowanie wielu zegarów analogowych SVG dla miast

W klasie `City` napisz publiczną metodę statyczną `generateAnalogClocksSvg`, która przyjmie listę obiektów `City` oraz obiekt `AnalogClock`.  
Metoda powinna założyć katalog o nazwie będącej wynikiem funkcji `toString` obiektu zegara.  
W katalogu, dla każdego miasta z listy, należy utworzyć plik SVG o nazwie odpowiadającej nazwie miasta. Pliki powinny zawierać wynik działania metody `toSvg` zegara w kolejnych miastach.

---

## Implementacja metody generateAnalogClocksSvg w klasie City

```java
// filepath: src\City.java
// ...existing code...
import java.util.List;
import java.io.File;

public class City {
    // ...existing code...

    /**
     * Generuje pliki SVG zegarów analogowych dla listy miast.
     * @param cities lista miast
     * @param clock obiekt AnalogClock (będzie zmieniał miasto i czas)
     */
    public static void generateAnalogClocksSvg(List<City> cities, AnalogClock clock) throws Exception {
        String dirName = clock.toString();
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        for (City city : cities) {
            clock.setCity(city);
            clock.setCurrentTime();
            String fileName = dirName + File.separator + city.getName().replaceAll("[^a-zA-Z0-9_\\-]", "_") + ".svg";
            clock.toSvg(fileName);
        }
    }

    // ...existing code...
}
```

---

## Przykład użycia generateAnalogClocksSvg w klasie Main (Krok 12)

```java
// filepath: src\Main.java
// ...existing code...
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        var cityMap = City.parseFile("src\\strefy.csv");
        List<City> cities = new ArrayList<>(cityMap.values());
        City wzorcoweMiasto = cityMap.get("Warszawa");
        AnalogClock clock = new AnalogClock(wzorcoweMiasto);
        City.generateAnalogClocksSvg(cities, clock);
        System.out.println("Wygenerowano SVG dla wszystkich miast.");
    }
}
```

---

## Wyjaśnienie

- **generateAnalogClocksSvg**: Tworzy katalog o nazwie będącej wynikiem `toString()` zegara (np. aktualny czas), a następnie dla każdego miasta ustawia miasto i czas w zegarze, generuje plik SVG z nazwą miasta.
- **Bezpieczne nazwy plików**: Zamienia znaki niealfanumeryczne w nazwie miasta na podkreślenia.
- **Współdzielenie obiektu AnalogClock**: Zegar jest jeden, zmienia tylko miasto i czas dla każdego SVG.

Dzięki temu można łatwo wygenerować zestaw plików SVG z zegarami dla wszystkich miast z listy.
