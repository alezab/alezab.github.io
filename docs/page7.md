## k2023

## Krok 1.
Zdefiniuj rekord lub klasę Point posiadającą dwa publiczne, ostateczne pola - liczby
zmiennoprzecinkowe. Liczby te powinny być ustawiane za pomocą konstruktora.
W dalszej części zadania, obiekty klasy Point będą skrótowo nazywane punktami.

To define a class `Point` in Java as specified in "Krok 1", you can create a simple class that contains two public final fields representing the coordinates, which are set via the constructor. Here's how you can do it:

```java
public class Point {
    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
```

This `Point` class:

- Has two public final fields, `x` and `y`, which represent the x and y coordinates of a point respectively.
- Includes a constructor that initializes `x` and `y` with values provided at the time of object creation.

You can now use this `Point` class in the rest of your program to represent coordinates or points in a two-dimensional space. This implementation aligns with typical usage in graphical or spatial data processing tasks.

## Krok 2.
Zdefiniuj klasę Polygon, która będzie posiadała prywatną listę punktów. Lista punktów powinna być
ustawiana przez konstruktor. Wielokąt zbudowany jest z krawędzi łączących kolejne punkty w liście
oraz ostatni z pierwszym punktem z listy.
W dalszej części zadania, obiekty klasy Polygon będą skrótowo nazywane wielokątami.

Oczywiście, oto jak możesz zdefiniować klasę `Polygon` w Javie zgodnie z opisem w "Krok 2":

```java
import java.util.List;

public class Polygon {
    private List<Point> points;

    public Polygon(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }
}
```

Klasa `Polygon`:

- Posiada prywatną listę `points`, która przechowuje punkty definiujące wierzchołki wielokąta.
- Lista punktów jest inicjowana przez konstruktor, co oznacza, że musisz podać listę punktów przy tworzeniu obiektu `Polygon`.
- Metoda `getPoints()` umożliwia dostęp do listy punktów z zewnątrz klasy, z zachowaniem zasad hermetyzacji (enkapsulacji), co jest zgodne z dobrymi praktykami programowania obiektowego.

Ten sposób implementacji pozwoli na tworzenie obiektów reprezentujących wielokąty na podstawie listy punktów, gdzie każdy punkt na liście jest połączony z kolejnym, a ostatni punkt jest połączony z pierwszym, tworząc zamkniętą figurę.

## Krok 3.
W klasie Polygon zdefiniuj publiczną metodę inside(Point), która zwraca wartość logiczną określającą,
czy dany argumentem metody punkt znajduje się w wielokącie, na rzecz którego wywoływana jest
metoda. Zaimplementuj w niej algorytm podany pseudokodem:
Dane są: wielokąt poly i sprawdzany punkt point.
counter = 0
Dla każdej pary (pa, pb) punktów tworzących krawędź wielokąta poly:
Jeżeli pa.y > pb.y:
Zamień pa z pb
Jeżeli pa.y < point.y < pb.y:
d = pb.x - pa.x
Jeżeli d == 0:
x = pa.x
W przeciwnym razie:
a = (pb.y - pa.y) / d
b = pa.y - a * pa.x
x = (point.y - b) / a
Jeżeli x < point.x:
counter++
Zwróć prawdę, jeżeli counter jest nieparzysty, a fałsz w przeciwnym przypadku.

Zgodnie z "Krok 3", oto jak można zaimplementować metodę `inside(Point point)` w klasie `Polygon`, która określi, czy punkt znajduje się wewnątrz wielokąta na podstawie algorytmu z pseudokodem:

```java
public class Polygon {
    private List<Point> points;

    public Polygon(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public boolean inside(Point point) {
        int counter = 0;
        int n = points.size();
        for (int i = 0; i < n; i++) {
            Point pa = points.get(i);
            Point pb = points.get((i + 1) % n);

            if (pa.y > pb.y) {
                Point temp = pa;
                pa = pb;
                pb = temp;
            }

            if (pa.y < point.y && point.y < pb.y) {
                double d = pb.x - pa.x;
                double x;
                if (d == 0) {
                    x = pa.x;
                } else {
                    double a = (pb.y - pa.y) / d;
                    double b = pa.y - a * pa.x;
                    x = (point.y - b) / a;
                }

                if (x < point.x) {
                    counter++;
                }
            }
        }

        return counter % 2 != 0;
    }
}
```

