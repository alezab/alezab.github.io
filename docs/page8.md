# Linux Geeks

## Lesson 6

1. Napisz program który tworzy trzy procesy - proces macierzysty i jego dwa procesy potomne. Pierwszy z procesów potomnych powinien zapisać do potoku napis „HALLO!", a drugi proces potomny powinien ten napis odczytać.

2. Napisz program który tworzy trzy procesy, z których dwa zapisują do potoku, a trzeci odczytuje z niego i drukuje otrzymane komunikaty.

3. Jako rozszerzenie zadania 1, proszę skorzystać z połączeń nazwanych (przy wykorzystaniu funkcji mkfifo)

#### Zadanie 1:
Program tworzy trzy procesy: jeden proces macierzysty i dwa procesy potomne. Pierwszy proces potomny zapisuje do potoku napis „HALLO!", a drugi proces potomny ten napis odczytuje.

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>

int main() {
    int fd[2];
    pid_t pid1, pid2;
    char message[] = "HALLO!";
    char buffer[100];

    // Tworzymy potok
    if (pipe(fd) == -1) {
        perror("pipe");
        exit(1);
    }

    // Tworzymy pierwszy proces potomny
    if ((pid1 = fork()) == -1) {
        perror("fork");
        exit(1);
    }

    if (pid1 == 0) { // Kod pierwszego procesu potomnego
        close(fd[0]); // Zamykamy niepotrzebny koniec do odczytu
        write(fd[1], message, strlen(message) + 1);
        close(fd[1]); // Zamykamy koniec do zapisu po zakończeniu
        exit(0);
    }

    // Tworzymy drugi proces potomny
    if ((pid2 = fork()) == -1) {
        perror("fork");
        exit(1);
    }

    if (pid2 == 0) { // Kod drugiego procesu potomnego
        close(fd[1]); // Zamykamy niepotrzebny koniec do zapisu
        read(fd[0], buffer, sizeof(buffer));
        printf("Odczytano: %s\n", buffer);
        close(fd[0]); // Zamykamy koniec do odczytu po zakończeniu
        exit(0);
    }

    // Proces macierzysty czeka na zakończenie procesów potomnych
    close(fd[0]);
    close(fd[1]);
    wait(NULL);
    wait(NULL);

    return 0;
}
```

**Wyjaśnienie:**
- `pipe(fd)` tworzy potok.
- `fork()` tworzy nowy proces. Dwa razy tworzymy nowe procesy, by uzyskać dwa procesy potomne.
- Pierwszy proces potomny zapisuje wiadomość "HALLO!" do potoku.
- Drugi proces potomny odczytuje wiadomość z potoku i wypisuje ją na konsolę.
- Proces macierzysty czeka na zakończenie procesów potomnych za pomocą `wait(NULL)`.

#### Zadanie 2:
Program tworzy trzy procesy: dwa z nich zapisują do potoku, a trzeci odczytuje z niego i drukuje otrzymane komunikaty.

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>

int main() {
    int fd[2];
    pid_t pid1, pid2, pid3;
    char message1[] = "Message from process 1";
    char message2[] = "Message from process 2";
    char buffer[100];

    // Tworzymy potok
    if (pipe(fd) == -1) {
        perror("pipe");
        exit(1);
    }

    // Tworzymy pierwszy proces potomny
    if ((pid1 = fork()) == -1) {
        perror("fork");
        exit(1);
    }

    if (pid1 == 0) { // Kod pierwszego procesu potomnego
        close(fd[0]); // Zamykamy niepotrzebny koniec do odczytu
        write(fd[1], message1, strlen(message1) + 1);
        close(fd[1]); // Zamykamy koniec do zapisu po zakończeniu
        exit(0);
    }

    // Tworzymy drugi proces potomny
    if ((pid2 = fork()) == -1) {
        perror("fork");
        exit(1);
    }

    if (pid2 == 0) { // Kod drugiego procesu potomnego
        close(fd[0]); // Zamykamy niepotrzebny koniec do odczytu
        write(fd[1], message2, strlen(message2) + 1);
        close(fd[1]); // Zamykamy koniec do zapisu po zakończeniu
        exit(0);
    }

    // Tworzymy trzeci proces potomny
    if ((pid3 = fork()) == -1) {
        perror("fork");
        exit(1);
    }

    if (pid3 == 0) { // Kod trzeciego procesu potomnego
        close(fd[1]); // Zamykamy niepotrzebny koniec do zapisu
        while (read(fd[0], buffer, sizeof(buffer)) > 0) {
            printf("Odczytano: %s\n", buffer);
        }
        close(fd[0]); // Zamykamy koniec do odczytu po zakończeniu
        exit(0);
    }

    // Proces macierzysty czeka na zakończenie procesów potomnych
    close(fd[0]);
    close(fd[1]);
    wait(NULL);
    wait(NULL);
    wait(NULL);

    return 0;
}
```

