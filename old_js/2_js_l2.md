# JS LEKCJA 2

## Zadanie 1

Zapoznaj się ze sposobami tworzenia obiektów w Javascript https://kursjs.pl/kurs/obiekty/obiekty. Stwórz obiekt currentUser. Obiekt niech ma właściwości: name, surname, email, www, userType, isActive oraz metodę show(), która wypisze wszystkie te właściwości w konsoli. Dodatkowo stwórz metodę setActive(active), która przestawi właściwość isActive na przekazaną wartość, która może być true/false.  

Rozwiązanie:  
```js
const currentUser = {
    name: "Jan",
    surname: "Kowalski",
    email: "jan.kowalski@example.com",
    www: "https://jan-kowalski.pl",
    userType: "admin",
    isActive: false,
    show() {
        console.log("name:", this.name);
        console.log("surname:", this.surname);
        console.log("email:", this.email);
        console.log("www:", this.www);
        console.log("userType:", this.userType);
        console.log("isActive:", this.isActive);
    },
    setActive(active) {
        this.isActive = active;
    }
};

// Przykład użycia:
currentUser.show();
currentUser.setActive(true);
currentUser.show();
```

## Zadanie 2

Zapoznaj się ze sposobami tworzenia klas w Javascript https://kursjs.pl/kurs/obiekty/class. Stwórz klasę book, który będzie miała:  

Właściwości: 

- users która będzie tablicą użytkowników (na początku pusta)  

Metody:  

- addUser(name, age, phone)  
która doda to tablicy users nowy obiekt, który będzie miał klucze: name, age, phone z przekazanymi do tej funkcji danymi  

- showUsers()  
która po odpaleniu wypisze w konsoli tekst: Wszyscy użytkownicy w książce a następnie w pętli wypisze wszystkich użytkowników  

- findByName(name)  
która wypisze w konsoli pierwszego użytkownika, który ma szukane imię lub false jeżeli nie znajdzie.  

- findByPhone(phone)  
która wypisze w konsoli pierwszego użytkownika, który ma szukany telefon lub false jeżeli nie znajdzie  

- getCount()  
która wypisze ile jest użytkowników w tabeli  

Rozwiązanie:  
```js
class Book {
    constructor() {
        this.users = [];
    }

    addUser(name, age, phone) {
        this.users.push({ name, age, phone });
    }

    showUsers() {
        console.log("Wszyscy użytkownicy w książce:");
        for (const user of this.users) {
            console.log(user);
        }
    }

    findByName(name) {
        const user = this.users.find(u => u.name === name);
        console.log(user || false);
    }

    findByPhone(phone) {
        const user = this.users.find(u => u.phone === phone);
        console.log(user || false);
    }

    getCount() {
        console.log(this.users.length);
    }
}

// Przykład użycia:
const book = new Book();
book.addUser("Jan", 30, "123456789");
book.showUsers();
book.findByName("Jan");
book.findByPhone("123456789");
book.getCount();
```

## Zadanie 3

Stwórz obiekt o nazwie text. Obiekt ten powinien mieć metody:  

- check(txt, word)  
która zwraca true/false jeżeli w tekście txt znajduje się szukane słowo word  
check("ala ma kota", "kota") ---> true  

- getCount(txt)  
Zwraca liczbę liter w tekście txt. Uwaga znaki interpunkcyjne też traktujmy jako litery  
getCount("ala ma kota") ---> 11  

- getWordsCount(txt)  
zwraca liczbę słów w przekazanym tekście  
getWordsCount("Ala ma kota") ---> 3  

- setCapitalize(txt)  
zwraca nowy tekst tak zamieniony, że każde słowo zaczyna się z dużej litery  
setCapitalize("ala ma kota") ---> "Ala Ma Kota"   

- setMix(txt)  
zwraca nowy tekst z naprzemiennie dużymi/małymi literami. Spację i znaki interpunkcyjne traktujemy jako litery.  
setMix("ala ma kota") ---> "aLa mA KoTa"  

- generateRandom(lng)  
Generuje tekst o długości lng, który składa się z losowych liter  
generateRandom(10) ---> "dkjiuhtjox"  


