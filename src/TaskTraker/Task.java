package TaskTraker;

class Task {
    protected final int id;
    protected final String title;
    protected final String description;
    protected StatusOfTasks statusOfTasks;

    protected Task(int id, String title, String description, StatusOfTasks statusOfTasks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.statusOfTasks = statusOfTasks;
    }

    protected StatusOfTasks getStatusOfTasks() {
        return statusOfTasks;
    }

    protected int getId() {
        return this.id;
    }

    protected Task taskUpdate() {
        statusUpdate();
        return this;
    }


    protected void statusUpdate() {
        if (this.statusOfTasks == StatusOfTasks.NEW) {
            this.statusOfTasks = StatusOfTasks.IN_PROGRESS;
            return;
        } else if (this.statusOfTasks == StatusOfTasks.IN_PROGRESS) {
            this.statusOfTasks = StatusOfTasks.DONE;
        } else {
            System.out.println("Задача завершена. Статус DONE");
        }
    }

    @Override
    public String toString() {
        return ("№" + getId() + " " + this.getClass().getSimpleName() + " Статус:" + this.statusOfTasks + " Название:" + this.title + " Описание:" + this.description);
    }
}