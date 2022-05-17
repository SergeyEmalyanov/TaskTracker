package TaskTraker;
import java.util.Scanner;

public class Menu {
    static Scanner scanner = new Scanner(System.in);
    static int printMainMenu() {
        System.out.println("ОСНОВНОЕ МЕНЮ");
        System.out.println("1.Создание и удаление");
        System.out.println("2.Вывод на экран");
        System.out.println("3.Изменение статуса");
        System.out.println("0.Выход из программы");
        return scanner.nextInt();
    }

    static int printMenu1() {
        System.out.println("МЕНЮ Создание и удаление");
        System.out.println("1.Создание");
        System.out.println("2.Удаление");
        System.out.println("3.Очистить менеджер задач");
        System.out.println("0.Выход");
        return scanner.nextInt();
    }

    static int printMenu2() {
        System.out.println("МЕНЮ Вывод на экран");
        System.out.println("1.Вывод по идентификатору");
        System.out.println("2.Вывести список");
        System.out.println("3.Получить список всех задач");
        System.out.println("0.Выход");
        return scanner.nextInt();
    }

    static int printMenuTypeOfTask(String headingMenu, int viewMenuTypeOfTask) {
        String task = ".Задачи";
        String epic = ".Эпики";
        String subtask = ".Подзадачи";
        String exit = "0.Выход";
        String output = "";
        if (viewMenuTypeOfTask==0) {
            output = 1 + task + "\n" + 2 + epic + "\n" + exit;
        } else if (viewMenuTypeOfTask>0){
            output = 1 + task + "\n" + 2 + epic + "\n" + 3 + subtask + "\n" + exit;
        } else {
            output = 1 + task + "\n" + 2 + subtask + "\n" + exit;
        }
        System.out.println(headingMenu);
        System.out.println(output);
        return scanner.nextInt();
    }

}
