package genealogy;

import java.util.*;

public class Family {
    private Map<String, List<Person>> people = new HashMap<>();

    public void add(Person... persons) {
        for (Person person : persons) {
            String key = person.getFirstName() + " " + person.getLastName();
            people.computeIfAbsent(key, k -> new ArrayList<>()).add(person);
        }
    }

    public Person[] get(String key) {
        List<Person> list = people.getOrDefault(key, Collections.emptyList());
        return list.stream()
                .sorted()
                .toArray(Person[]::new);
    }
}
