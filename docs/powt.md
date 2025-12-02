# Zadania - Bezpieczeństwo Systemów

## ZAD1
**Zahaszuj słowo Linux za pomocą algorytmu MD5.**

```bash
echo -n "Linux" | md5sum
```

**Wynik:**
```
edc9f0a5a5d57797bf68e37364743831  -
```

---

## ZAD2
**Zahaszuj słowo Linux za pomocą algorytmu SHA-256.**

```bash
echo -n "Linux" | sha256sum
```

**Wynik:**
```
4828e60247c1636f57b7446a314e7f599c12b53d40061cc851a1442004354fed  -
```

---

## ZAD3
**Zakoduj słowo Linux za pomocą kodowania base64.**

```bash
echo -n "Linux" | base64
```

**Wynik:**
```
TGludXg=
```

---

## ZAD4
**Odkoduj słowo VU1DUw== zakodowane za pomocą base64.**

```bash
echo -n "VU1DUw==" | base64 -d
```

**Wynik:**
```
UMCS
```

---

## ZAD5
**Zaszyfruj słowo Linux za pomocą algorytmu AES-256-ECB i klucza e592bc9e5fa8618a02edf437bdf3ffd4fcdfbe9e588749db898a3662b461a80e.**

```bash
echo -n "Linux" | openssl enc -e -aes-256-ecb \
-a -K e592bc9e5fa8618a02edf437bdf3ffd4fcdfbe9e588749db898a3662b461a80e
```

**Wynik:**
```
qN3bUtfgJx7jHygE8d8QlQ==
```

---

## ZAD6
**Zaszyfruj słowo Linux za pomocą AES-256-CFB, klucza e592bc9e5fa8618a02edf437bdf3ffd4fcdfbe9e588749db898a3662b461a80e oraz IV 83c468dc477543a6906912b6a1344416.**

```bash
echo -n "Linux" | openssl enc -e -aes-256-cfb \
-K e592bc9e5fa8618a02edf437bdf3ffd4fcdfbe9e588749db898a3662b461a80e \
-iv 83c468dc477543a6906912b6a1344416
```

**Wynik:**
```
~�f�
```

---

## ZAD7
**Zaszyfruj słowo Linux za pomocą AES-256-CFB, klucza e592bc9e5fa8618a02edf437bdf3ffd4fcdfbe9e588749db898a3662b461a80e oraz IV 83c468dc477543a6906912b6a1344416. Wynik szyfrowania zakoduj za pomocą kodowania base64.**

```bash
echo -n "Linux" | openssl enc -e -aes-256-cfb \
-K e592bc9e5fa8618a02edf437bdf3ffd4fcdfbe9e588749db898a3662b461a80e \
-iv 83c468dc477543a6906912b6a1344416 -a
```

**Wynik:**
```
foxmjAM=
```

---

## ZAD8
**Słowo f6NB8w== zostało zaszyfrowane algorytmem AES-256-CFB, kluczem e592bc9e5fa8618a02edf437bdf3ffd4fcdfbe9e588749db898a3662b461a80e oraz IV 83c468dc477543a6906912b6a1344416, a następnie zakodowane za pomocą kodowania base64. Odszyfruj i odkoduj słowo.**

```bash
echo "f6NB8w==" | openssl enc -d -aes-256-cfb \
-K e592bc9e5fa8618a02edf437bdf3ffd4fcdfbe9e588749db898a3662b461a80e \
-iv 83c468dc477543a6906912b6a1344416 -a
```

**Wynik:**
```
MFI
```

---

## ZAD9
**Wygeneruj klucz szyfrujący o długości 128 bitów.**

```bash
openssl rand -hex 16
```

**Wynik:**
```
9dfc24ada94ff5d5aa6cd9fc6d83c619
```

---

## ZAD10
**Zaszyfruj słowo Linux za pomocą AES-128-CBC, bez soli, hasło do generowania klucza to Ubuntu, funkcja generowania klucza to PBKDF2, z liczbą iteracji równą 256.**

```bash
echo -n "Linux" | openssl enc -e -aes-128-cbc \
-pbkdf2 -iter 256 -nosalt -pass pass:Ubuntu | base64
```

**Wynik:**
```
gpIb5SsCn+wwuKhTOCHAqw==
```

---

## ZAD11
**Wygeneruj parę kluczy (publiczny i prywatny), klucze mają 2048 bitów. Wyeksportuj klucze do pliku.**

```bash
# Klucz prywatny (2048 bitów)
openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048

# Klucz publiczny na podstawie prywatnego
openssl rsa -pubout -in private.pem -out public.pem
```

---

## ZAD12
**Wygeneruj parę kluczy (publiczny i prywatny), klucze mają 2048 bitów. Wyeksportuj klucze do pliku. Zaszyfruj słowo Linux kluczem publicznym i odszyfruj prywatnym. Użyj paddingu OAEP.**

```bash
openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in private.pem -out public.pem

echo -n "Linux" | openssl pkeyutl -encrypt -pubin -inkey public.pem -pkeyopt rsa_padding_mode:oaep -out linux.enc
echo -n "Linux" | openssl pkeyutl -encrypt -pubin -inkey public.pem -pkeyopt rsa_padding_mode:oaep | base64

openssl pkeyutl -decrypt -inkey private.pem -pkeyopt rsa_padding_mode:oaep -in linux.enc
```

