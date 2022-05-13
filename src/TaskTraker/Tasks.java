package TaskTraker;
import java.util.ArrayList;
abstract class Tasks {
    private String title;
    private String description;
    private StatusOfTasks statusOfTasks;
    private int idEpic;
    ArrayList <Integer> idSubTask=new ArrayList<>();

    public Tasks(){}

    public Tasks(String title, String description, StatusOfTasks statusOfTasks) {
        this.title = title;
        this.description = description;
        this.statusOfTasks = statusOfTasks;
    }

    public Tasks(String title, String description, StatusOfTasks statusOfTasks, int idEpic) {
        this.title = title;
        this.description = description;
        this.statusOfTasks = statusOfTasks;
        this.idEpic = idEpic;
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

    public int getIdEpic() {
        return idEpic;
    }

    public ArrayList<Integer> getIdSubTask() {
        return idSubTask;
    }

    public void setIdSubTask(int idSubTask) {
        this.idSubTask.add(idSubTask);
    }

    @Override
    public String toString() {
        return "Задача: " + getTitle() + "Статус: " + getStatusOfTasks()
                + "\n" + "Описание: " + getDescription() + "\n";
    }
}
