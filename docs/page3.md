# JAVA

## Temat 4. Pliki, wyjątki

Skorzystaj z udostępnionego pliku zawierającego opisy przykładowych osób tworzących ze sobą związki.
W kolejnych kolumnach pliku opisane są:

1. imię i nazwisko,
2. data narodzin,
3. data śmierci (pusta jeśli żyje),
4. imię rodzica (puste jeśli nieznany),
5. imię rodzica (puste jeśli nieznany).

genealogy/family.csv

```csv linenums="1"
imię i nazwisko,data urodzenia,data śmierci,rodzic,rodzic
Marek Kowalski,15.05.1899,25.06.1957,,
Ewa Kowalska,03.11.1901,05.03.1990,,
Anna Dąbrowska,07.02.1930,22.12.1991,Ewa Kowalska,Marek Kowalski
Andrzej Kowalski,12.09.1936,25.06.1990,Ewa Kowalska,Marek Kowalski
Krystyna Dąbrowska,25.06.1927,08.04.1940,Ewa Kowalska,Marek Kowalski
Alicja Wiśniewska,18.10.1963,18.10.2012,Anna Dąbrowska,
Tomasz Dąbrowski,24.01.1966,,Anna Dąbrowska,
Joanna Nowak,08.04.1973,,,Andrzej Kowalski
Kacper Kowalski,15.07.1970,,,Andrzej Kowalski
Elżbieta Kowalska,28.12.1990,,,Kacper Kowalski
Jan Kowalski,05.03.1992,,,Kacper Kowalski
```

L4: Zadanie 1.
Napisz klasę Person, w której znajdować będą się dane odpowiadające wierszowi pliku. Na tym etapie pomiń wczytywanie rodziców. Napisz metodę wytwórczą fromCsvLine() klasy Person przyjmującą jako argument linię opisanego pliku.

https://github.com/kdmitruk/java_lab_2024/commit/28be3d3362292bedcd6f6f851f07cf300212aa10

genealogy/src/Main.java

```java linenums="1"
public class Main {
    public static void main(String[] args) {

        String csvLine = "Marek Kowalski,15.05.1899,25.06.1957,,";
        Person person = Person.fromCsvLine(csvLine);
        System.out.println(person);

    }
}
```

genealogy/src/Person.java

```java linenums="1"
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Person {
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate deathDate;

    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }
    public static Person fromCsvLine(String csvLine){
        String[] parts = csvLine.split(",", -1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //Marek Kowalski,15.05.1899,25.06.1957,,
        LocalDate birthDate = LocalDate.parse(parts[1],formatter);
        LocalDate deathDate = !parts[2].equals("") ? LocalDate.parse(parts[2],formatter) :null;
        return new Person (parts[0],birthDate,deathDate);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", deathDate=" + deathDate +
                '}';
    }
}
```

L4: Zadanie 2.
Napisz metodę fromCsv(), która przyjmie ścieżkę do pliku i zwróci listę obiektów typu Person.

https://github.com/kdmitruk/java_lab_2024/commit/496b1b802fc7726e4e275a36ad6a2085deced455

genealogy/src/Main.java

```java
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

//        String csvLine = "Marek Kowalski,15.05.1899,25.06.1957,,";
//        Person person = Person.fromCsvLine(csvLine);
//        System.out.println(person);
        try {
            List<Person> people = Person.fromCsv("family.csv");
            for(Person person : people){
                System.out.println(person);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
```

genealogy/src/Person.java

```java
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate deathDate;

    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }
    public static Person fromCsvLine(String csvLine){
        String[] parts = csvLine.split(",", -1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //Marek Kowalski,15.05.1899,25.06.1957,,
        LocalDate birthDate = LocalDate.parse(parts[1],formatter);
        LocalDate deathDate = !parts[2].equals("") ? LocalDate.parse(parts[2],formatter) :null;
        return new Person (parts[0],birthDate,deathDate);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", deathDate=" + deathDate +
                '}';
    }

    public static List<Person> fromCsv(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        List<Person> people = new ArrayList<>();
        String line;
        reader.readLine();
        while((line = reader.readLine()) != null){
            people.add(Person.fromCsvLine(line));
        }
        reader.close();
        return people;
    }
}
```

L4: Zadanie 3.
Napisz klasę NegativeLifespanException. Rzuć jej obiekt jako wyjątek, jeżeli data śmierci osoby jest wcześniejsza niż data urodzin. Wywołanie metody getMessage() powinno wyświetlić stosowną informację zawierającą przyczyny rzucenia wyjątku.

https://github.com/kdmitruk/java_lab_2024/commit/8ab0c5f8bc8d1e470aa12e76fb22fd5d7b359787

genealogy/family.csv

```
Andrzej Kowalski,25.06.1990,12.09.1936,,Ewa Kowalska,Marek Kowalski
```

src/NegativeLifespanExeption.java

```java
public class NegativeLifespanExeption extends Exception{
    public NegativeLifespanExeption(Person person) {
        super(String.format("%s, urodził(a) się, %s, później niż umarła, %s", person.getName(), person.getBirthDate(), person.getDeathDate()));
    }
}
```

genealogy/src/Person.java

```java
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate deathDate;

    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }
    public static Person fromCsvLine(String csvLine){
        String[] parts = csvLine.split(",", -1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //Marek Kowalski,15.05.1899,25.06.1957,,
        LocalDate birthDate = LocalDate.parse(parts[1],formatter);
        LocalDate deathDate = !parts[2].equals("") ? LocalDate.parse(parts[2],formatter) :null;
        return new Person (parts[0],birthDate,deathDate);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", deathDate=" + deathDate +
                '}';
    }

    public static List<Person> fromCsv(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        List<Person> people = new ArrayList<>();
        String line;
        reader.readLine();
        while((line = reader.readLine()) != null){
            Person person = Person.fromCsvLine(line);
            try {
                person.validateLifespan();
                people.add(Person.fromCsvLine(line));
            } catch (NegativeLifespanExeption e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        reader.close();
        return people;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    private void validateLifespan() throws NegativeLifespanExeption {
        if(deathDate != null && deathDate.isBefore(birthDate)){
            throw new NegativeLifespanExeption(this);
        }
    }
}
```

