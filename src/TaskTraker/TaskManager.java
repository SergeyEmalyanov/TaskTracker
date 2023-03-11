package TaskTraker;

import java.util.List;

public interface TaskManager {

    <T extends Task> int add(Integer id, T task);

    void delete(int id);

    Task get(Integer id);

    List<Task> getAll();

    void deleteAll();

    List<Task> getSubTasksOfEpic(Epic epic);

    List <Task> getPrioritizedTask ();

    List<Task> getHistory();
}