### Wyjaśnienie metody `inside(Point point)`:
- Metoda przechodzi przez każdą krawędź wielokąta, traktując ją jako parę punktów `pa` i `pb`. Punkty te są wierzchołkami wielokąta i definiują linie tworzące boki wielokąta.
- Jeśli `pa.y` jest większe niż `pb.y`, punkty są zamieniane miejscami, aby ułatwić dalsze obliczenia.
- Algorytm sprawdza, czy y punktu do sprawdzenia (`point.y`) jest pomiędzy y dwóch punktów krawędzi (`pa.y < point.y < pb.y`). Jeżeli tak, to obliczana jest wartość x przecięcia linii krawędzi z poziomą linią przechodzącą przez `point.y`.
- Jeżeli obliczone x jest mniejsze od x punktu (`x < point.x`), inkrementowany jest licznik `counter`.
- Ostatecznie, jeśli `counter` jest nieparzysty, metoda zwraca `true`, co oznacza, że punkt znajduje się wewnątrz wielokąta. Jeśli jest parzysty, zwraca `false`, co oznacza, że punkt znajduje się na zewnątrz.

Ta metoda jest implementacją tzw. algorytmu promieniowania promienia (ray-casting algorithm), który jest powszechnie stosowany do sprawdzania, czy punkt znajduje się wewnątrz wielokąta.

## Krok 4.
Dodaj do projektu bibliotekę JUnit,. Napisz trzy testy. W każdym stwórz wielokąt oraz punkt - tak aby
w kolejnych testach:
- punkt leżał w wielokącie,
- punkt leżał pod wielokątem,
- punkt leżał na prawo od wielokąta.
Uwaga! W przypadku tego oraz pozostałych kroków wymagających testowania, w razie
nieumiejętności wykorzystania biblioteki JUnit, część punktów można zdobyć pisząc metody klasy
Main, wykonujące działania, które mają zostać przetestowane.
Wyobraźmy sobie dwuwymiarową mapę, na której położenie jest opisane za pomocą współrzędnych
x i y, gdzie punkt (0, 0) znajduje się w lewej górnej krawędzi mapy. Mapa jest uproszczonym
modelem, nie uwzględniającym krzywizny Ziemi, który może być wykorzystany np. na potrzeby gry
komputerowej. Na mapie znajdują się lądy opisane za pomocą wielokątów.

Rozumiem, nie będę powtarzać instrukcji dotyczących dodawania biblioteki JUnit do projektu. Zamiast tego, skupię się na napisaniu testów zgodnie z wymaganiami.

