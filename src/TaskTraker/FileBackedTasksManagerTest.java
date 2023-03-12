package TaskTraker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;

import static TaskTraker.StatusOfTasks.IN_PROGRESS;
import static TaskTraker.StatusOfTasks.NEW;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<TaskManager> {
    public FileBackedTasksManagerTest() {
        super(Managers.getDefault());
    }

    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
    }

    @Test
    // При нормальной работе
    public void shouldReturnTheSavedTasksAndHistory() {
        taskManager.get(1);
        taskManager.get(2);
        taskManager.get(3);

        TaskManager fileBackedTaskManager = Managers.getDefault();

        Epic epic = new Epic(1, "epic1", "descriptionOfEpic1", NEW);
        SubTask subtask1 = new SubTask(2, "subtask1", "descriptionOfSubtask1", NEW, epic);
        SubTask subtask2 = new SubTask(3, "subtask2", "descriptionOfSubtask2", NEW, epic);

        assertAll(
                () -> assertEquals(epic, fileBackedTaskManager.get(1)),
                () -> assertEquals(subtask1, fileBackedTaskManager.get(2)),
                () -> assertEquals(subtask2, fileBackedTaskManager.get(3))
        );
        List<Task> history = fileBackedTaskManager.getHistory();
        assertAll(
                () -> assertEquals(epic, history.get(0)),
                () -> assertEquals(subtask1, history.get(1)),
                () -> assertEquals(subtask2, history.get(2))
        );
    }

    @Test
    // При пустом менеджере задач
    public void shouldReturnEmptyListWhenThereAreNoTasks() {
        taskManager.deleteAll();
        TaskManager fileBackedTaskManager = Managers.getDefault();
        assertAll(
                () -> assertNull(fileBackedTaskManager.get(1)),
                () -> assertNull(fileBackedTaskManager.get(2)),
                () -> assertNull(fileBackedTaskManager.get(3)),
                () -> assertTrue(fileBackedTaskManager.getAll().isEmpty()),
                () -> assertTrue(fileBackedTaskManager.getHistory().isEmpty())
        );
    }

    @Test
    // Эпик без подзадач, эпик с подзадачами
    public void shouldReturnListOfEpicSubtasksAndEpicWithoutSubTask() {
        Epic epic = new Epic(1, "epic1", "descriptionOfEpic1", NEW);
        SubTask subtask1 = new SubTask(2, "subtask1", "descriptionOfSubtask1", NEW, epic);
        SubTask subtask2 = new SubTask(3, "subtask2", "descriptionOfSubtask2", NEW, epic);
        Epic epic2 = new Epic("epicWithoutSubtask", "descriptionOfEpicWithoutSubTask", NEW);
        int idEpic2 = taskManager.add(0, epic2);
        TaskManager fileBackedTaskManager = Managers.getDefault();
        assertAll(
                () -> assertEquals(subtask1, ((Epic) fileBackedTaskManager.get(1)).getSubTasksOfEpic().get(0)),
                () -> assertEquals(subtask2, ((Epic) fileBackedTaskManager.get(1)).getSubTasksOfEpic().get(1)),
                () -> assertTrue(((Epic) fileBackedTaskManager.get(idEpic2)).getSubTasksOfEpic().isEmpty())
        );
    }

    @Test
    // Пустой список истории просмотров
    public void shouldReturnAnEmptyBrowsingHistory() {
        TaskManager fileBackedTaskManager = Managers.getDefault();
        assertTrue(fileBackedTaskManager.getHistory().isEmpty());
    }

    @Test
    // Нормальная работа задача с временем и продолжительностью
    public void shouldReturnTaskFromFileWithStartTimeAndDuration() {
        taskManager.deleteAll();
        LocalDateTime localDateTime = LocalDateTime.now();
        Duration duration = Duration.ofDays(3);
        Task task = new Task("taskTitle", "taskDescription", NEW, localDateTime, duration);
        int idTask = taskManager.add(0, task);
        TaskManager fileBackedTaskManager = Managers.getDefault();
        assertEquals(task, fileBackedTaskManager.get(idTask));
    }

    @Test
    // Нормальная работа эпик с подзадачей с временем и продолжительностью
    public void shouldReturnEpicWithSubTaskOrEpicOFSubTaskWithStartTimeAndDuration() {
        taskManager.deleteAll();
        LocalDateTime localDateTimeSubTaskOne = LocalDateTime.now();
        LocalDateTime localDateTimeSubTaskTwo = localDateTimeSubTaskOne.plusDays(3);
        Duration durationSubTaskOne = Duration.ofDays(3);
        Duration durationSubTaskTwo = Duration.ofDays(6);
        Epic epic = new Epic("epicTitle", "epicDescription", NEW);
        int idEpic = taskManager.add(0, epic);
        SubTask subTaskOne = new SubTask("subTaskOneTitle", "subTaskOneDescription", NEW,
                localDateTimeSubTaskOne, durationSubTaskOne, epic);
        SubTask subTaskTwo = new SubTask("subTaskTwoTitle", "subTaskTwoDescription", IN_PROGRESS,
                localDateTimeSubTaskTwo, durationSubTaskTwo, epic);
        int idSubtaskOne = taskManager.add(0, subTaskOne);
        int idSubtaskTwo = taskManager.add(0, subTaskTwo);
        TaskManager fileBackedTaskManager = Managers.getDefault();
        assertEquals(epic, fileBackedTaskManager.get(idEpic));
        assertEquals(subTaskOne, ((Epic) fileBackedTaskManager.get(idEpic)).getSubTasksOfEpic().get(0));
        assertEquals(subTaskTwo, ((Epic) fileBackedTaskManager.get(idEpic)).getSubTasksOfEpic().get(1));
        assertEquals(epic, ((SubTask) fileBackedTaskManager.get(idSubtaskOne)).getEpicOfSubTask());
        assertEquals(epic, ((SubTask) fileBackedTaskManager.get(idSubtaskTwo)).getEpicOfSubTask());

        Duration durationEpic = fileBackedTaskManager.get(idEpic).getDuration();
        Duration durationSubTaskOneTwo = Duration.between(subTaskOne.getStartTime().get(), subTaskTwo.getEndTime().get());
        assertEquals(durationSubTaskOneTwo, durationEpic);
    }


    @Test
    // Нормальная работа метода getPrioritizedTask
    public void shouldReturnListOfTasksSortedByStartTime() {
        LocalDateTime localDateTimeOne = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("GMT+3")).minusWeeks(1);
        LocalDateTime localDateTimeTwo = localDateTimeOne.plusDays(1);
        LocalDateTime localDateTimeThree = localDateTimeTwo.plusDays(1);
        Duration duration = Duration.ofDays(1);
        TaskManager fileBackedTaskManager = Managers.getDefault();
        Task task = new Task("1taskTitle", "taskDescription", NEW, localDateTimeOne, duration);
        Epic epic = new Epic("1epicTitle", "epicDescription", NEW);
        SubTask subTaskOne = new SubTask("1subTaskOneTitle", "subTaskOneDescription", NEW,
                localDateTimeTwo, duration, epic);
        SubTask subTaskTwo = new SubTask("1subTaskTwoTitle", "subTaskTwoDescription", IN_PROGRESS,
                localDateTimeThree, duration, epic);
        Task taskEmptyOne = new Task("taskEmptyOne", "taskEmptyOne", NEW);
        Task taskEmptyTwo = new Task("taskEmptyTwo", "taskEmptyTwo", NEW);
        fileBackedTaskManager.deleteAll();
        fileBackedTaskManager.add(0, task);
        fileBackedTaskManager.add(0, epic);
        fileBackedTaskManager.add(0, subTaskOne);
        fileBackedTaskManager.add(0, subTaskTwo);
        fileBackedTaskManager.add(0, taskEmptyOne);
        fileBackedTaskManager.add(0, taskEmptyTwo);


        List<Task> list = fileBackedTaskManager.getPrioritizedTask();
        assertEquals(task, list.get(0));
        assertEquals(epic, list.get(1));
        assertEquals(subTaskOne, list.get(2));
        assertEquals(subTaskTwo, list.get(3));
        assertEquals(taskEmptyOne, list.get(4));
        assertEquals(taskEmptyTwo, list.get(5));
    }

    @Test
