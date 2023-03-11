package TaskTraker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

class Task implements Comparable<Task> {
    private int id;
    private final String title;
    private final String description;
    private StatusOfTasks statusOfTasks;

    private Optional<LocalDateTime> startTime;
    private Duration duration;

    protected Task(int id, String title, String description, StatusOfTasks statusOfTasks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.statusOfTasks = statusOfTasks;
        this.startTime = Optional.empty();
        this.duration = Duration.ZERO;
    }

    protected Task(String title, String description, StatusOfTasks statusOfTasks) {
        this.title = title;
        this.description = description;
        this.statusOfTasks = statusOfTasks;
        startTime = Optional.empty();
        duration = Duration.ZERO;

    }

    protected Task(int id, String title, String description, StatusOfTasks statusOfTasks, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.statusOfTasks = statusOfTasks;
        this.startTime = Optional.of(startTime);
        this.duration = duration;
    }

    protected Task(String title, String description, StatusOfTasks statusOfTasks, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.statusOfTasks = statusOfTasks;
        this.startTime = Optional.of(startTime);
        this.duration = duration;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected Task setStatusOfTasks(StatusOfTasks statusOfTasks) {
        this.statusOfTasks = statusOfTasks;
        return this;
    }

    protected void setStartTime(Optional<LocalDateTime> startTime) {
        this.startTime = startTime;
    }

    protected void setDuration(Duration duration) {
        this.duration = duration;
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

    protected Optional<LocalDateTime> getStartTime() {
        return startTime;
    }

    protected Duration getDuration() {
        return duration;
    }

    protected Optional<LocalDateTime> getEndTime() {
        if (startTime.isPresent()) return Optional.of(startTime.get().plus(duration));
        else return Optional.empty();
        //return startTime.map(localDateTime -> localDateTime.plus(duration));
    }

    @Override
    public int compareTo(Task task) {
        if (this.equals(task) && startTime.orElse(LocalDateTime.MAX)
                .equals(task.getStartTime().orElse(LocalDateTime.MAX))) {
            return 0;
        } else if (startTime.orElse(LocalDateTime.MAX)
                .isBefore(task.getStartTime().orElse(LocalDateTime.MAX))) {
            return -1;
        }
        return 1;
    }

    @Override
    public String toString() {
        return String.format("| %-2.3s | %-5.5s | %-5.5s | %-10.15s | %-10.16s | %-10.26s | %-40.70s"
                , id, getClass().getSimpleName(), statusOfTasks, title, description, startTime.toString(), duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && title.equals(task.title) && description.equals(task.description) &&
                statusOfTasks.equals(task.statusOfTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, statusOfTasks);
    }
}