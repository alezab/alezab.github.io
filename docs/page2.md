# C++ School

## Lesson 1: Creating directiories, scripting in Bash/C, infinite loops, cronjobs.

Exercises:

1. Create the directory structure: `kat1/`, `kat2/`, `kat3/`.
```
mkdir kat{1,2,3}
```

2. In `kat1`, create subdirectories: `subkat1_1/`, `subkat1_2/`.
```
mkdir ./kat1/subkat1_{1,2}
```

3. In `kat2`, create subdirectories: `subkat2_1/`, `subkat2_2/`.
```
mkdir ./kat2/subkat2_{1,2}
```

4. In `kat1/subkat1_1`, create a script that displays a message to standard output.
``` bash
#!/bin/bash
echo "Message to stdout"
```
```
echo -e '#!/bin/bash\necho "Message to stdout"' > ./kat1/subkat1_1/script.sh &&
chmod +x ./kat1/subkat1_1/script.sh &&
./kat1/subkat1_1/script.sh
```

5. Copy the script to `kat1/subkat1_2`.
```
cp ./kat1/subkat1_1/script.sh ./kat1/subkat1_2/
```

6. Modify the script so that it creates subdirectories: `subkat3_1/`, `subkat3_2/` in `kat3/`.
```
echo -e 'mkdir ./kat3/subkat3_{1,2}' >> kat1/subkat1_2/script.sh &&
chmod +x ./kat1/subkat1_2/script.sh &&
./kat1/subkat1_2/script.sh
```

7. In `kat3/subkat3_1/`, create a file containing the number of words and characters from the previous script.
```
wc ./kat1/subkat1_2/script.sh > kat3/subkat3_1/wc.txt &&
cat ./kat3/subkat3_1/wc.txt
```

8. Find the previous file using some search function, use a display function and save the contents of the file to a new one in `kat3/subkat3_2` - all in a single command line.
Using `find`:
```
find kat3/ -name wc.txt | xargs cat > ./kat3/subkat3_2/wc.txt &&
cat ./kat3/subkat3_2/wc.txt
```
Using `grep`:

9. Create a script that will enter an infinite loop (both a bash script and a C script). In a new terminal window, use a single line to find the process running this program and terminate it.  
Bash script: `infinite_loop.sh`:
``` bash
#!/bin/bash
while true; do
  echo -e "Infinite loop\n"
done
```
C script: `infinite_loop.c`:
``` c
int main() {
    while(1) {
        printf("Infinite loop\n");
    }
    return 0;
}
```
```
ps aux | grep "infinite_loop.sh"
```
```
pkill -9 -f infinite_loop
```

