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

}
