# Obsługa JSON w JavaScript

JSON (JavaScript Object Notation) to lekki format wymiany danych, często używany do przesyłania danych między serwerem a aplikacją webową.

## Konwersja obiektu na JSON

Aby zamienić obiekt JavaScript na tekst w formacie JSON, użyj `JSON.stringify`:

```javascript
const person = { name: "Jan", age: 30 };
const json = JSON.stringify(person);
console.log(json); // {"name":"Jan","age":30}
```

## Konwersja JSON na obiekt

Aby zamienić tekst JSON na obiekt JavaScript, użyj `JSON.parse`:

```javascript
const json = '{"name":"Jan","age":30}';
const person = JSON.parse(json);
console.log(person.name); // Jan
console.log(person.age);  // 30
```

## Uwaga

- JSON obsługuje tylko typy: obiekty, tablice, liczby, łańcuchy znaków, wartości logiczne i `null`.
- Funkcje, `undefined` oraz symbole nie są serializowane do JSON.
