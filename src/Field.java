import java.awt.*;
import java.io.FileReader;
import java.io.IOException;

public class Field {
    private Cell[][] cells; // Двумерный массив клеток
    private int rows; // Количество строк игрового поля
    private int cols; // Количество столбцов игрового поля
    private int sizeCell; // Размер одной клетки

    // Конструктор класса
    public Field(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];

        // Инициализация всех клеток как мёртвых
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell(0);
            }
        }
    }

    // Геттер для получения количества строк
    public int getRows() {
        return rows;
    }

    // Геттер для получения количества столбцов
    public int getCols() {
        return cols;
    }

    // Геттер для получения размера клетки
    public int getSizeCell() {
        return sizeCell;
    }

    // Геттер для получения массива клеток
    public Cell[][] getCells() {
        Cell[][] copy = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy[i][j] = new Cell(cells[i][j].getCell());
            }
        }
        return copy;
    }

    // Сеттер для изменения массива клеток
    public void setCells(Cell[][] newCells) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = newCells[i][j];
            }
        }
    }

    // Метод для рисования сетки поля
    public void drawField(Graphics g) {
        int cellWidth = (GraphicWindow.WIDTH - 150) / cols; // -150 для того, чтобы уйти от краёв окна.
        int cellHeight = (GraphicWindow.HEIGHT - 150) / rows;
        sizeCell = Math.min(cellWidth, cellHeight);

        // Рисуем сетку
        g.setColor(Color.BLACK);
        for (int i = 0; i <= rows; i++) {
            g.drawLine(0, i * sizeCell, cols * sizeCell, i * sizeCell);
        }
        for (int j = 0; j <= cols; j++) {
            g.drawLine(j * sizeCell, 0, j * sizeCell, rows * sizeCell);
        }
    }

    // Метод для рисования живых клеток
    public void drawLivingCells(Graphics g) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (cells[i][j].getCell() == 1) {
                    cells[i][j].draw(g, j * sizeCell, i * sizeCell, sizeCell);
                }
            }
        }
    }

    // Метод для загрузки фигуры из файла на поле
    public void loadingShape(String file, int startRow, int startCol) throws IOException {
        FileReader fileReader = new FileReader(file); // Открытие файла
        int row = startRow; // Номер текущей строки
        int symbol; // Переменная для считывания символов
        String str = ""; // Переменная для хранения строки

        while ((symbol = fileReader.read()) != -1) {
            if (symbol == '\n') {
                for (int col = 0; col < str.length(); col++) {
                    // Учёт бесконечности поля
                    int newRow = (row % rows);
                    int newCol = (startCol + col) % cols;

                    if (str.charAt(col) == '1') {
                        cells[newRow][newCol].setCell(1);
                    }
                }
                row++;
                str = "";
            } else {
                str += (char) symbol;
            }
        }

        // Обрабатываем последнюю строку, если она не оканчивается символом новой строки
        if (!str.isEmpty()) {
            for (int col = 0; col < str.length(); col++) {
                // Учёт бесконечности поля
                int newRow = (row % rows);
                int newCol = (startCol + col) % cols;

                if (str.charAt(col) == '1') {
                    cells[newRow][newCol].setCell(1);
                }
            }
        }

        fileReader.close(); // Закрываем файл
    }
}