**Wyjaśnienie:**
- Dwa procesy potomne zapisują różne wiadomości do potoku.
- Trzeci proces potomny odczytuje wszystkie wiadomości z potoku i wypisuje je na konsolę.
- Proces macierzysty czeka na zakończenie wszystkich procesów potomnych.

#### Zadanie 3:
Program podobny do Zadania 1, ale zamiast zwykłego potoku używamy potoku nazwanego (FIFO).

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <fcntl.h>

#define FIFO_NAME "myfifo"

int main() {
    pid_t pid1, pid2;
    char message[] = "HALLO!";
    char buffer[100];

    // Tworzymy potok nazwany
    if (mkfifo(FIFO_NAME, 0666) == -1) {
        perror("mkfifo");
        exit(1);
    }

    // Tworzymy pierwszy proces potomny
    if ((pid1 = fork()) == -1) {
        perror("fork");
        exit(1);
    }

    if (pid1 == 0) { // Kod pierwszego procesu potomnego
        int fd = open(FIFO_NAME, O_WRONLY);
        if (fd == -1) {
            perror("open");
            exit(1);
        }
        write(fd, message, strlen(message) + 1);
        close(fd); // Zamykamy plik po zakończeniu
        exit(0);
    }

    // Tworzymy drugi proces potomny
    if ((pid2 = fork()) == -1) {
        perror("fork");
        exit(1);
    }

    if (pid2 == 0) { // Kod drugiego procesu potomnego
        int fd = open(FIFO_NAME, O_RDONLY);
        if (fd == -1) {
            perror("open");
            exit(1);
        }
        read(fd, buffer, sizeof(buffer));
        printf("Odczytano: %s\n", buffer);
        close(fd); // Zamykamy plik po zakończeniu
        exit(0);
    }

    // Proces macierzysty czeka na zakończenie procesów potomnych
    wait(NULL);
    wait(NULL);

    // Usuwamy potok nazwany
    unlink(FIFO_NAME);

    return 0;
}
```

**Wyjaśnienie:**
- `mkfifo(FIFO_NAME, 0666)` tworzy potok nazwany o nazwie `myfifo`.
- Procesy potomne używają `open()` do otwierania potoku nazwanego do zapisu (`O_WRONLY`) i odczytu (`O_RDONLY`).
- Wiadomość jest zapisywana do potoku przez pierwszy proces potomny i odczytywana przez drugi proces potomny.
- Proces macierzysty czeka na zakończenie procesów potomnych, a następnie usuwa potok nazwany za pomocą `unlink(FIFO_NAME)`.

## Lesson 7

1. Napisz program który tworzy wątek (pthread_create) który między innymi przyjmie parametr funkcji robiącej dowolne operację np. wyświetlenie zawartości 10 elementowej tablicy. Następnie należy zamknąć wcześniej uruchomiony wątek (pthread_join, pthread_cancel).

Pamiętamy o dyrektywie: #include <pthread.h>
gcc thread.c -o thread -lpthread

2. Do poprzedniego programu założyć zmienną globalną. Ta zmienna globalna ma być ikrementowana 10 razy jednocześnie przez wątek( w funkcji przekazywanej do tworzenia wątku) i proces główny.

3. Skorzystać z mutexów (pthread_mutex_t) w celu zablokowania i zmiany asynchronicznej wartości globalnej zmiennej (pthread_mutex_lock(), pthread_mutex_unlock())

### Lesson 7

#### Zadanie 1:
Program, który tworzy wątek (pthread_create) i przekazuje do niego funkcję wyświetlającą zawartość 10-elementowej tablicy. Następnie zamykamy wątek (pthread_join, pthread_cancel).

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

void* print_array(void* arg) {
    int* array = (int*)arg;
    for (int i = 0; i < 10; i++) {
        printf("%d ", array[i]);
    }
    printf("\n");
    return NULL;
}

int main() {
    pthread_t thread;
    int array[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    // Tworzymy wątek
    if (pthread_create(&thread, NULL, print_array, (void*)array) != 0) {
        perror("pthread_create");
        return 1;
    }

    // Czekamy na zakończenie wątku
    if (pthread_join(thread, NULL) != 0) {
        perror("pthread_join");
        return 2;
    }

    return 0;
}
```

