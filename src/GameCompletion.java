import java.util.ArrayList;

public class GameCompletion {

    private ArrayList<Cell[][]> history = new ArrayList<>(); // Список для хранения истории изменения конфигурации
    private Field field;

    // Конструктор класса
    public GameCompletion(Field field) {
        this.field = field;
    }

    // Метод для проверки, не пора ли завершить игру
    public boolean checkingСompletionGame() {
        if (deadCells()) {
            System.out.println("Игра завершена: на поле не осталось живых клеток.");
            return true;
        }

        if (configurationNotChanged()) {
            System.out.println("Игра завершена: клетки не изменили своего состояния.");
            return true;
        }

        if (repeatConfigurationEarlier()) {
            System.out.println("Игра завершена: конфигурация повторилась спустя несколько шагов.");
            return true;
        }

        // Если игра не завершена, добавляем текущее состояние в историю
        history.add(field.getCells());
        return false;
    }

    // 1) Метод для проверки, что все клетки мёртвые
    private boolean deadCells() {
        Cell[][] currentField = field.getCells();
        for (int i = 0; i < field.getRows(); i++) {
            for (int j = 0; j < field.getCols(); j++) {
                if (currentField[i][j].getCell() == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    // 2) Метод для проверки повторения конфигурации ранее
    private boolean repeatConfigurationEarlier() {
        Cell[][] currentField = field.getCells();

        for (Cell[][] previousField : history) {
            if (identicalArrays(previousField, currentField)) {
                return true;
            }
        }
        return false;
    }

    // Метод для проверки, что два массива одинаковы
    private boolean identicalArrays(Cell[][] array1, Cell[][] array2) {
        for (int i = 0; i < array1.length; i++) {
            for (int j = 0; j < array1[i].length; j++) {
                if (array1[i][j].getCell() != array2[i][j].getCell()) {  // Сравниваем значения клеток
                    return false;
                }
            }
        }
        return true;
    }

    // 3) Метод для проверки, что конфигурация не изменилась
    private boolean configurationNotChanged() {
        // Была ли до этой конфигурации предыдущая
        if (history.isEmpty()) {
            return false;
        }

        Cell[][] currentField = field.getCells();
        Cell[][] previousField = history.get(history.size() - 1); // Получаем предыдущую конфигурацию
        return identicalArrays(previousField, currentField);
    }
}