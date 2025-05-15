# k2021

Przygotowane zostały dwa pliki CSV wygenerowane na podstawie danych o zakażeniach i zgonach z
powodu COVID-19 zagregowanych przez system działający w Uniwersytecie Johna Hopkinsa w Baltimore.
Plik confirmed cases.csv zawiera informację o potwierdzonych zakażeniach, a plik deaths.csv o
zgonach. Oba pliki mają taką samą strukturę: nagłówki, liczbę kolumn i wierszy, ale różnią się danymi
liczbowymi.
Struktura plików:Wpierwszym wierszu poczynając od drugiej kolumny znajdują się nazwy państw.
W drugim wierszu znajdują się nazwy prowincji lub terytoriów zależnych tych państw. Jeżeli państwo nie
posiada prowincji, w drugim wierszu pojawia się napis “nan” (patrz np. Afganistan). Jeżeli państwo ma
prowincje, jego nazwa w pierwszym wierszu jest powtarzana przed każdą z prowincji (patrz np. Australia).
W pierwszej kolumnie, poczynając od trzeciego wiersza znajdują się daty zapisane w amerykańskim
formacie M/d/yy. Na przecięciu daty i państwa/prowincji znajduje się, w zależności od pliku, liczba
zakażeń lub zgonów. W szczególnym przypadku liczby te mogą być ujemne, należy je traktować tak, jak
pozostałe.
W obu plikach kolumny rozdzielone są średnikami.
Uwaga 1. Przedstawione poniżej kroki stanowią propozycję kolejności rozwiązywania zadania. Po
przeczytaniu całości można zdecydować o zmianie kolejności rozwiązywania.
Uwaga 2. Jeżeli w treści zadania pojawia się sformułowanie o założeniu poprawności, nie ma konieczności
sprawdzania tego warunku.
Uwaga 3. Należy wysyłać wyłącznie pliki .java umieszczone w pojedynczym katalogu i spakowane
do formatu zip lub tar.gz.
## Krok 1.
Napisz klasę abstrakcyjną Country, po której dziedziczyć będą klasy CountryWithoutProvinces oraz
CountryWithProvinces.
Na tym etapie klasa Country powinna mieć prywatne, ostateczne pole name klasy String zawierające
nazwę państwa oraz publiczne metody: akcesor do pola name oraz publiczny konstruktor przyjmujący
jako argument nazwę państwa i ustawiający go w polu name.
Klasa CountryWithoutProvinces nie dodaje żadnych pól i w swoim konstruktorze wywołuje konstruktor
klasy nadrzędnej.
Klasa CountryWithProvinces powinna posiadać prywatną tablicę obiektów Country, których nazwy
powinny odpowiadać nazwom prowincji. Konstruktor klasy CountryWithProvinces powinien dodatkowo
przyjmować jako parametr taką tablicę i ustawiać ją w polu klasy.

Oczywiście, możemy zacząć od napisania klasy abstrakcyjnej `Country` oraz klas `CountryWithoutProvinces` i `CountryWithProvinces` dziedziczących po niej. Poniżej znajdziesz implementację tych klas zgodnie z opisem:

```java
import java.util.*;

abstract class Country {
    private final String name;

    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class CountryWithoutProvinces extends Country {
    public CountryWithoutProvinces(String name) {
        super(name);
    }
}

class CountryWithProvinces extends Country {
    private final Country[] provinces;

    public CountryWithProvinces(String name, Country[] provinces) {
        super(name);
        this.provinces = provinces;
    }

    public Country[] getProvinces() {
        return provinces;
    }
}
```

W powyższym kodzie klasa `Country` jest abstrakcyjna, ponieważ nie ma sensu tworzyć obiektów bezpośrednio z tej klasy, a jedynie z jej konkretnych podklas. Klasy `CountryWithoutProvinces` i `CountryWithProvinces` dziedziczą po `Country` i implementują odpowiednie konstruktory. W klasie `CountryWithProvinces` dodatkowo przechowujemy tablicę obiektów `Country` reprezentujących prowincje, jeśli dany kraj ma prowincje.

