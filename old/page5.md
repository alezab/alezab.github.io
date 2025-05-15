# k2022

W pliku data.zip dane są dwa katalogi: food oraz nonfood zawierające pliki CSV przedstawiające
zmiany cen różnych produktów do stycznia 2010 do marca 2022 roku. Pliki zostały stworzone na
podstawie danych dostępnych na stronach Głównego Urzędu Statystycznego. Każdy z plików w
pierwszej linii zawiera nazwę produktu lub usługi. W drugiej linii znajduje się nagłówek tabeli.
Nagłówek tabeli oraz jej wiersze różnią się od siebie w zależności od rodzaju pliku. Pliki z
katalogu nonfood od pierwszej kolumny posiadają cenę produktu w danym miesiącu. Ceny
produktów żywnościowych są rozróżniane ze względu na województwo. Pliki z katalogu food w
pierwszej kolumnie posiadają województwo, a od drugiej kolumny pojawiają się ceny produktu w
danym miesiącu.
Uwaga 1.
Przedstawione poniżej kroki stanowią propozycję kolejności rozwiązywania zadania. Po
przeczytaniu całości można zdecydować o rozwiązywaniu w innej kolejności.
Uwaga 2.
Należy wysyłać wyłącznie pliki .java bez ich uprzedniego spakowania.
Uwaga 3.
Należy założyć, że pliki z danymi będą miały postać taką, jak powyżej opisana. Nie ma potrzeby
sprawdzać ich poprawności.
Uwaga 4.
Należy założyć, że zapisy w plikach zaczynają się od stycznia 2010 roku, a każda kolejna
kolumna dotyczy kolejnego miesiąca - nie trzeba parsować nagłówka tabeli.
### Krok 0.
W pliku NonFoodProduct.java dana jest klasa NonFoodProduct, posiadająca prywatny
konstruktor oraz dwa prywatne pola:
● String name - zawierający nazwę produktu,
● Double[] prices - tablica zawierająca ceny w kolejnych miesiącach od 01.2010.
W klasie zaimplementowany jest publiczny akcesor do pola name oraz metoda fromCsv, która
odczytuje zawartość pliku CSV do takiej struktury. Zaznajom się z jej kodem.



