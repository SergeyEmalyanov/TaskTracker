package TaskTraker;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TasksManager {
    Scanner scanner;
    HashMap<Integer, Tasks> tasks;
    private int id;
    private int scoreEpic;

    public TasksManager() {
        scanner = new Scanner(System.in);
        tasks = new HashMap<>();
        id = 0;
        scoreEpic = 0;
    }

    void mainMenu() {
        while (true) {
            printMainMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    creatingAndDeleting();
                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Неизвестная команда");
                    break;
            }
        }
    }

    void creatingAndDeleting() {
        while (true) {
            printMenu1();
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    creating();
                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неизвестная команда");
                    break;
            }
        }
    }

    void creating() {
        while (true) {
            System.out.println("Что создать?");
            if (scoreEpic == 0) { // Заменить на if в методе
                printMenuTypeOfTask(0);
            } else {
                printMenuTypeOfTask(1);
            }
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    createAndAddTask();
                    break;
                case 2:
                    createAndAddEpic();
                    break;
                case 3:
                    createAndAddSubtask();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неизвестная команда");
                    break;

            }
        }
    }

    void createAndAddTask() {
        tasks.put(++id, new Task(createTitle(), createDescription(), StatusOfTasks.NEW));
    }

    void createAndAddEpic() {
        System.out.println("Новый эпик");
        tasks.put(++id, new Epic(createTitle(), createDescription(), StatusOfTasks.NEW));
        scoreEpic++;
    }

    void createAndAddSubtask() {
        int idEpic;
        if (scoreEpic == 0) {
            return;
        } else if (scoreEpic == 1) {
            idEpic=findEpicOne();
        } else {
            // Вывести список эпиков +if if
            System.out.println("Введите id эпика, к которому относится подзадача");
            idEpic = scanner.nextInt();
        }
        tasks.put(++id, new SubTask(createTitle(), createDescription(), StatusOfTasks.NEW, idEpic));
        tasks.get(idEpic).setIdSubTask(id);
    }

    String createTitle() {
        System.out.println("Название :");
        return scanner.next();
    }

    String createDescription() {
        System.out.println("Описанме :");
        return scanner.next();
    }

    void printMainMenu() {
        System.out.println("ОСНОВНОЕ МЕНЮ");
        System.out.println("1.Создание и удаление");
        System.out.println("2.Вывод на экран");
        System.out.println("3.Изменение статуса");
        System.out.println("0.Выход из программы");
    }

    public static void printMenu1() {
        System.out.println("МЕНЮ Создание и удаление");
        System.out.println("1.Создание");
        System.out.println("2.Удаление");
        System.out.println("3.Очистить менеджер задач");
        System.out.println("0.Выход");
    }

    public static void printMenu2() {
        System.out.println("МЕНЮ Вывод на экран");
        System.out.println("1.Вывод по идентификатору");
        System.out.println("2.Вывести список");
        System.out.println("3.Получить список всех задач");
        System.out.println("0.Выход");
    }

    public static void printMenuTypeOfTask(int viewMenuTypeOfTask) {
        String task = ".Задачи";
        String epic = ".Эпики";
        String subtask = ".Подзадачи";
        String exit = "0.Выход";
        String output = "";
        switch (viewMenuTypeOfTask) {
            case 0:
                output = 1 + task + "\n" + 2 + epic + "\n" + exit;
                break;
            case 1:
                output = 1 + task + "\n" + 2 + epic + "\n" + 3 + subtask + "\n" + exit;
                break;
        }
        System.out.println(output);
    }

    int findEpicOne() {
        int idEpic=0;
        for (Map.Entry entry : tasks.entrySet()) {
            if (entry.getValue().getClass() == Epic.class) {
                idEpic= (int) entry.getKey();
            }
        }
        return idEpic;
    }
}