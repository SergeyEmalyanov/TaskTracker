package TaskTraker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {
    InMemoryTaskManager inMemoryTaskManager;
    private Epic epic;
    private SubTask subTaskNew2;
    private SubTask subTaskNew3;
    private SubTask subTaskDone4;
    private SubTask subTaskDone5;
    private SubTask subTaskInProgress6;
    private SubTask subTaskInProgress7;

    @BeforeEach
    void setUp() {
        inMemoryTaskManager = new InMemoryTaskManager();
        epic = new Epic(0, "a", "a", StatusOfTasks.NEW, new ArrayList<>());
        subTaskNew2 = new SubTask(0, "b", "b", StatusOfTasks.NEW, 1);
        subTaskNew3 = new SubTask(0, "c", "c", StatusOfTasks.NEW, 1);
        subTaskDone4 = new SubTask(0, "d", "d", StatusOfTasks.DONE, 2);
        subTaskDone5 = new SubTask(0, "e", "e", StatusOfTasks.DONE, 2);
        subTaskInProgress6 = new SubTask(0, "f", "f", StatusOfTasks.IN_PROGRESS, 1);
        subTaskInProgress7 = new SubTask(0, "g", "g", StatusOfTasks.IN_PROGRESS, 1);
    }

    @Test
    void statusUpdate() {
        int epicId = inMemoryTaskManager.addNewEpic(epic);
        // Empty
        inMemoryTaskManager.statusUpdate(epicId);
        assertEquals(StatusOfTasks.NEW, inMemoryTaskManager.getEpicById(epicId).getStatusOfTasks(), "Статусы не совпадают");

        // NEW
        int subTaskNew2Id = inMemoryTaskManager.addNewSubTask(subTaskNew2);
        int subTaskNew3Id = inMemoryTaskManager.addNewSubTask(subTaskNew3);
        inMemoryTaskManager.getEpicById(epicId).addSubTaskOfEpic(subTaskNew2Id);
        inMemoryTaskManager.getEpicById(epicId).addSubTaskOfEpic(subTaskNew3Id);
        assertEquals(StatusOfTasks.NEW, epic.getStatusOfTasks());

        // DONE
        int epic2Id = inMemoryTaskManager.addNewEpic(
                new Epic(0, "done", "done", StatusOfTasks.NEW, new ArrayList<>()));
        int subTask4Id = inMemoryTaskManager.addNewSubTask(subTaskDone4);
        int subTask5Id = inMemoryTaskManager.addNewSubTask(subTaskDone5);
        inMemoryTaskManager.getEpicById(epic2Id).addSubTaskOfEpic(subTask4Id);
        inMemoryTaskManager.getEpicById(epic2Id).addSubTaskOfEpic(subTask5Id);
        inMemoryTaskManager.statusUpdate(epic2Id);
        assertEquals(StatusOfTasks.DONE, inMemoryTaskManager.getEpicById(epic2Id).getStatusOfTasks());

        //NEW DONE
        int idNewDone = inMemoryTaskManager.addNewSubTask(
                new SubTask(0, "newFor4", "newFor4", StatusOfTasks.DONE, 4));
        inMemoryTaskManager.getEpicById(epicId).addSubTaskOfEpic(idNewDone);
        inMemoryTaskManager.statusUpdate(epicId);
        assertEquals(StatusOfTasks.IN_PROGRESS, inMemoryTaskManager.getEpicById(epicId).getStatusOfTasks());

        // IN_PROGRESS
        int epic3Id = inMemoryTaskManager.addNewEpic(
                new Epic(0, "in_progress", "in_progress", StatusOfTasks.NEW, new ArrayList<>()));
        int subTask6Id = inMemoryTaskManager.addNewSubTask(subTaskInProgress6);
        int subTask7Id = inMemoryTaskManager.addNewSubTask(subTaskInProgress7);
        inMemoryTaskManager.getEpicById(epic3Id).addSubTaskOfEpic(subTask6Id);
        inMemoryTaskManager.getEpicById(epic3Id).addSubTaskOfEpic(subTask7Id);
        inMemoryTaskManager.statusUpdate(epic3Id);
        assertEquals(StatusOfTasks.IN_PROGRESS, inMemoryTaskManager.getEpicById(epic3Id).getStatusOfTasks());
    }
}