**Wyjaśnienie:**
- `pthread_create(&thread, NULL, print_array, (void*)array)` tworzy nowy wątek, który wykonuje funkcję `print_array`.
- `pthread_join(thread, NULL)` czeka na zakończenie wątku.
- Funkcja `print_array` wypisuje zawartość 10-elementowej tablicy.

#### Zadanie 2:
Dodajemy zmienną globalną, którą będziemy inkrementować jednocześnie przez wątek i proces główny.

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

int global_variable = 0;

void* increment_global(void* arg) {
    for (int i = 0; i < 10; i++) {
        global_variable++;
        printf("Wątek: global_variable = %d\n", global_variable);
    }
    return NULL;
}

int main() {
    pthread_t thread;

    // Tworzymy wątek
    if (pthread_create(&thread, NULL, increment_global, NULL) != 0) {
        perror("pthread_create");
        return 1;
    }

    // Inkrementujemy zmienną globalną w procesie głównym
    for (int i = 0; i < 10; i++) {
        global_variable++;
        printf("Proces główny: global_variable = %d\n", global_variable);
    }

    // Czekamy na zakończenie wątku
    if (pthread_join(thread, NULL) != 0) {
        perror("pthread_join");
        return 2;
    }

    return 0;
}
```

**Wyjaśnienie:**
- `global_variable` jest zmienną globalną, która jest inkrementowana zarówno przez wątek, jak i przez proces główny.
- Wątek inkrementuje zmienną globalną 10 razy, wyświetlając jej wartość.
- Proces główny również inkrementuje zmienną globalną 10 razy, wyświetlając jej wartość.

#### Zadanie 3:
Korzystamy z mutexów, aby zablokować i zmieniać asynchronicznie wartość globalnej zmiennej.

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

int global_variable = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void* increment_global(void* arg) {
    for (int i = 0; i < 10; i++) {
        pthread_mutex_lock(&mutex);
        global_variable++;
        printf("Wątek: global_variable = %d\n", global_variable);
        pthread_mutex_unlock(&mutex);
    }
    return NULL;
}

int main() {
    pthread_t thread;

    // Tworzymy wątek
    if (pthread_create(&thread, NULL, increment_global, NULL) != 0) {
        perror("pthread_create");
        return 1;
    }

    // Inkrementujemy zmienną globalną w procesie głównym
    for (int i = 0; i < 10; i++) {
        pthread_mutex_lock(&mutex);
        global_variable++;
        printf("Proces główny: global_variable = %d\n", global_variable);
        pthread_mutex_unlock(&mutex);
    }

    // Czekamy na zakończenie wątku
    if (pthread_join(thread, NULL) != 0) {
        perror("pthread_join");
        return 2;
    }

    // Niszczymy mutex
    pthread_mutex_destroy(&mutex);

    return 0;
}
```

**Wyjaśnienie:**
- Dodaliśmy mutex `pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER`.
- Przed inkrementacją zmiennej globalnej blokujemy mutex `pthread_mutex_lock(&mutex)`, a po inkrementacji odblokowujemy mutex `pthread_mutex_unlock(&mutex)`.
- Mutexy zapewniają, że tylko jeden wątek naraz może modyfikować zmienną globalną, co zapobiega warunkom wyścigu (race conditions).

## Lesson 8

https://www.geeksforgeeks.org/ipc-using-message-queues/

1. W jednym programie napisać prostą kolejkę komunikatów za pomocą funkcji - msgget, msgsnd,msgrcv.

2. W poprzednim programie rozbić komunikację na procesy - jeden proces coś wysyła, drugi to odbiera.

