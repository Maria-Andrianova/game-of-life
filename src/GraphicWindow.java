import javax.swing.*;
import java.awt.*;

public class GraphicWindow extends JFrame {
    public static final int WIDTH = 700;  // Константа: ширина окна
    public static final int HEIGHT = 700; // Константа: высота окна
    private Field field;

    // Конструктор класса
    public GraphicWindow(Field field) {
        this.field = field;
        setSize(WIDTH, HEIGHT); // Устанавливаем размеры окна
        setTitle("Game of Life"); // Устанавливаем заголовок окна
        setVisible(true); // Делаем окно видимым
        setLocation(700, 100); // Устанавливаем начальное положение окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Завершение программы при закрытии окна
        setAlwaysOnTop(true); // Устанавливаем окно всегда поверх других
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Очистка окна

        // Рассчитываем координаты для центровки игрового поля и перемещаем начало координат
        int x = (WIDTH - (field.getCols() * field.getSizeCell())) / 2;
        int y = (HEIGHT - (field.getRows() * field.getSizeCell())) / 2;
        g.translate(x, y);

        field.drawField(g); // Рисуем сетку игрового поля
        field.drawLivingCells(g); // Рисуем живые клетки
    }
}