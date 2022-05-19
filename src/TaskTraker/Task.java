package TaskTraker;

class Task {
    protected String title;
    protected String description;
    protected StatusOfTasks statusOfTasks;

    protected Task(String title, String description, StatusOfTasks statusOfTasks) {
        this.title = title;
        this.description = description;
        this.statusOfTasks = statusOfTasks;
    }

    public StatusOfTasks getStatusOfTasks() {
        return statusOfTasks;
    }

    protected Task taskUpdate() {
        statusUpdate();
        return new Task(this.title, this.description, this.statusOfTasks);
    }


    protected void statusUpdate() {
        if (this.statusOfTasks == StatusOfTasks.NEW) {
            this.statusOfTasks = StatusOfTasks.IN_PROGRESS;
        } else if (this.statusOfTasks == StatusOfTasks.IN_PROGRESS) {
            this.statusOfTasks = StatusOfTasks.DONE;
        } else {
            System.out.println("Задача завершена. Статус DONE");
        }
    }
}