### Lesson 8

#### Zadanie 1:
Program tworzy prostą kolejkę komunikatów za pomocą funkcji `msgget`, `msgsnd`, `msgrcv`.

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ipc.h>
#include <sys/msg.h>

#define MAX 10

struct message_buffer {
    long message_type;
    char message_text[100];
};

int main() {
    key_t key;
    int msgid;
    struct message_buffer message;

    // Generowanie unikalnego klucza
    key = ftok("progfile", 65);

    // Tworzenie kolejki komunikatów
    msgid = msgget(key, 0666 | IPC_CREAT);
    message.message_type = 1;

    printf("Wpisz wiadomość: ");
    fgets(message.message_text, sizeof(message.message_text), stdin);

    // Wysyłanie wiadomości do kolejki
    msgsnd(msgid, &message, sizeof(message), 0);

    printf("Wysłano wiadomość: %s\n", message.message_text);

    // Odbieranie wiadomości z kolejki
    msgrcv(msgid, &message, sizeof(message), 1, 0);

    printf("Otrzymano wiadomość: %s\n", message.message_text);

    // Usunięcie kolejki komunikatów
    msgctl(msgid, IPC_RMID, NULL);

    return 0;
}
```

**Wyjaśnienie:**
- `ftok("progfile", 65)` generuje unikalny klucz.
- `msgget(key, 0666 | IPC_CREAT)` tworzy kolejkę komunikatów z prawami dostępu 0666.
- `msgsnd(msgid, &message, sizeof(message), 0)` wysyła wiadomość do kolejki.
- `msgrcv(msgid, &message, sizeof(message), 1, 0)` odbiera wiadomość z kolejki.
- `msgctl(msgid, IPC_RMID, NULL)` usuwa kolejkę komunikatów.

#### Zadanie 2:
Rozbijamy komunikację na dwa procesy - jeden proces wysyła wiadomość, a drugi ją odbiera.

**Nadawca (sender.c):**
```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ipc.h>
#include <sys/msg.h>

struct message_buffer {
    long message_type;
    char message_text[100];
};

int main() {
    key_t key;
    int msgid;
    struct message_buffer message;

    // Generowanie unikalnego klucza
    key = ftok("progfile", 65);

    // Tworzenie kolejki komunikatów
    msgid = msgget(key, 0666 | IPC_CREAT);
    message.message_type = 1;

    printf("Wpisz wiadomość: ");
    fgets(message.message_text, sizeof(message.message_text), stdin);

    // Wysyłanie wiadomości do kolejki
    msgsnd(msgid, &message, sizeof(message), 0);

    printf("Wysłano wiadomość: %s\n", message.message_text);

    return 0;
}
```

**Odbiorca (receiver.c):**
```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ipc.h>
#include <sys/msg.h>

struct message_buffer {
    long message_type;
    char message_text[100];
};

int main() {
    key_t key;
    int msgid;
    struct message_buffer message;

    // Generowanie unikalnego klucza
    key = ftok("progfile", 65);

    // Tworzenie kolejki komunikatów
    msgid = msgget(key, 0666 | IPC_CREAT);

    // Odbieranie wiadomości z kolejki
    msgrcv(msgid, &message, sizeof(message), 1, 0);

    printf("Otrzymano wiadomość: %s\n", message.message_text);

    // Usunięcie kolejki komunikatów
    msgctl(msgid, IPC_RMID, NULL);

    return 0;
}
```

**Kompilacja i uruchomienie:**
1. Skompiluj programy:
   ```sh
   gcc sender.c -o sender
   gcc receiver.c -o receiver
   ```

2. Uruchom proces nadawcy:
   ```sh
   ./sender
   ```

3. Uruchom proces odbiorcy:
   ```sh
   ./receiver
   ```

**Wyjaśnienie:**
- `sender.c` generuje unikalny klucz, tworzy kolejkę komunikatów, wprowadza wiadomość od użytkownika i wysyła ją do kolejki.
- `receiver.c` generuje ten sam unikalny klucz, tworzy kolejkę komunikatów, odbiera wiadomość z kolejki i wyświetla ją na ekranie. Następnie usuwa kolejkę komunikatów.

## Lesson 9

Obsługa za pomocą metod: shmget, shmctl, shmat.
 
Napisać dwa programy komunikujące się przez pamięć współdzieloną:
- Pierwszy w nieskończonej pętli, naprzemiennie wysyła napisy „CIEPLO”, „ZIMNO” do pamięci współdzielonej.
- Drugi odczytuje z pamięci wartości (proszę pamiętać o walidacji czy dany napis jest poprawny - czy „CIEPLO, czy „ZIMNO” - jeśli niepoprawny, powinien zostać wyświetlony błąd).

### Lesson 9

#### Zadanie:
Napisać dwa programy komunikujące się przez pamięć współdzieloną. Pierwszy program w nieskończonej pętli naprzemiennie wysyła napisy „CIEPLO”, „ZIMNO” do pamięci współdzielonej. Drugi program odczytuje z pamięci wartości i waliduje, czy dany napis jest poprawny - jeśli niepoprawny, powinien wyświetlić błąd.

**Program wysyłający (sender.c):**
```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/shm.h>

