# JS LEKCJA 1

## Zadanie 1

Uruchom przykładowy program w Javascript.  

```js
let a = 10;
console.log(a == 10)
console.log(a == "10")
console.log(a != 20)
console.log(a != 10)
console.log(a != "10")
console.log(a === 10)
console.log(a === "10")
console.log(a !== 10)
console.log(a !== "10")
```
Zapoznaj się z operatorami w Javascript. https://kursjs.pl/kurs/super-podstawy/operatory.

Opisz różnicę między operatorem == a operatorem ===.  

Rozwiązanie:  
```js
let a = 10;

// == compares values after type coercion
console.log(a == 10)      // true, same value and type
console.log(a == "10")    // true, "10" is coerced to 10

// != compares values after type coercion
console.log(a != 20)      // true, 10 != 20
console.log(a != 10)      // false, 10 == 10
console.log(a != "10")    // false, "10" is coerced to 10

// === compares value and type (strict equality)
console.log(a === 10)     // true, same value and type
console.log(a === "10")   // false, different types

// !== compares value and type (strict inequality)
console.log(a !== 10)     // false, same value and type
console.log(a !== "10")   // true, different types
```

## Zadanie 2

Zapoznaj się z typami zmiennych w Javascript: https://kursjs.pl/kurs/super-podstawy/zmienne  

a) Opisz różnicę między let / const / var.  
b) Z jakiego rodzaju typowania korzysta Javascript?  

Rozwiązanie:  
```js
// a) Różnice między let / const / var:

// let - zmienna o zasięgu blokowym, można zmieniać jej wartość, nie można zadeklarować ponownie w tym samym bloku
let zmiennaLet = 5;
zmiennaLet = 10; // OK
// let zmiennaLet = 15; // Błąd: Identifier 'zmiennaLet' has already been declared

// const - stała o zasięgu blokowym, nie można zmienić wartości ani zadeklarować ponownie
const STALA = 20;
// STALA = 30; // Błąd: Assignment to constant variable

// var - zmienna o zasięgu funkcyjnym, można zmieniać wartość i deklarować ponownie
var zmiennaVar = 100;
var zmiennaVar = 200; // OK

// Przykład zasięgu:
{
    let x = 1;
    var y = 2;
}
// console.log(x); // Błąd: x is not defined
console.log(y); // 2

// b) Javascript korzysta z typowania dynamicznego i słabego (ang. dynamic and weak typing).
// Typ zmiennej może się zmieniać w trakcie działania programu, a operacje na różnych typach mogą prowadzić do konwersji.

let a = 10;      // liczba
a = "tekst";     // teraz string

let wynik = 5 + "5"; // "55" - liczba + string daje string (konwersja typów)
let suma = 5 + 5;    // 10 - liczby dodane

// typeof pozwala sprawdzić typ zmiennej:
console.log(typeof a);      // "string"
console.log(typeof suma);   // "number"
```
## Zadanie 3

Zapoznaj się ze sposobami konstruowania funkcji: https://kursjs.pl/kurs/super-podstawy/funkcje. Napisz funkcję printNumbers(nr), która wymagać będzie liczby. Funkcja powinna zwrócić tekst, który będzie składał się z kolejnych liczb.  

Rozwiązanie:  
```js
function printNumbers(nr) {
    let result = '';
    for (let i = 1; i <= nr; i++) {
        result += i;
        if (i < nr) result += ' ';
    }
    return result;
}

console.log(printNumbers(5)); // "1 2 3 4 5"
```

## Zadanie 4

Stwórz funkcję generateRandom(min, max), która będzie przyjmować dwie wartości - min i max. Funkcja powinna zwrócić losową liczbę z podanego przedziału. Do generowania losowej liczby możesz użyć wzoru Math.floor(Math.random()*(max-min+1)+min).  

Wykorzystaj ją do utworzenia 10 liczb z przedziału 1 - 20. Jeżeli połowa z nich będzie większa od 10, wypisz tekst udało się, w przeciwnym razie wypisz w konsoli tekst niestety nie.  

Rozwiązanie:  
```js
function generateRandom(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
}

const numbers = [];
for (let i = 0; i < 10; i++) {
    numbers.push(generateRandom(1, 20));
}

const countAbove10 = numbers.filter(n => n > 10).length;

if (countAbove10 >= 5) {
    console.log('udało się');
} else {
    console.log('niestety nie');
}
```