L4: Zadanie 4.
Napisz klasę AmbiguousPersonException. Rzuć jej obiekt jako wyjątek, jeżeli w pliku pojawi się więcej niż jedna osoba o tym samym imieniu i nazwisku. Wywołanie metody getMessage() powinno wyświetlić stosowną informację zawierającą przyczyny rzucenia wyjątku.

https://github.com/kdmitruk/java_lab_2024/commit/80530d22a0134d1ba8b24dd273e60e00107f2f4a

```
Elżbieta Kowalska,03.11.1901,05.03.1990,,
```

genealogy/src/AmbiguousPersonException.java

```java
public class AmbiguousPersonException extends Exception {
    public AmbiguousPersonException(Person person) {
        super(String.format("%s pojawia się w pliku wielokrotnie", person.getName()));
    }
}
```

person

```java
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate deathDate;

    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }
    public static Person fromCsvLine(String csvLine){
        String[] parts = csvLine.split(",", -1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //Marek Kowalski,15.05.1899,25.06.1957,,
        LocalDate birthDate = LocalDate.parse(parts[1],formatter);
        LocalDate deathDate = !parts[2].equals("") ? LocalDate.parse(parts[2],formatter) :null;
        return new Person (parts[0],birthDate,deathDate);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", deathDate=" + deathDate +
                '}';
    }

    public static List<Person> fromCsv(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        List<Person> people = new ArrayList<>();
        String line;
        reader.readLine();
        while((line = reader.readLine()) != null){
            Person person = Person.fromCsvLine(line);
            try {
                person.validateLifespan();
                person.validateAmbiguity(people);
                people.add(Person.fromCsvLine(line));
            } catch (NegativeLifespanExeption | AmbiguousPersonException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        reader.close();
        return people;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    private void validateLifespan() throws NegativeLifespanExeption {
        if(deathDate != null && deathDate.isBefore(birthDate)){
            throw new NegativeLifespanExeption(this);
        }
    }
    private void validateAmbiguity(List<Person> people) throws AmbiguousPersonException {
        for(Person person : people){
            if(person.getName().equals(getName())){
                throw new AmbiguousPersonException(person);
            }
        }
    }
}
```

L4: Zadanie 5.
Niech rodzice będą przechowywani w klasie Person w postaci listy obiektów Person. Zmodyfikuj metodę fromCsv, by w obiektach dzieci ustawiała referencje do obiektów rodziców.

https://github.com/kdmitruk/java_lab_2024/commit/5913deeb81273aee60a5c17fbf2b67eafbda04bd

