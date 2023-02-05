package TaskTraker;

class Task {
    private int id;
    private final String title;
    private final String description;
    private StatusOfTasks statusOfTasks;

    protected Task(int id, String title, String description, StatusOfTasks statusOfTasks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.statusOfTasks = statusOfTasks;
    }

    protected Task(String title, String description, StatusOfTasks statusOfTasks) {
        this.title = title;
        this.description = description;
        this.statusOfTasks = statusOfTasks;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected Task setStatusOfTasks(StatusOfTasks statusOfTasks) {
        this.statusOfTasks = statusOfTasks;
        return this;
    }

    protected int getId() {
        return this.id;
    }

    protected String getTitle() {
        return title;
    }

    protected String getDescription() {
        return description;
    }

    protected StatusOfTasks getStatusOfTasks() {
        return statusOfTasks;
    }

    @Override
    public String toString() {
        return String.format("| %-2.5s | %-10.7s | %-20.20s | %-22.20s | %-80.60s |"
                , id, getClass().getSimpleName(), statusOfTasks, title, description);
    }
}