**Wynik:**
```
Linux
```

---

## ZAD13
**Wygeneruj parę kluczy (publiczny i prywatny), klucze mają 2048 bitów. Wyeksportuj klucze do pliku. Zapisz do pliku słowo Linux. Podpisz plik używając klucza prywatnego, z paddingiem PSS: hash SHA-256, długość soli powinna odpowiadać długości funkcji skrótu. Zweryfikuj wygenerowany podpis.**

```bash
openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048 &&
openssl rsa -pubout -in private.pem -out public.pem

echo -n "Linux" > linux.txt

openssl dgst -sha256 -sigopt rsa_padding_mode:pss -sigopt rsa_pss_saltlen:32 -sign private.pem -out linux.sig linux.txt

openssl dgst -sha256 -sigopt rsa_padding_mode:pss -sigopt rsa_pss_saltlen:32 -verify public.pem -signature linux.sig linux.txt
```

**Wynik:**
```
Verified OK
```

---

## ZAD14
**Złam hash MD5: e2fc714c4727ee9395f324cd2e7f331f za pomocą:**
1. **ataku słownikowego**
2. **generując swój własny słownik, widząc, że hasło ma 4 znaki i składa się z małych liter a-z**
3. **za pomocą maski, widząc, że hasło ma 4 znaki i składa się z małych liter a-z**

**1) Atak słownikowy:**
```bash
echo "e2fc714c4727ee9395f324cd2e7f331f" > hash.txt

john --wordlist=rockyou.txt --format=raw-md5 hash.txt
john --show --format=raw-md5 hash.txt
```

**Wynik:**
```
?:abcd
```

**2) Własny słownik:**
```bash
crunch 4 4 abcdefghijklmnopqrstuvwxyz -o dict.txt
john --wordlist=dict.txt --format=raw-md5 hash.txt
john --show --format=raw-md5 hash.txt
```

**Wynik:**
```
?:abcd
```

```bash
cat ~/.john/john.pot
```

**Wynik:**
```
$dynamic_0$e2fc714c4727ee9395f324cd2e7f331f:abcd
```

**3) Maska:**
```bash
hashcat -a 3 -m 0 hash.txt ?l?l?l?l
```

---

## ZAD15
**Wygeneruj parę kluczy GPG dla maila student@umcs.pl. Klucz RSA, 1024 bity. Wyeksportuj klucz publiczny i prywatny do pliku.**

```bash
gpg --quick-generate-key "student@umcs.pl <student@umcs.pl>" rsa1024 sign 7d

gpg --output public.gpg --armor --export student@umcs.pl
gpg --output private.gpg --armor --export-secret-keys student@umcs.pl
```

---

## ZAD16
**Zaszyfruj słowo Linux algorytmem AES z kluczem 128 bitów, z hasłem Ubuntu używając GPG. Wynik zapisz do pliku. Odszyfruj plik.**

```bash
echo -n "Linux" | gpg --symmetric --cipher-algo AES128 --passphrase Ubuntu --batch --yes -o linux.gpg

gpg --decrypt --passphrase Ubuntu --batch linux.gpg
```

**Wynik:**
```
gpg: AES.CFB encrypted data
gpg: encrypted with 1 passphrase
```

---

## ZAD17
**Wygeneruj parę kluczy RSA (GPG) z domyślnymi parametrami. Zaszyfruj słowo Linux przy użyciu klucza publicznego. Wynik szyfrowania zapisz do pliku. Odszyfruj plik.**

```bash
gpg --delete-secret-keys "test@example.com" &&
gpg --delete-keys "test@example.com"

gpg --quick-generate-key "Test User <test@example.com>" rsa2048 encrypt 1d

echo -n "Linux" | gpg --encrypt --armor --recipient "test@example.com" -o linux.gpg

gpg --decrypt linux.gpg
```

**Wynik:**
```
gpg: encrypted with rsa2048 key, ID 884699B97C59F0CC, created 2025-11-25
      "Test User <test@example.com>"
```

```bash
gpg --output linux.txt --decrypt linux.gpg
cat linux.txt
```

---

## ZAD18
**Wygeneruj parę kluczy RSA (GPG) z domyślnymi parametrami. Zapisz słowo Linux do pliku, podpisz plik przy użyciu klucza publicznego. Użyj kodowania base64. Wynik podpisywania zapisz do pliku. Zweryfikuj podpisany plik.**

```bash
gpg --delete-secret-keys "test@example.com" &&
gpg --delete-keys "test@example.com"

gpg --quick-generate-key "Test User <test@example.com>" rsa2048 sign 1d

echo -n "Linux2" > linux2.txt

gpg --armor --output linux.sig --local-user test@example.com --sign linux2.txt

gpg --verify linux.sig
```

**Wynik:**
```
gpg: Good signature from "Test User <test@example.com>" [ultimate]
```

---

## ZAD19
**Uruchom serwer i przetesuj wysyłanie do serwera pliku / tekstu:**

```bash
docker run -p 5000:5000 ghcr.io/mazurkatarzynaumcs/echoserver:latest

curl -d "text=Hello world" http://127.0.0.1:5000/accepttext
```

**Wynik:**
```json
{"received_text":"Hello world"}
```