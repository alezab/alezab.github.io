https://github.com/michaldziuba03/java

implementcja excpetion

```java
import java.io.File;
import java.io.FileNotFoundException;

public class MyFileReader {
    private File file;

    public MyFileReader(String fileName) throws FileNotFoundException {
        this.file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
        // Other initialization code...
    }

    // Other methods for reading the file...
}
```

```java
public class Main {
    public static void main(String[] args) {
        try {
            MyFileReader reader = new MyFileReader("example.txt");
            // Do something with the reader...
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            // Handle the exception...
        }
    }
}
```

poli

```java
public abstract class Country {
    private final String name;

    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Tutaj dodajemy statyczne metody dla zarządzania plikami CSV (nie pokazane tutaj)

    // Metoda polimorficzna
    public static Country fromCsv(String countryName) {
        // Załóżmy, że logika decyduje, jaki typ obiektu zwrócić na podstawie nazwy kraju
        if (countryName.equals("Monoland")) {
            return new CountryWithoutProvinces(countryName);
        } else {
            // Tutaj przyjmujemy, że każdy inny kraj ma prowincje
            Country[] provinces = { new CountryWithoutProvinces("Province1"), new CountryWithoutProvinces("Province2") };
            return new CountryWithProvinces(countryName, provinces);
        }
    }
}

class CountryWithoutProvinces extends Country {
    public CountryWithoutProvinces(String name) {
        super(name);
    }
}

class CountryWithProvinces extends Country {
    private Country[] provinces;

    public CountryWithProvinces(String name, Country[] provinces) {
        super(name);
        this.provinces = provinces;
    }
}
```