Rozwiązanie:  
```js
const text = {
    check(txt, word) {
        return txt.includes(word);
    },
    getCount(txt) {
        return txt.length;
    },
    getWordsCount(txt) {
        return txt.trim().split(/\s+/).length;
    },
    setCapitalize(txt) {
        return txt
            .split(' ')
            .map(word => word.charAt(0).toUpperCase() + word.slice(1))
            .join(' ');
    },
    setMix(txt) {
        let result = '';
        for (let i = 0; i < txt.length; i++) {
            result += i % 2 === 0
                ? txt[i].toLowerCase()
                : txt[i].toUpperCase();
        }
        return result;
    },
    generateRandom(lng) {
        let result = '';
        const chars = 'abcdefghijklmnopqrstuvwxyz';
        for (let i = 0; i < lng; i++) {
            result += chars.charAt(Math.floor(Math.random() * chars.length));
        }
        return result;
    }
};

// Przykład użycia:
text.check("ala ma kota", "kota"); // true
text.getCount("ala ma kota"); // 11
text.getWordsCount("Ala ma kota"); // 3
text.setCapitalize("ala ma kota"); // "Ala Ma Kota"
text.setMix("ala ma kota"); // "aLa mA KoTa"
text.generateRandom(10); // np. "dkjiuhtjox"
```

## Zadanie 4

Zapoznaj się z informacjami o dziedziczeniu prototypowym w Javascript.  

a) Jak się to ma do działania obiektów i “klas” w Javascript? Dlaczego mówimy, że w Javascript klasy są tzw. “syntax sugar”? Opisz mechanizm własnymi słowami.  

b) Rozbuduj obiekty typu String dodając im metodę mirror(), która po odpaleniu dla tekstu zwróci jego odbicie:  

"Ala ma kota".mirror() === "atok am alA"  

Rozwiązanie:  
```js
// a)
// W JavaScript obiekty dziedziczą właściwości i metody po innych obiektach poprzez tzw. prototypy.
// Każdy obiekt ma ukryte odwołanie do swojego prototypu (np. Object.prototype, Array.prototype).
// Klasy w JavaScript są tzw. "syntactic sugar" – uproszczoną składnią dla mechanizmu prototypowego.
// Pod spodem klasa to funkcja, a metody trafiają do prototypu tej funkcji.
// Dzięki temu możemy korzystać z dziedziczenia i współdzielić metody między instancjami.

// b)
String.prototype.mirror = function () {
    return this.split("").reverse().join("");
};

// Przykład użycia:
console.log("Ala ma kota".mirror()); // "atok am alA"
```

## Zadanie 5

Zapoznaj się z informacjami o tzw. domknięciach (ang. clousures) w JavaScript.  

a) Opisz mechanizm własnymi słowami.  

b) Stwórz funkcję createCounter, która zwraca funkcję inkrementującą licznik. Funkcja zwrócona przez createCounter powinna:  

Zwiększać wartość licznika o 1 przy każdym wywołaniu,
Zwracać bieżącą wartość licznika po każdej inkrementacji.  
Dodatkowo funkcja createCounter powinna działać tak, że za każdym razem, gdy wywołujesz createCounter, tworzysz nowy licznik, który ma swoją własną odrębną wartość, niezależną od innych liczników. Użyj w tym celu mechanizmu domknięć.  

Przykład:  
```js
const counter1 = createCounter();
console.log(counter1()); // 1
console.log(counter1()); // 2
console.log(counter1()); // 3

const counter2 = createCounter();
console.log(counter2()); // 1
console.log(counter2()); // 2
```

Rozwiązanie:  
```js
// a)
// Domknięcie (closure) w JavaScript to mechanizm, w którym funkcja zapamiętuje swoje otoczenie (zakres zmiennych),
// nawet po zakończeniu działania funkcji, w której została utworzona. Dzięki temu funkcja wewnętrzna ma dostęp
// do zmiennych zadeklarowanych w funkcji zewnętrznej, nawet jeśli ta już się wykonała.

// b)
function createCounter() {
    let count = 0;
    return function () {
        count += 1;
        return count;
    };
}

function f() {
    let x = 5;
    return function () {
        return x;
    }
}

console.log(f()()); // 5

let y = f();
console.log(y()); // 5

// Przykład użycia:
const counter1 = createCounter();
console.log(counter1()); // 1
console.log(counter1()); // 2
console.log(counter1()); // 3

const counter2 = createCounter();
console.log(counter2()); // 1
console.log(counter2()); // 2
```