#define SHM_SIZE 1024

int main() {
    key_t key;
    int shmid;
    char *shm;
    char *messages[] = {"CIEPLO", "ZIMNO"};
    int i = 0;

    // Generowanie unikalnego klucza
    key = ftok("shmfile", 65);

    // Tworzenie segmentu pamięci współdzielonej
    shmid = shmget(key, SHM_SIZE, 0666 | IPC_CREAT);
    if (shmid == -1) {
        perror("shmget");
        exit(1);
    }

    // Dołączanie segmentu pamięci współdzielonej do przestrzeni adresowej procesu
    shm = (char *)shmat(shmid, NULL, 0);
    if (shm == (char *)-1) {
        perror("shmat");
        exit(1);
    }

    while (1) {
        strcpy(shm, messages[i]);
        printf("Wysłano: %s\n", messages[i]);
        i = (i + 1) % 2; // Przełączanie między "CIEPLO" a "ZIMNO"
        sleep(1); // Czekanie 1 sekundy
    }

    return 0;
}
```

**Program odbierający (receiver.c):**
```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/shm.h>

#define SHM_SIZE 1024

int main() {
    key_t key;
    int shmid;
    char *shm;
    char buffer[SHM_SIZE];

    // Generowanie unikalnego klucza
    key = ftok("shmfile", 65);

    // Tworzenie segmentu pamięci współdzielonej
    shmid = shmget(key, SHM_SIZE, 0666 | IPC_CREAT);
    if (shmid == -1) {
        perror("shmget");
        exit(1);
    }

    // Dołączanie segmentu pamięci współdzielonej do przestrzeni adresowej procesu
    shm = (char *)shmat(shmid, NULL, 0);
    if (shm == (char *)-1) {
        perror("shmat");
        exit(1);
    }

    while (1) {
        strcpy(buffer, shm);
        if (strcmp(buffer, "CIEPLO") == 0 || strcmp(buffer, "ZIMNO") == 0) {
            printf("Otrzymano: %s\n", buffer);
        } else {
            printf("Błąd: Niepoprawna wartość w pamięci: %s\n", buffer);
        }
        sleep(1); // Czekanie 1 sekundy
    }

    return 0;
}
```

**Kompilacja i uruchomienie:**
1. Skompiluj programy:
   ```sh
   gcc sender.c -o sender
   gcc receiver.c -o receiver
   ```

2. Uruchom program nadawcy w jednym terminalu:
   ```sh
   ./sender
   ```

3. Uruchom program odbiorcy w drugim terminalu:
   ```sh
   ./receiver
   ```

**Wyjaśnienie:**
- `ftok("shmfile", 65)` generuje unikalny klucz do segmentu pamięci współdzielonej.
- `shmget(key, SHM_SIZE, 0666 | IPC_CREAT)` tworzy segment pamięci współdzielonej o rozmiarze `SHM_SIZE`.
- `shmat(shmid, NULL, 0)` dołącza segment pamięci współdzielonej do przestrzeni adresowej procesu.
- `strcpy(shm, messages[i])` kopiuje napisy "CIEPLO" i "ZIMNO" do pamięci współdzielonej w nieskończonej pętli.
- Program odbierający kopiuje dane z pamięci współdzielonej do lokalnego bufora i sprawdza, czy odczytana wartość jest poprawna. Jeśli nie, wyświetla komunikat o błędzie.

## Lesson 10

Dodać funkcjonalność "zamykania" i "otwierania" semafora w obydwu programach z poprzednich zajęć. Powinny one otwierać dostęp do zmiennej która jest przekazywana w pamięci współdzielonej - zalecam przekształcenie jej na strukturę której atrybut będzie stanowił ciąg znaków.
 
http://www.vishalchovatiya.com/semaphore-between-processes-example-in-c/
https://eric-lo.gitbook.io/synchronization/semaphore-in-c
https://riptutorial.com/c/example/31715/semaphores - metoda IPC ("od zera")

### Lesson 10

Dodanie funkcjonalności semaforów do programów komunikujących się przez pamięć współdzieloną. Semafory będą używane do synchronizacji dostępu do zmiennej w pamięci współdzielonej.

#### Struktura danych:
Przekształcimy zmienną tekstową na strukturę, której atrybut będzie stanowił ciąg znaków.

**Struktura danych:**
```c
struct shared_memory {
    char message[100];
};
```

**Program wysyłający (sender.c):**
```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <sys/types.h>