## Zadanie 5

Zapoznaj się ze sposobami przetwarzania napisów: https://kursjs.pl/kurs/super-podstawy/string. Napisz funkcję checkPalindrom(txt), która zwróci true/false w zależności od tego, czy przekazane słowo jest palindromem.  

Rozwiązanie:  
```js
function checkPalindrom(txt) {
    let reversedTxt = txt.split("").reverse().join("");
    return txt === reversedTxt;
}

console.log(checkPalindrom("kot"));
```

## Zadanie 6

Zapoznaj się ze sposobami działania na tablicach: https://kursjs.pl/kurs/super-podstawy/tablice. Stwórz funkcję random(max), która będzie zwracać losową liczbę z zakresu 0 - max. Wykorzystaj tą funkcję do wygenerowania 20-elementowej tablicy. Posortuj tą tablicę, a następnie wypisz sumę oraz średnią wszystkich liczb z tej tablicy.  

Rozwiązanie:  
```js
function random(max) {
    return Math.floor(Math.random() * (max + 1));
}

const arr = [];
for (let i = 0; i < 20; i++) {
    arr.push(random(100)); // przykładowy max = 100
}

// Funkcja sort przyjmuje funkcję porównującą dwa elementy tablicy: a i b.
// Jeśli wynik jest ujemny, a znajdzie się przed b; jeśli dodatni - po b; jeśli 0 - bez zmian.
// (a, b) => a - b sortuje liczby rosnąco (od najmniejszej do największej).
arr.sort((a, b) => a - b);

// Funkcja reduce sumuje wszystkie elementy tablicy.
// acc (akumulator) to bieżąca suma, val to aktualny element tablicy.
// Dla każdego elementu do akumulatora dodawana jest jego wartość.
const sum = arr.reduce((acc, val) => acc + val, 0);

// Średnia to suma podzielona przez liczbę elementów tablicy
const avg = sum / arr.length;

console.log('Tablica:', arr);
console.log('Suma:', sum);
console.log('Średnia:', avg);
```

## Zadanie 7

Biorąc pod uwagę tablicę liczb całkowitych nums posortowaną w porządku niemalejącym, usuń duplikaty w miejscu, tak aby każdy unikalny element pojawił się tylko raz. Względna kolejność elementów powinna pozostać taka sama. Następnie zwróć liczbę unikalnych elementów w nums.  

Rozważmy, że liczba unikalnych elementów numswynosi k, aby uzyskać akceptację, należy wykonać następujące czynności:  

Zmienić tablicę numsw taki sposób, aby pierwsze k elementów nums zawierało unikalne elementy w kolejności, w jakiej były one początkowo obecne w nums. Pozostałe elementy nums nie mają znaczenia, podobnie jak rozmiar nums.
Zwraca k.  

Przykład:  
```js
Input: nums = [1,1,2]
Output: 2, nums = [1,2,_]
```

Wyjaśnienie:  
Twoja funkcja powinna zwrócić k = 2, przy czym pierwsze dwa elementy nums to odpowiednio 1 i 2. Nie ma znaczenia, co pozostawisz poza zwróconym k (dlatego są to podkreślenia).  

```js
function removeDuplicates(nums) {
    if (nums.length === 0) return 0;
    let k = 1; // wskaźnik na miejsce do wstawienia unikalnej wartości
    for (let i = 1; i < nums.length; i++) {
        if (nums[i] !== nums[k - 1]) {
            nums[k] = nums[i];
            k++;
        }
    }
    return k;
}

// Przykład użycia:
const nums = [1, 1, 2];
const k = removeDuplicates(nums);

// Nadpisujemy pozostałe elementy znakiem "_"
for (let i = k; i < nums.length; i++) {
    nums[i] = "_";
}

console.log(k, nums); // 2, [1, 2, "_"]
// Po wykonaniu removeDuplicates(nums) dla [1, 1, 2]:
// nums = [1, 2, "_"]
// Pierwsze k=2 elementy to unikalne wartości, reszta (np. nums[2]) może być dowolna.
```

## Zadanie 8

Napisz funkcję znajdującą najdłuższy wspólny ciąg prefiksów w tablicy ciągów. Jeśli nie ma wspólnego prefiksu, zwróć pusty ciąg.  

Przykład 1:  
```js
Input: strs = ["flower","flow","flight"]
Output: "fl"
```

