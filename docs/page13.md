```python
import random
import os
import time

ALIVE = "o"
DEAD = "_"

# Parametry automatu
rows = 20  # Liczba wierszy
cols = 50  # Liczba kolumn
k = 100  # Liczba iteracji
# Zasada 23/3
#012345678/3

survival = [4,5,6,7,8]  # Jeśli komórka jest żywa, musi mieć określoną liczbę żywych sąsiadów, aby przeżyć
birth = [3]        # Jeśli komórka jest martwa, ma się urodzić przy dokładnie 3 żywych sąsiadach

# Inicjalizacja losowej siatki rows x cols
grid = [[random.choice([DEAD, ALIVE]) for _ in range(cols)] for _ in range(rows)]

# Funkcja licząca żywych sąsiadów komórki (8-sąsiedztwo - reguła Moore'a)
def count_neighbors(grid, x, y):
    # (-1,-1)   (-1,0)   (-1,1)
    # (0,-1)    (x,y)    (0,1)
    # (1,-1)    (1,0)    (1,1)
    directions = [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]
    return sum(1 for dx, dy in directions if 0 <= x + dx < rows and 0 <= y + dy < cols and grid[x + dx][y + dy] == ALIVE)

# Główna pętla automatu
for _ in range(k):
    os.system("clear")  # Czyści ekran terminala (dla Windows: os.system("cls"))
    
    # Wyświetlanie aktualnej siatki
    for row in grid:
        print("".join(row))
    
    # Tworzenie nowej generacji na podstawie reguły 23/3
    new_grid = [[
        ALIVE if (grid[x][y] == ALIVE and count_neighbors(grid, x, y) in survival) or
                (grid[x][y] == DEAD and count_neighbors(grid, x, y) in birth)
        else DEAD
        for y in range(cols)
    ] for x in range(rows)]
    
    grid = new_grid  # Aktualizacja siatki
    time.sleep(0.1)  # Krótkie opóźnienie dla efektu animacji
```
