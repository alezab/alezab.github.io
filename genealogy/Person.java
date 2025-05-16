package genealogy;

import java.time.LocalDate;
import java.util.*;

public class Person implements Comparable<Person>, java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private Set<Person> children = new HashSet<>();

    public Person(String firstName, String lastName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public Person(String firstName, String lastName, LocalDate birthDate, LocalDate deathDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }

    // Gettery
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getBirthDate() { return birthDate; }
    public LocalDate getDeathDate() { return deathDate; }

    @Override
    public int compareTo(Person other) {
        return this.birthDate.compareTo(other.birthDate);
    }

    public boolean adopt(Person child) {
        return children.add(child);
    }

    public Person getYoungestChild() {
        return children.stream()
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    public List<Person> getChildren() {
        List<Person> sortedChildren = new ArrayList<>(children);
        sortedChildren.sort(Comparator.naturalOrder()); // od najstarszego do najmłodszego
        return sortedChildren;
    }

    public static Person fromCsvLine(String line) throws NegativeLifespanException {
        String[] parts = line.split(",");
        String[] nameParts = parts[0].trim().split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        LocalDate birthDate = LocalDate.parse(parts[1].trim(), java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        LocalDate deathDate = parts[2].trim().isEmpty() ? null : LocalDate.parse(parts[2].trim(), java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        if (deathDate != null && deathDate.isBefore(birthDate)) {
            throw new NegativeLifespanException(firstName, lastName, parts[1].trim(), parts[2].trim());
        }
        return new Person(firstName, lastName, birthDate, deathDate);
    }

    public static List<Person> fromCsv(String filePath) throws java.io.IOException, AmbiguousPersonException, NegativeLifespanException {
        List<Person> people = new ArrayList<>();
        Map<String, Person> personMap = new HashMap<>();
        Set<String> uniqueNames = new HashSet<>();
        List<String[]> rawLines = new ArrayList<>();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filePath))) {
            String line = reader.readLine(); // pomiń nagłówek
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    rawLines.add(parts);
                    Person p = fromCsvLine(line);
                    String key = p.getFirstName() + " " + p.getLastName();
                    if (!uniqueNames.add(key)) {
                        throw new AmbiguousPersonException(p.getFirstName(), p.getLastName());
                    }
                    people.add(p);
                    personMap.put(key, p);
                }
            }
        }
        // Drugi przebieg: ustawianie relacji rodzic-dziecko
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        for (int i = 0; i < rawLines.size(); i++) {
            String[] parts = rawLines.get(i);
            Person child = people.get(i);
            for (int j = 3; j <= 4; j++) {
                String parentName = parts.length > j ? parts[j].trim() : "";
                if (!parentName.isEmpty()) {
                    Person parent = personMap.get(parentName);
                    if (parent != null) {
                        boolean valid = true;
                        String reason = null;
                        if (parent.getBirthDate() != null && child.getBirthDate() != null) {
                            if (parent.getBirthDate().plusYears(15).isAfter(child.getBirthDate())) {
                                valid = false;
                                reason = "Rodzic ma mniej niż 15 lat w chwili narodzin dziecka.";
                            }
                            if (parent.getDeathDate() != null && parent.getDeathDate().isBefore(child.getBirthDate())) {
                                valid = false;
                                reason = "Rodzic nie żyje w chwili narodzin dziecka.";
                            }
                        }
                        if (!valid) {
                            try {
                                throw new ParentingAgeException(parentName, child.getFirstName() + " " + child.getLastName(), reason);
                            } catch (ParentingAgeException ex) {
                                System.out.println(ex.getMessage());
                                System.out.print("Czy zaakceptować tę relację? (Y aby zaakceptować): ");
                                String input = scanner.nextLine();
                                if (!input.trim().equalsIgnoreCase("Y")) {
                                    continue;
                                }
                            }
                        }
                        parent.adopt(child);
                    }
                }
            }
        }
        return people;
    }

    public static void toBinaryFile(List<Person> people, String filePath) throws java.io.IOException {
        try (java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(filePath))) {
            out.writeObject(people);
        }
    }

    public static List<Person> fromBinaryFile(String filePath) throws java.io.IOException, ClassNotFoundException {
        try (java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(filePath))) {
            return (List<Person>) in.readObject();
        }
    }

    public String toPlantUML() {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        String selfName = getFirstName() + " " + getLastName();
        sb.append("object \"" + selfName + "\" as O1\n");
        int parentIdx = 2;
        for (Person parent : findParents()) {
            String parentName = parent.getFirstName() + " " + parent.getLastName();
            sb.append("object \"" + parentName + "\" as O" + parentIdx + "\n");
            sb.append("O1 --> O" + parentIdx + "\n");
            parentIdx++;
        }
        sb.append("@enduml\n");
        return sb.toString();
    }

    public String toPlantUML(java.util.function.Function<String, String> postProcess) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        String selfName = getFirstName() + " " + getLastName();
        String objLine = "object \"" + selfName + "\" as O1";
        sb.append(postProcess.apply(objLine) + "\n");
        int parentIdx = 2;
        for (Person parent : findParents()) {
            String parentName = parent.getFirstName() + " " + parent.getLastName();
            String parentObjLine = "object \"" + parentName + "\" as O" + parentIdx;
            sb.append(postProcess.apply(parentObjLine) + "\n");
            sb.append("O1 --> O" + parentIdx + "\n");
            parentIdx++;
        }
        sb.append("@enduml\n");
        return sb.toString();
    }

    public String toPlantUML(java.util.function.Function<String, String> postProcess, java.util.function.Predicate<Person> condition) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        String selfName = getFirstName() + " " + getLastName();
        String objLine = "object \"" + selfName + "\" as O1";
        if (condition.test(this)) {
            sb.append(postProcess.apply(objLine) + "\n");
        } else {
            sb.append(objLine + "\n");
        }
        int parentIdx = 2;
        for (Person parent : findParents()) {
            String parentName = parent.getFirstName() + " " + parent.getLastName();
            String parentObjLine = "object \"" + parentName + "\" as O" + parentIdx;
            if (condition.test(parent)) {
                sb.append(postProcess.apply(parentObjLine) + "\n");
            } else {
                sb.append(parentObjLine + "\n");
            }
            sb.append("O1 --> O" + parentIdx + "\n");
            parentIdx++;
        }
        sb.append("@enduml\n");
        return sb.toString();
    }

    public static String toPlantUML(List<Person> people) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        Map<Person, String> objNames = new HashMap<>();
        int idx = 1;
        for (Person p : people) {
            String name = p.getFirstName() + " " + p.getLastName();
            String obj = "O" + idx++;
            objNames.put(p, obj);
            sb.append("object \"" + name + "\" as " + obj + "\n");
        }
        for (Person child : people) {
            for (Person parent : people) {
                if (parent.getChildren().contains(child)) {
                    sb.append(objNames.get(child) + " --> " + objNames.get(parent) + "\n");
                }
            }
        }
        sb.append("@enduml\n");
        return sb.toString();
    }

    // Pomocnicza metoda do znalezienia rodziców (na podstawie relacji dzieci)
    private List<Person> findParents() {
        List<Person> parents = new ArrayList<>();
        // Przeszukujemy wszystkich rodziców wstecznie (jeśli są dostępni)
        // W tej wersji zakładamy, że rodzice są tymi, którzy mają to dziecko w children
        // W praktyce, jeśli mamy referencje do rodziców, można to uprościć
        // Tu uproszczona wersja: brak rodziców, bo nie ma referencji
        return parents;
    }

    public static List<Person> filterByNameSubstring(List<Person> people, String substring) {
        String lower = substring.toLowerCase();
        List<Person> result = new ArrayList<>();
        for (Person p : people) {
            String fullName = (p.getFirstName() + " " + p.getLastName()).toLowerCase();
            if (fullName.contains(lower)) {
                result.add(p);
            }
        }
        return result;
    }

    public static List<Person> sortByBirthYear(List<Person> people) {
        List<Person> sorted = new ArrayList<>(people);
        sorted.sort(Comparator.comparing(Person::getBirthDate));
        return sorted;
    }

    public static List<Person> getDeceasedSortedByLifespanDesc(List<Person> people) {
        List<Person> deceased = new ArrayList<>();
        for (Person p : people) {
            if (p.getDeathDate() != null) {
                deceased.add(p);
            }
        }
        deceased.sort((a, b) -> {
            long lifeA = java.time.temporal.ChronoUnit.DAYS.between(a.getBirthDate(), a.getDeathDate());
            long lifeB = java.time.temporal.ChronoUnit.DAYS.between(b.getBirthDate(), b.getDeathDate());
            return Long.compare(lifeB, lifeA); // malejąco
        });
        return deceased;
    }

    public static Person getOldestLiving(List<Person> people) {
        return people.stream()
            .filter(p -> p.getDeathDate() == null)
            .min(Comparator.comparing(Person::getBirthDate))
            .orElse(null);
    }
}
