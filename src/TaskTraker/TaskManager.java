package TaskTraker;

import java.util.List;

public interface TaskManager {

    <T extends Task> int add (T task);

    void remove(int id);

    Task get(int id);

    List<Task> getAll();

    void deleteAll();

    List<Task> getSubTasksOfEpic(Epic epic);
}