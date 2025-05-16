package genealogy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Jan", "Kowalski", LocalDate.of(1980, 5, 10)));
        people.add(new Person("Anna", "Nowak", LocalDate.of(1990, 3, 15)));
        people.add(new Person("Piotr", "Zieliński", LocalDate.of(2000, 7, 20)));
    }
}
