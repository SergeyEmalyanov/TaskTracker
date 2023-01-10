package TaskTraker;

import java.util.ArrayList;

class Epic extends Task {
    private final ArrayList<SubTask> subTaskOfEpic;

    protected Epic(int id, String title, String description, StatusOfTasks statusOfTasks, ArrayList<SubTask> subTaskOfEpic) {
        super(id, title, description, statusOfTasks);
        this.subTaskOfEpic = subTaskOfEpic;
    }

    protected Epic addSubTaskOfEpic(SubTask subTask) {
        subTaskOfEpic.add(subTask);
        return this;
    }

    protected ArrayList<SubTask> getSubTaskOfEpic() {
        return this.subTaskOfEpic;
    }

    @Override
    protected Epic taskUpdate() {
        if (subTaskOfEpic.isEmpty()) return this;
        boolean statusEpic = true;
        StatusOfTasks statusSubTask;
        for (SubTask subTask : subTaskOfEpic) {
            statusSubTask = subTask.getStatusOfTasks();
            if (statusSubTask == StatusOfTasks.IN_PROGRESS && statusOfTasks == StatusOfTasks.NEW) {
                statusOfTasks = StatusOfTasks.IN_PROGRESS;
            }
            statusEpic = statusEpic && (statusSubTask == StatusOfTasks.DONE);
        }
        if (statusEpic) {statusOfTasks = StatusOfTasks.DONE;}
        return this;
    }
}