package TaskTraker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static TaskTraker.StatusOfTasks.*;

class Epic extends Task {
    private final List<Task> subTasksOfEpic;
    private Optional<LocalDateTime> endTime;

    protected Epic(int id, String title, String description, StatusOfTasks statusOfTasks) {
        super(id, title, description, statusOfTasks);
        this.subTasksOfEpic = new ArrayList<>();
        endTime=Optional.empty();
        epicCheckAndUpdate();
    }

    protected Epic(String title, String description, StatusOfTasks statusOfTasks) {
        super(title, description, statusOfTasks);
        this.subTasksOfEpic = new ArrayList<>();
        endTime=Optional.empty();
        epicCheckAndUpdate();
    }

    protected void addSubTaskOfEpic(SubTask subTask) {
        if (subTasksOfEpic.contains(subTask)) subTasksOfEpic.remove(subTask);
        subTasksOfEpic.add(subTask);
        epicCheckAndUpdate();
    }


    protected List<Task> getSubTasksOfEpic() {
        return subTasksOfEpic;
    }

    protected void removeSubTaskOfEpic(Task subTask) {
        if (!subTasksOfEpic.contains(subTask)) {
            throw new NoSuchElementException("Такой подзадачи нет в этом эпике");
        }
        subTasksOfEpic.remove(subTask);
        epicCheckAndUpdate();
    }

    @Override
    protected Optional<LocalDateTime> getEndTime() {
        return endTime;
    }

    private void epicCheckAndUpdate() {
        List<Task> subTasks = getSubTasksOfEpic();
        if (subTasks.isEmpty()) {
            setStatusOfTasks(NEW);
            return;
        }
        StatusOfTasks statusSubTask;
        boolean statusEpicNew = true;
        boolean statusEpicDone = true;
        for (Task subTask : subTasks) {
            statusSubTask = subTask.getStatusOfTasks();
            if ((statusSubTask == IN_PROGRESS || statusSubTask == DONE)
                    && this.getStatusOfTasks() == NEW) {
                this.setStatusOfTasks(IN_PROGRESS);
            }
            statusEpicNew = statusEpicNew && (statusSubTask == NEW);
            statusEpicDone = statusEpicDone && (statusSubTask == DONE);
        }
        if (statusEpicNew) this.setStatusOfTasks(NEW);
        else if (statusEpicDone) {
            this.setStatusOfTasks(DONE);
        }
        ////////////////////////////// Добавить в цикл выше /////////////////////////////////////////////////////////
        LocalDateTime startTimeEpic = LocalDateTime.MAX;
        LocalDateTime endTimeEpic = LocalDateTime.MIN;
        for (Task subTask : subTasks) {
            if (subTask.getStartTime().isPresent()) {
                if (subTask.getStartTime().get().isBefore(startTimeEpic)) startTimeEpic = subTask.getStartTime().get();
            }
            if (subTask.getEndTime().isPresent()) {
                if (subTask.getEndTime().get().isAfter(endTimeEpic)) endTimeEpic = subTask.getEndTime().get();
            }
        }
        if (LocalDateTime.MAX.equals(startTimeEpic)) setStartTime(Optional.empty());
        else setStartTime(Optional.of(startTimeEpic));
        if (LocalDateTime.MIN.equals(endTimeEpic)) endTime=Optional.empty();
        else endTime=Optional.of(endTimeEpic);
        setDuration(Duration.between(getStartTime().orElse(LocalDateTime.MIN),endTime.orElse(LocalDateTime.MIN)));
    }
}