# IntelliJ IDEA Live Templates (Java)

## 📤 Output Templates

| Abbreviation | Description                       | Expanded Code                                      |
|--------------|-----------------------------------|----------------------------------------------------|
| `sout`       | Print to stdout                   | `System.out.println();`                            |
| `soutv`      | Print variable                    | `System.out.println("var = " + var);`              |
| `soutm`      | Print current method name         | `System.out.println("methodName");`                |
| `soutp`      | Print method parameters           | `System.out.println("param = " + param);`          |
| `serr`       | Print to stderr                   | `System.err.println();`                            |

---

## 🧵 Main Method

| Abbreviation | Description           | Expanded Code                           |
|--------------|-----------------------|-----------------------------------------|
| `psvm`       | Main method           | `public static void main(String[] args)`|
| `main`       | Alias for `psvm`      | Same as above                           |

---

## 🔄 Loops

| Abbreviation | Description                        | Expanded Code                                             |
|--------------|------------------------------------|-----------------------------------------------------------|
| `fori`       | Classic for loop                   | `for (int i = 0; i < length; i++)`                        |
| `iter`       | Enhanced for-each loop             | `for (Type item : collection)`                            |
| `itar`       | For loop over array                | `for (int i = 0; i < arr.length; i++)`                    |
| `while`      | While loop                         | `while (condition) { }`                                   |

---

## 🔁 Conditionals

| Abbreviation | Description                | Expanded Code                   |
|--------------|----------------------------|----------------------------------|
| `ifn`        | If null check              | `if (object == null)`           |
| `inn`        | If not null check          | `if (object != null)`           |
| `instanceof` | Instance check             | `if (object instanceof Type)`   |
| `switch`     | Switch statement           | `switch (expression)`           |

---

## ⚠️ Exceptions

| Abbreviation | Description                       | Expanded Code                                 |
|--------------|-----------------------------------|-----------------------------------------------|
| `try`        | Try-catch block                   | `try { } catch (Exception e) { }`             |
| `tryf`       | Try-with-resources                | `try (Resource res = ...) { }`                |
| `thr`        | Throw new Exception               | `throw new Exception();`                      |

---

## 🔙 Return / Declaration

| Abbreviation | Description        | Expanded Code         |
|--------------|--------------------|------------------------|
| `return`     | Return statement   | `return ;`             |
| `null`       | Return null        | `return null;`         |

---

## 💡 Class / Method / Field Templates

| Abbreviation | Description        | Expanded Code Example           |
|--------------|--------------------|----------------------------------|
| `inst`       | instanceof check   | `if (object instanceof Type)`    |
| `soutc`      | Print class        | `System.out.println(getClass());`|
| `soutm`      | Print method name  | `System.out.println("method");`  |

---

## 🧠 Tips

- Use **Tab** to expand abbreviations.
- Customize or add your own in `Settings > Editor > Live Templates`.
- Use `$END$` in custom templates to mark cursor placement.