Przykład 2:  
```js
Input: strs = ["dog","racecar","car"]
Output: ""
```

Założenia:  
- 1 <= strs.length <= 200
- 0 <= strs[i].length <= 200
- strs[i] składa się tylko z małych angielskich liter.

Rozwiązanie:  
```js
function najdluzszyPrefiks(strs) {
    if (!strs.length) return "";
    let prefix = strs[0];
    for (let i = 1; i < strs.length; i++) {
        while (strs[i].indexOf(prefix) !== 0) {
            prefix = prefix.split("");
            prefix.pop();
            prefix = prefix.join("");
            if (!prefix) return "";
        }
    }
    return prefix;
}

// Przykłady użycia
console.log(najdluzszyPrefiks(["flower", "flow", "flight"])); // "fl"
console.log(najdluzszyPrefiks(["dog", "racecar", "car"])); // ""
console.log(najdluzszyPrefiks([""])) // ""

// Przykład działania indexOf:
console.log("flower".indexOf("fl")); // 0, bo "fl" jest na początku
console.log("flower".indexOf("ow")); // 2, bo "ow" zaczyna się na pozycji 2
console.log("flower".indexOf("abc")); // -1, bo "abc" nie występuje

function prefix(arr) {
    let x = "";
    for (let i = 0; i < arr[0].length; i++) {
        for (let j = 0; j < arr.length; j++) {
            if (arr[j][i] !== arr[0][i]) {
                return x;
            }
        }
        x += arr[0][i];
    }
    return x;
}

console.log(prefix(["flower", "flow", "flight"])); // "fl"
console.log(prefix(["dog", "racecar", "car"])); // ""
```

## Zadanie 9

Cyfry rzymskie są reprezentowane przez siedem różnych symboli: I, V, X, L, C, D i M. Podając liczbę rzymską, przekonwertuj ją na liczbę całkowitą.  

Przykład 1:  
```js
Input: s = "III"
Output: 3
```

Przykład 2:  
```js
Input: s = "LVIII"
Output: 58
```

Przykład 3:  
```js
Input: s = "MCMXCIV"
Output: 1994
```

Założenia:  
- 1 <= s.length <= 15  
- s zawiera wyłącznie znaki ('I', 'V', 'X', 'L', 'C', 'D', 'M').  
- Zakładamy że s jest poprawną liczbą rzymską z zakresu [1, 3999].  

Rozwiązanie:  
```js
function romanToInt(s) {
    let reversedS = s.split("").reverse().join("");
    const romanMap = {
        'I': 1,
        'V': 5,
        'X': 10,
        'L': 50,
        'C': 100,
        'D': 500,
        'M': 1000
    };
    let res = 0;
    let previous = 0;
    for (let i = 0; i < s.length; i++) {
        const roman = romanMap[reversedS[i]];
        if (roman < previous) {
            res -= roman;
        } else {
            res += roman;
        }
        previous = roman;
    }
    return res;
}

console.log(romanToInt("III"));
console.log(romanToInt("LVIII"));
console.log(romanToInt("MCMXCIV"));
```

## Zadanie 10

Jako poszukiwacz przygód, doszedłeś do tajemniczej komnaty, na ścianie której znajduje się tajemniczy zapis:  

```js
const arr = [
	[ 66,  97, 114, 100,   4,   2, 110,  11,   1,   6,  20, ],
	[ 99,   3,  10, 122,  76, 101, 111,   3,  32, 100,   0, ],
	[  6,  22,   1, 111,  32,  10, 110,   7,  97,  97,  67, ],
	[ 60,  97, 116,  32, 100,  23,  97, 114, 100,  32,  34, ],
	[  2, 106,  15,   6, 111,  56,  80,  20,  10,  86,  10, ],
	[ 20, 110,  121, 32, 107,  55,  50,  99, 110, 105,   8, ],
	[ 12,   9,  22, 102,  66, 100,  12, 105,  50,  76, 110, ],
	[ 42,  81, 123,  92,  26,  98,  20,   1,  20,  11,  10, ],
]
```

W starych notatkach znajdujesz tylko krótki ciąg, który ponoć wskazuje kierunek poruszania się po powyższym zapisie.  

```js
const str = "rrrdddllddrrruuuurrddrruurddddlld";
```

Twoim zadaniem jest zdekodować ukrytą informację. Dekodowanie rozpoczynasz w lewym górnym rogu, a następnie powinieneś się poruszać zgodnie z literami, gdzie r oznacza prawo, d dół, l lewo, a u górę.  

