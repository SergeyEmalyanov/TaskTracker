package TaskTraker;

import java.util.ArrayList;
import java.util.Map;

public interface TaskManager2 {

    int addNewTask(Task task);

    void taskUpdate(int id, Task task);

    void taskRemove(int id);

    Task getTaskById(int id);

    Map<Integer, Task> getAllTasks();

    void deleteAllTasks();

    int addNewEpic(Epic epic);

    void EpicRemove(int id);

    Epic getEpicById(int id);

    Map<Integer, Epic> getAllEpics();

    void deleteAllEpics();

    int addNewSubTask(SubTask subTask);

    void subTaskUpdate(int id, SubTask subTask);

    void subTaskRemove(int id);

    SubTask getSubTaskById(int id);

    Map<Integer,SubTask> getAllSubTasks();

    void deleteAllSubTasks();

    ArrayList<SubTask> getSubTasksOfEpic(Epic epic);
}