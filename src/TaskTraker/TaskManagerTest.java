package TaskTraker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static TaskTraker.StatusOfTasks.*;
import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;
    HistoryManager historyManager;

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
        historyManager = Managers.getDefaultHistory();
    }

    @BeforeEach
    public void beforeEach() {
        taskManager.deleteAll();
        Epic epic = new Epic("epic1", "descriptionOfEpic1", NEW);
        SubTask subtask1 = new SubTask("subtask1", "descriptionOfSubtask1", NEW, epic);
        SubTask subtask2 = new SubTask("subtask2", "descriptionOfSubtask2", NEW, epic);

        taskManager.add(0, epic);
        taskManager.add(0, subtask1);
        taskManager.add(0, subtask2);

    }

    ///////////////////////////////////ТЕСТЫ EPIC////////////////////////////////////////////////////////////
    @Test
    // тест на возврат статуса NEW у эпика при пустом списке подзадач
    public void shouldReturnEpicStatusAsNewWhenEmptyListOfSubtask() {
        taskManager.deleteAll();
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
        taskManager.delete(2);
        taskManager.delete(3);
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
        assertEquals(task, taskManager.getHistory().get(0));
    }

    @Test
    // Тест на получение задачи при пустом списке add empty
    public void getTaskShouldReturnNullFromTaskManagerByEmpty() {
        taskManager.deleteAll();
        assertNull(taskManager.get(1), "Должен вернуть Null");
        assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    // Тест на получение задачи при несуществующем ID
    public void getTaskShouldReturnNullWhenIdNotCorrect() {
        assertNull(taskManager.get(1000));
        assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    // Тест на получение задачи при пустом ID
    public void getTaskShouldReturnNullWhenIdNull() {
        assertNull(taskManager.get(null));
        assertTrue(taskManager.getHistory().isEmpty());
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

    ///////////////////////////////////ТЕСТЫ DELETE/////////////////////////////////////////////////////////////
    @Test
    //Тест delete при нормальной работе
    public void ShouldDeleteTask() {
        taskManager.deleteAll();
        Task task = new Task("Task", "DescriptionOfTask", NEW);
        Epic epic = new Epic("Epic", "DescriptionOfEpic", NEW);
        SubTask subTask = new SubTask("SubTask", "DescriptionOfSubTask", NEW, epic);
        int idTask = taskManager.add(0, task);
        int idEpic = taskManager.add(0, epic);
        int idSubTask = taskManager.add(0, subTask);
        taskManager.get(idTask);//////Создаем историю просмотров
        taskManager.get(idEpic);
        taskManager.get(idSubTask);
        assertEquals(3, taskManager.getAll().size());
        assertEquals(3, taskManager.getHistory().size());//В истории три просмотра
        taskManager.delete(idTask);
        assertEquals(2, taskManager.getAll().size());
        assertEquals(epic, taskManager.getHistory().get(0));// После удаления задачи первый в исории эпик
        assertEquals(subTask, taskManager.getHistory().get(1));// После удаления задачи последний в исории подзадача
        taskManager.delete(idSubTask);
        assertEquals(1, taskManager.getAll().size());
        assertEquals(1, taskManager.getHistory().size());// В истории должен остаться только эпик
        assertEquals(epic, taskManager.getHistory().get(0));
        taskManager.delete(idEpic);
        assertTrue(taskManager.getAll().isEmpty());
        assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    //Тест delete на удаление подзадачи у эпика
    public void epicShouldReturnSubTaskListWithoutRemoteSubTask() {
        taskManager.deleteAll();
        Epic epic2 = new Epic("Epic", "DescriptionOfEpic", NEW);
        SubTask subTask1 = new SubTask("SubTask1", "DescriptionOfSubTask1", NEW, epic2);
        SubTask subTask2 = new SubTask("SubTask2", "DescriptionOfSubTask2", DONE, epic2);
        int idEpic = taskManager.add(0, epic2);
        int idSubTask1 = taskManager.add(0, subTask1);
        int idSubTask2 = taskManager.add(0, subTask2);
        taskManager.get(idEpic);//////Создаем историю просмотров
        taskManager.get(idSubTask2);
        taskManager.get(idSubTask1);
        assertEquals(2, ((Epic) taskManager.get(idEpic)).getSubTasksOfEpic().size(), "У эпика должно быть" +
                "две задачи");
        assertEquals(3, taskManager.getHistory().size(), "В истории должно быть три просмотра");
        taskManager.delete(idSubTask2);
        assertAll(
                () -> assertNull(taskManager.get(idSubTask2), "Вторая задача удвлена"),
                () -> assertEquals(1, ((Epic) taskManager.get(idEpic)).getSubTasksOfEpic().size(), "У" +
                        " эпика осталась одна задача"),
                () -> assertEquals(NEW, taskManager.get(idEpic).getStatusOfTasks(), "Статус эпика должен быть NEW"),
                () -> assertEquals(2, taskManager.getHistory().size(), "В истории должно остаться два просмотра")
        );
    }

    @Test
    // Тест delete на удаление эпика и его подзадач
    public void remoteEpicShouldRemoteHisSubTasks() {
        taskManager.deleteAll();
        Epic epic = new Epic("Epic3", "DescriptionOfEpic3", NEW);
        SubTask subTask1 = new SubTask("SubTask1", "DescriptionOfSubTask1", NEW, epic);
        SubTask subTask2 = new SubTask("SubTask2", "DescriptionOfSubTask2", DONE, epic);
        int idEpic = taskManager.add(0, epic);
        int idSubTask1 = taskManager.add(0, subTask1);
        int idSubTask2 = taskManager.add(0, subTask2);
        taskManager.get(idEpic);//////Создаем историю просмотров
        taskManager.get(idSubTask2);
        taskManager.get(idSubTask1);
        assertEquals(3, taskManager.getHistory().size(), "В истории должно быть три просмотра");
        taskManager.delete(idEpic);
        assertAll(
                () -> assertNull(taskManager.get(idEpic)),
                () -> assertNull(taskManager.get(idSubTask1)),
                () -> assertNull(taskManager.get(idSubTask2)),
                () -> assertTrue(taskManager.getHistory().isEmpty(), "История должна быть пустой")

        );
    }

    @Test
    //Тест delete на удаление несуществующей задачи
    public void deleteShouldThrowException() {
        final Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.delete(1000));
        assertEquals("Задачи с таким ID не существует", exception.getMessage());
    }

    ////////////////////////////////////////  ТЕСТЫ HISTORY  //////////////////////////////////////////////////////
    @Test
    // Тест add, getHistory, delete при нормальной работе, дублировании.
    public void getHistoryShouldReturnListOfViews() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.remove(-1);
        Epic epic = new Epic(1, "epic1", "descriptionOfEpic1", NEW);
        SubTask subtask1 = new SubTask(2, "subtask1", "descriptionOfSubtask1", NEW, epic);
        SubTask subtask2 = new SubTask(3, "subtask2", "descriptionOfSubtask2", NEW, epic);
        Epic epic2 = new Epic(4, "epic2", "descriptionOfEpic2", NEW);
        SubTask subtask3 = new SubTask(5, "subTask3", "descriptionOfSubTask3", NEW, epic2);
        SubTask subtask4 = new SubTask(6, "subTask4", "descriptionOfSubTask4", DONE, epic2);
        SubTask subtask5 = new SubTask(7, "subTask5", "descriptionOfSubTask5", NEW, epic2);
        Epic epic3 = new Epic(8, "epic3", "descriptionOfEpic3", NEW);
        Task task = new Task(9, "task", "descriptionOfTask", NEW);
        Task task2 = new Task(10, "task2", "descriptionOfTask2", IN_PROGRESS);
        Task task3 = new Task(11, "task3", "descriptionOfTask3", DONE);

        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.add(subtask2);
        historyManager.add(epic2);
        historyManager.add(subtask3);
        historyManager.add(subtask4);
        historyManager.add(subtask5);
        historyManager.add(epic3);
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);

        //Добавление с переполнением
        assertAll(
                () -> assertEquals(10, historyManager.getHistory().size(), "В истории должно быть не более 10 задач"),
                () -> assertEquals(subtask1, historyManager.getHistory().get(0), "Первой должна быть задача subTask1"),
                () -> assertEquals(task3, historyManager.getHistory().get(9), "Последней должна быть Task3")
        );
        //Удаление
        historyManager.remove(2); //Первая задача
        historyManager.remove(11);//Последняя задача
        historyManager.remove(8);//Задача из середины
        List<Task> list = historyManager.getHistory();

        assertAll(
                () -> assertFalse(list.contains(subtask1), "Первая задача должна быть удалена"),
                () -> assertFalse(list.contains(task3), "Последняя задача должна быть удалена"),
                () -> assertFalse(list.contains(epic3), "Задача из середины должна быть удалена"),
                () -> assertEquals(7, historyManager.getHistory().size(), "Список просмотров должен быть меньше на 3")
        );

        // Дублирование
        historyManager.add(subtask2);
        assertAll(
                () -> assertNotSame(subtask2, historyManager.getHistory().get(0)),
                () -> assertSame(subtask2, historyManager.getHistory().get(historyManager.getHistory().size() - 1))
        );
    }

    @Test
    public void getHistoryShouldReturnEmptyListWhenEmptyBrowsingHistory(){
        historyManager.remove(-1);
        assertTrue(historyManager.getHistory().isEmpty());
    }
}