Po pobraniu kodów z odpowiednich miejsc zamień je na litery za pomocą String.fromCharCode(x). Wynikiem powinien być wypisane w konsoli hasło. 

Rozwiązanie:  
```js
const arr = [
    [66, 97, 114, 100, 4, 2, 110, 11, 1, 6, 20,],
    [99, 3, 10, 122, 76, 101, 111, 3, 32, 100, 0,],
    [6, 22, 1, 111, 32, 10, 110, 7, 97, 97, 67,],
    [60, 97, 116, 32, 100, 23, 97, 114, 100, 32, 34,],
    [2, 106, 15, 6, 111, 56, 80, 20, 10, 86, 10,],
    [20, 110, 121, 32, 107, 55, 50, 99, 110, 105, 8,],
    [12, 9, 22, 102, 66, 100, 12, 105, 50, 76, 110,],
    [42, 81, 123, 92, 26, 98, 20, 1, 20, 11, 10,],
]

const str = "rrrdddllddrrruuuurrddrruurddddlld";

let pos_x = 0;
let pos_y = 0;

function moveInArr(movement) {
    switch (movement) {
        case "r":
            pos_x++;
            break;
        case "l":
            pos_x--;
            break;
        case "u":
            pos_y--;
            break;
        case "d":
            pos_y++;
            break;
        default:
            console.log("Invalid movement");
            break;
    }
}

function getCode(arr, str) {
    let res = String.fromCharCode(arr[pos_y][pos_x]);
    for (let i = 0; i < str.length; i++) {
        const movement = str[i];
        moveInArr(movement);
        res += String.fromCharCode(arr[pos_y][pos_x]);
    }
    return res;
}

console.log(getCode(arr, str));
```

## Zadanie 11

Biorąc pod uwagę ciąg znaków s składającego się ze słów i spacji, napisz funkcję która zwraca długość ostatniego słowa w ciągu. Słowo jest maksymalnym podciągiem składającym się wyłącznie ze znaków innych niż spacje.  

Przykład 1:  
```js
Input: s = "Hello World"
Output: 5
```

Przykład 2:  
```js
Input: s = "   fly me   to   the moon  "
Output: 4
```

Założenia  
- 1 <= s.length <= 104
- s zawiera tylko znaki angielskiego alfabetu i spacje ' '.
- W s znajduje się co najmniej jedno słowo.

Rozwiązanie:  
```js
function getLastWordLength(s) {
    let lastWord = s.trimEnd().split(' ').reverse()[0];
    return lastWord.length;

}

console.log(getLastWordLength("Hello World"));
console.log(getLastWordLength("   fly me   to   the moon  "));
```

## Zadanie 12

Wspinasz się po schodach. Dotarcie na szczyt zajmuje n kroków. Za każdym razem można pokonać 1 lub 2 stopnie. Na ile różnych sposobów można wspiąć się na szczyt?  

Przykład 1:  
```js
Input: n = 2
Output: 2
```

Wyjaśnienie:  
- 1 stopień + 1 stopień
- 2 stopnie

Przykład 2:  
```js
Input: n = 3
Output: 3
```

Wyjaśnienie:  
- 1 stopień + 1 stopień + 1 stopień
- 1 stopień + 2 stopnie
- 2 stopnie + 1 stopień

Założenia:  
- 1 <= n <= 45

Rozwiązanie:  
```js
function factorial(n) {
    if (n === 0 || n === 1) {
        return 1;
    }
    return n * factorial(n - 1);
}

function getCombinationsCount(n) {
    let res = 0;
    for (let k = 0; k <= n / 2; k++) {
        res += factorial(n - k) / (factorial(k) * factorial(n - 2 * k));
    }
    return res;
}

console.log(getCombinationsCount(2));
console.log(getCombinationsCount(3));
console.log(getCombinationsCount(4));
```

## Bonus

Int to roman:  
```js
function intToRoman(num) {
    const val = [
        1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1
    ];
    const syms = [
        "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"
    ];
    let res = '';
    for (let i = 0; i < val.length; i++) {
        while (num >= val[i]) {
            num -= val[i];
            res += syms[i];
        }
    }
    return res;
}

console.log(intToRoman(3));
console.log(intToRoman(58));
console.log(intToRoman(1994));
```