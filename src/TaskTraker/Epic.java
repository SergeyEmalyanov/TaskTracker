package TaskTraker;
import java.util.List;
import java.util.ArrayList;
public class Epic extends Tasks {
    List <String> affiliationSubTask=new ArrayList<>();

    public Epic(String title, String description, StatusOfTasks statusOfTasks) {
        super(title, description, statusOfTasks);
    }

    public List<String> getAffiliationSubTask() {
        return affiliationSubTask;
    }

    public void setAffiliationSubTask(String subtask) {
        affiliationSubTask.add(subtask);
    }
}
