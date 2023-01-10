package TaskTraker;

class SubTask extends Task{
    protected Epic epic;

    protected SubTask (int id, String title, String description, StatusOfTasks statusOfTasks,Epic epic) {
        super(id, title, description, statusOfTasks);
        this.epic=epic;
    }

    protected Epic getEpicOfSubTask() {
        return epic;
    }

    @Override
    protected SubTask taskUpdate() {
        super.statusUpdate();
        return this;
    }
}