package TaskTraker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;


class EpicTest {
    private Epic epic;

    @BeforeEach
    void setUp() {
        epic = new Epic(0, "Epic", "Epic", StatusOfTasks.NEW, new ArrayList<>());
    }

    @Test
    void addSubTaskOfEpic() {
        epic.addSubTaskOfEpic(2);
        assertEquals(2, epic.getSubTasksOfEpicId().get(0), "Задачи нет");
    }

    @Test
    void removeSubTaskOfEpic() {
        epic.addSubTaskOfEpic(2);
        epic.addSubTaskOfEpic(3);
        epic.removeSubTaskOfEpic(2);
        assertFalse(epic.getSubTasksOfEpicId().contains(2));
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> epic.removeSubTaskOfEpic(4));
        assertEquals("Такой подзадачи нет в этом эпике", exception.getMessage());
    }

    @Test
    void getSubTaskOfEpic() {
        epic.addSubTaskOfEpic(2);
        epic.addSubTaskOfEpic(3);
        ArrayList<Integer> test = new ArrayList<>();
        test.add(2);
        test.add(3);
        assertAll(
                () -> assertEquals(test.size(), epic.getSubTasksOfEpicId().size(), "Размер не совпадает"),
                () -> {
                    for (int i = 0; i < test.size(); i++) {
                        assertEquals(test.get(i), epic.getSubTasksOfEpicId().get(i), "Подзадачи не совпадают");
                    }
                }
        );
        epic.getSubTasksOfEpicId().clear();
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> epic.getSubTasksOfEpicId());
        assertEquals("Подзадачи отсутствуют", exception.getMessage());
    }
}