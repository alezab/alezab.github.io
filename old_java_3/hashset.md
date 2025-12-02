Here are Java code snippets formatted in Markdown for common operations involving `HashMap` key-value iteration and sorting collections:

---

### 🔁 **Iterate over a HashMap (Key-Value Pairs)**

```java
import java.util.HashMap;
import java.util.Map;

public class HashMapIterationExample {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("apple", 3);
        map.put("banana", 1);
        map.put("cherry", 2);

        // Method 1: Using entrySet()
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

        // Method 2: Using keySet()
        for (String key : map.keySet()) {
            System.out.println(key + " => " + map.get(key));
        }

        // Method 3: Using forEach with lambda
        map.forEach((key, value) -> System.out.println(key + " => " + value));
    }
}
```

---

### 🔽 **Sort a List of Integers**

```java
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortListExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 3);

        // Ascending order
        Collections.sort(numbers);
        System.out.println("Ascending: " + numbers);

        // Descending order
        Collections.sort(numbers, Collections.reverseOrder());
        System.out.println("Descending: " + numbers);
    }
}
```

---

### 🔠 **Sort a HashMap by Keys or Values**

```java
import java.util.*;

public class SortMapExample {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("banana", 2);
        map.put("apple", 5);
        map.put("cherry", 1);

        // Sort by keys
        map.entrySet().stream()
           .sorted(Map.Entry.comparingByKey())
           .forEach(entry -> System.out.println(entry.getKey() + " => " + entry.getValue()));

        // Sort by values
        map.entrySet().stream()
           .sorted(Map.Entry.comparingByValue())
           .forEach(entry -> System.out.println(entry.getKey() + " => " + entry.getValue()));
    }
}
```

---

Let me know if you want versions for Java 8+, parallel streams, or sorted maps (`TreeMap`).