NonFoodProduct.java:
```java
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class NonFoodProduct {
    String name;
    Double[] prices;

    private NonFoodProduct(String name, Double[] prices) {
        this.name = name;
        this.prices = prices;
    }

    public static NonFoodProduct fromCsv(Path path) {
        String name;
        Double[] prices;

        try {
            Scanner scanner = new Scanner(path);
            name = scanner.nextLine(); // odczytuję pierwszą linię i zapisuję ją jako nazwa
            scanner.nextLine();  // pomijam drugą linię z nagłówkiem tabeli
            prices = Arrays.stream(scanner.nextLine().split(";")) // odczytuję kolejną linię i dzielę ją na tablicę
                    .map(value -> value.replace(",",".")) // zamieniam polski znak ułamka dziesiętnego - przecinek na kropkę
                    .map(Double::valueOf) // konwertuję string na double
                    .toArray(Double[]::new); // dodaję je do nowo utworzonej tablicy

            scanner.close();

            return new NonFoodProduct(name, prices);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

Analizując kod klasy `NonFoodProduct` w Java, możemy zobaczyć, że zapewnia ona sposób na tworzenie obiektów na podstawie danych z plików CSV, które nie zawierają informacji o produktach spożywczych. Kluczowymi elementami klasy są:

1. **Pola klasy**:
   - `String name` - przechowuje nazwę produktu.
   - `Double[] prices` - tablica przechowująca ceny produktu w kolejnych miesiącach, zaczynając od stycznia 2010 roku.

2. **Konstruktor**:
   - Konstruktor jest prywatny, co sugeruje, że obiekty klasy są tworzone wyłącznie przez metodę statyczną `fromCsv`.

3. **Metoda `fromCsv(Path path)`**:
   - Ta metoda statyczna służy do tworzenia obiektu `NonFoodProduct` z pliku CSV. Oto jej kluczowe działania:
     - Użycie `Scanner` do czytania pliku wskazanego przez ścieżkę `path`.
     - Pierwsza linia pliku jest czytana jako nazwa produktu (`name`).
     - Druga linia, która zawiera nagłówki, jest pomijana.
     - Trzecia linia jest przetwarzana na ceny; funkcja dzieli linię na poszczególne wartości rozdzielane średnikiem, zamienia przecinki na kropki (zamiana formatu liczby), a następnie konwertuje każdą wartość na typ `Double` i zapisuje je w tablicy `prices`.
     - Na końcu, jeśli wszystko przebiegnie pomyślnie, tworzony jest nowy obiekt `NonFoodProduct` z użyciem wczytanych wartości.

4. **Obsługa wyjątków**:
   - Jeśli wystąpi błąd I/O podczas czytania pliku, metoda rzuca wyjątek `RuntimeException` z załączonym oryginalnym wyjątkiem `IOException`.

Podsumowując, klasa `NonFoodProduct` jest skutecznym narzędziem do przetwarzania danych o produktach nieżywnościowych, umożliwiając łatwe wczytywanie i dostęp do informacji o nazwie produktu oraz jego cenach w różnych miesiącach.


### Krok 1.
Napisz klasę abstrakcyjną Product. Niech po tej klasie dziedziczy istniejąca klasa
NonFoodProduct. Przenieś do niej pole name pozostawiając je prywatnym. Przenieś także
akcesor. W klasie Product zdefiniuj publiczną, abstrakcyjną metodę:
double getPrice(int year, int month).
Metoda powinna zwracać cenę produktu w danym roku i miesiącu, a jeżeli dane nie mieszczą się
w zakresie 01.2010 - 03.2022, lub gdy miesiąc będzie spoza zakresu 1-12 rzucić wyjątek
IndexOutOfBoundsException. Nadpisz i zaimplementuj tę metodę w klasie NonFoodProduct.
Przetestuj jej działanie w metodzie Main::main.

Zadanie wymaga stworzenia abstrakcyjnej klasy bazowej `Product`, a następnie rozszerzenia klasy `NonFoodProduct` tak, aby dziedziczyła po tej nowej klasie abstrakcyjnej. Oto kroki, które należy podjąć:

### Krok 1: Utworzenie klasy abstrakcyjnej `Product`

Klasa `Product` będzie zawierać pole `name`, prywatny akcesor do tego pola oraz abstrakcyjną metodę `getPrice(int year, int month)`, która pozwala pobrać cenę produktu na podstawie roku i miesiąca.

```java
public abstract class Product {
    private String name;

    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract double getPrice(int year, int month) throws IndexOutOfBoundsException;
}
```

### Krok 2: Modyfikacja klasy `NonFoodProduct` do dziedziczenia po `Product`

Klasa `NonFoodProduct` zostanie zmodyfikowana, tak aby dziedziczyła po `Product`. Metoda `getPrice(int year, int month)` będzie implementowana w tej klasie, zwracając cenę w oparciu o dany rok i miesiąc.

```java
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class NonFoodProduct extends Product {
    Double[] prices;

    private NonFoodProduct(String name, Double[] prices) {
        super(name);
        this.prices = prices;
    }

    public static NonFoodProduct fromCsv(Path path) {
        String name;
        Double[] prices;

        try {
            Scanner scanner = new Scanner(path);
            name = scanner.nextLine();
            scanner.nextLine();  // Pomijam drugą linię z nagłówkiem tabeli
            prices = Arrays.stream(scanner.nextLine().split(";"))
                    .map(value -> value.replace(",","."))
                    .map(Double::valueOf)
                    .toArray(Double[]::new);
            scanner.close();

            return new NonFoodProduct(name, prices);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getPrice(int year, int month) throws IndexOutOfBoundsException {
        if (month < 1 || month > 12) {
            throw new IndexOutOfBoundsException("Month must be between 1 and 12");
        }

        int index = (year - 2010) * 12 + month - 1;
        if (index < 0 || index >= prices.length) {
            throw new IndexOutOfBoundsException("No data for the given year and month");
        }

        return prices[index];
    }
}
```

### Krok 3: Testowanie w metodzie `Main::main`

Przykładowa implementacja metody `main` do testowania działania klasy `NonFoodProduct`:

```java
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            NonFoodProduct product = NonFoodProduct.fromCsv(Paths.get("ścieżka_do_pliku.csv"));
            System.out.println("Price in January 2010: " + product.getPrice(2010, 1));
            System.out.println("Price in March 2022: " + product.getPrice(2022, 3));
            System.out.println("Price in December 2021: " + product.getPrice(2021, 12));
            // Test z nieistniejącą datą
            System.out.println("Price in December 2023: " + product.getPrice(2023, 12));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Other error: " + e.getMessage());
        }
    }
}
```

Upewnij się, że ścieżka do pliku CSV w `Paths.get("ścieżka_do_pliku.csv")` jest prawidłowa. W metodzie `main` możemy testować różne scenariusze, w tym te, które przekraczają zakres dat.

## Krok 2.
Napisz klasę FoodProduct dziedziczącą po Product. Stwórz w niej statyczną, publiczną metodę
wytwórczą, analogiczną do tej istniejącej w NonFoodProduct:
FoodProduct fromCsv(Path path),
działającej tak, aby możliwe było wywołanie opisanych w dalszej części tego kroku metod.
Klasa powinna posiadać publiczną metodę:
double getPrice(int year, int month, String province).
Metoda ma zwracać cenę w określonym województwie przekazanym napisem składającym się z
wielkich liter (jak w plikach z danymi). Jeżeli zostanie podany napis nie pasujący do żadnego
województwa lub data będzie niewłaściwa, należy rzucić wyjątek IndexOutOfBoundsException.
Klasa powinna także nadpisywać metodę:
double getPrice(int year, int month)
w taki sposób, że jako wynik będzie zwracana średnia arytmetyczna cen ze wszystkich
województw.
Przetestuj działanie dwu- i trójargumentowej metody FoodProduct::getPrice w metodzie
Main::main.


W tym kroku stworzymy klasę `FoodProduct`, która dziedziczy po klasie abstrakcyjnej `Product`. Klasa ta będzie zawierać dodatkową funkcjonalność do obsługi produktów spożywczych z cenami różniącymi się w zależności od województwa. Oto kroki, które należy podjąć:

### Krok 1: Utworzenie klasy `FoodProduct`

Klasa `FoodProduct` dziedziczy po `Product` i zawiera dodatkowe metody do zarządzania cenami produktów spożywczych w różnych województwach.

```java
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class FoodProduct extends Product {
    private HashMap<String, Double[]> pricesByProvince;

    private FoodProduct(String name, HashMap<String, Double[]> pricesByProvince) {
        super(name);
        this.pricesByProvince = pricesByProvince;
    }

    public static FoodProduct fromCsv(Path path) {
        String name;
        HashMap<String, Double[]> pricesByProvince = new HashMap<>();

        try {
            Scanner scanner = new Scanner(path);
            name = scanner.nextLine();
            String[] provinces = scanner.nextLine().split(";");
            
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(";");
                String province = data[0];
                Double[] prices = Arrays.stream(data, 1, data.length)
                                        .map(value -> value.replace(",", "."))
                                        .map(Double::valueOf)
                                        .toArray(Double[]::new);
                pricesByProvince.put(province, prices);
            }

            scanner.close();
            return new FoodProduct(name, pricesByProvince);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getPrice(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IndexOutOfBoundsException("Month must be between 1 and 12");
        }

        int index = (year - 2010) * 12 + month - 1;
        double total = 0;
        int count = 0;

        for (Double[] prices : pricesByProvince.values()) {
            if (index < 0 || index >= prices.length) {
                throw new IndexOutOfBoundsException("No data for the given year and month");
            }
            total += prices[index];
            count++;
        }

        return total / count;
    }

    public double getPrice(int year, int month, String province) {
        if (month < 1 || month > 12) {
            throw new IndexOutOfBoundsException("Month must be between 1 and 12");
        }

        int index = (year - 2010) * 12 + month - 1;
        Double[] prices = pricesByProvince.get(province);
        if (prices == null || index < 0 || index >= prices.length) {
            throw new IndexOutOfBoundsException("Invalid province or no data for the given year and month");
        }

        return prices[index];
    }
}
```

### Krok 2: Testowanie metody `FoodProduct::getPrice` w metodzie `Main::main`

Teraz, gdy mamy zaimplementowane wszystkie funkcjonalności, możemy przetestować działanie metod `getPrice` w klasie `FoodProduct`.

```java
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            FoodProduct product = FoodProduct.fromCsv(Paths.get("ścieżka_do_pliku.csv"));
            System.out.println("Average price in January 2010: " + product.getPrice(2010, 1));
            System.out.println("Price in January 2010, WIELKOPOLSKIE: " + product.getPrice(2010, 1, "WIELKOPOLSKIE"));
            // Test z nieistniejącą datą i województwem
            System.out.println("Price in January 2030, WIELKOPOLSKIE: " + product.getPrice(2030, 1, "WIELKOPOLSKIE"));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Other error: " + e.getMessage());
        }
    }
}
```

Upewnij się, że ścieżka do pliku CSV w `Paths.get("ścieżka_do_pliku.csv")` jest prawidłowa, a także że testujesz różne scenariusze, które obejmują różne województwa oraz przekroczenie zakresu dat.


## Krok 3.
W klasie Product stwórz prywatną, statyczną listę obiektów klasy Product. Napisz statyczną,
publiczną metodę Product::clearProducts czyszczącą listę products oraz metodę
Product::addProducts dodającą do niej elementy, która przyjmie dwa parametry:
● obiekt funkcyjny, do którego można przypisać metody FoodProduct::fromCsv oraz
NonFoodProduct::fromCsv,
● obiekt Path zawierający ścieżkę do katalogu z plikami danych.
Metoda Product::addProducts powinna dodać do obiektu products obiekty utworzone na
podstawie plików z danymi.
W metodzie Main::main należy wywołać metodę Product::addProducts dwa razy: dla ścieżki
“data/nonfood” i metody NonFoodProduct::fromCsv oraz dla ścieżki “data/food” i metody
FoodProduct::fromCsv.

W Krok 3, będziemy implementować metodę do zarządzania globalną listą produktów w klasie `Product`. Lista ta będzie przechowywać obiekty zarówno klasy `NonFoodProduct`, jak i `FoodProduct`. Oto szczegółowe kroki i kod implementujący to rozwiązanie:

### Krok 1: Modyfikacje w klasie `Product`

Dodamy prywatną, statyczną listę obiektów klasy `Product` oraz metody do zarządzania tą listą.

```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class Product {
    private String name;
    private static List<Product> products = new ArrayList<>();

    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract double getPrice(int year, int month) throws IndexOutOfBoundsException;

    public static void clearProducts() {
        products.clear();
    }

    public static void addProducts(Function<Path, Product> productCreator, Path directoryPath) {
        try {
            Files.list(directoryPath)
                .forEach(path -> {
                    Product product = productCreator.apply(path);
                    products.add(product);
                });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read directory: " + directoryPath, e);
        }
    }
}
```

### Krok 2: Implementacja i testowanie w `Main::main`

Teraz możemy wykorzystać te metody w metodzie `Main::main` do wczytywania produktów z odpowiednich katalogów.

```java
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            // Clear the product list
            Product.clearProducts();

            // Load non-food products
            Product.addProducts(NonFoodProduct::fromCsv, Paths.get("data/nonfood"));

            // Load food products
            Product.addProducts(FoodProduct::fromCsv, Paths.get("data/food"));

            // Example output to verify loaded products
            System.out.println("Products loaded: " + Product.getProducts().size());
        } catch (Exception e) {
            System.out.println("Error during product loading: " + e.getMessage());
        }
    }
}
```

W tym przykładzie `Product::addProducts` przyjmuje funkcję tworzącą produkty oraz ścieżkę do katalogu z plikami. Funkcja ta jest wykorzystywana do iteracji przez wszystkie pliki w katalogu i przekształcenie każdego pliku w obiekt `Product`, który jest następnie dodawany do listy. Funkcje `NonFoodProduct::fromCsv` i `FoodProduct::fromCsv` są przekazywane jako referencje do metod.

Pamiętaj, aby sprawdzić, czy podane ścieżki są poprawne i istnieją w systemie plików, zanim uruchomisz metodę `main`. Jeśli ścieżki są nieprawidłowe, program rzuci wyjątkiem podczas próby odczytu katalogu.

## Krok 4.
Napisz klasę wyjątku AmbigiousProductException, która przyjmuje w konstruktorze obiekt
List/<String/> i zaprogramuj ją tak, by wyświetlała jej zawartość w stosie błędów (formatowanie
jest dowolne).
Napisz publiczną, statyczną metodę Product::getProducts rzucającą wyjątki
AmbigiousProductException oraz IndexOutOfBoundsException. Metoda powinna przyjąć napis
będący prefiksem nazwy produktu (zwracanej przez Product::getName). Jeżeli w liście
produktów znalezione zostanie:
● 0 produktów o nazwie rozpoczynającej się od tego prefiksu - należy rzucić wyjątek
IndexOutOfBoundsException, np. prefiks "Abc" nie wskazuje na żaden obiekt,
● 1 produkt - należy go zwrócić, np. prefiks "Bu" wskazuje jednoznacznie na obiekt,
którego metoda Product::getName zwróci: "Buraki - za 1 kg",
● więcej niż 1 produkt - należy rzucić wyjątek AmbigiousProductException, przekazując mu
jako parametr konstruktora, listę nazw przedmiotów zaczynających się od danego
prefiksu, np. prefiks "Ja" powinien spowodować rzucenie wyjątku, któremu jako parametr
konstruktora zostanie przekazana lista: ["Jabłka - za 1 kg", "Jaja kurze świeże - za 1
szt"].
W metodzie Main::main przetestuj wszystkie trzy wymienione przypadki.

### Krok 4: Implementacja klasy wyjątku i metody wyszukiwania produktów

**Krok 4.1: Klasa wyjątku `AmbiguousProductException`**

Zaczniemy od stworzenia niestandardowego wyjątku, który pozwoli zgłaszać sytuacje, gdy wyszukiwanie produktu po prefiksie jest niejednoznaczne.

```java
import java.util.List;