#define SHM_SIZE sizeof(struct shared_memory)

struct shared_memory {
    char message[100];
};

void sem_lock(int semid) {
    struct sembuf sem_op;
    sem_op.sem_num = 0;
    sem_op.sem_op = -1;
    sem_op.sem_flg = 0;
    semop(semid, &sem_op, 1);
}

void sem_unlock(int semid) {
    struct sembuf sem_op;
    sem_op.sem_num = 0;
    sem_op.sem_op = 1;
    sem_op.sem_flg = 0;
    semop(semid, &sem_op, 1);
}

int main() {
    key_t key;
    int shmid, semid;
    struct shared_memory *shm;
    char *messages[] = {"CIEPLO", "ZIMNO"};
    int i = 0;

    // Generowanie unikalnego klucza
    key = ftok("shmfile", 65);

    // Tworzenie segmentu pamięci współdzielonej
    shmid = shmget(key, SHM_SIZE, 0666 | IPC_CREAT);
    if (shmid == -1) {
        perror("shmget");
        exit(1);
    }

    // Tworzenie semafora
    semid = semget(key, 1, 0666 | IPC_CREAT);
    if (semid == -1) {
        perror("semget");
        exit(1);
    }
    semctl(semid, 0, SETVAL, 1);

    // Dołączanie segmentu pamięci współdzielonej do przestrzeni adresowej procesu
    shm = (struct shared_memory *)shmat(shmid, NULL, 0);
    if (shm == (struct shared_memory *)-1) {
        perror("shmat");
        exit(1);
    }

    while (1) {
        sem_lock(semid);
        strcpy(shm->message, messages[i]);
        printf("Wysłano: %s\n", messages[i]);
        sem_unlock(semid);
        i = (i + 1) % 2; // Przełączanie między "CIEPLO" a "ZIMNO"
        sleep(1); // Czekanie 1 sekundy
    }

    return 0;
}
```

**Program odbierający (receiver.c):**
```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <sys/types.h>

#define SHM_SIZE sizeof(struct shared_memory)

struct shared_memory {
    char message[100];
};

void sem_lock(int semid) {
    struct sembuf sem_op;
    sem_op.sem_num = 0;
    sem_op.sem_op = -1;
    sem_op.sem_flg = 0;
    semop(semid, &sem_op, 1);
}

void sem_unlock(int semid) {
    struct sembuf sem_op;
    sem_op.sem_num = 0;
    sem_op.sem_op = 1;
    sem_op.sem_flg = 0;
    semop(semid, &sem_op, 1);
}

