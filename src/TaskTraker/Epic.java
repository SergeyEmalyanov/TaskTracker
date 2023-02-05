package TaskTraker;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static TaskTraker.StatusOfTasks.*;

class Epic extends Task {
    private final List<Task> subTasksOfEpic;

    protected Epic(int id, String title, String description, StatusOfTasks statusOfTasks) {
        super(id, title, description, statusOfTasks);
        this.subTasksOfEpic = new ArrayList<>();
        epicCheckAndUpdate();
    }

    protected Epic(String title, String description, StatusOfTasks statusOfTasks) {
        super(title, description, statusOfTasks);
        this.subTasksOfEpic = new ArrayList<>();
        epicCheckAndUpdate();
    }

    protected void addSubTaskOfEpic(SubTask subTask) {
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
            statusEpicNew = statusEpicNew  && (statusSubTask == NEW);
            statusEpicDone = statusEpicDone && (statusSubTask == DONE);
        }
        if (statusEpicNew) this.setStatusOfTasks(NEW);
        else if (statusEpicDone) { this.setStatusOfTasks(DONE);
        }
    }
}