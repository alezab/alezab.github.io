# Array (tablica) w JavaScript

Tablica (`Array`) to uporządkowana kolekcja elementów dowolnego typu.

## Tworzenie tablic

```javascript
const arr1 = [1, 2, 3];
const arr2 = new Array(4, 5, 6);
const empty = [];
```

## Podstawowe operacje

### Dostęp do elementów

```javascript
const arr = [10, 20, 30];
console.log(arr[0]); // 10
console.log(arr.length); // 3
```

### Iterowanie po tablicy

```javascript
const arr = [1, 2, 3];
for (const el of arr) {
    console.log(el);
}
```

### Dodawanie i usuwanie elementów

```javascript
const arr = [1, 2, 3];
arr.push(4);      // dodaje na koniec
arr.pop();        // usuwa z końca
arr.unshift(0);   // dodaje na początek
arr.shift();      // usuwa z początku
```

### Sprawdzanie, czy coś jest tablicą

```javascript
console.log(Array.isArray(arr)); // true
```

## Najważniejsze metody tablic

### forEach – wykonuje funkcję dla każdego elementu tablicy

```javascript
arr.forEach((el, i) => {
    console.log(i, el);
});
```

### map – tworzy nową tablicę z wynikami wywołania funkcji na każdym elemencie

```javascript
const squares = arr.map(x => x * x);
```

### filter – zwraca nową tablicę z elementami spełniającymi warunek

```javascript
const even = arr.filter(x => x % 2 === 0);
```

### find / findIndex – znajduje pierwszy element (lub jego indeks) spełniający warunek

```javascript
const found = arr.find(x => x > 1);
const idx = arr.findIndex(x => x > 1);
```

### includes – sprawdza, czy tablica zawiera dany element

```javascript
console.log(arr.includes(2)); // true
```

### indexOf / lastIndexOf – zwraca pierwszy/ostatni indeks danego elementu

```javascript
arr.indexOf(2);      // pierwszy indeks 2
arr.lastIndexOf(2);  // ostatni indeks 2
```

### sort – sortuje tablicę (domyślnie jako stringi, można podać własną funkcję porównującą)

```javascript
const nums = [3, 1, 2];
nums.sort(); // [1, 2, 3] (domyślnie sortuje jako stringi!)
nums.sort((a, b) => a - b); // poprawne sortowanie liczb
```

### reverse – odwraca kolejność elementów w tablicy

```javascript
arr.reverse();
```

### join – łączy elementy tablicy w string

```javascript
const str = arr.join(", ");
```

### split (na stringu, by uzyskać tablicę) – dzieli string na tablicę

```javascript
const txt = "a,b,c";
const arr = txt.split(",");
```

### slice – zwraca fragment tablicy (nie zmienia oryginału)

```javascript
const part = arr.slice(1, 3);
```

### splice – usuwa/dodaje elementy (zmienia oryginał tablicy)

```javascript
arr.splice(1, 2); // usuwa 2 elementy od indeksu 1
arr.splice(1, 0, "x"); // wstawia "x" na indeks 1
```

### concat – łączy tablice

```javascript
const arr2 = [4, 5];
const merged = arr.concat(arr2);
```

### flat – spłaszcza zagnieżdżone tablice

```javascript
const nested = [1, [2, 3], [4, [5]]];
console.log(nested.flat()); // [1, 2, 3, 4, [5]]
console.log(nested.flat(2)); // [1, 2, 3, 4, 5]
```

### reduce – redukuje tablicę do jednej wartości (np. suma)

```javascript
const sum = arr.reduce((acc, x) => acc + x, 0);
```

---

Więcej metod: [MDN Array](https://developer.mozilla.org/pl/docs/Web/JavaScript/Reference/Global_Objects/Array)
