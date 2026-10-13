import java.io.IOException;
import java.util.Scanner;

public class Main {
    // Метод для знакомства пользователя с игрой и её правилами
    public static void rulesGame() {
        System.out.println("Добро пожаловать в игру «Жизнь» Джона Конвея!");
        System.out.println("Это игра, в которой клетки на поле изменяют своё состояние в зависимости от соседних клеток.");
        System.out.println("Суть игры – следить за развитием колонии живых клеток, которая эволюционирует по следующим правилам:");

        System.out.println("\nПравила эволюции:");
        System.out.println("1. В пустой (мёртвой) клетке зарождается жизнь, если с ней соседствуют три живые клетки.");
        System.out.println("2. Живая клетка продолжает жить, если с ней соседствуют две или три живые клетки.");
        System.out.println("3. Живая клетка умирает от одиночества, если у нее меньше двух живых соседей.");
        System.out.println("4. Живая клетка умирает от перенаселённости, если у нее больше трёх живых соседей.");

        System.out.println("\nУсловия завершения игры:");
        System.out.println("1. Игра завершается, если на поле не останется ни одной живой клетки.");
        System.out.println("2. Игра завершается, если конфигурация на очередном шаге в точности (без сдвигов и поворотов) повторит себя на одном из более ранних шагов.");
        System.out.println("3. Игра завершается, если при очередном шаге ни одна из клеток не меняет своего состояния.");

        System.out.println("\nПеред началом игры, вам нужно создать файл и задать в нём начальную конфигурацию живых клеток.");
        System.out.println("Конфигурация представляется в виде прямоугольной фигуры, где:");
        System.out.println("'1' — живая клетка");
        System.out.println("'0' — мёртвая клетка");
        System.out.println("Пример конфигурации 'Планер':");
        System.out.println("010");
        System.out.println("001");
        System.out.println("111");
        System.out.println("Эта конфигурация при каждом шаге меняет своё положение, создавая эффект движения.");
        System.out.println("Варианты других конфигураций Вы можете посмотреть на сайте: https://kvant.mccme.ru/1974/09/igra_zhizn.htm");
        System.out.println("\nНажмите Enter, чтобы начать игру.");
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        rulesGame(); // Знакомство с игрой
        scanner.nextLine();

        System.out.println("Сначала создадим поле для игры.");
        System.out.print("Введите количество строк: ");
        int rows = scanner.nextInt();

        System.out.print("Введите количество столбцов: ");
        int cols = scanner.nextInt();

        Field field = new Field(rows, cols); // Создаём объект поля
        GraphicWindow window = new GraphicWindow(field); // Создаём графическое окно

        System.out.println("\nПоле создано! Теперь разместим на него вашу фигуру (или колонию).");
        System.out.print("Введите путь к файлу с конфигурацией: ");
        scanner.nextLine(); // Очищаем поток ввода
        String file = scanner.nextLine();

        System.out.println("\nПуть к Вашему файлу получен! Теперь нужен номер строки и столбца левого верхнего угла расположения фигуры на поле. ");
        System.out.println("Обратите внимание, что фигура может распасться на части, если окажется за границами поля, " +
                "\nв нашей игре верхняя граница поля считается продолжением нижней, а левая граница – продолжением правой.");
        System.out.print("Введите номер строки (от 1 до " + rows + "): ");
        int startRow = scanner.nextInt();
        System.out.print("Введите номер столбца (от 1 до " + cols + "): ");
        int startCol = scanner.nextInt();

        field.loadingShape(file, startRow - 1, startCol - 1); // Загружаем фигуру из файла на игровое поле
        window.repaint(); // Обновляем отображение графического окна

        Evolution evolution = new Evolution(field); // Создаём объект для управления эволюцией клеток
        GameCompletion gameCompletion = new GameCompletion(field); // Создаём объект для проверки завершения игры

        System.out.println("\nВот и всё, Ваша фигура на поле, а это значит, что можно уже начать игру!");
        scanner.nextLine(); // Очищаем поток ввода

        while (true) { // Бесконечный цикл игры
            System.out.println("Нажмите Enter для следующего шага или введите 'выход': ");
            String command = scanner.nextLine();

            if (command.equals("выход")) {
                System.out.println("Игра завершена.");
                break;
            }

            // Проверяем условия завершения игры
            if (gameCompletion.checkingСompletionGame()) {
                break;
            }

            evolution.evolve(); // Выполняем шаг эволюции
            window.repaint(); // Обновляем отображение графического окна
        }

        System.out.println("Закройте графическое окно, чтобы завершить работу программы.");
    }
}