package TaskTraker;

class Task {
    private final int id;
    private final String title;
    private final String description;
    StatusOfTasks statusOfTasks;

    protected Task(int id, String title, String description, StatusOfTasks statusOfTasks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.statusOfTasks = statusOfTasks;
    }

    protected int getId() {
        return this.id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    protected StatusOfTasks getStatusOfTasks() {
        return statusOfTasks;
    }

    protected Task taskUpdate() {
        statusUpdate();
        return this;
    }
    public <T extends Task> T taskUpdate2(){//Проверить!
        statusUpdate();
        return this.taskUpdate2();//!!!!
    }

    private void statusUpdate() {
        if (this.statusOfTasks == StatusOfTasks.NEW) {
            this.statusOfTasks = StatusOfTasks.IN_PROGRESS;
        } else if (this.statusOfTasks == StatusOfTasks.IN_PROGRESS) {
            this.statusOfTasks = StatusOfTasks.DONE;
        } else {
            System.out.println("Задача завершена. Статус DONE");
        }
    }

    @Override
    public String toString() {
        return String.format("| %-2.5s | %-10.7s | %-20.20s | %-22.20s | %-80.60s |"
                ,id,getClass().getSimpleName(),statusOfTasks,title,description);
    }
}