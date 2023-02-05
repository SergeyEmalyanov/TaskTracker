package TaskTraker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }
/*
    @BeforeEach
    void setUp(){
        super.setUp();
    }

    @Test
    void statusUpdate(){
        super.statusUpdate();
    }*/
}