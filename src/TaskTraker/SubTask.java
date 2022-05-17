package TaskTraker;

public class SubTask extends Tasks{

    public SubTask (String title, String description, StatusOfTasks statusOfTasks,int idEpic) {
        super(title, description, statusOfTasks, idEpic, "Подзадача");
    }
}
