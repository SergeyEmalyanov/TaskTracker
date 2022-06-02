package TaskTraker;

import java.util.List;

class Task {
    protected final String title;
    protected final String description;
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
        return "Статус:" + this.statusOfTasks + " Название:" + this.title  + " Описание:" + this.description;
    }
}