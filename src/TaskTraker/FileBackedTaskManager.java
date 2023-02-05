package TaskTraker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final String file;
    private final Path path;

    protected FileBackedTaskManager(String file) {
        this.file = file;
        path = Paths.get("backupPath.csv");
    }

    @Override
    public int add(Integer id, Task task) {
        int i = super.add(id, task);
        save();
        return i;
    }

    @Override
    public void remove(int id) {
        super.remove(id);
        save();
    }

    @Override
    public Task get(Integer id) {
        return super.get(id);
    }

    @Override
    public List<Task> getAll() {
        return super.getAll();
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
    }

    public List<Task> getSubTasksOfEpic(Epic epic) {
        return super.getSubTasksOfEpic(epic);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
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
            System.out.println("Файл backUp отсутствует");//// Переделать через исключение
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
               // super.id++;
                break;

            case "Epic":
                epics.put(id, new Epic(id, title, description, status));///!!!
                //super.id++;
                break;

            case "SubTask":
                int idEpic = Integer.parseInt(taskFromString[5]);
                subTasks.put(id, new SubTask(id, title, description, status, epics.get(idEpic)));
                epics.get(idEpic).addSubTaskOfEpic(subTasks.get(id));
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

    private void save() {
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
                for (int i = 0; i < epic.getSubTasksOfEpic().size(); i++) {
                    stringBuilder.append(epic.getSubTasksOfEpic().get(i).getId());
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
                task.getDescription() + "," + task.getStatusOfTasks().toString() + ",");
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
                throw new ManagerSaveException("Чтение из файла readString", e.getCause());
            } catch (ManagerSaveException ex) {
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

class ManagerSaveException extends IOException {

    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}