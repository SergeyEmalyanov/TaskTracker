package TaskTraker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;



class EpicTest {
    private Epic epic;
    private SubTask subTaskNew2;
    private SubTask subTaskNew3;
    private SubTask subTaskDone4;
    private SubTask subTaskDone5;
    private SubTask subTaskInProgress6;
    private SubTask subTaskInProgress7;

    @BeforeEach
    void setUp() {
        epic = new Epic(1, "a", "a", StatusOfTasks.NEW, new ArrayList<>());
        subTaskNew2 = new SubTask(2, "b", "b", StatusOfTasks.NEW, epic);
        subTaskNew3 = new SubTask(3, "c", "c", StatusOfTasks.NEW, epic);
        subTaskDone4 = new SubTask(4, "d", "d", StatusOfTasks.DONE, epic);
        subTaskDone5 = new SubTask(5, "e", "e", StatusOfTasks.DONE, epic);
        subTaskInProgress6 = new SubTask(6, "f", "f", StatusOfTasks.IN_PROGRESS, epic);
        subTaskInProgress7 = new SubTask(7, "g", "g", StatusOfTasks.IN_PROGRESS, epic);
    }

    @Test
    void addSubTaskOfEpic() {
        epic.addSubTaskOfEpic(subTaskNew2);
        assertEquals(subTaskNew2, epic.getSubTaskOfEpic().get(0),"Задачи нет");
    }

    @Test
    void getSubTaskOfEpic() {
        epic.addSubTaskOfEpic(subTaskNew2);
        epic.addSubTaskOfEpic(subTaskNew3);
        ArrayList<SubTask> test = new ArrayList<>();
        test.add(subTaskNew2);
        test.add(subTaskNew3);
        assertAll(
                () -> assertEquals(test.size(), epic.getSubTaskOfEpic().size()),
                () -> {
                    for (int i = 0; i < test.size(); i++) {
                        assertEquals(test.get(i), epic.getSubTaskOfEpic().get(i));
                    }
                }
        );
    }

    @Test
    void taskUpdate() {
        Epic epic2=new Epic(8,"done","done",StatusOfTasks.NEW,new ArrayList<>());
        Epic epic3=new Epic(9,"in_progress","in_progress",StatusOfTasks.NEW,new ArrayList<>());

        // Пустой
        epic.taskUpdate();
        assertEquals(StatusOfTasks.NEW,epic.getStatusOfTasks());

        // NEW
        epic.addSubTaskOfEpic(subTaskNew2);
        epic.addSubTaskOfEpic(subTaskNew3);
        epic.taskUpdate();
        assertEquals(StatusOfTasks.NEW,epic.getStatusOfTasks());

        // DONE
        epic2.addSubTaskOfEpic(subTaskDone4);
        epic2.addSubTaskOfEpic(subTaskDone5);
        epic2.taskUpdate();
        assertEquals(StatusOfTasks.DONE,epic2.getStatusOfTasks());

        // NEW DONE
        epic.addSubTaskOfEpic(subTaskDone4);
        epic.addSubTaskOfEpic(subTaskDone5);
        epic.taskUpdate();
        assertEquals(StatusOfTasks.IN_PROGRESS,epic.getStatusOfTasks());

        // IN_PROGRESS
        epic3.addSubTaskOfEpic(subTaskInProgress6);
        epic3.addSubTaskOfEpic(subTaskInProgress7);
        epic3.taskUpdate();
        ///int x = 0b0000_0000_0001;//_1111_1111_1111;
        //System.out.println(x);
        assertEquals(StatusOfTasks.IN_PROGRESS,epic3.getStatusOfTasks());
    }
}