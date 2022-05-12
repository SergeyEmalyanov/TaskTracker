package TaskTraker;

abstract class Tasks {
    private String title;
    private String description;
    private StatusOfTasks statusOfTasks;

    public Tasks(String title, String description, StatusOfTasks statusOfTasks) {
        this.title = title;
        this.description = description;
        this.statusOfTasks = statusOfTasks;
    }

    public StatusOfTasks getStatusOfTasks() {
        return statusOfTasks;
    }

    public void setStatusOfTasks(StatusOfTasks statusOfTasks) {
        this.statusOfTasks = statusOfTasks;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    @Override
    public String toString() {
        return "Задача: " + getTitle() + "Статус: " + getStatusOfTasks()
                + "\n" + "Описание: " + getDescription() + "\n";
    }
}
