# Obiekty i zmienne w JavaScript

## Zmienne

W JavaScript zmienne deklaruje się za pomocą `let`, `const` lub starszego `var`.

- `let` – zmienna o zasięgu blokowym, można zmieniać wartość.
- `const` – stała o zasięgu blokowym, nie można przypisać nowej wartości.
- `var` – zmienna o zasięgu funkcyjnym (niezalecane w nowym kodzie).

```javascript
let x = 5;
const y = 10;
var z = 15;
```

## Obiekty

Obiekt (`Object`) to kolekcja par klucz-wartość.

### Tworzenie obiektu

```javascript
const person = {
    name: "Jan",
    age: 30,
    isStudent: true
};
```

### Dostęp do właściwości

```javascript
console.log(person.name);      // "Jan"
console.log(person["age"]);    // 30
```

### Dodawanie i usuwanie właściwości

```javascript
person.city = "Warszawa";      // dodanie
delete person.isStudent;       // usunięcie
```

### Iterowanie po właściwościach

```javascript
for (const key in person) {
    console.log(key, person[key]);
}
```

### Metody obiektu

```javascript
const car = {
    brand: "Toyota",
    drive() {
        console.log("Jedzie!");
    }
};
car.drive();
```

### Sprawdzanie istnienia właściwości

```javascript
console.log("name" in person); // true
```

### Pobieranie kluczy, wartości, par [key, value]

```javascript
Object.keys(person);   // ["name", "age", "city"]
Object.values(person); // ["Jan", 30, "Warszawa"]
Object.entries(person); // [["name", "Jan"], ["age", 30], ["city", "Warszawa"]]
```

### Kopiowanie obiektu (płytka kopia)

```javascript
const copy = { ...person };
```

---

Więcej: [MDN Object](https://developer.mozilla.org/pl/docs/Web/JavaScript/Reference/Global_Objects/Object)