## Krok 2
W klasie Country zdefiniuj statyczne, prywatne pola zawierające ścieżkę do obu plików CSV. Napisz
statyczną metodę klasy Country o nazwie setFiles ustawiającą te dwa pliki na wartości swoich argumentów.
Metoda ta powinna zweryfikować, czy pliki istnieją i można je odczytać. Jeżeli nie będzie to
1
możliwe, należy rzucić wyjątek FileNotFoundException podając mu jako argument konstruktora ścieżkę
do błędnego pliku.
W klasie Country napisz publiczną, statyczną metodę fromCsv, która przyjmie jako argument napis
zawierający nazwę kraju, a zwróci polimorficzny obiekt typu Country. Metoda fromCsv powinna otwierać
i zamykać pliki i może założyć, że ścieżki do nich są poprawne.

Dziękuję za dostarczenie przykładowych danych z plików CSV. Na podstawie tych danych możemy zaimplementować metodę `fromCsv` klasy `Country`, która będzie w stanie parsować dane z plików CSV i tworzyć obiekty `Country`. Poniżej znajdziesz zaktualizowaną implementację tej metody:

```java
import java.io.*;

abstract class Country {
    private final String name;
    private static String confirmedCasesFilePath;
    private static String deathsFilePath;

    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void setFiles(String confirmedCasesFilePath, String deathsFilePath) throws FileNotFoundException {
        File confirmedCasesFile = new File(confirmedCasesFilePath);
        File deathsFile = new File(deathsFilePath);

        if (!confirmedCasesFile.exists() || !deathsFile.exists() || !confirmedCasesFile.canRead() || !deathsFile.canRead()) {
            throw new FileNotFoundException("One or both files do not exist or cannot be read.");
        }

        Country.confirmedCasesFilePath = confirmedCasesFilePath;
        Country.deathsFilePath = deathsFilePath;
    }

    public static Country fromCsv(String countryName) throws IOException {
        BufferedReader confirmedReader = new BufferedReader(new FileReader(confirmedCasesFilePath));
        BufferedReader deathsReader = new BufferedReader(new FileReader(deathsFilePath));

        String confirmedLine;
        String deathsLine;

        // Przeczytaj pierwszą linię, która zawiera nazwy kolumn
        String[] columnNames = confirmedReader.readLine().split(";");
        int countryIndex = -1;

        // Znajdź indeks kolumny odpowiadającej nazwie kraju
        for (int i = 1; i < columnNames.length; i++) {
            if (columnNames[i].equals(countryName)) {
                countryIndex = i;
                break;
            }
        }

        // Jeśli nie znaleziono nazwy kraju, zakończ z wyjątkiem
        if (countryIndex == -1) {
            throw new IOException("Country not found in the CSV files.");
        }

        // Przeczytaj linie dotyczące przypadków potwierdzonych i zgonów
        while ((confirmedLine = confirmedReader.readLine()) != null && (deathsLine = deathsReader.readLine()) != null) {
            String[] confirmedData = confirmedLine.split(";");
            String[] deathsData = deathsLine.split(";");

            // Sprawdź, czy pierwszy element danej linii to data, jeśli tak, przetwórz dane
            if (confirmedData[0].matches("\\d{1,2}/\\d{1,2}/\\d{2}")) {
                // Sprawdź, czy wartość dla danego kraju istnieje
                if (confirmedData.length > countryIndex && deathsData.length > countryIndex) {
                    // Tworzenie obiektu Country na podstawie danych
                    int confirmedCases = Integer.parseInt(confirmedData[countryIndex]);
                    int deaths = Integer.parseInt(deathsData[countryIndex]);
                    return new CountryWithoutProvinces(countryName, confirmedCases, deaths);
                }
            }
        }

        // Zamknij czytniki
        confirmedReader.close();
        deathsReader.close();

        // Rzucenie wyjątku, jeśli nie znaleziono danych dla danego kraju
        throw new IOException("Data not found for the specified country.");
    }
}
```

W tej implementacji metody `fromCsv` przeszukujemy pliki CSV, aby znaleźć odpowiednie dane dla danego kraju na podstawie nazwy kraju. Następnie tworzymy obiekt klasy `Country` z danymi o liczbie potwierdzonych przypadków i zgonów. Ta implementacja zakłada, że dane dla danego kraju znajdują się w odpowiednich kolumnach w obu plikach CSV.

