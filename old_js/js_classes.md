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

## Słowo kluczowe `super`

`super` pozwala odwołać się do metod lub konstruktora klasy nadrzędnej. Najczęściej używane jest w konstruktorze klasy pochodnej, aby wywołać konstruktor klasy bazowej, lub do wywołania metody z klasy nadrzędnej.

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
  constructor(name, breed) {
    super(name); // wywołuje konstruktor klasy Animal
    this.breed = breed;
  }
  speak() {
    super.speak(); // wywołuje metodę speak z klasy Animal
    console.log(`${this.name} szczeka.`);
  }
}

const dog = new Dog('Reksio', 'owczarek');
dog.speak();
// Reksio wydaje dźwięk.
// Reksio szczeka.
```