genealogy/src/PersonWithParentStrings.java

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonWithParentStrings {

    final public Person person;

    final public List<String> parentNames;

    private PersonWithParentStrings(Person person, List<String> parentNames) {
        this.person = person;
        this.parentNames = parentNames;
    }

    static public PersonWithParentStrings fromCsvLine(String line){
        Person person = Person.fromCsvLine(line);
        List<String> parentNames = new ArrayList<>();

        String[] values = line.split(",", -1);

        for(int i = 3; i <= 4; i++){
            if(!values[i].isEmpty())
                parentNames.add(values[i]);
        }

        return new PersonWithParentStrings(person, parentNames);
    }

    static void linkRelatives(Map<String, PersonWithParentStrings> people){
        for(var personWithParentStrings : people.values()){
            for(var parentName : personWithParentStrings.parentNames){
                personWithParentStrings.person.addParent(people.get(parentName).person);
            }
        }
    }

}
```

person

```java
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person {
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate deathDate;

    private List<Person> parents =  new ArrayList<>();

    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }
    public static Person fromCsvLine(String csvLine){
        String[] parts = csvLine.split(",", -1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //Marek Kowalski,15.05.1899,25.06.1957,,
        LocalDate birthDate = LocalDate.parse(parts[1],formatter);
        LocalDate deathDate = !parts[2].equals("") ? LocalDate.parse(parts[2],formatter) :null;
        return new Person (parts[0],birthDate,deathDate);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", deathDate=" + deathDate +
                ", parents=" + parents +
                '}';
    }

    public static List<Person> fromCsv(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        List<Person> people = new ArrayList<>();
        Map<String, PersonWithParentStrings> peopleWithParentStrings = new HashMap<>();

        String line;
        reader.readLine();
        while((line = reader.readLine()) != null){

//            Person person = Person.fromCsvLine(line);
            var personWithParentStrings = PersonWithParentStrings.fromCsvLine(line);
            var person = personWithParentStrings.person;

            try {
                person.validateLifespan();
                person.validateAmbiguity(people);
                people.add(person);

                peopleWithParentStrings.put(person.name, personWithParentStrings);
            }
            catch (NegativeLifespanExeption | AmbiguousPersonException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        PersonWithParentStrings.linkRelatives(peopleWithParentStrings);

        reader.close();
        return people;
    }

    public void addParent(Person parent){
        parents.add(parent);
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    private void validateLifespan() throws NegativeLifespanExeption {
        if(deathDate != null && deathDate.isBefore(birthDate)){
            throw new NegativeLifespanExeption(this);
        }
    }
    private void validateAmbiguity(List<Person> people) throws AmbiguousPersonException {
        for(Person person : people){
            if(person.getName().equals(getName())){
                throw new AmbiguousPersonException(person);
            }
        }
    }
}
```

L4: Zadanie 6.
Napisz klasę ParentingAgeException. Rzuć jej obiekt jako wyjątek, jeżeli rodzic jest młodszy niż 15 lat lub nie żyje w chwili narodzin dziecka. Przechwyć ten wyjątek tak, aby nie zablokował dodania takiej osoby, a jedynie poprosił użytkownika o potwierdzenie lub odrzucenie takiego przypadku za pomocą wpisania znaku “Y” ze standardowego wejścia.

L4: Zadanie 7.
W klasie Person napisz statyczne metody toBinaryFile i fromBinaryFile, które zapiszą i odczytają listę osób do i z pliku binarnego.

## Temat 5. Programowanie funkcyjne

L5: Zadanie 1.

Pobierz aplikację PlantUML. Przetestuj jej działanie z linii komend.

Napisz klasę PlantUMLRunner, posiadającą publiczne statyczne metody:

- ustawienie ścieżki do uruchamialnego pliku jar.
- wygenerowanie schematu po przekazaniu: napisu z danymi, ścieżki do katalogu wynikowego i nazwy pliku wynikowego.

src/PlantUMLRunner.java

```java
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PlantUMLRunner {

    private static String jarPath;

    public static void setJarPath(String jarPath) {
        PlantUMLRunner.jarPath = jarPath;
    }

    public static void generate(String data, String outputDirPath, String outputFileName){
        File directory = new File(outputDirPath);
        directory.mkdirs();

        File outputFile = new File(outputDirPath + '/' + outputFileName);
        try(
            FileWriter outputWriter = new FileWriter(outputFile);
        ){
            outputWriter.write(data);
            outputWriter.close();

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "java", "-jar", jarPath, outputFile.getPath()
            );

            Process process = processBuilder.start();

            process.waitFor();

            outputFile.delete();
        }
        catch (InterruptedException | IOException e){}
    }
}
```

L5: Zadanie 2.

W klasie Person napisz bezargumentową metodę, która zwróci napis sformatowany według składni PlantUML. Napis, korzystając z diagramu obiektów, powinien przedstawiać obiekt osoby na rzecz której została wywołana metoda oraz jej rodziców (o ile są zdefiniowani). Obiekty powinny zawierać nazwę osoby. Od dziecka do rodziców należy poprowadzić strzałki.

main

```java
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //String csvLine = "Marek Kowalski,15.05.1899,25.06.1957,,";
        //Person person = Person.fromCsvLine(csvLine);
        //System.out.println(person.generateTree());
        PlantUMLRunner.setJarPath("./plantuml-1.2024.3.jar");
        try {
            List<Person> people = Person.fromCsv("family.csv");
            for(Person person : people){
                //System.out.println(person.generateTree());
               PlantUMLRunner.generate(person.generateTree(),"image_output", person.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//
//        PlantUMLRunner.generate("@startuml\n" +
//                "Class11 <|.. Class12\n" +
//                "Class13 --> Class14\n" +
//                "Class15 ..> Class16\n" +
//                "Class17 ..|> Class18\n" +
//                "Class19 <--* Class20\n" +
//                "@enduml\n",
//                "image_output",
//                "test"
//                );


    }
}
```

person

```java
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Person {
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate deathDate;

    private List<Person> parents =  new ArrayList<>();

    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }
    public static Person fromCsvLine(String csvLine){
        String[] parts = csvLine.split(",", -1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //Marek Kowalski,15.05.1899,25.06.1957,,
        LocalDate birthDate = LocalDate.parse(parts[1],formatter);
        LocalDate deathDate = !parts[2].equals("") ? LocalDate.parse(parts[2],formatter) :null;
        return new Person (parts[0],birthDate,deathDate);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", deathDate=" + deathDate +
                ", parents=" + parents +
                '}';
    }

    public static List<Person> fromCsv(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        List<Person> people = new ArrayList<>();
        Map<String, PersonWithParentStrings> peopleWithParentStrings = new HashMap<>();

        String line;
        reader.readLine();
        while((line = reader.readLine()) != null){

//            Person person = Person.fromCsvLine(line);
            var personWithParentStrings = PersonWithParentStrings.fromCsvLine(line);
            var person = personWithParentStrings.person;

            try {
                person.validateLifespan();
                person.validateAmbiguity(people);
                people.add(person);

                peopleWithParentStrings.put(person.name, personWithParentStrings);
            }
            catch (NegativeLifespanExeption | AmbiguousPersonException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        PersonWithParentStrings.linkRelatives(peopleWithParentStrings);

        reader.close();
        return people;
    }

    public void addParent(Person parent){
        parents.add(parent);
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    private void validateLifespan() throws NegativeLifespanExeption {
        if(deathDate != null && deathDate.isBefore(birthDate)){
            throw new NegativeLifespanExeption(this);
        }
    }
    private void validateAmbiguity(List<Person> people) throws AmbiguousPersonException {
        for(Person person : people){
            if(person.getName().equals(getName())){
                throw new AmbiguousPersonException(person);
            }
        }
    }
    //object "Janusz Kowalski" as JanuszKowalski
    public String generateTree(){
        String result="@startuml\n%s\n%s\n@enduml";
        Function<Person,String> objectName=person->person.getName().replaceAll(" ","");
        Function<Person,String> objectLine=person -> String.format("object \"%s\" as %s",person.getName(),objectName.apply(person));
        //result=String.format(result,objectLine.apply(this));
        StringBuilder objects=new StringBuilder();
        StringBuilder relations=new StringBuilder();
        objects.append(objectLine.apply(this)).append("\n");
        parents.forEach(parent->{
            objects.append(objectLine.apply(parent)).append("\n");
            relations.append(String.format("%s <-- %s\n",objectName.apply(parent),objectName.apply(this)));
        });
        result=String.format(result,objects,relations);
        return result;
    }
}
```

L5: Zadanie 3.

W klasie Person napisz statyczną metodę, która przyjmie listę osób. Lista powinna zwrócić podobny jak w poprzedni zadaniu napis. Tym razem powinien on zawierać wszystkie osoby w liście i ich powiązania.

https://github.com/kdmitruk/java_lab_2024/commit/e447a7b94a8f871776bcce99ae091fd6146e107c

main

```java
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //String csvLine = "Marek Kowalski,15.05.1899,25.06.1957,,";
        //Person person = Person.fromCsvLine(csvLine);
        //System.out.println(person.generateTree());
        PlantUMLRunner.setJarPath("./plantuml-1.2024.3.jar");
        try {
            List<Person> people = Person.fromCsv("family.csv");
            PlantUMLRunner.generate(
                    Person.generateTree(people),
                    "image_output", "all"
                    );
//            for (Person person : people) {
//                System.out.println(person.generateTree());
//                PlantUMLRunner.generate(person.generateTree(), "image_output", person.getName());
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//
//        PlantUMLRunner.generate("@startuml\n" +
//                "Class11 <|.. Class12\n" +
//                "Class13 --> Class14\n" +
//                "Class15 ..> Class16\n" +
//                "Class17 ..|> Class18\n" +
//                "Class19 <--* Class20\n" +
//                "@enduml\n",
//                "image_output",
//                "test"
//                );


    }
}
```

person

```java
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Person {
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate deathDate;

    private List<Person> parents =  new ArrayList<>();

    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }
    public static Person fromCsvLine(String csvLine){
        String[] parts = csvLine.split(",", -1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //Marek Kowalski,15.05.1899,25.06.1957,,
        LocalDate birthDate = LocalDate.parse(parts[1],formatter);
        LocalDate deathDate = !parts[2].equals("") ? LocalDate.parse(parts[2],formatter) :null;
        return new Person (parts[0],birthDate,deathDate);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", deathDate=" + deathDate +
                ", parents=" + parents +
                '}';
    }

    public static List<Person> fromCsv(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        List<Person> people = new ArrayList<>();
        Map<String, PersonWithParentStrings> peopleWithParentStrings = new HashMap<>();

        String line;
        reader.readLine();
        while((line = reader.readLine()) != null){

//            Person person = Person.fromCsvLine(line);
            var personWithParentStrings = PersonWithParentStrings.fromCsvLine(line);
            var person = personWithParentStrings.person;

            try {
                person.validateLifespan();
                person.validateAmbiguity(people);
                people.add(person);

                peopleWithParentStrings.put(person.name, personWithParentStrings);
            }
            catch (NegativeLifespanExeption | AmbiguousPersonException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        PersonWithParentStrings.linkRelatives(peopleWithParentStrings);

        reader.close();
        return people;
    }

    public void addParent(Person parent){
        parents.add(parent);
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    private void validateLifespan() throws NegativeLifespanExeption {
        if(deathDate != null && deathDate.isBefore(birthDate)){
            throw new NegativeLifespanExeption(this);
        }
    }
    private void validateAmbiguity(List<Person> people) throws AmbiguousPersonException {
        for(Person person : people){
            if(person.getName().equals(getName())){
                throw new AmbiguousPersonException(person);
            }
        }
    }
    //object "Janusz Kowalski" as JanuszKowalski
    public String generateTree(){
        String result="@startuml\n%s\n%s\n@enduml";
        Function<Person,String> objectName=person->person.getName().replaceAll(" ","");
        Function<Person,String> objectLine=person -> String.format("object \"%s\" as %s",person.getName(),objectName.apply(person));
        //result=String.format(result,objectLine.apply(this));
        StringBuilder objects=new StringBuilder();
        StringBuilder relations=new StringBuilder();
        objects.append(objectLine.apply(this)).append("\n");
        parents.forEach(parent->{
            objects.append(objectLine.apply(parent)).append("\n");
            relations.append(String.format("%s <-- %s\n",objectName.apply(parent),objectName.apply(this)));
        });
        result=String.format(result,objects,relations);
        return result;
    }


    public static String generateTree(List<Person> people) {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<String, String> objectName = str -> str.replaceAll(" ", "");
        Function<String, String> objectLine = str -> String.format("object \"%s\" as %s",str, objectName.apply(str));

        Set<String> objects = new HashSet<>();
        Set<String> relations = new HashSet<>();

        Consumer<Person> addPerson = person -> {
            objects.add(objectLine.apply(person.name));
            for (Person parent : person.parents)
                relations.add(objectName.apply(parent.name) + "<--" + objectName.apply(person.name));
        };

        people.forEach(addPerson);
        String objectString = String.join("\n", objects);
        String relationString = String.join("\n", relations);

        return String.format(result, objectString, relationString);
    }
}
```

https://github.com/kdmitruk/java_lab_2024/commit/3331c4208ae260c33e67fe6fbaf8965888e4e1dc

person

```java
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Person {
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate deathDate;

    private List<Person> parents = new ArrayList<>();

    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }

    public static Person fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //Marek Kowalski,15.05.1899,25.06.1957,,
        LocalDate birthDate = LocalDate.parse(parts[1], formatter);
        LocalDate deathDate = !parts[2].equals("") ? LocalDate.parse(parts[2], formatter) : null;
        return new Person(parts[0], birthDate, deathDate);
    }

    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + ", birthDate=" + birthDate + ", deathDate=" + deathDate + ", parents=" + parents + '}';
    }

    public static List<Person> fromCsv(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        List<Person> people = new ArrayList<>();
        Map<String, PersonWithParentStrings> peopleWithParentStrings = new HashMap<>();

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {

//            Person person = Person.fromCsvLine(line);
            var personWithParentStrings = PersonWithParentStrings.fromCsvLine(line);
            var person = personWithParentStrings.person;

            try {
                person.validateLifespan();
                person.validateAmbiguity(people);
                people.add(person);

                peopleWithParentStrings.put(person.name, personWithParentStrings);
            } catch (NegativeLifespanExeption | AmbiguousPersonException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        PersonWithParentStrings.linkRelatives(peopleWithParentStrings);

        reader.close();
        return people;
    }

    public void addParent(Person parent) {
        parents.add(parent);
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    private void validateLifespan() throws NegativeLifespanExeption {
        if (deathDate != null && deathDate.isBefore(birthDate)) {
            throw new NegativeLifespanExeption(this);
        }
    }

    private void validateAmbiguity(List<Person> people) throws AmbiguousPersonException {
        for (Person person : people) {
            if (person.getName().equals(getName())) {
                throw new AmbiguousPersonException(person);
            }
        }
    }

    //object "Janusz Kowalski" as JanuszKowalski
    public String generateTree() {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<Person, String> objectName = person -> person.getName().replaceAll(" ", "");
        Function<Person, String> objectLine = person -> String.format("object \"%s\" as %s", person.getName(), objectName.apply(person));
        //result=String.format(result,objectLine.apply(this));
        StringBuilder objects = new StringBuilder();
        StringBuilder relations = new StringBuilder();
        objects.append(objectLine.apply(this)).append("\n");
        parents.forEach(parent -> {
            objects.append(objectLine.apply(parent)).append("\n");
            relations.append(String.format("%s <-- %s\n", objectName.apply(parent), objectName.apply(this)));
        });
        result = String.format(result, objects, relations);
        return result;
    }


    public static String generateTree(List<Person> people) {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<String, String> objectName = str -> str.replaceAll(" ", "");
        Function<String, String> objectLine = str -> String.format("object \"%s\" as %s", str, objectName.apply(str));

        Set<String> objects = new HashSet<>();
        Set<String> relations = new HashSet<>();

        Consumer<Person> addPerson = person -> {
            objects.add(objectLine.apply(person.name));
            for (Person parent: person.parents)
                relations.add(objectName.apply(parent.name) + "<--" + objectName.apply(person.name));
        };

        people.forEach(addPerson);
        String objectString = String.join("\n", objects);
        String relationString = String.join("\n", relations);

        return String.format(result, objectString, relationString);
    }
}
```

L5: Zadanie 4.

W klasie Person napisz statyczną metodę, która przyjmie listę osób oraz napis substring. Metoda powinna zwrócić listę osób z listy wejściowej, ograniczoną do osób, których nazwa zawiera substring.

https://github.com/kdmitruk/java_lab_2024/commit/bf6890a22e381aa29ae1c2de325dc7448a9cad11

main

```java
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //String csvLine = "Marek Kowalski,15.05.1899,25.06.1957,,";
        //Person person = Person.fromCsvLine(csvLine);
        //System.out.println(person.generateTree());
        PlantUMLRunner.setJarPath("./plantuml-1.2024.3.jar");
        try {
            List<Person> people = Person.fromCsv("family.csv");
//            PlantUMLRunner.generate(
//                    Person.generateTree(people),
//                    "image_output", "all"
//                    );
//            for (Person person : people) {
//                System.out.println(person.generateTree());
//                PlantUMLRunner.generate(person.generateTree(), "image_output", person.getName());
//            }
            Person.filterByName(people, "Kowalsk").forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//
//        PlantUMLRunner.generate("@startuml\n" +
//                "Class11 <|.. Class12\n" +
//                "Class13 --> Class14\n" +
//                "Class15 ..> Class16\n" +
//                "Class17 ..|> Class18\n" +
//                "Class19 <--* Class20\n" +
//                "@enduml\n",
//                "image_output",
//                "test"
//                );


    }
}
```

person

```java
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Person {
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate deathDate;

    private List<Person> parents = new ArrayList<>();

    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }

    public static Person fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //Marek Kowalski,15.05.1899,25.06.1957,,
        LocalDate birthDate = LocalDate.parse(parts[1], formatter);
        LocalDate deathDate = !parts[2].equals("") ? LocalDate.parse(parts[2], formatter) : null;
        return new Person(parts[0], birthDate, deathDate);
    }

    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + ", birthDate=" + birthDate + ", deathDate=" + deathDate + ", parents=" + parents + '}';
    }

    public static List<Person> fromCsv(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        List<Person> people = new ArrayList<>();
        Map<String, PersonWithParentStrings> peopleWithParentStrings = new HashMap<>();

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {

//            Person person = Person.fromCsvLine(line);
            var personWithParentStrings = PersonWithParentStrings.fromCsvLine(line);
            var person = personWithParentStrings.person;

            try {
                person.validateLifespan();
                person.validateAmbiguity(people);
                people.add(person);

                peopleWithParentStrings.put(person.name, personWithParentStrings);
            } catch (NegativeLifespanExeption | AmbiguousPersonException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        PersonWithParentStrings.linkRelatives(peopleWithParentStrings);

        reader.close();
        return people;
    }

    public void addParent(Person parent) {
        parents.add(parent);
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    private void validateLifespan() throws NegativeLifespanExeption {
        if (deathDate != null && deathDate.isBefore(birthDate)) {
            throw new NegativeLifespanExeption(this);
        }
    }

    private void validateAmbiguity(List<Person> people) throws AmbiguousPersonException {
        for (Person person : people) {
            if (person.getName().equals(getName())) {
                throw new AmbiguousPersonException(person);
            }
        }
    }

    //object "Janusz Kowalski" as JanuszKowalski
    public String generateTree() {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<Person, String> objectName = person -> person.getName().replaceAll(" ", "");
        Function<Person, String> objectLine = person -> String.format("object \"%s\" as %s", person.getName(), objectName.apply(person));
        //result=String.format(result,objectLine.apply(this));
        StringBuilder objects = new StringBuilder();
        StringBuilder relations = new StringBuilder();
        objects.append(objectLine.apply(this)).append("\n");
        parents.forEach(parent -> {
            objects.append(objectLine.apply(parent)).append("\n");
            relations.append(String.format("%s <-- %s\n", objectName.apply(parent), objectName.apply(this)));
        });
        result = String.format(result, objects, relations);
        return result;
    }


    public static String generateTree(List<Person> people) {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<String, String> objectName = str -> str.replaceAll(" ", "");
        Function<String, String> objectLine = str -> String.format("object \"%s\" as %s", str, objectName.apply(str));

        Set<String> objects = new HashSet<>();
        Set<String> relations = new HashSet<>();

        Consumer<Person> addPerson = person -> {
            objects.add(objectLine.apply(person.name));
            for (Person parent: person.parents)
                relations.add(objectName.apply(parent.name) + "<--" + objectName.apply(person.name));
        };

        people.forEach(addPerson);
        String objectString = String.join("\n", objects);
        String relationString = String.join("\n", relations);

        return String.format(result, objectString, relationString);
    }

    public static List<Person> filterByName(List<Person> people, String text){
        return people.stream()
                .filter(person -> person.getName().contains(text))
                .collect(Collectors.toList());
    }
}
```

L5: Zadanie 5.

W klasie Person napisz statyczną metodę, która przyjmie listę osób. Metoda powinna zwrócić listę osób z listy wejściowej, posortowanych według roku urodzenia.

L5: Zadanie 6.

W klasie Person napisz statyczną metodę, która przyjmie listę osób. Metoda powinna zwrócić listę zmarłych osób z listy wejściowej, posortowanych malejąco według długości życia.

https://github.com/kdmitruk/java_lab_2024/commit/7a2aaa4cd29ee840e68061f30f234a62ad3ae061

```
Elżbieta Kowalska,28.12.1993,,,Kacper Kowalski
```

main

```java
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //String csvLine = "Marek Kowalski,15.05.1899,25.06.1957,,";
        //Person person = Person.fromCsvLine(csvLine);
        //System.out.println(person.generateTree());
        PlantUMLRunner.setJarPath("./plantuml-1.2024.3.jar");
        try {
            List<Person> people = Person.fromCsv("family.csv");
//            PlantUMLRunner.generate(
//                    Person.generateTree(people),
//                    "image_output", "all"
//                    );
//            for (Person person : people) {
//                System.out.println(person.generateTree());
//                PlantUMLRunner.generate(person.generateTree(), "image_output", person.getName());
//            }
            //Person.filterByName(people, "Kowalsk").forEach(System.out::println);
//            Person.sortedByBirth(people).forEach(System.out::println);
            Person.sortByLifespan(people).forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//
//        PlantUMLRunner.generate("@startuml\n" +
//                "Class11 <|.. Class12\n" +
//                "Class13 --> Class14\n" +
//                "Class15 ..> Class16\n" +
//                "Class17 ..|> Class18\n" +
//                "Class19 <--* Class20\n" +
//                "@enduml\n",
//                "image_output",
//                "test"
//                );


    }
}
```

person

```java
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Person {
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate deathDate;

    private List<Person> parents = new ArrayList<>();

    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }

    public static Person fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //Marek Kowalski,15.05.1899,25.06.1957,,
        LocalDate birthDate = LocalDate.parse(parts[1], formatter);
        LocalDate deathDate = !parts[2].equals("") ? LocalDate.parse(parts[2], formatter) : null;
        return new Person(parts[0], birthDate, deathDate);
    }

    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + ", birthDate=" + birthDate + ", deathDate=" + deathDate + ", parents=" + parents + '}';
    }

    public static List<Person> fromCsv(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        List<Person> people = new ArrayList<>();
        Map<String, PersonWithParentStrings> peopleWithParentStrings = new HashMap<>();

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {

//            Person person = Person.fromCsvLine(line);
            var personWithParentStrings = PersonWithParentStrings.fromCsvLine(line);
            var person = personWithParentStrings.person;

            try {
                person.validateLifespan();
                person.validateAmbiguity(people);
                people.add(person);

                peopleWithParentStrings.put(person.name, personWithParentStrings);
            } catch (NegativeLifespanExeption | AmbiguousPersonException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        PersonWithParentStrings.linkRelatives(peopleWithParentStrings);

        reader.close();
        return people;
    }

    public void addParent(Person parent) {
        parents.add(parent);
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    private void validateLifespan() throws NegativeLifespanExeption {
        if (deathDate != null && deathDate.isBefore(birthDate)) {
            throw new NegativeLifespanExeption(this);
        }
    }

    private void validateAmbiguity(List<Person> people) throws AmbiguousPersonException {
        for (Person person : people) {
            if (person.getName().equals(getName())) {
                throw new AmbiguousPersonException(person);
            }
        }
    }

    //object "Janusz Kowalski" as JanuszKowalski
    public String generateTree() {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<Person, String> objectName = person -> person.getName().replaceAll(" ", "");
        Function<Person, String> objectLine = person -> String.format("object \"%s\" as %s", person.getName(), objectName.apply(person));
        //result=String.format(result,objectLine.apply(this));
        StringBuilder objects = new StringBuilder();
        StringBuilder relations = new StringBuilder();
        objects.append(objectLine.apply(this)).append("\n");
        parents.forEach(parent -> {
            objects.append(objectLine.apply(parent)).append("\n");
            relations.append(String.format("%s <-- %s\n", objectName.apply(parent), objectName.apply(this)));
        });
        result = String.format(result, objects, relations);
        return result;
    }


    public static String generateTree(List<Person> people) {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<String, String> objectName = str -> str.replaceAll(" ", "");
        Function<String, String> objectLine = str -> String.format("object \"%s\" as %s", str, objectName.apply(str));

        Set<String> objects = new HashSet<>();
        Set<String> relations = new HashSet<>();

        Consumer<Person> addPerson = person -> {
            objects.add(objectLine.apply(person.name));
            for (Person parent: person.parents)
                relations.add(objectName.apply(parent.name) + "<--" + objectName.apply(person.name));
        };

        people.forEach(addPerson);
        String objectString = String.join("\n", objects);
        String relationString = String.join("\n", relations);

        return String.format(result, objectString, relationString);
    }

    public static List<Person> filterByName(List<Person> people, String text){
        return people.stream()
                .filter(person -> person.getName().contains(text))
                .collect(Collectors.toList());
    }
    public static List<Person> sortedByBirth(List<Person> people){
        return people.stream()
                .sorted(Comparator.comparing(Person::getBirthDate))
                .collect(Collectors.toList());
    }

    public static List<Person> sortByLifespan(List<Person> people){

        Function<Person, Long> getLifespan = person
                -> person.deathDate.toEpochDay() - person.birthDate.toEpochDay();

        return people.stream()
                .filter(person -> person.deathDate != null)
                .sorted((o2, o1) -> Long.compare(getLifespan.apply(o1), getLifespan.apply(o2)))
//                .sorted(Comparator.comparingLong(getLifespan::apply))
//                .sorted(Collections.reverseOrder())
                .toList();
    }
}
```

L5: Zadanie 7.

W klasie Person napisz statyczną metodę, która przyjmie listę osób. Metoda powinna zwrócić najstarszą żyjącą osobę.

https://github.com/kdmitruk/java_lab_2024/commit/d23bdf88e6c46eacf1ad7cf02da01eb458158f2b

main

```java
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //String csvLine = "Marek Kowalski,15.05.1899,25.06.1957,,";
        //Person person = Person.fromCsvLine(csvLine);
        //System.out.println(person.generateTree());
        PlantUMLRunner.setJarPath("./plantuml-1.2024.3.jar");
        try {
            List<Person> people = Person.fromCsv("family.csv");
            PlantUMLRunner.generate(
                    Person.generateTree(people, text->text+" #FFFF00"),
                    "image_output", "all"
                    );
//            for (Person person : people) {
//                System.out.println(person.generateTree());
//                PlantUMLRunner.generate(person.generateTree(), "image_output", person.getName());
//            }
            //Person.filterByName(people, "Kowalsk").forEach(System.out::println);
//            Person.sortedByBirth(people).forEach(System.out::println);
//            Person.sortByLifespan(people).forEach(System.out::println);
            System.out.println(Person.findOldestLiving(people));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//
//        PlantUMLRunner.generate("@startuml\n" +
//                "Class11 <|.. Class12\n" +
//                "Class13 --> Class14\n" +
//                "Class15 ..> Class16\n" +
//                "Class17 ..|> Class18\n" +
//                "Class19 <--* Class20\n" +
//                "@enduml\n",
//                "image_output",
//                "test"
//                );


    }
}
```

person

```java
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Person {
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate deathDate;

    private List<Person> parents = new ArrayList<>();

    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }

    public static Person fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //Marek Kowalski,15.05.1899,25.06.1957,,
        LocalDate birthDate = LocalDate.parse(parts[1], formatter);
        LocalDate deathDate = !parts[2].equals("") ? LocalDate.parse(parts[2], formatter) : null;
        return new Person(parts[0], birthDate, deathDate);
    }

    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + ", birthDate=" + birthDate + ", deathDate=" + deathDate + ", parents=" + parents + '}';
    }

    public static List<Person> fromCsv(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        List<Person> people = new ArrayList<>();
        Map<String, PersonWithParentStrings> peopleWithParentStrings = new HashMap<>();

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {

//            Person person = Person.fromCsvLine(line);
            var personWithParentStrings = PersonWithParentStrings.fromCsvLine(line);
            var person = personWithParentStrings.person;

            try {
                person.validateLifespan();
                person.validateAmbiguity(people);
                people.add(person);

                peopleWithParentStrings.put(person.name, personWithParentStrings);
            } catch (NegativeLifespanExeption | AmbiguousPersonException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        PersonWithParentStrings.linkRelatives(peopleWithParentStrings);

        reader.close();
        return people;
    }

    public void addParent(Person parent) {
        parents.add(parent);
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    private void validateLifespan() throws NegativeLifespanExeption {
        if (deathDate != null && deathDate.isBefore(birthDate)) {
            throw new NegativeLifespanExeption(this);
        }
    }

    private void validateAmbiguity(List<Person> people) throws AmbiguousPersonException {
        for (Person person : people) {
            if (person.getName().equals(getName())) {
                throw new AmbiguousPersonException(person);
            }
        }
    }

    //object "Janusz Kowalski" as JanuszKowalski
    public String generateTree() {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<Person, String> objectName = person -> person.getName().replaceAll(" ", "");
        Function<Person, String> objectLine = person -> String.format("object \"%s\" as %s", person.getName(), objectName.apply(person));
        //result=String.format(result,objectLine.apply(this));
        StringBuilder objects = new StringBuilder();
        StringBuilder relations = new StringBuilder();
        objects.append(objectLine.apply(this)).append("\n");
        parents.forEach(parent -> {
            objects.append(objectLine.apply(parent)).append("\n");
            relations.append(String.format("%s <-- %s\n", objectName.apply(parent), objectName.apply(this)));
        });
        result = String.format(result, objects, relations);
        return result;
    }


    public static String generateTree(List<Person> people,Function<String,String> postprocess) {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<String, String> objectName = str -> str.replaceAll(" ", "");
        Function<String, String> objectLine = str -> String.format("object \"%s\" as %s", str, objectName.apply(str));
        Function<String, String> objectLineWithPostprocess = objectLine.andThen(postprocess);
        Set<String> objects = new HashSet<>();
        Set<String> relations = new HashSet<>();

        Consumer<Person> addPerson = person -> {
            objects.add(objectLineWithPostprocess.apply(person.name));
            for (Person parent: person.parents)
                relations.add(objectName.apply(parent.name) + "<--" + objectName.apply(person.name));
        };

        people.forEach(addPerson);
        String objectString = String.join("\n", objects);
        String relationString = String.join("\n", relations);

        return String.format(result, objectString, relationString);
    }

    public static List<Person> filterByName(List<Person> people, String text){
        return people.stream()
                .filter(person -> person.getName().contains(text))
                .collect(Collectors.toList());
    }
    public static List<Person> sortedByBirth(List<Person> people){
        return people.stream()
                .sorted(Comparator.comparing(Person::getBirthDate))
                .collect(Collectors.toList());
    }

    public static List<Person> sortByLifespan(List<Person> people){

        Function<Person, Long> getLifespan = person
                -> person.deathDate.toEpochDay() - person.birthDate.toEpochDay();

        return people.stream()
                .filter(person -> person.deathDate != null)
                .sorted((o2, o1) -> Long.compare(getLifespan.apply(o1), getLifespan.apply(o2)))
//                .sorted(Comparator.comparingLong(getLifespan::apply))
//                .sorted(Collections.reverseOrder())
                .toList();
    }

    public static Person findOldestLiving(List<Person> people){
        return people.stream()
                .filter(person -> person.deathDate == null)
                .min(Comparator.comparing(Person::getBirthDate))
                .orElse(null);

    }
}
```

L5: Zadanie 8.

Zmodyfikuj metodę zadania 2. poprzez dodanie do jej argumentów obiektu Function<String, String> postProcess. Funkcja powinna przekształcić wszystkie linie odpowiadające obiektom za pomocą tej funkcji. Przetestuj metodę z dwiema funkcjami: funkcją zmieniającą kolor obiektu na żółty oraz funkcją nie wprowadzającą żadnych zmian.

https://github.com/kdmitruk/java_lab_2024/commit/ca11df03fb8a9f36ea72d5750f4ec66891834478

main

```java
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //String csvLine = "Marek Kowalski,15.05.1899,25.06.1957,,";
        //Person person = Person.fromCsvLine(csvLine);
        //System.out.println(person.generateTree());
        PlantUMLRunner.setJarPath("./plantuml-1.2024.3.jar");
        try {
            List<Person> people = Person.fromCsv("family.csv");
            PlantUMLRunner.generate(
                    Person.generateTree(people,
                            //person -> Person.sortByLifespan(people).contains(person),
                            person -> Person.findOldestLiving(people) == person,
                            text->text+" #FFFF00"),
                    "image_output", "all"
                    );
//            for (Person person : people) {
//                System.out.println(person.generateTree());
//                PlantUMLRunner.generate(person.generateTree(), "image_output", person.getName());
//            }
            //Person.filterByName(people, "Kowalsk").forEach(System.out::println);
//            Person.sortedByBirth(people).forEach(System.out::println);
//            Person.sortByLifespan(people).forEach(System.out::println);
            System.out.println(Person.findOldestLiving(people));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//
//        PlantUMLRunner.generate("@startuml\n" +
//                "Class11 <|.. Class12\n" +
//                "Class13 --> Class14\n" +
//                "Class15 ..> Class16\n" +
//                "Class17 ..|> Class18\n" +
//                "Class19 <--* Class20\n" +
//                "@enduml\n",
//                "image_output",
//                "test"
//                );


    }
}
```

person

```java
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Person {
    private final String name;
    private final LocalDate birthDate;
    private final LocalDate deathDate;

    private List<Person> parents = new ArrayList<>();

    public Person(String name, LocalDate birthDate, LocalDate deathDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }

    public static Person fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //Marek Kowalski,15.05.1899,25.06.1957,,
        LocalDate birthDate = LocalDate.parse(parts[1], formatter);
        LocalDate deathDate = !parts[2].equals("") ? LocalDate.parse(parts[2], formatter) : null;
        return new Person(parts[0], birthDate, deathDate);
    }

    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + ", birthDate=" + birthDate + ", deathDate=" + deathDate + ", parents=" + parents + '}';
    }

    public static List<Person> fromCsv(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        List<Person> people = new ArrayList<>();
        Map<String, PersonWithParentStrings> peopleWithParentStrings = new HashMap<>();

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {

//            Person person = Person.fromCsvLine(line);
            var personWithParentStrings = PersonWithParentStrings.fromCsvLine(line);
            var person = personWithParentStrings.person;

            try {
                person.validateLifespan();
                person.validateAmbiguity(people);
                people.add(person);

                peopleWithParentStrings.put(person.name, personWithParentStrings);
            } catch (NegativeLifespanExeption | AmbiguousPersonException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        PersonWithParentStrings.linkRelatives(peopleWithParentStrings);

        reader.close();
        return people;
    }

    public void addParent(Person parent) {
        parents.add(parent);
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    private void validateLifespan() throws NegativeLifespanExeption {
        if (deathDate != null && deathDate.isBefore(birthDate)) {
            throw new NegativeLifespanExeption(this);
        }
    }

    private void validateAmbiguity(List<Person> people) throws AmbiguousPersonException {
        for (Person person : people) {
            if (person.getName().equals(getName())) {
                throw new AmbiguousPersonException(person);
            }
        }
    }

    //object "Janusz Kowalski" as JanuszKowalski
    public String generateTree() {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<Person, String> objectName = person -> person.getName().replaceAll(" ", "");
        Function<Person, String> objectLine = person -> String.format("object \"%s\" as %s", person.getName(), objectName.apply(person));
        //result=String.format(result,objectLine.apply(this));
        StringBuilder objects = new StringBuilder();
        StringBuilder relations = new StringBuilder();
        objects.append(objectLine.apply(this)).append("\n");
        parents.forEach(parent -> {
            objects.append(objectLine.apply(parent)).append("\n");
            relations.append(String.format("%s <-- %s\n", objectName.apply(parent), objectName.apply(this)));
        });
        result = String.format(result, objects, relations);
        return result;
    }


    public static String generateTree(List<Person> people, Predicate<Person> condition, Function<String, String> postProcess) {
        String result = "@startuml\n%s\n%s\n@enduml";
        Function<String, String> objectName = str -> str.replaceAll("\\s+", "");
        Function<String, String> objectLine = str -> String.format("object \"%s\" as %s",str, objectName.apply(str));
        Function<String, String> objectLineAndPostprocess = objectLine.andThen(postProcess);

        Map<Boolean, List<Person>> groupedPeople = people.stream()
                .collect(Collectors.partitioningBy(condition));

        Set<String> objects = groupedPeople.get(true).stream()
                .map(person -> person.name)
                .map(objectLineAndPostprocess)
                .collect(Collectors.toSet());
        objects.addAll(groupedPeople.get(false).stream()
                .map(person -> person.name)
                .map(objectLine)
                .collect(Collectors.toSet())
        );

        Set<String> relations = people.stream()
                .flatMap(person -> person.parents.stream()
                        .map(parent -> objectName.apply(parent.name) + "<--" + objectName.apply(person.name)))
                .collect(Collectors.toSet());

        String objectString = String.join("\n", objects);
        String relationString = String.join("\n", relations);

        return String.format(result, objectString, relationString);
    }

    public static List<Person> filterByName(List<Person> people, String text){
        return people.stream()
                .filter(person -> person.getName().contains(text))
                .collect(Collectors.toList());
    }
    public static List<Person> sortedByBirth(List<Person> people){
        return people.stream()
                .sorted(Comparator.comparing(Person::getBirthDate))
                .collect(Collectors.toList());
    }

    public static List<Person> sortByLifespan(List<Person> people){

        Function<Person, Long> getLifespan = person
                -> person.deathDate.toEpochDay() - person.birthDate.toEpochDay();

        return people.stream()
                .filter(person -> person.deathDate != null)
                .sorted((o2, o1) -> Long.compare(getLifespan.apply(o1), getLifespan.apply(o2)))
//                .sorted(Comparator.comparingLong(getLifespan::apply))
//                .sorted(Collections.reverseOrder())
                .toList();
    }

    public static Person findOldestLiving(List<Person> people){
        return people.stream()
                .filter(person -> person.deathDate == null)
                .min(Comparator.comparing(Person::getBirthDate))
                .orElse(null);

    }
}
```

L5: Zadanie 9.

Zmodyfikuj metodę z poprzedniego zadania poprzez dodanie do jej argumentów obiektu Predicate<Person> condition. Metoda postProcess powinna wywołać się wyłącznie dla osób spełniających warunek condition. Przetestuj metodę z danymi wygenerowanymi w zadaniach 6. i 7.
