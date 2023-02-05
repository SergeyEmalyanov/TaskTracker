package TaskTraker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static TaskTraker.StatusOfTasks.*;
import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    @BeforeEach
    public void beforeEach() {
        taskManager.deleteAll();
        Task epic = new Epic("epic1", "descriptionOfEpic1", NEW);
        Task subtask1 = new SubTask("subtask1", "descriptionOfSubtask1", NEW, (Epic) epic);
        Task subtask2 = new SubTask("subtask2", "descriptionOfSubtask2", NEW, (Epic) epic);
        taskManager.add(0, epic);
        taskManager.add(0, subtask1);
        taskManager.add(0, subtask2);
    }

    ///////////////////////////////////ТЕСТЫ EPIC////////////////////////////////////////////////////////////
    @Test
    // тест на возврат статуса NEW у эпика при пустом списке подзадач
    public void shouldReturnEpicStatusAsNewWhenEmptyListOfSubtask() {
        Task epic = new Epic("epic1", "descriptionOfEpic1", StatusOfTasks.DONE);
        final int epicId = taskManager.add(0, epic);
        epic.setId(epicId);
        assertEquals(NEW, taskManager.get(epicId).getStatusOfTasks(), "У эпика должен быть статус NEW");
    }

    @Test
    // тест на возврат статуса NEW у эпика, если у всех подзадач статус NEW
    public void shouldReturnEpicStatusAsNewWhenAllSubtasksInStatusNew() {
        assertEquals(NEW, taskManager.get(1).getStatusOfTasks(), "У эпика должен быть статус NEW");
    }

    @Test
    // тест на возврат статуса DONE у эпика, если у всех подзадач статус DONE
    public void shouldReturnEpicStatusAsDoneWhenAllSubtasksInStatusDone() {
        taskManager.add(2, taskManager.get(2).setStatusOfTasks(DONE));
        taskManager.add(3, taskManager.get(3).setStatusOfTasks(DONE));
        assertEquals(DONE, taskManager.get(1).getStatusOfTasks(), "У эпика должен быть статус DONE");
    }

    @Test
    // тест на возврат статуса IN_PROGRESS у эпика, если у подзадач статусы DONE и NEW
    public void shouldReturnEpicStatusAsInProgressWhenSubtaskInStatusDoneOrNew() {
        //taskManager.add(2,taskManager.get(2).setStatusOfTasks(NEW));
        taskManager.add(3, taskManager.get(3).setStatusOfTasks(DONE));
        assertEquals(IN_PROGRESS, taskManager.get(1).getStatusOfTasks(), "У эпика должен быть статус IN_PROGRESS");
    }

    @Test
    // тест на возврат статуса IN_PROGRESS у эпика, если всех подзадач статусы IN_PROGRESS
    public void shouldReturnEpicStatusAsInProgressWhenAllSubtaskInStatusInProgress() {
        taskManager.add(2, taskManager.get(2).setStatusOfTasks(IN_PROGRESS));
        taskManager.add(3, taskManager.get(3).setStatusOfTasks(IN_PROGRESS));
        assertEquals(IN_PROGRESS, taskManager.get(1).getStatusOfTasks(), "У эпика должен быть статус IN_PROGRESS");
    }

    @Test
    // тест на возврат статуса IN_PROGRESS у эпика, если у подзадач разные статусы
    public void shouldReturnEpicsStatusInProgressWhenSubtaskInDifferentStatus() {
        taskManager.add(3, taskManager.get(3).setStatusOfTasks(IN_PROGRESS));
        taskManager.add(0, new SubTask("subtask2", "descriptionOfSubtask2", DONE,
                (Epic) taskManager.get(1)));
        assertEquals(IN_PROGRESS, taskManager.get(1).getStatusOfTasks(), "У эпика должен быть статус IN_PROGRESS");
    }

    @Test
    // тест на получение эпика из его подзадачи при нормальной работе
    public void shouldReturnEpicFromHisSubTask() {
        assertEquals(taskManager.get(1), ((SubTask) taskManager.get(2)).getEpicOfSubTask());
    }

    @Test
    // тест на получение подзадач эпика при пустом списке подзадач
    public void shouldReturnNullFromHisSubtask() {
        taskManager.remove(2);
        taskManager.remove(3);
        List<Task> list = ((Epic) taskManager.get(1)).getSubTasksOfEpic();
        assertTrue(list.isEmpty());
    }

    ///////////////////////////////////ТЕСТЫ GET////////////////////////////////////////////////////////////
    @Test
    // Тест на получение задачи при нормальном поведении add norm
    public void shouldReturnTaskFromTaskManagerAfterAdd() {
        Task task = new Task("Task", "DescriptionOfTask", NEW);
        final int taskId = taskManager.add(0, task);
        task.setId(taskId);
        assertEquals(task, taskManager.get(taskId), "Задачи должны быть равны");
    }

    @Test
    // Тест на получение задачи при пустом списке add empty
    public void getTaskShouldReturnNullFromTaskManagerByEmpty() {
        taskManager.deleteAll();
        assertNull(taskManager.get(1), "Должен вернуть Null");
    }

    @Test
    // Тест на получение задачи при несуществующем ID
    public void getTaskShouldReturnNullWhenIdNotCorrect() {
        assertNull(taskManager.get(1000));
    }

    @Test
    // Тест на получение задачи при пустом ID
    public void getTaskShouldReturnNullWhenIdNull() {
        assertNull(taskManager.get(null));
    }

    ///////////////////////////////////ТЕСТЫ ADD////////////////////////////////////////////////////////////
    @Test
    // Тест add при нормальной работе
    public void addTaskShouldReturnTasks() {
        taskManager.deleteAll();
        Task task = new Task("Task", "DescriptionOfTask", NEW);
        Epic epic = new Epic("Epic", "DescriptionOfEpic", NEW);
        SubTask subTask = new SubTask("SubTask", "DescriptionOfSubTask", NEW, epic);
        epic.addSubTaskOfEpic(subTask);
        int idTask = taskManager.add(0, task);
        int idEpic = taskManager.add(0, epic);
        int idSubTask = taskManager.add(0, subTask);
        assertAll(
                () -> assertEquals(task, taskManager.get(idTask)),
                () -> assertEquals(epic, taskManager.get(idEpic)),
                () -> assertEquals(subTask, taskManager.get(idSubTask))
        );
    }

    @Test
    //Тест add при пустом идентификаторе
    public void addTaskShouldReturnIdNotNull() {
        Task task = new Task("Task", "DescriptionOfTask", NEW);
        final int idTask = taskManager.add(null, task);
        assertTrue(idTask > 0);
    }

    @Test
    //Тест add при отсутствии задачи
    public void addShouldReturnNull() {
        final int idTask = taskManager.add(0, null);
        assertEquals(0, idTask);
    }

    ///////////////////////////////////ТЕСТЫ REMOVE/////////////////////////////////////////////////////////////
    @Test
    //Тест remove при нормальной работе
    public void removeShouldDeleteTask() {
        taskManager.deleteAll();
        Task task = new Task("Task", "DescriptionOfTask", NEW);
        Epic epic = new Epic("Epic", "DescriptionOfEpic", NEW);
        SubTask subTask = new SubTask("SubTask", "DescriptionOfSubTask", NEW, epic);
        epic.addSubTaskOfEpic(subTask);
        int idTask = taskManager.add(0, task);
        int idEpic = taskManager.add(0, epic);
        int idSubTask = taskManager.add(0, subTask);
        assertEquals(3, taskManager.getAll().size());
        taskManager.remove(idTask);
        assertEquals(2, taskManager.getAll().size());
        taskManager.remove(idSubTask);
        assertEquals(1, taskManager.getAll().size());
        taskManager.remove(idEpic);
        assertTrue(taskManager.getAll().isEmpty());
    }

    @Test
    //Тест на удаление подзадачи у эпика
    public void epicShouldReturnSubTaskListWithoutRemoteSubTask() {
        taskManager.deleteAll();
        Epic epic2 = new Epic("Epic", "DescriptionOfEpic", NEW);
        SubTask subTask1 = new SubTask("SubTask1", "DescriptionOfSubTask1", NEW, epic2);
        SubTask subTask2 = new SubTask("SubTask2", "DescriptionOfSubTask2", DONE, epic2);
        int idEpic = taskManager.add(0, epic2);
        int idSubTask1 = taskManager.add(0,subTask1);
        int idSubTask2 = taskManager.add(0,subTask2);
        assertEquals(2,((Epic) taskManager.get(idEpic)).getSubTasksOfEpic().size());
        taskManager.remove(idSubTask2);
        assertAll(
                ()->assertNull(taskManager.get(idSubTask2)),
                ()->assertEquals(1,((Epic) taskManager.get(idEpic)).getSubTasksOfEpic().size()),
                ()->assertEquals(NEW,taskManager.get(idEpic).getStatusOfTasks())
        );
    }
}