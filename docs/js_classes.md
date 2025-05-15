# Klasy w JavaScript

Klasa w JavaScript to szablon do tworzenia obiektów z określonymi właściwościami (polami) i funkcjonalnościami (metodami).

## Definiowanie klasy

```javascript
class Person {
  // Pole (właściwość) klasy
  name;

  // Konstruktor - wywoływany przy tworzeniu obiektu
  constructor(name) {
    this.name = name;
  }

  // Metoda klasy
  greet() {
    console.log(`Cześć, jestem ${this.name}`);
  }
}
```

## Tworzenie obiektu

```javascript
const person1 = new Person('Jan');
person1.greet(); // Cześć, jestem Jan
```

## Pola i metody statyczne

Pola i metody statyczne należą do klasy, a nie do instancji.

```javascript
class MathHelper {
  static PI = 3.14;

  static square(x) {
    return x * x;
  }
}

console.log(MathHelper.PI); // 3.14
console.log(MathHelper.square(5)); // 25
```

## Dziedziczenie klas

```javascript
class Animal {
  constructor(name) {
    this.name = name;
  }
  speak() {
    console.log(`${this.name} wydaje dźwięk.`);
  }
}

class Dog extends Animal {
  speak() {
    console.log(`${this.name} szczeka.`);
  }
}

const dog = new Dog('Reksio');
dog.speak(); // Reksio szczeka.
```
