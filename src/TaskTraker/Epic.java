package TaskTraker;

import java.util.ArrayList;

class Epic extends Task {
    ArrayList<Integer> idSubTask;

    protected Epic(String title, String description, StatusOfTasks statusOfTasks, ArrayList idSubTask) {
        super(title, description, statusOfTasks);
        this.idSubTask = idSubTask;
    }

    @Override
    protected Task taskUpdate() {
        statusUpdate();
        return new Epic (this.title, this.description, this.statusOfTasks, this.idSubTask);
    }
}
