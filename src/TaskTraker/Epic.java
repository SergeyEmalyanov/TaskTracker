package TaskTraker;

import java.util.ArrayList;
import java.util.NoSuchElementException;

class Epic extends Task {
    private final ArrayList<SubTask> subTasksOfEpic;

    protected Epic(int id, String title, String description, StatusOfTasks statusOfTasks, ArrayList<SubTask> subTasksOfEpic) {
        super(id, title, description, statusOfTasks);
        this.subTasksOfEpic = subTasksOfEpic;
    }

    protected Epic(String title, String description, StatusOfTasks statusOfTasks, ArrayList<SubTask> subTasksOfEpic) {
        super(title, description, statusOfTasks);
        this.subTasksOfEpic = subTasksOfEpic;
    }

    protected void addSubTaskOfEpic(SubTask subTask) {
        subTasksOfEpic.add(subTask);
        epicCheckAndUpdate();
    }

    protected ArrayList<SubTask> getSubTasksOfEpic() {
        if (subTasksOfEpic.isEmpty()) {
            throw new NoSuchElementException("Подзадачи отсутствуют");
        }
        return subTasksOfEpic;
    }

    protected void removeSubTaskOfEpic(SubTask subTask) {
        if (!subTasksOfEpic.contains(subTask)) {
            throw new NoSuchElementException("Такой подзадачи нет в этом эпике");
        }
        subTasksOfEpic.remove(subTask);
        epicCheckAndUpdate();
    }

    private boolean epicCheckAndUpdate() {/// void
        StatusOfTasks currentStatus = this.getStatusOfTasks();
        try {
            ArrayList<SubTask> subTasks = getSubTasksOfEpic();
            StatusOfTasks statusSubTask;
            boolean statusEpic = true;
            for (SubTask subTask : subTasks) {
                statusSubTask = subTask.getStatusOfTasks();
                if ((statusSubTask == StatusOfTasks.IN_PROGRESS || statusSubTask == StatusOfTasks.DONE)
                        && this.getStatusOfTasks() == StatusOfTasks.NEW) {
                    this.setStatusOfTasks(StatusOfTasks.IN_PROGRESS);
                }
                statusEpic = statusEpic && (statusSubTask == StatusOfTasks.DONE);
            }
            if (statusEpic) {
                this.setStatusOfTasks(StatusOfTasks.DONE);
            }
        } catch (NoSuchElementException exception) {
            System.out.println("Печать из блока Catch " + exception.getMessage());

        }
        return (currentStatus == this.getStatusOfTasks());
    }
}