Oto jak możesz napisać testy dla klasy `Polygon`, zakładając, że już masz skonfigurowaną bibliotekę JUnit:

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PolygonTest {
    @Test
    public void testPointInsidePolygon() {
        // Tworzenie wielokąta
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(4, 0));
        points.add(new Point(4, 4));
        points.add(new Point(0, 4));
        Polygon polygon = new Polygon(points);

        // Tworzenie punktu wewnątrz wielokąta
        Point point = new Point(2, 2);

        // Sprawdzenie czy punkt znajduje się wewnątrz wielokąta
        assertTrue(polygon.inside(point));
    }

    @Test
    public void testPointOutsidePolygon() {
        // Tworzenie wielokąta
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(4, 0));
        points.add(new Point(4, 4));
        points.add(new Point(0, 4));
        Polygon polygon = new Polygon(points);

        // Tworzenie punktu poza wielokątem
        Point point = new Point(5, 5);

        // Sprawdzenie czy punkt znajduje się poza wielokątem
        assertFalse(polygon.inside(point));
    }

    @Test
    public void testPointOnEdgeOfPolygon() {
        // Tworzenie wielokąta
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(4, 0));
        points.add(new Point(4, 4));
        points.add(new Point(0, 4));
        Polygon polygon = new Polygon(points);

        // Tworzenie punktu na krawędzi wielokąta
        Point point = new Point(2, 0);

        // Sprawdzenie czy punkt znajduje się na krawędzi wielokąta
        assertFalse(polygon.inside(point));
    }
}
```

Te testy sprawdzają różne przypadki:

1. `testPointInsidePolygon`: Tworzy wielokąt o wierzchołkach (0,0), (4,0), (4,4), (0,4) i punkt (2,2), który znajduje się wewnątrz wielokąta.
2. `testPointOutsidePolygon`: Tworzy ten sam wielokąt, ale sprawdza punkt (5,5), który znajduje się poza wielokątem.
3. `testPointOnEdgeOfPolygon`: Tworzy wielokąt i sprawdza punkt (2,0), który leży na krawędzi wielokąta, ale nie jest wewnątrz niego.

## Krok 5.
Napisz klasę Land dziedziczącą po klasie Polygon. Napisz w niej taki sam konstruktor jak w Polygon.
Na mapie znajdują się także miasta. Każde miasto otoczone jest murami w kształcie kwadratu,
o ścianach wychodzących na cztery strony świata (parami równoległe do osi układu współrzędnych).
Środek miasta leży na przecięciu przekątnych kwadratu.

Oto jak możesz napisać klasę `Land`, która dziedziczy po klasie `Polygon` i reprezentuje lądy na mapie, z uwzględnieniem opisu murów otaczających miasta:

```java
import java.util.List;

public class Land extends Polygon {
    public Land(List<Point> points) {
        super(points);
    }
}
```

W klasie `Land`:

- Wykorzystujemy dziedziczenie, aby dziedziczyć funkcjonalność klasy `Polygon`.
- Definiujemy konstruktor, który przyjmuje listę punktów jako parametr i przekazuje ją do konstruktora klasy nadrzędnej `Polygon`.

Klasa `Land` będzie więc posiadała wszystkie metody i właściwości klasy `Polygon`, ale możemy również dodawać do niej dodatkowe funkcje i zachowania specyficzne dla lądów.

## Krok 6.
Napisz klasę City dziedziczącą po Polygon. W klasie zdefiniuj publiczną ostateczną zmienną center -
punkt będący środkiem miasta oraz prywatną nazwę miasta. Napisz publiczny konstruktor, takie dwa
argumenty oraz liczbę zmiennoprzecinkową - długość ściany muru. Konstruktor powinien zapisać
w polu klasy środek miasta oraz nazwę, a w liście punktów odziedziczonej z klasy Polygon,
wierzchołki kwadratu stanowiącego mury.
Uwaga! Ten krok da się rozwiązać bez zmiany modyfikatora dostępu do listy wierzchołków wielokąta
na chroniony. Można dokonać takiej zmiany, ale takie rozwiązanie nie będzie w pełni punktowane.
Miasta mogą znajdować się na lądzie - nie na wodzie.

Oto jak możesz zaimplementować klasę `City`, dziedziczącą po klasie `Polygon`, zgodnie z opisem w "Krok 6":

```java
import java.util.List;
import java.util.ArrayList;

public class City extends Polygon {
    public final Point center;
    private final String cityName;

    public City(Point center, String cityName, double wallLength) {
        super(generateCityVertices(center, wallLength));
        this.center = center;
        this.cityName = cityName;
    }

    private static List<Point> generateCityVertices(Point center, double wallLength) {
        List<Point> vertices = new ArrayList<>();
        double halfWall = wallLength / 2;
        vertices.add(new Point(center.x - halfWall, center.y - halfWall));
        vertices.add(new Point(center.x + halfWall, center.y - halfWall));
        vertices.add(new Point(center.x + halfWall, center.y + halfWall));
        vertices.add(new Point(center.x - halfWall, center.y + halfWall));
        return vertices;
    }

