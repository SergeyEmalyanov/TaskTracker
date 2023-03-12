package TaskTraker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final String file;
    private final Path path;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy: HH.mm");

    protected FileBackedTaskManager(String file) {
        this.file = file;
        path = Paths.get("backupPath.csv");
        backUp();
    }

    @Override
    public <T extends Task> int add(Integer id, T task) {
        int i = super.add(id, task);
        save();
        return i;
    }

    @Override
    public void delete(int id) {
        super.delete(id);
        save();
    }

    @Override
    public Task get(Integer id) {
        Task task = super.get(id);
        save();
        return task;
    }

    @Override
    public List<Task> getAll() {
        return super.getAll();
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        save();
    }

    @Override
    public List<Task> getSubTasksOfEpic(Epic epic) {
        return super.getSubTasksOfEpic(epic);
    }

    @Override
    public List<Task> getPrioritizedTask() {
        return super.getPrioritizedTask();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    ////////////////////////////////////////  FILE BACKED  ////////////////////////////////////////////////////////
    private void backUp() {
        System.out.println("Читаем даные из файла: " + file);
        if (Files.exists(Path.of(file))) {
            String[] stringFromFile = loadFromFileBufferReader(file);
            if (stringFromFile == null) System.out.println("Данные отсутствуют");///  Не нужная проверка
            else {
                boolean taskOrHistory = true;
                int id = 0;
                for (String s : stringFromFile) {
                    if (taskOrHistory) {
                        if ("----HISTORY----".equals(s)) {
                            taskOrHistory = false;
                        } else {
                            stringToTask(s);
                            id++;
                        }
                    } else {
                        stringToHistory(s);
                    }
                }
                super.setId(id);
            }
        } else {
            System.out.println("Файл backUp отсутствует");//// Переделать через исключение
        }
    }

    private void stringToTask(String string) {
        String[] taskFromString = string.split(",");
        String typeOfTask = taskFromString[0];
        int id = Integer.parseInt(taskFromString[1]);
        String title = taskFromString[2];
        String description = taskFromString[3];
        StatusOfTasks status = statusOfTasks(taskFromString[4]);
        Optional<LocalDateTime> localDateTime;
        if (!Objects.equals(taskFromString[5], "")) {
            localDateTime =Optional.of(LocalDateTime.parse(taskFromString[5],dateTimeFormatter));
        } else {localDateTime = Optional.empty();}
        Duration duration = Duration.parse(taskFromString[6]);

        switch (typeOfTask) {
            case "Task" -> {
                tasks.put(id, new Task(id, title, description, status));
                tasks.get(id).setStartTime(localDateTime);
                tasks.get(id).setDuration(duration);
            }

            // super.id++;
            case "Epic" -> {
                epics.put(id, new Epic(id, title, description, status));///!!!
                epics.get(id).setStartTime(localDateTime);
                epics.get(id).setDuration(duration);

                //super.id++;
            }
            case "SubTask" -> {
                int idEpic = Integer.parseInt(taskFromString[7]);
                subTasks.put(id, new SubTask(id, title, description, status, epics.get(idEpic)));
                subTasks.get(id).setStartTime(localDateTime);
                subTasks.get(id).setDuration(duration);
                epics.get(idEpic).addSubTaskOfEpic(subTasks.get(id));
            }
        }

    }

    private StatusOfTasks statusOfTasks(String status) {
        return switch (status) {
            case "NEW" -> StatusOfTasks.NEW;
            case "IN_PROGRESS" -> StatusOfTasks.IN_PROGRESS;
            case "DONE" -> StatusOfTasks.DONE;
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }

    private void stringToHistory(String string) {
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
        String localDateTime;
        if (task.getStartTime().isPresent()) localDateTime = task.getStartTime().get().format(dateTimeFormatter);
        else localDateTime="";
        return new StringBuilder(task.getClass().getSimpleName() + "," + task.getId() + "," + task.getTitle() + "," +
                task.getDescription() + "," + task.getStatusOfTasks().toString() + "," +
                localDateTime + "," + task.getDuration().toString() + ",");
    }

    private String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        for (Task task : manager.getHistory()) {
            history.append(task.getId());
            history.append(",");
        }
        return history.toString();
    }

    private static String[] loadFromFileReadString(String file) {
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
        if (string == null || string.equals("")) return null;
        else return string.split(",\n");
    }

    private static String[] loadFromFileBufferReader(String file) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferReader = Files.newBufferedReader(Paths.get(file))) {
            while (bufferReader.ready()) {
                stringBuilder.append(bufferReader.readLine()).append("\n");
            }
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Чтение из файла newBufferedReader", e.getCause());
            } catch (ManagerSaveException ex) {
                ex.printStackTrace();
            }
        }
        String string = stringBuilder.toString();
        if (string.equals("")) return null;
        else return string.split(",\n");
    }
}

class ManagerSaveException extends IOException {

    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerSaveException(String message) {
        super(message);
    }
}