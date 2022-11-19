package TaskTraker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTasksManager implements TaskManager {
    static String file;
    static Path path;

    protected FileBackedTaskManager() {
        file = "backup.csv";
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
        save();
    }

    @Override
    public void update() {
        super.update();
        save();
    }

    @Override
    public void gettingByID() {
        super.gettingByID();
        save();
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
        save();
    }

    @Override
    public void deletingAllTasks() {
        super.deletingAllTasks();
        save();
    }

    @Override
    public void history() {
        super.history();
    }

    void backUp() {
        String[] stringFromFile = loadFromFileReadString().split(",\n");
        /*System.out.println("loadFromFile");
        for (String val: stringFromFile) {
            System.out.println(val);
        }*/
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
    }

    Task stringToTask(String string) {
        System.out.println("stringToTask");
        System.out.println(string);
        String[] taskFromString = string.split(",");
        String typeOfTask = taskFromString[0];
        int id = Integer.parseInt(taskFromString[1]);
        String title = taskFromString[2];
        String description = taskFromString[3];
        StatusOfTasks status = statusOfTasks(taskFromString[4]);
        if ("Task".equals(typeOfTask)) {
            return new Task(id, title, description, status);
        } else if ("SubTask".equals(typeOfTask)) {
            int idEpic = Integer.parseInt(taskFromString[5]);
            return new SubTask(id,title,description,status,idEpic);
        } else if ("Epic".equals(typeOfTask)){
            int length = taskFromString.length-5;
            int idSubTask;
            ArrayList <Integer> idSubtaskArrayList = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                idSubTask = Integer.parseInt(taskFromString[i]);
                idSubtaskArrayList.add(idSubTask);
            }
            return new Epic(id,title,description,status,idSubtaskArrayList);
        }
        return null;
    }

    StatusOfTasks statusOfTasks(String status) {
        if ("NEW".equals(status)) {
            return StatusOfTasks.NEW;
        } else if ("IN_PROGRESS".equals(status)) {
            return StatusOfTasks.IN_PROGRESS;
        } else if ("DONE".equals(status)) {
            return StatusOfTasks.DONE;
        } else {
            return null;
        }
    }

    void stringToHistory(String string) {
        System.out.println("stringToHistory");
        System.out.println(string);
    }

    void save() {
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
            for (SubTask subTask : subTasks.values()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(taskToString(subTask));
                stringBuilder.append(subTask.idEpic);
                stringBuilder.append(",\n");
                bufferBackUp.write(stringBuilder.toString()); //1
                backUpPath.write(stringBuilder.toString()); //2
            }
            for (Epic epic : epics.values()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(taskToString(epic));
                for (int i = 0; i < epic.idSubTask.size(); i++) {
                    stringBuilder.append(epic.idSubTask.get(i));
                    stringBuilder.append(",");
                }
                stringBuilder.append("\n");
                bufferBackUp.write(stringBuilder.toString());//1
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
        return new StringBuilder(task.getClass().getSimpleName() + "," + task.id + "," + task.title + "," +
                task.description + "," + task.statusOfTasks.toString() + ",");
    }

    private String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        for (Task task : manager.getHistory()) {
            history.append(task.id);
            history.append(",");
        }
        return history.toString();
    }

    static String loadFromFileReadString() {
        String a = null;
        try {
            a = Files.readString(Path.of(file));
            //System.out.println("Чтение из файла");
            //System.out.println(a);

        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Чтение из файла readString", e.getCause());
            } catch (ManagerSaveException ex) {
                ex.printStackTrace();
            }
        }
        return a;
    }

    static String[] loadFromFileBufferReader() {
        ArrayList<String> arrayTasks = new ArrayList<>();
        String[] stringFromFile = null;
        try (BufferedReader bufferReader = Files.newBufferedReader(path)) {
            System.out.println("Чтение из файла2");
            while (bufferReader.ready()) {
                arrayTasks.add(bufferReader.readLine());
                System.out.println(bufferReader.readLine());
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

class ManagerSaveException extends IOException {

    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}