int main() {
    key_t key;
    int shmid, semid;
    struct shared_memory *shm;

    // Generowanie unikalnego klucza
    key = ftok("shmfile", 65);

    // Tworzenie segmentu pamięci współdzielonej
    shmid = shmget(key, SHM_SIZE, 0666 | IPC_CREAT);
    if (shmid == -1) {
        perror("shmget");
        exit(1);
    }

    // Tworzenie semafora
    semid = semget(key, 1, 0666 | IPC_CREAT);
    if (semid == -1) {
        perror("semget");
        exit(1);
    }

    // Dołączanie segmentu pamięci współdzielonej do przestrzeni adresowej procesu
    shm = (struct shared_memory *)shmat(shmid, NULL, 0);
    if (shm == (struct shared_memory *)-1) {
        perror("shmat");
        exit(1);
    }

    while (1) {
        sem_lock(semid);
        if (strcmp(shm->message, "CIEPLO") == 0 || strcmp(shm->message, "ZIMNO") == 0) {
            printf("Otrzymano: %s\n", shm->message);
        } else {
            printf("Błąd: Niepoprawna wartość w pamięci: %s\n", shm->message);
        }
        sem_unlock(semid);
        sleep(1); // Czekanie 1 sekundy
    }

    return 0;
}
```

**Kompilacja i uruchomienie:**
1. Skompiluj programy:
   ```sh
   gcc sender.c -o sender
   gcc receiver.c -o receiver
   ```

2. Uruchom program nadawcy w jednym terminalu:
   ```sh
   ./sender
   ```

3. Uruchom program odbiorcy w drugim terminalu:
   ```sh
   ./receiver
   ```

**Wyjaśnienie:**
- `struct shared_memory` definiuje strukturę danych, którą przechowujemy w pamięci współdzielonej.
- `sem_lock` i `sem_unlock` to funkcje używane do blokowania i odblokowywania dostępu do pamięci współdzielonej za pomocą semafora.
- W obu programach tworzymy i uzyskujemy dostęp do segmentu pamięci współdzielonej oraz semafora.
- `sender.c` naprzemiennie zapisuje "CIEPLO" i "ZIMNO" do pamięci współdzielonej, korzystając z semafora, aby zapewnić synchronizację.
- `receiver.c` odczytuje wartości z pamięci współdzielonej, sprawdza ich poprawność i wyświetla komunikaty, również korzystając z semafora dla synchronizacji.

## Lesson 11: Docker

1. Docker

https://docs.docker.com/get-started/02_our_app/

```
# syntax=docker/dockerfile:1
FROM python:3.9
 
ENV PYTHONUNBUFFERED=1
WORKDIR /code
 
RUN pip install Django
RUN pip install djangorestframework
 
EXPOSE 8000
 
CMD ["python", "-m", "http.server"]
```

2. Docker Compose
https://docs.docker.com/compose/gettingstarted/

```
services:
  web:
    build: .
    depends_on:
      - db
  db:
    image: postgres
```

3. Docker Networking:
https://docs.docker.com/compose/networking/
https://forums.docker.com/t/static-ip-on-docker-containers/110412/5


### Lesson 11: Docker

#### Dockerfile

1. **Dockerfile Explanation**

The Dockerfile provided creates a Docker image for a Python environment with Django and Django REST Framework installed. It then exposes port 8000 and starts a simple HTTP server.

**Dockerfile:**
```Dockerfile
# Use the official Python image from the Docker Hub
FROM python:3.9

# Ensure the stdout and stderr streams are unbuffered
ENV PYTHONUNBUFFERED=1

# Set the working directory in the container
WORKDIR /code

# Install Django and Django REST Framework
RUN pip install Django
RUN pip install djangorestframework

# Expose port 8000
EXPOSE 8000

# Command to run when the container starts
CMD ["python", "-m", "http.server"]
```

**Explanation:**
- `FROM python:3.9`: Specifies the base image for the container.
- `ENV PYTHONUNBUFFERED=1`: Sets an environment variable to ensure the Python output is displayed in real-time.
- `WORKDIR /code`: Sets the working directory in the container to `/code`.
- `RUN pip install Django` and `RUN pip install djangorestframework`: Install Django and Django REST Framework.
- `EXPOSE 8000`: Exposes port 8000 to allow traffic to reach the container.
- `CMD ["python", "-m", "http.server"]`: Specifies the command to run when the container starts, which in this case is a simple HTTP server.

2. **Docker Compose**

Docker Compose is used to define and manage multi-container Docker applications. Here's an example `docker-compose.yml` file that defines a web service and a database service using PostgreSQL.

**docker-compose.yml:**
```yaml
version: '3'

services:
  web:
    build: .
    ports:
      - "8000:8000"
    depends_on:
      - db
  db:
    image: postgres
    environment:
      POSTGRES_DB: example_db
      POSTGRES_USER: example_user
      POSTGRES_PASSWORD: example_password
