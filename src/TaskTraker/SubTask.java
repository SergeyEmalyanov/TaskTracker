package TaskTraker;

class SubTask extends Task {
    private final Epic epicOfSubTask;

    protected SubTask(int id, String title, String description, StatusOfTasks statusOfTasks, Epic epicOfSubTask) {
        super(id, title, description, statusOfTasks);
        this.epicOfSubTask = epicOfSubTask;
    }

    protected SubTask(String title, String description, StatusOfTasks statusOfTasks, Epic epicOfSubTask) {
        super(title, description, statusOfTasks);
        this.epicOfSubTask=epicOfSubTask;
    }

    protected Epic getEpicOfSubTask() {
        return epicOfSubTask;
    }
}