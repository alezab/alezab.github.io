# Pliki tekstowe w Javie

## Zapis do pliku

- **FileWriter** – zapisuje znaki do pliku.
- **PrintWriter** – wygodne metody do zapisu tekstu (np. `println`).
- **BufferedWriter** – buforuje zapis, zwiększa wydajność.

**Przykład:**
```java
// Zapis do pliku przy użyciu BufferedWriter
try (BufferedWriter writer = new BufferedWriter(new FileWriter("plik.txt"))) {
    writer.write("Przykładowy tekst");
    writer.newLine();
    writer.write("Druga linia");
}
```

## Odczyt z pliku

- **FileReader** – czyta znaki z pliku.
- **BufferedReader** – buforuje odczyt, pozwala czytać linie.
- **Scanner** – wygodne czytanie linii, słów, liczb.

**Przykład:**
```java
// Odczyt z pliku przy użyciu BufferedReader
try (BufferedReader reader = new BufferedReader(new FileReader("plik.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
}
```

```java
// Odczyt z pliku przy użyciu Scanner
try (Scanner scanner = new Scanner(new File("plik.txt"))) {
    while (scanner.hasNextLine()) {
        System.out.println(scanner.nextLine());
    }
}
```

---

## Przykłady użycia różnych klas

### StringBuilder – odczyt całego pliku do Stringa

Pozwala na efektywne budowanie tekstu z wielu fragmentów, np. podczas odczytu pliku znak po znaku.

```java
File file = new File("in.txt");
StringBuilder result = new StringBuilder();
try {
    FileReader reader = new FileReader(file);
    int next;
    while ((next = reader.read()) != -1)
        result.append((char) next);
    reader.close();
} catch (IOException e) {
    System.err.println("Cannot access: " + file.getName());
}
System.out.println(result);
```

---

### BufferedReader – odczyt pliku linia po linii do StringBuildera

BufferedReader pozwala czytać plik linia po linii, co jest wygodne przy analizie tekstu.

```java
File file = new File("in.txt");
StringBuilder result = new StringBuilder();
try {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;
    while ((line = reader.readLine()) != null)
        result.append(line).append("\n");
    reader.close();
} catch (IOException e) {
    System.err.println("Cannot access: " + file.getName());
}
System.out.println(result);
```

---

# Pliki binarne w Javie

### Zapis do pliku binarnego

- **FileOutputStream** – zapis bajtów do pliku.
- **DataOutputStream** – zapis typów prymitywnych w formacie binarnym.

**Przykład:**
```java
// Zapis tablicy bajtów do pliku binarnego
File file = new File("out.bin");
byte[] data = {1, 2, 3, 4, 5};
try {
    FileOutputStream fos = new FileOutputStream(file);
    fos.write(data);
    fos.close();
} catch (IOException e) {
    System.err.println("Cannot access: " + file.getName());
}
```

```java
// Zapis tablicy intów do pliku binarnego (każdy int na 4 bajty)
File file = new File("out.bin");
int[] data = {1, 2, 3, 4, 5};
try {
    FileOutputStream fos = new FileOutputStream(file);
    for (int i = 0; i < data.length; i++) {
        fos.write(data[i] >> 24);
        fos.write(data[i] >> 16);
        fos.write(data[i] >> 8);
        fos.write(data[i]);
    }
    fos.close();
} catch (IOException e) {
    System.err.println("Cannot access: " + file.getName());
}
```

```java
// Zapis tablicy intów przy użyciu DataOutputStream
File file = new File("out.bin");
int[] data = {1, 2, 3, 4, 5};
try {
    FileOutputStream fos = new FileOutputStream(file);
    DataOutputStream dos = new DataOutputStream(fos);
    for (int i = 0; i < data.length; i++)
        dos.writeInt(data[i]);
    dos.close();
} catch (IOException e) {
    System.err.println("Cannot access: " + file.getName());
}
```

### Odczyt z pliku binarnego

- **DataInputStream** – odczyt typów prymitywnych z pliku binarnego.

**Przykład:**
```java
// Odczyt tablicy intów z pliku binarnego
File file = new File("in.bin");
try {
    FileInputStream fis = new FileInputStream(file);
    DataInputStream dis = new DataInputStream(fis);
    int size = (int) (file.length() / Integer.BYTES);
    int[] data = new int[size];
    for(int i=0; i<size; ++i)
        data[i] = dis.readInt();
    for(int value: data) System.out.println(value);
    dis.close();
} catch (IOException e) {
    System.err.println("Cannot access: " + file.getName());
}
```

---

### DataOutputStream – zapis tablicy bajtów do pliku binarnego

Można też zapisywać bajty, choć DataOutputStream jest najczęściej używany do typów prymitywnych.

```java
File file = new File("out.bin");
byte[] data = {1, 2, 3, 4, 5};
try {
    FileOutputStream fos = new FileOutputStream(file);
    DataOutputStream dos = new DataOutputStream(fos);
    for (int i = 0; i < data.length; i++)
        dos.writeByte(data[i]);
    dos.close();
} catch (IOException e) {
    System.err.println("Cannot access: " + file.getName());
}
```

---

### Try-with-resources – automatyczne zamykanie strumieni

Blok try automatycznie zamyka wszystkie zadeklarowane zasoby po zakończeniu działania.

```java
File file = new File("in.bin");
try (
    FileInputStream fis = new FileInputStream(file);
    DataInputStream dis = new DataInputStream(fis);
) {
    int size = (int) (file.length() / Integer.BYTES);
    int[] data = new int[size];
    for(int i=0; i<size; ++i)
        data[i] = dis.readInt();
    for(int value: data) System.out.println(value);
} catch (IOException e) {
    System.err.println("Cannot access: " + file.getName());
}
```

---
java snippety i objasnienia