```

**Explanation:**
- `version: '3'`: Specifies the version of Docker Compose.
- `services`: Defines the services in the application.
  - `web`: The web service
    - `build: .`: Builds the Docker image using the Dockerfile in the current directory.
    - `ports`: Maps port 8000 of the host to port 8000 of the container.
    - `depends_on`: Ensures that the `db` service is started before the `web` service.
  - `db`: The database service
    - `image: postgres`: Uses the official PostgreSQL image from Docker Hub.
    - `environment`: Sets environment variables for the PostgreSQL database (database name, user, password).

3. **Docker Networking**

Docker Compose sets up a default network for your application. Containers can communicate with each other using their service names as hostnames.

To set up static IPs for Docker containers, you need to create a custom network in your `docker-compose.yml`:

**docker-compose.yml with custom network:**
```yaml
version: '3'

services:
  web:
    build: .
    ports:
      - "8000:8000"
    depends_on:
      - db
    networks:
      custom_network:
        ipv4_address: 172.20.0.2
  db:
    image: postgres
    environment:
      POSTGRES_DB: example_db
      POSTGRES_USER: example_user
      POSTGRES_PASSWORD: example_password
    networks:
      custom_network:
        ipv4_address: 172.20.0.3

networks:
  custom_network:
    driver: bridge
    ipam:
      config:
        - subnet: 172.20.0.0/16
```

**Explanation:**
- `networks`: Defines a custom network.
  - `custom_network`: The name of the custom network.
    - `driver: bridge`: Uses the bridge driver for the network.
    - `ipam`: IP address management settings.
      - `config`: Configuration for IP allocation.
        - `subnet: 172.20.0.0/16`: Specifies the subnet for the network.
- `services.web.networks.custom_network`: Assigns the web service to the custom network with a static IP.
- `services.db.networks.custom_network`: Assigns the db service to the custom network with a static IP.

This setup ensures that the `web` and `db` services are on the same custom network and can communicate with each other using their static IP addresses.

---

### Using `docker exec` to Enter a Docker Container and Print Environment Variables

To enter a Docker container and print environment variables, you can follow these steps:

1. **Start the Docker Container:**
   Ensure your Docker container is running. If you haven't started your container yet, use `docker-compose up` to start it or `docker run` if you're using a single container.

   ```sh
   docker-compose up -d
   ```

2. **List Running Containers:**
   List all running containers to get the container ID or name.

   ```sh
   docker ps
   ```

   This command will output a list of running containers. Note the container ID or name of the container you want to enter.

3. **Enter the Docker Container:**
   Use the `docker exec` command to start a new shell session inside the running container. Replace `<container_id_or_name>` with the actual container ID or name.

   ```sh
   docker exec -it <container_id_or_name> /bin/bash
   ```

   For example, if your container ID is `abc123`, the command would be:

   ```sh
   docker exec -it abc123 /bin/bash
   ```

4. **Print Environment Variables:**
   Once inside the container, you can print the environment variables using the `env` command or `printenv`.

   ```sh
   env
   ```

   Or

   ```sh
   printenv
   ```

   This will list all the environment variables set in the container.

### Example Workflow

Let's go through an example workflow using a simple setup with a `docker-compose.yml`:

**docker-compose.yml:**
```yaml
version: '3'

services:
  web:
    image: python:3.9
    environment:
      - MY_VAR=HelloWorld
    command: sleep infinity
```

1. **Start the Services:**

   ```sh
   docker-compose up -d
   ```

2. **List Running Containers:**

   ```sh
   docker ps
   ```

   Output:
   ```
   CONTAINER ID   IMAGE         COMMAND             CREATED         STATUS         PORTS     NAMES
   abc123         python:3.9    "sleep infinity"    2 minutes ago   Up 2 minutes             myproject_web_1
   ```

3. **Enter the Docker Container:**

   ```sh
   docker exec -it myproject_web_1 /bin/bash
   ```

4. **Print Environment Variables:**

   ```sh
   env
   ```

   Output:
   ```
   MY_VAR=HelloWorld
   PATH=/usr/local/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
   ...
   ```

By following these steps, you can enter any running Docker container and print its environment variables.