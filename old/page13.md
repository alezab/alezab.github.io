```python
import random
import os
import time
import curses

ALIVE_CELL = "o"
DEAD_CELL = "_"

# Parametry automatu
rows = 20  # Liczba wierszy
cols = 50  # Liczba kolumn
k = 100  # Liczba iteracji
# Zasada 23/3
# Zasada koralowa 45678/3

survival = [4,5,6,7,8]  # Ile sąsiadów, żeby przeżyć
birth = [3]             # Ile sąsiadów, żeby narodzić się

# 2D Grid: rows x cols
grid = [[random.choice([DEAD_CELL, ALIVE_CELL]) for _ in range(cols)] for _ in range(rows)]

# Reguła Moore'a (sprawdzamy 8 sąsiadów)
def count_neighbors(grid, x, y):
    # Pozycja sąsiadów:
    #       (-1,-1)   (-1,0)   (-1,1)
    #       (0,-1)     (x,y)    (0,1)
    #       (1,-1)    (1,0)    (1,1)
    directions = [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]
    return sum(1 for dx, dy in directions 
               if 0 <= x + dx < rows and 0 <= y + dy < cols and grid[x + dx][y + dy] == ALIVE_CELL)

def display_grid():
    global grid
    # Główna pętla automatu
    for _ in range(k):
        os.system("clear")
        
        # Wyświetl grid
        for row in grid:
            print("".join(row))
        
        # Nowy grid
        new_grid = [[
            ALIVE_CELL if (grid[x][y] == ALIVE_CELL and count_neighbors(grid, x, y) in survival) or
                    (grid[x][y] == DEAD_CELL and count_neighbors(grid, x, y) in birth)
            else DEAD_CELL
            for y in range(cols)
        ] for x in range(rows)]
        
        grid = new_grid  # Aktualizacja grida
        time.sleep(0.1)

def dispaly_grid_curses(stdscr):
    global grid
    stdscr.nodelay(1)   
    stdscr.timeout(100) 

    # Boundaries
    max_y, max_x = rows, cols

    for _ in range(k):
        stdscr.clear()

        # Grid y (row) / x (col)
        for i in range(rows):       # rows y
            for j in range(cols):   # cols x
                pos_y = i
                pos_x = j      
                if pos_y < max_y and pos_x < max_x:
                    try:
                        stdscr.addch(pos_y, pos_x, grid[i][j])
                    except curses.error:
                        pass

        stdscr.refresh()
        time.sleep(0.1)

        # Nowa siatka
        new_grid = [[
            ALIVE_CELL if (grid[x][y] == ALIVE_CELL and count_neighbors(grid, x, y) in survival) or
                          (grid[x][y] == DEAD_CELL and count_neighbors(grid, x, y) in birth)
            else DEAD_CELL
            for y in range(cols)
        ] for x in range(rows)]
        
        grid = new_grid

        # Wyjście on key
        if stdscr.getch() != -1:
            break

#display_grid()
curses.wrapper(dispaly_grid_curses)```
