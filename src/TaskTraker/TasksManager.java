package TaskTraker;
import java.util.HashMap;
import java.util.Scanner;

public class TasksManager {
    Scanner scanner;
    HashMap <Integer,Tasks> tasksHash;
    int id;

    public TasksManager() {
        scanner = new Scanner(System.in);
        tasksHash=new HashMap<>();
        int id=0;
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
    void creating(){
        System.out.println("Что создать?");
        printMenuTypeOfTask (1);
        int command = scanner.nextInt();
        switch (command) {
            case 1:
                Task task= (Task) create();
                tasksHash.put(++id, task);
                break;
            case 2:
                SubTask subTask= (SubTask) create();
                System.out.println("Идентификатор эпика, к которому относится подзадача?");
                int affiliationOfEpic=scanner.nextInt();
                subTask.setAffiliationOfEpic(affiliationOfEpic); // Заменить переменную на idEpic
                tasksHash.put(++id, subTask);
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
    Tasks create(){
        System.out.println("Название :");
        String title=scanner.nextLine();
        System.out.println("Описанме :");
        String description=scanner.nextLine();
        StatusOfTasks statusOfTasks=StatusOfTasks.NEW;
        return new Task(title, description, statusOfTasks);
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
        String task=".Задачи";
        String subtask=".Подзадачи";
        String epic=".Эпики";
        String exit="0.Выход";
        String output="";
        switch (viewMenuTypeOfTask) {
            case 1:
                output = 1 + task + "\n" + 2 + subtask+ "\n" + 3 + epic + "\n" + exit;
                break;
        }
        System.out.println(output);
    }
}