    public String getCityName() {
        return cityName;
    }
}
```

W klasie `City`:

- Dziedziczymy po klasie `Polygon`, co pozwala nam używać konstruktorów, metod i pól zdefiniowanych w klasie `Polygon`.
- Definiujemy publiczną ostateczną zmienną `center`, która przechowuje środek miasta.
- Tworzymy prywatne pole `cityName`, które przechowuje nazwę miasta.
- Implementujemy konstruktor `City`, który przyjmuje środek miasta (`center`), nazwę miasta (`cityName`) i długość ściany muru (`wallLength`). W konstruktorze generujemy wierzchołki kwadratu (muru) wokół środka miasta i przekazujemy je do konstruktora klasy `Polygon`.
- Tworzymy metodę statyczną `generateCityVertices`, która generuje wierzchołki kwadratu na podstawie środka miasta i długości ściany muru.
- Dodajemy getter `getCityName()`, aby umożliwić dostęp do nazwy miasta z zewnątrz klasy.

## Krok 7.
W klasie Land stwórz prywatną listę miast. Napisz metodę addCity(City), która doda miasto do tej
listy. Miasto może zostać dodane wyłącznie jeżeli jego środek znajduje się na lądzie. W przeciwnym
razie należy rzucić wyjątek RuntimeException, którego metoda getMessage() powinna wyświetlić
nazwę miasta.

Okej, bez użycia list, możemy wykorzystać klasę `ArrayList` do przechowywania miast w klasie `Land`, ale bez korzystania bezpośrednio z jej metod.

```java
import java.util.ArrayList;

public class Land extends Polygon {
    private City[] cities;
    private int cityCount;

    public Land(List<Point> points) {
        super(points);
        cities = new City[10]; // zakładamy maksymalnie 10 miast, możesz dostosować rozmiar według potrzeb
        cityCount = 0;
    }

    public void addCity(City city) {
        if (cityIsOnLand(city)) {
            if (cityCount < cities.length) {
                cities[cityCount] = city;
                cityCount++;
            } else {
                System.out.println("Brak miejsca na dodanie miasta: " + city.getCityName());
            }
        } else {
            throw new RuntimeException("Miasto " + city.getCityName() + " nie znajduje się na lądzie.");
        }
    }

    private boolean cityIsOnLand(City city) {
        return inside(city.center);
    }
}
```

W tej implementacji:

- `cities` jest tablicą miast.
- `cityCount` śledzi liczbę miast dodanych do tablicy.
- Metoda `addCity` dodaje miasto do tablicy `cities`, jeśli jest miejsce i jeśli jego środek znajduje się na lądzie. W przeciwnym razie rzuca wyjątek `RuntimeException`.
- Metoda `cityIsOnLand` sprawdza, czy środek miasta znajduje się na lądzie. Wykorzystuje metodę `inside`, która została już zaimplementowana w klasie `Polygon`.

## Krok 8.
Napisz test sprawdzający rzucanie i poprawność nazwy wyświetlanej przez getMessage().
Miasta portowe mają dostęp do wody.

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LandTest {
    @Test
    public void testAddCityException() {
        // Tworzenie lądu reprezentowanego przez wielokąt
        List<Point> landPoints = new ArrayList<>();
        landPoints.add(new Point(0, 0));
        landPoints.add(new Point(10, 0));
        landPoints.add(new Point(10, 10));
        landPoints.add(new Point(0, 10));
        Land land = new Land(landPoints);

        // Tworzenie miasta poza lądem
        Point cityCenter = new Point(15, 15);
        City city = new City(cityCenter, "City Outside Land", 5);

        // Sprawdzenie czy dodanie miasta spoza lądu powoduje rzucenie wyjątku
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            land.addCity(city);
        });

        // Sprawdzenie czy nazwa miasta w wyjątku jest poprawna
        assertEquals("Miasto City Outside Land nie znajduje się na lądzie.", exception.getMessage());
    }

    @Test
    public void testAddCityNoException() {
        // Tworzenie lądu reprezentowanego przez wielokąt
        List<Point> landPoints = new ArrayList<>();
        landPoints.add(new Point(0, 0));
        landPoints.add(new Point(10, 0));
        landPoints.add(new Point(10, 10));
        landPoints.add(new Point(0, 10));
        Land land = new Land(landPoints);

        // Tworzenie miasta na lądzie
        Point cityCenter = new Point(5, 5);
        City city = new City(cityCenter, "City On Land", 5);

        // Sprawdzenie czy dodanie miasta na lądzie nie powoduje rzucenia wyjątku
        assertDoesNotThrow(() -> {
            land.addCity(city);
        });
    }
}


## Krok 9.
W klasie City dodaj prywatne pole logiczne port określające, czy miasto jest portowe. Miasto jest
miastem portowym, jeżeli co najmniej jeden z wierzchołków kwadratu stanowiącego mury miasta
znajduje się poza lądem.
Pole to powinna ustawiać metoda znająca zarówno obiekt City, jak i obiekt Land, na którym istnieje
lub do którego dodawane jest miasto. Samodzielnie wybierz odpowiednie położenie definicji
i wywołania oraz symbol tej metody.
Lądy i morza są pełne zasobów.

Aby dodać pole `port` do klasy `City` oraz metodę ustawiającą to pole w oparciu o informacje o położeniu miasta na lądzie, możemy zmodyfikować klasę `City` w następujący sposób:

```java
public class City extends Polygon {
    private final Point center;
    private final String cityName;
    private boolean port;

