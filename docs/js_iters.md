# Pętle i iteratory w JavaScript

## Pętle

JavaScript oferuje kilka rodzajów pętli do iterowania po danych:

### for

```javascript
for (let i = 0; i < 5; i++) {
  console.log(i);
}
```

### for...of

Służy do iterowania po elementach kolekcji iterowalnych (np. tablicach, stringach):

```javascript
const arr = [1, 2, 3];
for (const el of arr) {
  console.log(el);
}
```

### for...in

Służy do iterowania po kluczach (indeksach/właściwościach) obiektu:

```javascript
const obj = { a: 1, b: 2 };
for (const key in obj) {
  console.log(key, obj[key]);
}
```

### while

```javascript
let i = 0;
while (i < 3) {
  console.log(i);
  i++;
}
```

### do...while

```javascript
let i = 0;
do {
  console.log(i);
  i++;
} while (i < 3);
```

## Iteratory

Iterator to obiekt, który pozwala przechodzić po elementach kolekcji. Każdy iterator posiada metodę `next()`, która zwraca obiekt `{ value, done }`.

Przykład własnego iteratora:

```javascript
const iterable = {
  from: 1,
  to: 3,
  [Symbol.iterator]() {
    let current = this.from;
    let last = this.to;
    return {
      next() {
        if (current <= last) {
          return { value: current++, done: false };
        } else {
          return { done: true };
        }
      }
    };
  }
};

for (const num of iterable) {
  console.log(num); // 1, 2, 3
}
```

Wiele wbudowanych obiektów (np. Array, Map, Set, String) jest iterowalnych i można je używać z pętlą `for...of`.