10. Using cron, create a task that will run the script in `kat1/subkat1_2` at a specific time interval, for example, every minute.
Using `crontab -e` command. Adding this line will run `script.sh` at every minute. See [crontab.guru](https://crontab.guru) for other schedule expressions.
```
* * * * * ~/kat1/subkat1_2/script.sh
```

11. Create a process (using bash -c) that will run in the background.
```
bash -c './infinite_loop.sh' &
```

## Lesson 2: Environmental variables

Environments:  
DEVELOPMENT/DEV -> TESTING/QA/STAGE -> PRODUCTION/PROD

ORDERS.URL = "HTTPS://ORDERS.DEV.COM"  
ORDERS.URL = "HTTPS://ORDERS.STAGE.COM"  

Global variable handling:  
```export ORDERS.URL = "HTTPS://ORDERS.DEV.COM"```

Same can be achieved by editing `~/.bashrc` or `~/.bash_profile`:

```
nano ~/.bashrc
source .bashrc
```


Command to list all the environment variables defined for a current session:

* `env`

Printing variable:

* `printenv VARIABLE_NAME`
* `echo $varname`

Source: [freecodecamp.org/news/how-to-set-an-environment-variable-in-linux](https://www.freecodecamp.org/news/how-to-set-an-environment-variable-in-linux/)

**1\. Write a script that takes three parameters:**

1. Filename
2. Action name - delete, create, copy
3. (Optional) filename

    Additionally, the script must be dependent on an environmental variable. For example, let this variable be the path to a file which will differ depending on the environment.  

    Script operations:  

    1. Delete - deletes the file specified in the first parameter
    2. Create - creates a new file and adds the content from the file specified by the environmental variable
    3. Copy - copies the content from the third parameter to the file specified in the first parameter

``` bash
#!/bin/bash

if [ "$#" -lt 2 ]; then
  echo "Incorrect number of args."
  echo "Use: $0 filename1 action (optional)filename2."
  echo "Available actions: delete, create, copy."
  exit 1
fi

FILE_NAME_1=$1 # Arg1: Filename 1
ACTION=$2 # Arg2: Action name
FILE_NAME_2=$3 # Arg3 (Optional) Filename 2

case "$ACTION" in
  "delete") 
    rm -f "$FILE_NAME_1"
    echo "Deleted file: $FILE_NAME_1"
    ;;
  "create")
    if [ -z "$ENV_FILE_PATH" ]; then
      echo "ENV_FILE_PATH was not set."
      exit 1
    fi 
    cp -f "$ENV_FILE_PATH" "$FILE_NAME_1"
    echo "Created file $FILE_NAME_1 the contents of the file from $ENV_FILE_PATH"
    ;;
  "copy")
    if [ -z "$3" ]; then
      echo "3rd arg was not set."
      exit 1
    fi
    cp -f "$FILE_NAME_2" "$FILE_NAME_1"
    echo "Copied the contents of the file $FILE_NAME_2 to file  $FILE_NAME_1"
    ;;  
  *)
    echo "Incorrect $ACTION. Available actions: delete, create, copy."
    exit 1
    ;;
esac
```

**2\. As an extension to the previous exercise:**  
Place the path to the script under the environmental variable (the script can do anything, it's important that it shows the result of its actions).  
The first argument is the name of the new file (script), and the second option is "run-copy".  
The task involves running the script whose name is passed as the first argument. Copying involves copying the script from the variable and adding additional content.  
At the end, execute the script from the variable to demonstrate its correct operation.  

``` bash
#!/bin/bash

if [ "$#" -lt 2 ]; then
  echo "Incorrect number of args."
  echo "Use: $0 filename1 action (optional)filename2."
  echo "Available actions: delete, create, copy, run-copy."
  exit 1
fi

FILE_NAME_1=$1 # Arg1: Filename 1
ACTION=$2 # Arg2: Action name
FILE_NAME_2=$3 # Arg3 (Optional) Filename 2

case "$ACTION" in
  "delete") 
    rm -f "$FILE_NAME_1"
    echo "Deleted file: $FILE_NAME_1"
    ;;
  "create")
    if [ -z "$ENV_FILE_PATH" ]; then
      echo "ENV_FILE_PATH was not set."
      exit 1
    fi 
    cp -f "$ENV_FILE_PATH" "$FILE_NAME_1"
    echo "Created file $FILE_NAME_1 the contents of the file from $ENV_FILE_PATH"
    ;;
  "copy")
    if [ -z "$3" ]; then
      echo "3rd arg was not set."
      exit 1
    fi
    cp -f "$FILE_NAME_2" "$FILE_NAME_1"
    echo "Copied the contents of the file $FILE_NAME_2 to file  $FILE_NAME_1"
    ;;
  "run-copy")
    if [ -z "$ENV_FILE_PATH" ]; then
      echo "ENV_FILE_PATH variable was not set."
      exit 1
    fi
    cp -f "$ENV_FILE_PATH" "$FILE_NAME_1"
    echo "echo 'Script DLC content.'" >> "$FILE_NAME_1"
    echo "The script was copied to $FILE_NAME_1 and DLC content was added."

    echo ""

    echo "Running script with DLC:"
    chmod +x "$FILE_NAME_1"
    bash "$FILE_NAME_1"

    echo ""

    echo "Running script without DLC:"
    chmod +x "$ENV_FILE_PATH"
    bash "$ENV_FILE_PATH"
    ;;    
  *)
    echo "Incorrect $ACTION. Available actions: delete, create, copy, run-copy."
    exit 1
    ;;
esac
```

## Lesson 3: File Handling in C

1. Create a file, and in a C program, add some string of characters to this file.
``` c
#include <stdio.h>
#include <stdlib.h>

int main()
{
    const char *filename = "file.txt";
    const char *text = "Example text.";

    // Open file for writing
    // If the file does not exist, it will be created.
    FILE *fp = fopen(filename, "w");
    if (fp == NULL)
    {
        perror("Error opening the file");
        return -1;
    }

    // Write text to file.
    fprintf(fp, "%s", text);
    // Close the file.
    fclose(fp);

    return 0;
}
```

2. As an extension of the previous task, the program should accept the filename. The program should check if the file exists (if not, throw an appropriate error) and write some content to it. If the file exists, the content should be appended, not overwritten.
Usage:  
`./2 ./file.txt`  
`cat file.txt`  
``` c
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{
    FILE *fp;
    const char *text = "Example text.";

    if (argc < 2)
    {
        printf("Usage: %s filename\n", argv[0]);
        return -1;
    }

    const char *filename = argv[1];

    // Check if the file exists. If not, the program returns an error.
    if ((fp = fopen(filename, "r")) != NULL)
    {
        fclose(fp); // File exists, close it and reopen in append mode.
        fp = fopen(filename, "a");
    }
    else
    {
        // File does not exist, open in "w" mode which will create the file.
        fp = fopen(filename, "w");
    }

    if (fp == NULL)
    {
        perror("Error opening the file");
        return -1;
    }

    fprintf(fp, "%s", text); // Append text to the file.
    fclose(fp);              // Close the file.

    return 0;
}

```

3. Another extension is to check the length of the file's content. If it exceeds 10 characters, new content is appended; if not, it is skipped.  
Usage:  
`./3 ./file.txt`  
`cat file.txt`  
``` c
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{
    FILE *fp = NULL;
    const char *text = "New text.";
    long fileSize;

    if (argc < 2)
    {
        printf("Usage: %s filename\n", argv[0]);
        return -1;
    }

    const char *filename = argv[1];

    // Open file in read and write mode "r+".
    fp = fopen(filename, "r+");

    // Check if the file exists.
    if (fp == NULL)
    {
        // If file does not exist then open in "w" mode to create and write.
        fp = fopen(filename, "w");
        // Check if the file was created.
        if (fp != NULL)
        {
            // Write new text and close the file without checking length.
            fprintf(fp, "%s", text);
            fclose(fp);
            return 0;
        }
        else
        {
            perror("Error opening the file");
            return -1;
        }
    }

    // File exists. Move to the end of the file to check its size.
    fseek(fp, 0, SEEK_END);

    // Calculate the size of the file.
    fileSize = ftell(fp);

    if (fileSize > 10)
    {
        fseek(fp, 0, SEEK_END); // Move to the end of the file before appending.
        fprintf(fp, "%s", text);
    }
    else
    {
        printf("File length: %ld, below 10.", fileSize);
    }

    fileSize = ftell(fp);
    printf("\nCurrent file length:\n%ld\n", fileSize);

    fclose(fp); // Close the file in one place.

    return 0;
}
```

## Lesson 4: C Pointers

1. Write a function `int max1(int *a, int *b)` that returns the value of the larger of the two values pointed to by the parameters.
``` c
int max1(int *a, int *b) {
    return *a > *b ? *a : *b;
}
```

2. Write a function `int * max2(int *a, int *b)` that returns the address of the larger value pointed to by the parameters.
``` c
int *max2(int *a, int *b) {
    return *a > *b ? a : b;
}
```

3. Write a function `int * max3(int *a, int *b)` that returns the larger address passed via the parameters.
``` c
int *max3(int *a, int *b) {
    return a > b ? a : b;
}
```

4. Write a function that receives pointers to two int values as parameters, and swaps the values pointed to only if the value pointed to by the first parameter is greater than the second.
``` c
#include <stdio.h>
#include <stdlib.h>

int max1(int *a, int *b) {
    return *a > *b ? *a : *b;
}

int *max2(int *a, int *b) {
    return *a > *b ? a : b;
}

int *max3(int *a, int *b) {
    return a > b ? a : b;
}

void swap(int *a, int *b) {
    int tmp = 0;
    if (*a > *b) {
        tmp = *b;
        *b = *a;
        *a = tmp;
    }
}

int main(int argc, char *argv[]) {
    int a, b;

    printf("Enter a and b: ");
    scanf("%d %d", &a, &b);
    int *ptr_a = &a;
    int *ptr_b = &b;

    printf("max1: %d\n", max1(ptr_a, ptr_b));
    printf("max2: %p\n", (void *)max2(ptr_a, ptr_b));
    printf("max3: %p\n", (void *)max3(ptr_a, ptr_b));
    printf("Values before swap: a = %d, b = %d\n", a, b);
    swap(ptr_a, ptr_b);
    printf("Values after swap: a = %d, b = %d\n", a, b);

    return 0;
}
```

## Lesson 5: Linux System Calls in C: fork(), wait(), getpid(), getenv()

1. Write a program that creates two processes. Each of the created processes should spawn a child process. Display the identifiers of the parent and child processes after each call to the fork function.
``` c
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main()
{
    pid_t pid1, pid2, childpid;

    pid1 = fork();
    if (pid1 == 0)
    {
        printf("Parent process (PID: %d) created a child process (PID: %d)\n", getppid(), getpid());
        childpid = fork();
        if (childpid == 0)
        {
            printf("Child process (PID: %d) created a child process (PID: %d)\n", getppid(), getpid());
            return 0; // Note: The wait(NULL) after this return is unreachable and has no effect.
        }
        else
        {
            pid2 = fork();
            if (pid2 == 0)
            {
                printf("Parent process (PID: %d) created a child process (PID: %d)\n", getppid(), getpid());
                childpid = fork();
                if (childpid == 0)
                {
                    printf("Child process (PID: %d) created a child process (PID: %d)\n", getppid(), getpid());
                    return 0;
                }
                wait(NULL);
                return 0;
            }
            wait(NULL);
            wait(NULL);
        }
        return 0;
    }
}
```

2. Write a program that will print the contents of the current directory preceded by the phrase "Beginning" and concluded with the phrase "End."
``` c
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main()
{
    pid_t pid;

    printf("Beginning\n");
    pid = fork();

    if (pid == 0)
    {
        execlp("ls", "ls", "-la", NULL); // Execute 'ls -la' in the child process.
    }
    else if (pid > 0)
    {
        wait(NULL); // Wait for the child process to finish.
        printf("End\n");
    }
    else
    {
        perror("fork"); // Error handling if fork fails.
        return 1;
    }

    return 0;
}
```

3. Write a program that will create any file with any name (if such a file already exists, the program should open it in "append" mode). The program should append randomly generated strings of characters - the number of words should depend on an environmental variable, as well as the maximum number of characters. In the program, two separate processes should also be created that will do exactly the same as the main process.
``` c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <time.h>

#define DEFAULT_WORD_COUNT 5
#define DEFAULT_MAX_LENGTH 10

void generate_and_append(FILE *fp, int num_words, int max_length) {
    const char charset[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    int charset_size = sizeof(charset) - 1; // -1 to exclude null terminator

    for (int i = 0; i < num_words; i++) {
        int word_length = rand() % max_length + 1; // +1 to ensure minimum length of 1
        char word[word_length + 1];
        for (int j = 0; j < word_length; j++) {
            word[j] = charset[rand() % charset_size];
        }
        word[word_length] = '\0'; // null-terminate the string
        fprintf(fp, "%s ", word);
    }
    fprintf(fp, "\n");
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        printf("Usage: %s <filename>\n", argv[0]);
        return 1;
    }

    const char *filename = argv[1];
    FILE *fp = fopen(filename, "a"); // Open in append mode
    if (!fp) {
        perror("Failed to open file");
        return 1;
    }

    // Set up random seed
    srand(time(NULL) + getpid());

    // Read environment variables
    char *env_words = getenv("NUM_WORDS");
    char *env_max_length = getenv("MAX_LENGTH");

    int num_words = env_words ? atoi(env_words) : DEFAULT_WORD_COUNT;
    int max_length = env_max_length ? atoi(env_max_length) : DEFAULT_MAX_LENGTH;

    // Generate and append random words
    generate_and_append(fp, num_words, max_length);

    // Fork two child processes
    for (int i = 0; i < 2; i++) {
        pid_t pid = fork();
        if (pid == 0) { // Child process
            // Re-seed for each child to ensure different random sequences
            srand(time(NULL) + getpid());
            generate_and_append(fp, num_words, max_length);
            fclose(fp);
            return 0;
        } else if (pid < 0) {
            perror("Failed to fork");
            fclose(fp);
            return 1;
        }
    }

    // Wait for child processes to finish
    while (wait(NULL) > 0);

    fclose(fp);
    return 0;
}
```