    public City(Point center, String cityName, double wallLength) {
        super(generateCityVertices(center, wallLength));
        this.center = center;
        this.cityName = cityName;
        this.port = false;
    }

    // Metoda ustawiająca pole port na true, jeśli przynajmniej jeden wierzchołek miasta znajduje się poza lądem
    public void checkIfPort(Land land) {
        if (land != null) {
            for (Point vertex : getVertices()) {
                if (!land.inside(vertex)) {
                    port = true;
                    return;
                }
            }
        }
    }

    public boolean isPort() {
        return port;
    }

    // Pozostała część klasy City...
}
```

W tej zmodyfikowanej wersji klasy `City`:

- Dodajemy pole `port`, które określa, czy miasto jest portowe.
- W konstruktorze ustawiamy początkową wartość pola `port` na `false`.
- Dodajemy metodę `checkIfPort`, która sprawdza, czy przynajmniej jeden z wierzchołków miasta znajduje się poza lądem. Jeśli tak, ustawia pole `port` na `true`. Ta metoda może być wywoływana z klasy `Land`, przekazując do niej obiekt `Land`, na którym istnieje lub do którego dodawane jest miasto.
- Dodajemy getter `isPort`, aby umożliwić sprawdzenie, czy miasto jest portowe z zewnątrz klasy.

## Krok 10.
Zdefiniuj klasę Resource. W klasie Resource zdefiniuj publiczny typ wyliczeniowy Type {Coal, Wood,
Fish}. Klasa Resource powinna posiadać dwa publiczne, ostateczne pola: punkt (Point) określający
rozmieszczenie pozycję zasobu na mapie oraz typ (Type) zasobu. Napisz konstruktor ustawiający te
pola.

Oto jak możesz zdefiniować klasę `Resource` zgodnie z opisem w "Krok 10":

```java
public class Resource {
    public enum Type {Coal, Wood, Fish}

    public final Point position;
    public final Type type;

