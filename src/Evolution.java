public class Evolution {
    private Field field; // Игровое поле

    // Конструктор класса
    public Evolution(Field field) {
        this.field = field;
    }

    // Метод, выполняющий один шаг эволюции
    public void evolve() {
        int rows = field.getRows();
        int cols = field.getCols();
        Cell[][] cells = field.getCells();
        Cell[][] newCells = new Cell[rows][cols];

        // Создаём объекты Cell для каждой клетки в новом массиве
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newCells[i][j] = new Cell(0); // Инициализация каждой клетки
            }
        }

        // Проходим по каждой клетке поля
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int liveNeighbors = countLiveNeighbors(i, j);
                int currentCell = cells[i][j].getCell();

                // Применяем правила игры
                if (currentCell == 1) {
                    if (liveNeighbors < 2 || liveNeighbors > 3) {
                        newCells[i][j].setCell(0); // Умирает от одиночества или перенаселённости
                    } else {
                        newCells[i][j].setCell(1);
                    }
                } else {
                    if (liveNeighbors == 3) {
                        newCells[i][j].setCell(1); // В мёртвой клетке зарождается жизнь
                    } else {
                        newCells[i][j].setCell(0);
                    }
                }
            }
        }

        field.setCells(newCells);
    }

    // Метод для подсчёта количества живых соседей клетки
    public int countLiveNeighbors(int row, int col) {
        int liveNeighbors = 0; // Cчётчик живых соседей
        int rows = field.getRows();
        int cols = field.getCols();
        Cell[][] cells = field.getCells();

        // Перебираем всех соседей клетки
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue; // Пропускаем саму клетку
                }
                // Учёт бесконечности поля
                int neighborRow = (row + i + rows) % rows;
                int neighborCol = (col + j + cols) % cols;

                // Увеличиваем счётчик, если соседняя клетка живая
                if (cells[neighborRow][neighborCol].getCell() == 1) {
                    liveNeighbors++;
                }
            }
        }

        return liveNeighbors;
    }
}