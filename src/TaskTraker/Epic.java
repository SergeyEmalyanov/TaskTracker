package TaskTraker;

import java.util.ArrayList;

class Epic extends Task {
    ArrayList<Integer> idSubTask;

    protected Epic(int id,String title, String description, StatusOfTasks statusOfTasks, ArrayList idSubTask) {
        super(id, title, description, statusOfTasks);
        this.idSubTask = idSubTask;
    }


    protected Epic EpicUpdate(StatusOfTasks statusOfTasks) {
        this.statusOfTasks = statusOfTasks;
        return this;
    }

    protected Epic addSubTask(int idSubTask) {
        this.idSubTask.add(idSubTask);
        return this;
    }

    protected ArrayList<Integer> getIdSubTask() {
        return this.idSubTask;
    }

   /* protected Epic calculationEpicStatus(){
        for (Integer id : idSubTask){
            if (TasksManager.subTasks.get(id).getStatusOfTasks()==StatusOfTasks.IN_PROGRESS){

            }
        }
        return this;
    }*/
}
