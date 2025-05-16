# Deep, Shallow i Lazy Copy w Javie

## Shallow Copy (Płytka kopia)
- Tworzy nowy obiekt, ale kopiowane są tylko referencje do obiektów zagnieżdżonych.
- Zmiana w zagnieżdżonym obiekcie w kopii wpływa na oryginał.

**Przykład:**
```java
class Person implements Cloneable {
    String name;
    Address address;

    public Person clone() throws CloneNotSupportedException {
        return (Person) super.clone(); // shallow copy
    }
}

class Address {
    String city;
}
```

## Deep Copy (Głęboka kopia)
- Tworzy nowy obiekt oraz nowe kopie wszystkich obiektów zagnieżdżonych.
- Zmiana w kopii nie wpływa na oryginał.

**Przykład:**
```java
class Person implements Cloneable {
    String name;
    Address address;

    public Person clone() throws CloneNotSupportedException {
        Person cloned = (Person) super.clone();
        cloned.address = new Address(address.city); // deep copy
        return cloned;
    }
}

class Address {
    String city;
    Address(String city) { this.city = city; }
}
```

## Lazy Copy (Kopia leniwa, copy-on-write)
- Kopia tworzona jest dopiero przy próbie modyfikacji (copy-on-write).
- Oszczędza pamięć, dopóki nie zajdzie potrzeba rozdzielenia obiektów.

**Przykład:**
```java
class LazyCopyList {
    private List<String> data;
    private boolean shared = true;

    public LazyCopyList(List<String> data) {
        this.data = data;
    }

    public void set(int idx, String value) {
        if (shared) {
            data = new ArrayList<>(data); // copy on write
            shared = false;
        }
        data.set(idx, value);
    }
}
```

## Referencja
- [Java Deep vs Shallow Copy](https://www.baeldung.com/java-deep-copy)
- [Copy-on-write w Java Collections](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CopyOnWriteArrayList.html)