    public Resource(Point position, Type type) {
        this.position = position;
        this.type = type;
    }
}
```

W tej klasie:

- Zdefiniowano typ wyliczeniowy `Type`, który reprezentuje rodzaje zasobów (`Coal`, `Wood`, `Fish`).
- Klasa `Resource` posiada dwa publiczne, ostateczne pola:
  - `position`: punkt (`Point`) określający rozmieszczenie pozycję zasobu na mapie.
  - `type`: typ (`Type`) zasobu.
- Zdefiniowano konstruktor, który przyjmuje punkt i typ zasobu i ustawia odpowiednie pola.

## Krok 11.
Załóżmy bez sprawdzenia, że węgiel i drewno znajdują się na lądzie, a ryby w wodzie.
W klasie City utwórz zbiór (Set) obiektów Resource.Type o nazwie resources i dostępie pakietowym.
W tej samej klasie napisz metodę addResourcesInRange, która przyjmie listę obiektów Resource
oraz liczbę zmiennoprzecinkową range i umieści w zbiorze typy tych zasobów, które znajdują się
w odległości nie większej niż range od środka miasta. Ryby powinny być uwzględniane wyłącznie
w miastach portowych.

Okej, w klasie `City` możemy dodać zbiór `resources` typów zasobów (`Resource.Type`) oraz metodę `addResourcesInRange`, która będzie dodawać odpowiednie typy zasobów do tego zbioru w zależności od odległości od środka miasta oraz typu miasta. Poniżej przedstawiam implementację tego kroku:

```java
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class City extends Polygon {
    private final Point center;
    private final String cityName;
    private boolean port;
    Set<Resource.Type> resources;

    public City(Point center, String cityName, double wallLength) {
        super(generateCityVertices(center, wallLength));
        this.center = center;
        this.cityName = cityName;
        this.port = false;
        this.resources = new HashSet<>();
    }

    // Metoda ustawiająca pole port na true, jeśli przynajmniej jeden wierzchołek miasta znajduje się poza lądem
    public void checkIfPort(Land land) {
        if (land != null) {
            for (Point vertex : getVertices()) {
                if (!land.inside(vertex)) {
                    port = true;
                    return;
                }
            }
        }
    }

    public boolean isPort() {
        return port;
    }

    // Metoda dodająca zasoby w zasięgu do zbioru resources
    void addResourcesInRange(List<Resource> allResources, double range) {
        for (Resource resource : allResources) {
            if (resource.position.distanceTo(center) <= range) {
                if (resource.type == Resource.Type.Fish && !port) {
                    continue; // Jeśli zasób to ryby i miasto nie jest portowe, pomijamy
                }
                resources.add(resource.type);
            }
        }
    }

    // Pozostała część klasy City...
}
```

W tej implementacji:

- Dodajemy zbiór `resources` typów zasobów.
- Metoda `addResourcesInRange` iteruje przez wszystkie zasoby przekazane jako listę oraz sprawdza, czy są one w zasięgu odległości `range` od środka miasta. Jeśli zasób jest w zasięgu, a jeśli miasto nie jest portem lub zasób nie jest rybą, dodajemy jego typ do zbioru `resources`.

## Krok 12.
Napisz klasę CityTest zawierającą sparametryzowany test zawierający:
- poprawne dodanie węgla,
- nieudaną próbę dodania drewna spoza zasięgu miasta,
- poprawne dodanie ryb do miasta portowego,
- nieudaną próbę dodania ryb do miasta nieportowego.
Test będzie wymagał definicji miasta śródlądowego i morskiego, co można wykonać na wiele
sposobów, np. przez ustawienie ich jako pola klasy.
Test powinien przyjmować argumenty: miasto, zasób, oczekiwana wartość logiczna.
Sposób parametryzacji testu jest dowolny. Test, przy użyciu pakietowego dostępu do pola
City.resources powinien sprawdzać, czy testowany zasób został dodany do zbioru.
Dany jest plik map.svg zawierający przykładową mapę. Mapa zawiera niebieski prostokąt
reprezentujący wodę, który można pominąć w dalszych rozważaniach oraz za pomocą znaczników:
- polygon o kolorze zielonym - ląd,
- rect o kolorze czerwonym - miasto,
- circle o kolorze czarnym, brązowym, jasnoniebieskim - zasoby, odpowiednio węgiel, drewno,
ryby.
- text - nazwy miast.
Symbole zasobów nie będą rozważane w dalszej części zadania.

## Krok 13.
Zapoznaj się z dołączoną klasą MapParser służącą do parsowania tego pliku. Klasa wykorzystuje
bibliotekę Jackson Dataformat XML. Dołącz ją do projektu. W klasie MapParser znajduje się lista
obiektów typu Label zawierającego reprezentację znaczników <text>. Metody parse i parseText
prezentują proces konwersji zawartości znacznika na obiekt.
W klasie MapParser stwórz analogiczne do istniejącej listy nazw, nowe listy lądów i miast i zapełnij je
wzorując się na istniejącym kodzie. Napisz publiczny akcesor do listy lądów.
Uwaga! Podczas wczytywania miasta jego nazwa nie jest znana. Można ją tymczasowo ustawić jako
null. Wartość x i y w znaczniku rect definiuje lewy, górny wierzchołek prostokąta. Pamiętaj
o obliczeniu pozycji środka miasta.

Aby przetestować klasę `City`, możemy utworzyć klasę `CityTest` zawierającą testy opisane w "Kroku 12". Poniżej przedstawiam implementację tej klasy testowej:

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CityTest {

    private static Stream

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CityTest {
    private City inlandCity;
    private City portCity;

    @BeforeEach
    void setUp() {
        // Utworzenie miasta śródlądowego
        inlandCity = new City(new Point(100, 100), "Inland City", 20);

        // Utworzenie miasta portowego
        portCity = new City(new Point(200, 200), "Port City", 20);
        portCity.port = true;
    }

    @ParameterizedTest
    @MethodSource("provideResourcesForCityTest")
    void testAddResourcesInRange(City city, List<Resource> resources, boolean expected) {
        city.addResourcesInRange(resources, 30); // Zakładając zasięg 30 jednostek
        Set<Resource.Type> actualResources = city.resources;
        assertEquals(expected, actualResources.contains(Resource.Type.Coal));
    }

    private static Stream

```java
 provideResourcesForCityTest() {
        // Utworzenie zasobów: węgla, drewna, ryb
        Resource coal = new Resource(new Point(90, 90), Resource.Type.Coal);
        Resource wood = new Resource(new Point(300, 300), Resource.Type.Wood);
        Resource fish = new Resource(new Point(220, 220), Resource.Type.Fish);

        return Stream.of(
                // Test poprawnego dodania węgla
                Arguments.of(inlandCity, List.of(coal), true),
                // Test nieudanej próby dodania drewna spoza zasięgu miasta
                Arguments.of(inlandCity, List.of(wood), false),
                // Test poprawnego dodania ryb do miasta portowego
                Arguments.of(portCity, List.of(fish), true),
                // Test nieudanej próby dodania ryb do miasta nieportowego
                Arguments.of(inlandCity, List.of(fish), false)
        );
    }
}
```

W tej klasie testowej:

- Metoda `setUp` jest oznaczona adnotacją `@BeforeEach` i inicjuje dwa miasta: jedno śródlądowe (`inlandCity`) i jedno portowe (`portCity`).
- Metoda `provideResourcesForCityTest` dostarcza dane testowe dla parametryzowanego testu `testAddResourcesInRange`. Zwraca strumień argumentów testowych, gdzie każdy argument zawiera miasto, listę zasobów i oczekiwaną wartość logiczną. Dla każdego testu tworzone są zasoby węgla, drewna i ryb, a następnie zwracane są argumenty testowe dla różnych przypadków.
- Test `testAddResourcesInRange` sprawdza, czy dodane zasoby znajdują się w zbiorze `resources` miasta. Dla każdego testu pobierane są rzeczywiste zasoby z miasta i sprawdzane jest, czy zbiór zawiera zasób węgla, zgodnie z oczekiwaną wartością logiczną.

## Krok 14.
W klasie MapParser zaimplementuj metodę addCitiesToLands. Metoda powinna pracować na liście
lądów i miast w obiekcie klasy MapParser. Dla każdego lądu, dodać do jego prywatnej listy miast,
miasta, których środki znajdują się na tym lądzie.

Oto implementacja metody `addCitiesToLands` w klasie `MapParser`:

```java
void addCitiesToLands() {
    for (Land land : lands) {
        for (City city : cities) {
            if (land.inside(city.center)) {
                land.addCity(city);
            }
        }
    }
}
```

Ta metoda iteruje przez listę lądów (`lands`) i dla każdego lądu sprawdza, czy środek każdego miasta znajduje się na tym lądzie za pomocą metody `inside` klasy `Land`. Jeśli tak, dodaje to miasto do prywatnej listy miast lądu za pomocą metody `addCity`.

## Krok 15.
W klasie MapParser zaimplementuj metodę matchLabelsToTowns. Metoda powinna dla każdego
miasta w liście miast przyporządkować najbliższy obiekt Label. Konieczny będzie publiczny mutator
nazwy miasta w klasie City.

Oto implementacja metody `matchLabelsToTowns` w klasie `MapParser`:

```java
void matchLabelsToTowns() {
    for (City city : cities) {
        Label closestLabel = null;
        double closestDistance = Double.MAX_VALUE;

        for (Label label : labels) {
            double distance = calculateDistance(city.center, label.point);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestLabel = label;
            }
        }

        if (closestLabel != null) {
            city.setName(closestLabel.text);
        }
    }
}

