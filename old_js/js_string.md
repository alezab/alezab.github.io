# String w JavaScript

Typ `string` służy do przechowywania tekstu. W JavaScript można używać pojedynczych (`'...'`), podwójnych (`"..."`) lub szablonowych (`` `...` ``) cudzysłowów.

## Przykładowe operacje na stringach

### 1. Iterowanie po słowach oddzielonych spacją

```javascript
const text = "Ala ma kota";
const words = text.split(" ");
for (const word of words) {
    console.log(word);
}
// Wynik: "Ala", "ma", "kota"
```

### 2. Usuwanie białych znaków z przodu i z tyłu (trim)

```javascript
const messy = "   Hello world!   ";
const clean = messy.trim();
console.log(clean); // "Hello world!"
```

### 3. Iterowanie po literach

```javascript
const str = "JavaScript";
for (const char of str) {
    console.log(char);
}
```

### 4. Iterowanie po literach od końca

```javascript
const str = "JavaScript";
for (let i = str.length - 1; i >= 0; i--) {
    console.log(str[i]);
}
```

### 5. Sprawdzanie długości stringa

```javascript
const str = "Hello";
console.log(str.length); // 5
```

### 6. Zmiana wielkości liter

```javascript
const str = "JavaScript";
console.log(str.toUpperCase()); // "JAVASCRIPT"
console.log(str.toLowerCase()); // "javascript"
```

### 7. Sprawdzanie, czy string zawiera podciąg

```javascript
const str = "Ala ma kota";
console.log(str.includes("ma")); // true
```

### 8. Zamiana fragmentu tekstu

```javascript
const str = "Ala ma kota";
const newStr = str.replace("kota", "psa");
console.log(newStr); // "Ala ma psa"
```

### 9. Wycinanie fragmentu tekstu (substring, slice)

```javascript
const str = "JavaScript";
console.log(str.substring(0, 4)); // "Java"
console.log(str.slice(-6)); // "Script"
```

### 10. Rozdzielanie stringa na tablicę (split)

```javascript
const str = "a,b,c";
const arr = str.split(",");
console.log(arr); // ["a", "b", "c"]
```

### 11. Łączenie tablicy w string (join)

```javascript
const arr = ["a", "b", "c"];
const str = arr.join("-");
console.log(str); // "a-b-c"
```

### 12. Usuwanie wszystkich białych znaków (replace + regex)

```javascript
const messy = " a b c ";
const clean = messy.replace(/\s/g, "");
console.log(clean); // "abc"
```

### 13. Odwracanie stringa (reverse + join)

```javascript
const str = "JavaScript";
const reversed = str.split("").reverse().join("");
console.log(reversed); // "tpircSavaJ"
```

---
Więcej metod znajdziesz w dokumentacji: [MDN String](https://developer.mozilla.org/pl/docs/Web/JavaScript/Reference/Global_Objects/String)
