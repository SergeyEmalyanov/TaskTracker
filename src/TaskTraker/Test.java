package TaskTraker;

import java.util.List;

import static TaskTraker.StatusOfTasks.*;

public class Test {
    public static void main(String[] args) {
        //Managers.getDefault();
        TaskManager taskManager = new InMemoryTaskManager();
        Task epic = new Epic("epic1", "descriptionOfEpic1", NEW);
        Task subtask1 = new SubTask("subtask1", "descriptionOfSubtask1", NEW, (Epic) epic);
        Task subtask2 = new SubTask("subtask2", "descriptionOfSubtask2", DONE, (Epic) epic);
        taskManager.add(0, epic);
        taskManager.add(0, subtask1);
        taskManager.add(0, subtask2);
        System.out.println(epic.getStatusOfTasks());
        List <Task> all = taskManager.getAll();
        for (Task task:all) {
            System.out.println(task.toString());
        }
        taskManager.get(1);
        taskManager.get(2);
        taskManager.get(3);
        taskManager.get(3);
        taskManager.get(2);
        taskManager.getHistory();

    }
}