private double calculateDistance(Point p1, Point p2) {
    double dx = p1.getX() - p2.getX();
    double dy = p1.getY() - p2.getY();
    return Math.sqrt(dx * dx + dy * dy);
}
```

Ta metoda iteruje przez listę miast (`cities`). Dla każdego miasta znajduje najbliższy obiekt etykiety (`Label`) iterując przez listę etykiet (`labels`). Oblicza odległość między środkiem miasta a punktem etykiety za pomocą metody `calculateDistance`. Następnie, jeśli znajdzie najbliższą etykietę, przypisuje jej nazwę do miasta za pomocą publicznego mutatora `setName`.

## Krok 16.
W klasie Main utwórz obiekt MapParser i wywołaj jego metodę parse dla dołączonej w pliku svg
mapy. W klasie City napisz metodę toString, która wyświetli nazwę miasta oraz symbol⚓, jeżeli jest
to miasto portowe. W klasie Land napisz metodę toString, która wywoła metody toString wszystkich
miast na tym lądzie i zwróci ich wynik w postaci napisu połączonego przecinkami.
Wynik działania takiej funkcji, dla wszystkich lądów z mapy będzie wyglądał następująco:
Kryształowiec, Złoty Horyzont⚓
Słoneczna Przystań⚓, Srebrzysko, Kamienna Brama, Księżycowe Miasto, Prześwitnia, Pustelnia⚓
Mglisty Port⚓, Złote Wrzosowiska, Morska Ostoja⚓, Diamentowa Wieża, Różany Las
Martwe Ogrody, Czarne Mokradła, Smoczy Kraniec⚓
Burzowe Wybrzeże⚓, Żelazna Oaza, Granitowe Wzgórza
Centra miast Wodny Kamień i Zatoka Nadziei zostały umieszczone na wodzie i dlatego nie zostały
dodane do żadnego z lądów.

Oto implementacja metody `toString` w klasach `City` i `Land`, oraz utworzenie obiektu `MapParser` i wywołanie metody `parse` w klasie `Main`:

```java
// Metoda toString w klasie City
@Override
public String toString() {
    StringBuilder builder = new StringBuilder(name);
    if (port) {
        builder.append("⚓");
    }
    return builder.toString();
}

// Metoda toString w klasie Land
@Override
public String toString() {
    StringBuilder builder = new StringBuilder();
    for (City city : cities) {
        builder.append(city.toString()).append(", ");
    }
    if (builder.length() > 0) {
        builder.setLength(builder.length() - 2); // Usunięcie ostatniego przecinka i spacji
    }
    return builder.toString();
}

// Utworzenie obiektu MapParser i wywołanie metody parse w klasie Main
public class Main {
    public static void main(String[] args) {
        MapParser mapParser = new MapParser();
        mapParser.parse("map.svg");

        // Wyświetlenie wyniku dla wszystkich lądów
        for (Land land : mapParser.getLands()) {
            System.out.println(land.toString());
        }
    }
}
```

W metodzie `toString` klasy `City` dodano wyświetlanie symbolu ankeru (⚓) dla miasta portowego. W metodzie `toString` klasy `Land` zwracany jest napis z nazwami wszystkich miast na danym lądzie, z opcjonalnym symbolem ankeru dla miast portowych. W klasie `Main` tworzony jest obiekt `MapParser` i wywoływana jest jego metoda `parse` z plikiem svg. Następnie wyświetlane są wszystkie lądy z mapy wraz z ich miastami.