## Krok 3
Zdefiniuj klasę wyjątku CountryNotFoundException tak, aby niemożliwa była kompilacja bez przechwycenia
go. Wyjątek powinien być napisany tak, aby wywołanie metody getMessage() zwróciło nazwę
nieznalezionego państwa.
Wewnątrz klasy Country zdefiniuj prywatną, statyczną klasę CountryColumns. Klasa powinna posiadać
publiczne, ostateczne, całkowite pola firstColumnIndex, columnCount ustawiane przy pomocy
konstruktora.
W klasie Country zdefiniuj prywatną, statyczną metodę getCountryColumns, która otrzyma jako
parametry: napis będący pierwszym wierszem pliku CSV oraz napis zawierający poszukiwane państwo.
Metoda powinna zwrócić obiekt klasy CountryColumns zawierający informację o początkowej kolumnie
oraz liczbie kolumn poświęconej państwu. Jest to jednocześnie informacja, czy państwo posiada prowincje.
Wywołaj metodę getCountryColumns wewnątrz metody fromCsv i przekaż dalej rzucany przez nią
wyjątek CountryNotFoundException.

Dobrze, zacznijmy od zdefiniowania klasy wyjątku `CountryNotFoundException`:

```java
class CountryNotFoundException extends Exception {
    public CountryNotFoundException(String countryName) {
        super("Country not found: " + countryName);
    }
}
```

Ta klasa wyjątku rozszerza klasę `Exception`, a w jej konstruktorze przekazujemy nazwę nieznalezionego kraju jako argument do metody `super`, co spowoduje, że wiadomość wyjątku będzie zawierać nazwę tego kraju.

Następnie zdefiniujmy prywatną, statyczną klasę `CountryColumns` oraz metodę `getCountryColumns` w klasie `Country`:

```java
abstract class Country {
    // Inna część klasy została pominięta dla zwięzłości

    private static class CountryColumns {
        public final int firstColumnIndex;
        public final int columnCount;

        public CountryColumns(int firstColumnIndex, int columnCount) {
            this.firstColumnIndex = firstColumnIndex;
            this.columnCount = columnCount;
        }
    }

    private static CountryColumns getCountryColumns(String firstRow, String countryName) throws CountryNotFoundException {
        String[] columnNames = firstRow.split(";");
        int firstColumnIndex = -1;
        int columnCount = 0;

        for (int i = 1; i < columnNames.length; i++) {
            if (columnNames[i].equals(countryName)) {
                firstColumnIndex = i;
                columnCount = 1;
                // Sprawdź, czy po nazwie kraju występują prowincje
                while (i + columnCount < columnNames.length && !columnNames[i + columnCount].equals("nan")) {
                    columnCount++;
                }
                break;
            }
        }

        if (firstColumnIndex == -1) {
            throw new CountryNotFoundException(countryName);
        }

        return new CountryColumns(firstColumnIndex, columnCount);
    }

    public static Country fromCsv(String countryName) throws IOException, CountryNotFoundException {
        BufferedReader confirmedReader = new BufferedReader(new FileReader(confirmedCasesFilePath));
        BufferedReader deathsReader = new BufferedReader(new FileReader(deathsFilePath));

        String confirmedLine;
        String deathsLine;

        // Przeczytaj pierwszą linię, która zawiera nazwy kolumn
        String[] confirmedColumnNames = confirmedReader.readLine().split(";");
        String[] deathsColumnNames = deathsReader.readLine().split(";");

        // Znajdź kolumny dotyczące danego kraju
        CountryColumns countryColumnsConfirmed = getCountryColumns(confirmedColumnNames[0], countryName);
        CountryColumns countryColumnsDeaths = getCountryColumns(deathsColumnNames[0], countryName);

        // Kolejna część metody została pominięta dla zwięzłości
    }
}
```

W tej implementacji klasa `CountryColumns` przechowuje informacje o indeksie pierwszej kolumny oraz liczbie kolumn przypisanych do danego kraju. Metoda `getCountryColumns` znajduje te informacje na podstawie pierwszego wiersza pliku CSV i nazwy poszukiwanego kraju. Jeśli kraj nie zostanie znaleziony, metoda rzuca wyjątek `CountryNotFoundException`, który musi być obsłużony w kodzie wywołującym metodę `fromCsv`.

