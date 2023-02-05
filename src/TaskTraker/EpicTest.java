package TaskTraker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


class EpicTest {
    private Epic epic;
    private SubTask subTask;
    private SubTask subTask2;

    @BeforeEach
    void setUp() {
        epic = new Epic(1, "Epic", "Epic", StatusOfTasks.NEW);
        subTask = new SubTask(2, "subTask", "Subtask", StatusOfTasks.NEW, epic);
        subTask2 = new SubTask(3, "subTask2", "subTask2", StatusOfTasks.NEW, epic);
    }

    @Test
    void addSubTaskOfEpic() {
        epic.addSubTaskOfEpic(subTask);
        assertEquals(subTask, epic.getSubTasksOfEpic().get(0), "Задачи нет");
    }

    @Test
    void getSubTaskOfEpic() {
        epic.addSubTaskOfEpic(subTask);
        epic.addSubTaskOfEpic(subTask2);
        List<Task> test = new ArrayList<>();
        test.add(subTask);
        test.add(subTask2);
        assertAll(
                () -> assertEquals(test.size(), epic.getSubTasksOfEpic().size(), "Размер не совпадает"),
                () -> {
                    for (int i = 0; i < test.size(); i++) {
                        assertEquals(test.get(i), epic.getSubTasksOfEpic().get(i), "Подзадачи не совпадают");
                    }
                }
        );
    }

    @Test
    void removeSubTaskOfEpic() {
        epic.addSubTaskOfEpic(subTask);
        epic.addSubTaskOfEpic(subTask2);
        epic.removeSubTaskOfEpic(subTask);
        assertFalse(epic.getSubTasksOfEpic().contains(subTask));
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> epic.removeSubTaskOfEpic(subTask));
        assertEquals("Такой подзадачи нет в этом эпике", exception.getMessage());
    }
}