//Работа метода getPrioritizedTask при дублировании задачи
    public void shouldReturnSingleTaskWhenDuplicated() {
        Task task1 = new Task("1taskTitle", "taskDescription", NEW);
        Task task2 = new Task("1taskTitle", "taskDescription", NEW);
        TaskManager fileBackedTaskManager = Managers.getDefault();
        fileBackedTaskManager.deleteAll();
        fileBackedTaskManager.add(1, task1);
        fileBackedTaskManager.add(1, task2);

        List<Task> list = fileBackedTaskManager.getPrioritizedTask();
        assertEquals(1, list.size());
        assertEquals(task1, list.get(0));
        assertEquals(task2, list.get(0));
    }

    @Test
    // Работа метода getPrioritizedTask с задачами без времени и продолжительности
    public void shouldReturnListOfTasksSortedWithoutStartTime() {
        Task taskOne = new Task("taskOneTitle", "taskTwoDescription", NEW);
        Epic epicWithSubTask = new Epic("epicWithSubtaskTitle", "epicWithSubtaskDescription", NEW);
        Epic epicWithoutSubTask = new Epic("epicWithoutSubTaskTitle", "epicWithoutSubTaskDescription",
                IN_PROGRESS);
        SubTask subTaskOne = new SubTask("1subTaskOneTitle", "subTaskOneDescription",
                NEW, epicWithSubTask);
        SubTask subTaskTwo = new SubTask("1subTaskTwoTitle", "subTaskTwoDescription",
                IN_PROGRESS, epicWithSubTask);

        TaskManager fileBackedTaskManager = Managers.getDefault();
        fileBackedTaskManager.deleteAll();

        fileBackedTaskManager.add(0, taskOne);
        fileBackedTaskManager.add(0, epicWithSubTask);
        fileBackedTaskManager.add(0, epicWithoutSubTask);
        fileBackedTaskManager.add(0, subTaskOne);
        fileBackedTaskManager.add(0, subTaskTwo);

        List<Task> list = fileBackedTaskManager.getPrioritizedTask();
        assertEquals(taskOne, list.get(0));
        assertEquals(epicWithSubTask, list.get(1));
        assertEquals(epicWithoutSubTask, list.get(2));
        assertEquals(subTaskOne, list.get(3));
        assertEquals(subTaskTwo, list.get(4));
    }

    @Test
    // Тест нормальной работы метода isIntersectionsByTime
    public void shouldReturnIdGreaterThanOrEqualToZero() {
        LocalDateTime localDateTimeOne = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("GMT+3")).minusWeeks(1);
        LocalDateTime localDateTimeTwo =localDateTimeOne.plusHours(1);
        LocalDateTime localDateTimeThree = localDateTimeOne.plusHours(2);
        Duration duration = Duration.ofHours(1);
        Task task1 = new Task("taskTitle1", "taskDescription1", NEW,localDateTimeOne,duration);
        Task task2 = new Task("taskTitle2", "taskDescription2", NEW,localDateTimeTwo,duration);
        Task task3 = new Task("taskTitle3", "taskDescription3", NEW,localDateTimeThree,duration);
        boolean test;
        TaskManager fileBackedTaskManager = Managers.getDefault();
        fileBackedTaskManager.deleteAll();
        test = (fileBackedTaskManager.add(0,task1)>0);
        assertTrue(test);
        test = (fileBackedTaskManager.add(0,task2)>0);
        assertTrue(test);
        test = (fileBackedTaskManager.add(0,task3)>0);
        assertTrue(test);
    }

    @Test
    // Тест работы метода isIntersectionsByTime при пересечениях во времени
    public void shouldReturnIdEqualMinusOne (){
        LocalDateTime localDateTimeOne = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("GMT+3")).minusDays(1);
        LocalDateTime localDateTimeTwo =localDateTimeOne.plusHours(1);
        LocalDateTime localDateTimeThree = localDateTimeOne.minusHours(1);
        Duration durationOneHour = Duration.ofHours(1);
        Duration durationTwoHour = Duration.ofHours(2);
        Task task1 = new Task("taskTitle1", "taskDescription1", NEW,localDateTimeOne,durationTwoHour);
        Task task11 = new Task("taskTitle1", "taskDescription1", NEW,
                localDateTimeOne,durationTwoHour.minusMinutes(90));
        Task task12 = new Task("taskTitle1", "taskDescription1", NEW,
                localDateTimeOne.plusMinutes(90),durationTwoHour.minusMinutes(90));
        Task task2 = new Task("taskTitle2", "taskDescription2", NEW,localDateTimeTwo,durationTwoHour);
        Task task3 = new Task("taskTitle3", "taskDescription3", NEW,localDateTimeThree,durationTwoHour);
        Task task4 = new Task("taskTitle4", "taskDescription4", NEW,
                localDateTimeOne.plusMinutes(30),durationOneHour);
        Task task5 = new Task("taskTitle5", "taskDescription5", NEW,
                localDateTimeOne.minusMinutes(30),durationOneHour.plusHours(2));

        TaskManager fileBackedTaskManager = Managers.getDefault();
        fileBackedTaskManager.deleteAll();
        assertTrue(fileBackedTaskManager.add(0,task1)>0);
        assertEquals(-1,fileBackedTaskManager.add(0,task2));
        assertEquals(-1,fileBackedTaskManager.add(0,task3));
        assertEquals(-1,fileBackedTaskManager.add(0,task4));
        assertEquals(-1,fileBackedTaskManager.add(0,task5));
        assertEquals(-1,fileBackedTaskManager.add(0,task11));
        assertEquals(-1,fileBackedTaskManager.add(0,task12));

        var list = fileBackedTaskManager.getPrioritizedTask();
        assertTrue(list.contains(task1));
        assertFalse(list.contains(task2));
        assertFalse(list.contains(task3));
        assertFalse(list.contains(task4));
        assertFalse(list.contains(task5));
        assertFalse(list.contains(task11));
        assertFalse(list.contains(task12));
    }

    @Test
    //// Тест работы метода isIntersectionsByTime при удалении
    public void shouldReturnListSortedByTimeWithoutDeletedTask (){
        int size;
        LocalDateTime localDateTimeOne = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("GMT+3"));
        LocalDateTime localDateTimeTwo =localDateTimeOne.plusHours(1);
        LocalDateTime localDateTimeThree = localDateTimeOne.plusHours(2);
        Duration duration = Duration.ofHours(1);
        Task task1 = new Task("taskTitle1", "taskDescription1", NEW,localDateTimeOne,duration);
        Task task2 = new Task("taskTitle2", "taskDescription2", NEW,localDateTimeTwo,duration);
        Task task3 = new Task("taskTitle3", "taskDescription3", NEW,localDateTimeThree,duration);

        TaskManager fileBackedTaskManager = Managers.getDefault();

        int id1=fileBackedTaskManager.add(0,task1);
        int id2=fileBackedTaskManager.add(0,task2);
        fileBackedTaskManager.add(0,task3);
        var list = fileBackedTaskManager.getPrioritizedTask();
        size=list.size();
        fileBackedTaskManager.delete(id2);
        list = fileBackedTaskManager.getPrioritizedTask();
        assertEquals(size-1,list.size());
        assertFalse(list.contains(task2));
        fileBackedTaskManager.delete(id1);
        list = fileBackedTaskManager.getPrioritizedTask();
        assertEquals(size-2,list.size());
        assertFalse(list.contains(task1));
    }
}