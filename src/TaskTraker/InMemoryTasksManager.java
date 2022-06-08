package TaskTraker;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class InMemoryTasksManager implements TaskManager {
    private int id;
    private static Scanner scanner;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;
    private HistoryManager historyManager;

    protected InMemoryTasksManager(){
        id=0;
        scanner = new Scanner(System.in);
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager=Managers.getDefaultHistory();
    }

    @Override
    public void menu() {
        while (true) {
            switch (printMenu()) {
                case 1:
                    create();
                    break;
                case 2:
                    update();
                    break;
                case 3:
                    gettingByID();
                    break;
                case 4:
                    gettingListOfAllEpicSubtasks();
                    break;
                case 5:
                    gettingListOfAllTasks();
                    break;
                case 6:
                    deletionByID();
                    break;
                case 7:
                    deletingAllTasks();
                    break;
                case 8:
                    history();
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Неизвестная команда");
                    break;
            }
        }

    }

    @Override
    public void create() {
        while (true) {
            switch (printMenuTypeOfTask(epics.size())) {
                case 1:
                    tasks.put(++id, new Task(createTitle(), createDescription(), StatusOfTasks.NEW));
                    break;
                case 2:
                    epics.put(++id, new Epic(createTitle(), createDescription(),
                            StatusOfTasks.NEW, new ArrayList<Integer>()));
                    break;
                case 3:
                    gettingListOfAllTasks();
                    System.out.println("ID эпика");
                    int idEpic = scanner.nextInt();
                    subTasks.put(++id, new SubTask(createTitle(), createDescription(), StatusOfTasks.NEW, idEpic));
                    epics.put(idEpic, epics.get(idEpic).addSubTask(id));
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неизвестная команда");
                    break;

            }
        }
    }

    @Override
    public void update() {
        switch (printMenuTypeOfTask(-1)) {
            case 1:
                System.out.println("ID задачи");
                int idTask = scanner.nextInt();
                tasks.put(idTask, tasks.get(idTask).taskUpdate());
                break;
            case 2:
                System.out.println("Номер подзадачи");
                int idSubTask = scanner.nextInt();
                subTasks.put(idSubTask, subTasks.get(idSubTask).taskUpdate());
                checkAndUpdateEpic(idSubTask);
                break;
            case 0:
                return;
            default:
                System.out.println("Неизвестная команда");
                break;

        }
    }

    void checkAndUpdateEpic(int idSubTask) {
        int idEpic = subTasks.get(idSubTask).getIdEpic();
        ArrayList<Integer> subTaskOfEpic = epics.get(idEpic).getIdSubTask();
        boolean statusEpic = true;
        StatusOfTasks statusSubTask;
        for (int i = 0; i < subTaskOfEpic.size(); i++) {
            statusSubTask = subTasks.get(subTaskOfEpic.get(i)).getStatusOfTasks();
            if (statusSubTask == StatusOfTasks.IN_PROGRESS && epics.get(idEpic).statusOfTasks == StatusOfTasks.NEW) {
                epics.put(idEpic, epics.get(idEpic).EpicUpdate(StatusOfTasks.IN_PROGRESS));
            }
            statusEpic = statusEpic && (statusSubTask == StatusOfTasks.DONE);
        }
        if (statusEpic) {
            epics.put(idEpic, epics.get(idEpic).EpicUpdate(StatusOfTasks.DONE));
        }
    }

    @Override
    public void gettingByID() {
        System.out.println("ID задачи ?");
        int idSomeTask = scanner.nextInt();
        if (tasks.containsKey(idSomeTask)) {
            System.out.println(tasks.get(idSomeTask));
           historyManager.add(tasks.get(idSomeTask));
        } else if (subTasks.containsKey(idSomeTask)) {
            System.out.println(subTasks.get(idSomeTask));
            historyManager.add(subTasks.get(idSomeTask));
        } else if (epics.containsKey(idSomeTask)) {
            System.out.println(epics.get(idSomeTask));
            historyManager.add(epics.get(idSomeTask));
        } else {
            System.out.println("Задачи  с таким ID нет");
        }
    }

    @Override
    public void gettingListOfAllEpicSubtasks() {
        System.out.println("ID эпика ?");
        int idEpic = scanner.nextInt();
        printEpicAndSubTask(idEpic);
    }

    void printEpicAndSubTask(int idEpic) {
        System.out.print("Эпик № " + idEpic + " ");
        System.out.println(epics.get(idEpic));
        System.out.println("Подзадачи:");
        ArrayList<Integer> subTaskOfEpic = epics.get(idEpic).getIdSubTask();
        for (int idSubTask : subTaskOfEpic) {
            System.out.print("    № " + idSubTask + " ");
            System.out.println(subTasks.get(idSubTask));
        }
    }
    @Override
    public void gettingListOfAllTasks() {
        System.out.println("Всё содержимое менеджера");
        System.out.println("Задачи:");
        for (int idTask : tasks.keySet()) {
            System.out.print("№ " + idTask + " ");
            System.out.println(tasks.get(idTask));
        }
        System.out.println("Эпики:");
        for (int idEpic : epics.keySet()) {
            printEpicAndSubTask(idEpic);
        }
    }

    @Override
    public void deletionByID() {
        gettingListOfAllTasks();
        System.out.println("ID задачи ?");
        int idSomeTask = scanner.nextInt();
        if (tasks.containsKey(idSomeTask)) {
            tasks.remove(idSomeTask);
        } else if (subTasks.containsKey(idSomeTask)) {
            subTasks.remove(idSomeTask);
        } else if (epics.containsKey(idSomeTask)) {
            ArrayList<Integer> subTaskOfEpic = epics.get(idSomeTask).getIdSubTask();
            for (int idSubTask : subTaskOfEpic) {
                subTasks.remove(idSubTask);
            }
            epics.remove(idSomeTask);
        } else {
            System.out.println("Задачи  с таким ID нет");
        }
    }
    @Override
    public void deletingAllTasks() {
        tasks.clear();
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void history(){
        for (Task task: historyManager.getHistory()){
            System.out.println(task);
        }
    }



    static String createTitle() {
        System.out.println("Название :");
        return scanner.next();
    }

    static String createDescription() {
        System.out.println("Описанме :");
        return scanner.next();
    }

    static int printMenu() {
        System.out.println("1.Создание");
        System.out.println("2.Обновление");
        System.out.println("3.Получение по идентификатору");
        System.out.println("4.Получение списка всех подзадач эпика");
        System.out.println("5.Получение списка всех задач");
        System.out.println("6.Удаление по идентификатору");
        System.out.println("7.Удаление всех задач");
        System.out.println("8.История просмотров");
        System.out.println("0.Выход");
        return scanner.nextInt();
    }

    static int printMenuTypeOfTask(int viewMenuTypeOfTask) {
        String task = ".Задачи";
        String epic = ".Эпики";
        String subtask = ".Подзадачи";
        String exit = "0.Выход";
        String output = "";
        if (viewMenuTypeOfTask == 0) {
            output = 1 + task + "\n" + 2 + epic + "\n" + exit;
        } else if (viewMenuTypeOfTask > 0) {
            output = 1 + task + "\n" + 2 + epic + "\n" + 3 + subtask + "\n" + exit;
        } else {
            output = 1 + task + "\n" + 2 + subtask + "\n" + exit;
        }
        System.out.println(output);
        return scanner.nextInt();
    }
}