public class AmbiguousProductException extends Exception {
    public AmbiguousProductException(List<String> productNames) {
        super("Multiple products found: " + String.join(", ", productNames));
    }
}
```

Wyjątek ten przyjmuje listę nazw produktów i formatuje ją w czytelny sposób w wiadomości wyjątku.

**Krok 4.2: Metoda `Product::getProducts`**

Następnie dodamy do klasy `Product` metodę statyczną `getProducts`, która wyszukuje produkty na podstawie prefiksu nazwy i zarządza sytuacjami wyjątkowymi.

```java
import java.util.ArrayList;
import java.util.List;

public abstract class Product {
    private static List<Product> products = new ArrayList<>();
    // Inne metody klasy Product

    public static Product getProducts(String prefix) throws AmbiguousProductException, IndexOutOfBoundsException {
        List<Product> foundProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().startsWith(prefix)) {
                foundProducts.add(product);
            }
        }
        
        if (foundProducts.isEmpty()) {
            throw new IndexOutOfBoundsException("No product found with the prefix: " + prefix);
        } else if (foundProducts.size() == 1) {
            return foundProducts.get(0);
        } else {
            List<String> productNames = new ArrayList<>();
            for (Product product : foundProducts) {
                productNames.add(product.getName());
            }
            throw new AmbiguousProductException(productNames);
        }
    }
}
```

**Krok 4.3: Testowanie w `Main::main`**

Ostatecznie, przetestujemy wszystkie wymienione przypadki w metodzie `Main::main`, gdzie przykładamy do wywołania metody `getProducts` i obsługi wyjątków.

```java
public class Main {
    public static void main(String[] args) {
        try {
            // Przykładowe dodanie produktów do listy
            Product.addProducts(FoodProduct::fromCsv, Paths.get("data/food"));
            Product.addProducts(NonFoodProduct::fromCsv, Paths.get("data/nonfood"));
            
            // Testowanie przypadku z jednym produktem
            try {
                Product product = Product.getProducts("Bu");
                System.out.println("Found product: " + product.getName());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            // Testowanie przypadku z wieloma produktami
            try {
                Product product = Product.getProducts("Ja");
                System.out.println("Found product: " + product.getName());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            // Testowanie przypadku bez żadnego produktu
            try {
                Product product = Product.getProducts("Zx");
                System.out.println("Found product: " + product.getName());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error during product loading or processing: " + e.getMessage());
        }
    }
}
```

Upewnij się, że testy odnoszą się do rzeczywistych prefiksów, które są stosowne do danych w plikach, jak również że odpowiednie wyjątki są rzucane w określonych warunkach.

## Krok 5
Napisz klasę Cart posiadającą publiczne metody
● void addProduct(Product product, int amount) - dodającą do koszyka produkt w liczbie
sztuk określonej zmienną amount,
● double getPrice(int year, int month) - zwracającą wartość koszyka w zł we wskazanym
roku i miesiącu,
● double getInflation(int year1, int month1, int year2, int month2) - zwraca procentową
wartość inflacji w ujęciu rocznym między dwoma wskazanymi miesiącami na podstawie
zawartości koszyka, zakładając, że y1, m1 < y2, m2. Należy ją wyliczyć według wzoru:
(price2 - price1) / price1 * 100 / months * 12,
gdzie price1 i price2 to wartości koszyków w dwóch wskazanych miesiącach, a months to
liczba miesięcy dzieląca wskazane daty.
W metodzie Main::main, przy użyciu metody Product::addProduct dodaj do koszyka kilka
produktów i wywołaj na jego rzecz metody Cart::getPrice i Cart::getInflation.

### Krok 5: Implementacja klasy `Cart`

Klasa `Cart` będzie zarządzać produktami dodawanymi do koszyka oraz umożliwiać obliczanie wartości koszyka i inflacji między dwoma okresami.

**Krok 5.1: Definicja klasy `Cart`**

Poniżej znajduje się implementacja klasy `Cart` z metodami dodającymi produkty, obliczającymi wartość koszyka i inflację.

```java
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Product, Integer> cartItems = new HashMap<>();

    public void addProduct(Product product, int amount) {
        if (cartItems.containsKey(product)) {
            cartItems.put(product, cartItems.get(product) + amount);
        } else {
            cartItems.put(product, amount);
        }
    }

    public double getPrice(int year, int month) {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double price = product.getPrice(year, month);
            total += price * quantity;
        }
        return total;
    }

    public double getInflation(int year1, int month1, int year2, int month2) {
        double price1 = getPrice(year1, month1);
        double price2 = getPrice(year2, month2);
        int months = (year2 - year1) * 12 + (month2 - month1);
        if (months <= 0) {
            throw new IllegalArgumentException("The second date must be later than the first date.");
        }
        return (price2 - price1) / price1 * 100 / months * 12;
    }
}
```

**Krok 5.2: Testowanie w `Main::main`**

W metodzie `Main::main`, przetestujemy dodanie produktów do koszyka i wywołanie metod `Cart::getPrice` oraz `Cart::getInflation`.

```java
public class Main {
    public static void main(String[] args) {
        try {
            Product.clearProducts();
            Product.addProducts(FoodProduct::fromCsv, Paths.get("data/food"));
            Product.addProducts(NonFoodProduct::fromCsv, Paths.get("data/nonfood"));

            Cart cart = new Cart();

            Product milk = Product.getProducts("Milk");
            Product bread = Product.getProducts("Bread");

            cart.addProduct(milk, 10);  // Załóżmy, że "Milk" oznacza "Mleko - 1l"
            cart.addProduct(bread, 5);  // Załóżmy, że "Bread" oznacza "Chleb pszenny"

            System.out.println("Total price in January 2010: " + cart.getPrice(2010, 1));
            System.out.println("Total price in March 2022: " + cart.getPrice(2022, 3));

            double inflationRate = cart.getInflation(2010, 1, 2022, 3);
            System.out.println("Inflation rate from January 2010 to March 2022: " + inflationRate + "%");
        } catch (AmbiguousProductException | IndexOutOfBoundsException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Other error: " + e.getMessage());
        }
    }
}
```

W powyższym przykładzie używamy założonych nazw produktów, które mogą być różne w rzeczywistości w zależności od danych. Upewnij się, że używasz nazw produktów, które faktycznie istnieją w wczytanych plikach CSV. W ten sposób można przetestować, czy metody obliczające ceny i inflację działają prawidłowo, zarówno w przypadku poprawnych danych, jak i różnych scenariuszy błędów.