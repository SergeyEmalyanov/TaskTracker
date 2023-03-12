package TaskTraker;
/*
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileBackedTaskManagerOld extends InMemoryTasksManager implements TaskManager {
    protected String file;
    static Path path;

    protected FileBackedTaskManagerOld(String file) {
        this.file = file;
        path = Paths.get("backupPath.csv");
    }

    @Override
    public void menu() {
        backUp();
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
        super.create();
        save(file);
    }

    @Override
    public void update() {
        super.update();
        save(file);
    }

    @Override
    public void gettingByID() {
        super.gettingByID();
        save(file);
    }

    @Override
    public void gettingListOfAllEpicSubtasks() {
        super.gettingListOfAllEpicSubtasks();
    }

    @Override
    void printEpicAndSubTask(int idEpic) {
        super.printEpicAndSubTask(idEpic);
    }

    @Override
    public void gettingListOfAllTasks() {
        super.gettingListOfAllTasks();
    }

    @Override
    public void deletionByID() {
        super.deletionByID();
        save(file);
    }

    @Override
    public void deletingAllTasks() {
        super.deletingAllTasks();
        save(file);
    }

    @Override
    public void history() {
        super.history();
    }

    void backUp() {
        if (Files.exists(Path.of(file))) {
            String[] stringFromFile = loadFromFileReadString(file);
            boolean taskOrHistory = true;
            for (String s : stringFromFile) {
                if (taskOrHistory) {
                    if ("----HISTORY----".equals(s)) {
                        taskOrHistory = false;
                    } else {
                        stringToTask(s);
                    }
                } else {
                    stringToHistory(s);
                }
            }
        } else {
            System.out.println("Файл backUp отсутствует");
        }
    }


    void stringToTask(String string) {
        String[] taskFromString = string.split(",");
        String typeOfTask = taskFromString[0];
        int id = Integer.parseInt(taskFromString[1]);
        String title = taskFromString[2];
        String description = taskFromString[3];
        StatusOfTasks status = statusOfTasks(taskFromString[4]);
        switch (typeOfTask) {
            case "Task":
                tasks.put(id, new Task(id, title, description, status));
                super.id++;
                break;

            case "Epic":
                epics.put(id, new Epic(id, title, description, status, new ArrayList<>()));///!!!
                super.id++;
                break;

            case "SubTask":
                int idEpic = Integer.parseInt(taskFromString[5]);
                subTasks.put(id, new SubTask(id, title, description, status, epics.get(idEpic)));
                epics.put(idEpic, epics.get(idEpic).addSubTaskOfEpic(subTasks.get(id)));
                super.id++;
                break;
        }
    }

    StatusOfTasks statusOfTasks(String status) {
        switch (status) {
            case "NEW":
                return StatusOfTasks.NEW;
            case "IN_PROGRESS":
                return StatusOfTasks.IN_PROGRESS;
            case "DONE":
                return StatusOfTasks.DONE;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    void stringToHistory(String string) {
        String[] numOfHistoryString = string.split(",");
        for (String s : numOfHistoryString) {
            int numOfHistory = Integer.parseInt(s);
            if (tasks.containsKey(numOfHistory)) {
                historyManager.add(tasks.get(numOfHistory));
            } else if (subTasks.containsKey(numOfHistory)) {
                historyManager.add(subTasks.get(numOfHistory));
            } else if (epics.containsKey(numOfHistory)) {
                historyManager.add(epics.get(numOfHistory));
            } else {
                System.out.println("Ошибка записи истории из файла");
            }

        }

    }

    void save(String file) {
        try (Writer backUp = new FileWriter(file); BufferedWriter bufferBackUp = new BufferedWriter(backUp);
             Writer backUpPath = Files.newBufferedWriter(path)) {

            StringBuilder stringBuilder;
            for (Task task : tasks.values()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(taskToString(task));
                stringBuilder.append("\n");
                bufferBackUp.write(stringBuilder.toString()); //1
                backUpPath.write(stringBuilder.toString()); //2
            }

            for (Epic epic : epics.values()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(taskToString(epic));
                for (int i = 0; i < epic.getSubTaskOfEpic().size(); i++) {
                    stringBuilder.append(epic.getSubTaskOfEpic().get(i).getId());
                    stringBuilder.append(",");
                }
                stringBuilder.append("\n");
                bufferBackUp.write(stringBuilder.toString());//1
                backUpPath.write(stringBuilder.toString()); //2
            }

            for (SubTask subTask : subTasks.values()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(taskToString(subTask));
                stringBuilder.append(subTask.getEpicOfSubTask().getId());
                stringBuilder.append(",\n");
                bufferBackUp.write(stringBuilder.toString()); //1
                backUpPath.write(stringBuilder.toString()); //2
            }

            if (!historyManager.getHistory().isEmpty()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("----HISTORY----,\n");
                stringBuilder.append(historyToString(historyManager));
                bufferBackUp.write(stringBuilder.toString());//1
                backUpPath.write(stringBuilder.toString()); //2
            }
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Запись в файл", e.getCause());
            } catch (ManagerSaveException ex) {
                ex.printStackTrace();
            }
        }
    }

    private <K extends Task> StringBuilder taskToString(K task) {
        return new StringBuilder(task.getClass().getSimpleName() + "," + task.getId() + "," + task.getTitle() + "," +
                task.getDescription() + "," + task.statusOfTasks.toString() + ",");
    }

    private String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        for (Task task : manager.getHistory()) {
            history.append(task.getId());
            history.append(",");
        }
        return history.toString();
    }

    static String[] loadFromFileReadString(String file) {
        String string = null;
        try {
            string = Files.readString(Path.of(file));
        } catch (IOException e) {
            try {
                throw new ManagerSaveExceptionOld("Чтение из файла readString", e.getCause());
            } catch (ManagerSaveExceptionOld ex) {
                ex.printStackTrace();
            }
        }
        assert string != null;
        return string.split(",\n");
    }

    static String[] loadFromFileBufferReader(Path path) {
        ArrayList<String> arrayTasks = new ArrayList<>();
        String[] stringFromFile = null;
        try (BufferedReader bufferReader = Files.newBufferedReader(path)) {
            while (bufferReader.ready()) {
                arrayTasks.add(bufferReader.readLine());
            }
            stringFromFile = arrayTasks.toArray(new String[0]);
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Чтение из файла newBufferedReader", e.getCause());
            } catch (ManagerSaveException ex) {
                ex.printStackTrace();
            }
        }
        return stringFromFile;
    }
}

class ManagerSaveExceptionOld extends IOException {

    public ManagerSaveExceptionOld(String message, Throwable cause) {
        super(message, cause);
    }
}*/