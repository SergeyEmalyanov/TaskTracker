package TaskTraker;
import java.util.List;
import java.util.ArrayList;
public class Epic extends Tasks {
    List <String> idSubTask=new ArrayList<>(); //

    public Epic(String title, String description, StatusOfTasks statusOfTasks) {
        super(title, description, statusOfTasks);
    }

    public List<String> getAffiliationSubTask() {
        return idSubTask;
    }

    public void setAffiliationSubTask(String addSubtask) {
        idSubTask.add(addSubtask);
    }
}