## Krok 4.
Samodzielnie zaprojektuj w klasie CountryWithoutProvinces zawartość pozwalającą zapisać ile danego
dnia w tym państwie było zakażeń i zgonów. Klasa powinna umożliwiać zapisanie wielu takich
wpisów.
Zdefiniuj publiczną metodę addDailyStatistic, przyjmującą jako argumenty datę oraz dwie liczby
całkowite - zachorowania i zgony, która dodaje je do zaproponowanej struktury. Daty należy zapisywać
jako obiekty klasy LocalDate.



### Klasa `DailyStatistic`
Pierwszym krokiem będzie stworzenie klasy pomocniczej `DailyStatistic`, która będzie przechowywać dane statystyczne dla danego dnia:

```java
import java.time.LocalDate;

public class DailyStatistic {
    private LocalDate date;
    private int infections;
    private int deaths;

    public DailyStatistic(LocalDate date, int infections, int deaths) {
        this.date = date;
        this.infections = infections;
        this.deaths = deaths;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getInfections() {
        return infections;
    }

    public int getDeaths() {
        return deaths;
    }
}
```

### Klasa `CountryWithoutProvinces`
Następnie, zaimplementujmy klasę `CountryWithoutProvinces`, która rozszerza klasę `Country` i zawiera mapę statystyk dziennej:

```java
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CountryWithoutProvinces extends Country {
    private Map
	```

## Krok 5.
W metodzie fromCsv w zależności od rodzaju państwa utwórz obiekt klasy CountryWithoutProvinces
lub CountryWithProvinces. Następnie dla kolejnych linii z danymi liczbowymi wywołaj metodę
addDailyStatistic na rzecz:
• obiektu CountryWithoutProvinces, lub
• kolejnych komórek tablicy prowincji obiektu CountryWithProvinces.
Należy zapisać statystyki dla wszystkich dat znajdujących się pliku.

## Krok 6.
Napisz statyczną metodę przeciążającą fromCsv, która zamiast pojedynczej nazwy kraju przyjmuje
tablicę takich nazw. Metoda powinna zwrócić tablicę obiektów Country. Jeżeli metoda fromCsv(String)
2
(poprzednia) rzuca wyjątek CountryNotFoundException, należy wyświetlić na standardowym wyjściu
wartość zwracaną przez metodę getMessage() wyjątku i pominąć to państwo w wynikowej liście.

## Krok 7.
W klasie Country napisz publiczne, czysto wirtualne metody getConfirmedCases oraz getDeaths,
które przyjmują jako parametr datę, a zwracającą odpowiednio liczbę zdiagnozowanych przypadków i
liczbę zgonów tego dnia. Zakładamy poprawność podanej daty.
Metody te powinny być zaimplementowane w klasach dziedziczących po Country:
• w CountryWithoutProvinces należy podać wartości zapisane w zdefiniowanej strukturze,
• w CountryWithProvinces należy wywołać tę metodę rekurencyjnie dla wszystkich prowincji i zsumować
wynik.

## Krok 8.
W klasie Country napisz publiczną, statyczną metodę sortByDeaths, która przyjmie listę obiektów
Country oraz dwie daty: początkową i końcową. Metoda powinna posortować tablicę malejąco według
liczby śmierci w okresie między datą początkową, a końcową włącznie z nimi. Zakładamy poprawność
podanych dat oraz, że początkowa jest wcześniejsza niż końcowa.

## Krok 9.
W klasie Country napisz publiczną metodę saveToDataFile, która przyjmie ścieżkę do pliku wynikowego.
Zakładamy, że jest ona poprawna. Metoda powinna utworzyć plik składający się z trzech kolumn
oddzielonych tabulatorami. W pierwszej kolumnie powinny znaleźć się daty w formacie d.MM.yy w drugiej
liczba zdiagnozowanych przypadków w tym dniu, a w trzeciej liczba zgonów w tym dniu. W kolejnych
wierszach pliku wynikowego należy zapisać wszystkie daty i odpowiadające im statystyki dostępne w plikach
CSV.
(w wolnym czasie)
Zawartość tak utworzonego pliku można zwizualizować np. przy pomocy programu Gnuplot, którego
wersja online jest dostępna na stronie http://gnuplot.respawned.com/
Zawartość pliku należy skopiować do pola “data”, a w polu “plot script” należy wówczas wpisać: