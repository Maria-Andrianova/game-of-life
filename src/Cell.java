import java.awt.*;

public class Cell {
    private int cell; // Состояние клетки (0 — мёртвая, 1 — живая)

    // Конструктор класса
    public Cell(int cell) {
        this.cell = cell;
    }

    // Геттер для получения состояния клетки
    public int getCell() {
        return cell;
    }

    // Сеттер для изменения состояния клетки
    public void setCell(int cell) {
        this.cell = cell;
    }

    // Метод для отрисовки клетки
    public void draw(Graphics g, int x, int y, int sizeCell) {
        g.setColor(new Color(0x29B817));
        g.fillOval(x, y, sizeCell, sizeCell);
    }
}