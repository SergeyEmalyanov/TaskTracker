package TaskTraker;

class SubTask extends Task{
    int idEpic;

    protected SubTask (String title, String description, StatusOfTasks statusOfTasks,int idEpic) {
        super(title, description, statusOfTasks);
        this.idEpic=idEpic;
    }
    @Override
    protected Task taskUpdate() {
        statusUpdate();
        return new SubTask(this.title, this.description, this.statusOfTasks, this.idEpic);
    }
}
