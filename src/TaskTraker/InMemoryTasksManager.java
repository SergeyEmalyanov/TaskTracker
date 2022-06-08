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







/*
import java.util.ArrayList;
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
            switch (Menu.printMainMenu()) {
                case 1://Создание и удаление
                    creatingAndDeleting();
                    break;
                case 2://Вывод на экаран
                    outputToTheScreen();
                    break;
                case 3://Изменение статуса
                    statusChange();
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
            switch (Menu.printMenu1()) {
                case 1://Создание
                    creating();
                    break;
                case 2://Удаление
                    deleting();
                    break;
                case 3://Очистить менеджер задач
                    tasks.clear();
                    System.out.println("Список задач пуст");
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
            switch (Menu.printMenuTypeOfTask("Что создать?", scoreEpic)) {
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

        System.out.println(tasks.get(id));
    }

    void createAndAddEpic() {
        System.out.println("Новый эпик");
        tasks.put(++id, new Epic(createTitle(), createDescription(), StatusOfTasks.NEW));
        scoreEpic++;

        System.out.println(tasks.get(id));
    }

    void createAndAddSubtask() {
        int idEpic;
        if (scoreEpic == 0) {
            return;
        } else if (scoreEpic == 1) {
            idEpic=findEpicOne();
        } else {
            // Вывести список эпиков
            System.out.println("Введите id эпика, к которому относится подзадача");
            idEpic = scanner.nextInt();
        }
        tasks.put(++id, new SubTask(createTitle(), createDescription(), StatusOfTasks.NEW, idEpic));
        tasks.get(idEpic).setIdSubTask(id);

        System.out.println(tasks.get(id));
    }

    String createTitle() {
        System.out.println("Название :");
        return scanner.next();
    }

    String createDescription() {
        System.out.println("Описанме :");
        return scanner.next();
    }

    void deleting(){
        while (true){
            switch (Menu.printMenuTypeOfTask("Что удалить?", 1)) {
                case 1:
                    deletingTask();
                    break;
                case 2:
                    deletingEpic();
                    break;
                case 3:
                    deletingSubTask();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неизвестная команда");
                    break;
            }
        }
    }

    void deletingTask() {
        outputTasks();
        System.out.println("Введите ID задачи, которую вы хотите удалить");
        tasks.remove(scanner.nextInt());
    }

    void deletingEpic() {
        outputEpics();
        System.out.println("Введите ID эпика, который вы хотите удалить");
        tasks.remove(scanner.nextInt());
    }

    void deletingSubTask(){
        outputEpics();
        System.out.println("Введите ID подзадачи, которую вы хотите удалить");
        tasks.remove(scanner.nextInt());
    }


    void outputToTheScreen() {
        while (true) {
            switch (Menu.printMenu2()) {
                case 1://Вывод по идентификатору
                    outputByID();
                    break;
                case 2://Вывести список
                    outputList();
                    break;
                case 3://Получить список всех задач
                    outputTasks();
                    outputEpics();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неизвестная команда");
                    break;

            }
        }
    }

    void outputByID(){
        System.out.println("Номер идентификатора?");
        int idPrint= scanner.nextInt();
        if (tasks.get(idPrint).getClass()==Epic.class) {
            outputEpic(idPrint);
        } else if (tasks.get(idPrint).getClass()==SubTask.class) {
            System.out.println("Нет задачи или эпика с таким ID");
        } else {
            outputTask(idPrint);
        }
    }

    void outputTask(int idTask){
        System.out.println("ЗАДАЧА ID:"+idTask+"  "+ tasks.get(idTask));
    }

    void outputEpic(int idEpic){
        System.out.println("ЭПИК ID:"+idEpic+"  "+ tasks.get(idEpic));
        System.out.println("ПОДЗАДАЧИ ЭПИКА");
        for (int i=0; i<tasks.get(idEpic).getIdSubTask().size(); i++){
            System.out.println("    "+ (i + 1) + ". " + tasks.get(tasks.get(idEpic).getIdSubTask(i)));
        }
        System.out.println("\n");
    }

    void outputList(){
        switch (Menu.printMenuTypeOfTask("Что вывести?", 0)){
            case 1:
                outputTasks();
                break;
            case 2:
                outputEpics();
                break;
            case 0:
                return;
            default:
                System.out.println("Неизвестная команда");
                break;
        }
    }
    void outputTasks(){
        for (int i=1; i<=id; i++){
            if (tasks.get(i).getClass()==Task.class){
                outputTask(i);
            }
        }
    }

    void outputEpics(){
        for (int i=1; i<=id; i++){
            if (tasks.get(i).getClass()==Epic.class){
                outputEpic(i);
            }
        }
    }

    void statusChange() {
        while (true) {
            switch (Menu.printMenuTypeOfTask("Выберите тип задачи, статус которой вы хотите изменить",
                    -1)) {
                case 1:
                    statusChangeTask();
                    break;
                case 2:
                    statusChangeSubTask();
                case 0:
                    return;
                default:
                    System.out.println("Неизвестная команда");
                    break;



            }
        }
    }

    void statusChangeTask(){
        Task task;
        StatusOfTasks statusOfTasks = null;
        outputTasks();
        System.out.println("Введите ID задачи, статус которой вы хотите изменить");
        int statusChangeID=scanner.nextInt();
        String title = tasks.get(statusChangeID).getTitle();
        String description= tasks.get(statusChangeID).getDescription();
        if (tasks.get(statusChangeID).getStatusOfTasks()==StatusOfTasks.NEW){
            statusOfTasks=StatusOfTasks.IN_PROGRESS;
        } else if(tasks.get(statusChangeID).getStatusOfTasks()==StatusOfTasks.IN_PROGRESS){
            statusOfTasks=StatusOfTasks.DONE;
        } else if (tasks.get(statusChangeID).getStatusOfTasks()==StatusOfTasks.DONE) {
            System.out.println("Задача завершена, изменение статуса невозможно");
        }
        task=new Task(title,description,statusOfTasks);
        tasks.put(statusChangeID,task);
    }

    void statusChangeSubTask(){
        outputEpics();
        boolean status=true;
        SubTask task;
        StatusOfTasks statusOfTasks = null;
        System.out.println("Введите ID подзадачи, статус которой вы хотите изменить");
        int statusChangeID=scanner.nextInt();
        String title = tasks.get(statusChangeID).getTitle();
        String description= tasks.get(statusChangeID).getDescription();
        int idEpic=tasks.get(statusChangeID).getIdEpic();
        if (tasks.get(statusChangeID).getStatusOfTasks()==StatusOfTasks.NEW){
            statusOfTasks=StatusOfTasks.IN_PROGRESS;
        } else if(tasks.get(statusChangeID).getStatusOfTasks()==StatusOfTasks.IN_PROGRESS){
            statusOfTasks=StatusOfTasks.DONE;
        } else if (tasks.get(statusChangeID).getStatusOfTasks()==StatusOfTasks.DONE) {
            System.out.println("Задача завершена, изменение статуса невозможно");
        }
        task= new SubTask(title,description,statusOfTasks,idEpic);
        tasks.put(statusChangeID,task);
        ///////////////////////////////////////////////////////////////////////
        ArrayList<Integer> idSubTask = tasks.get(idEpic).getIdSubTask();
        for (int i=0; i<idSubTask.size(); i++){
            if (tasks.get(idSubTask.get(i)).getStatusOfTasks()==StatusOfTasks.IN_PROGRESS){
                //Меняем статус эпика на IN_PROGRESS
            }
            status = status && tasks.get(idSubTask.get(i)).getStatusOfTasks()==StatusOfTasks.DONE;
        }
        if (status) {
            //Меняем статус эпика на DONE
        }


    }

    int findEpicOne() {
        int idEpic=0;
        for (Map.Entry entry : tasks.entrySet()) {
            if (entry.getValue().getClass() == Epic.class) {
                idEpic= (int) entry.getKey();
            }
        }
        System.out.println(idEpic);
        return idEpic;
    }
}

 */