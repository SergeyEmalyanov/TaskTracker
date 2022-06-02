package TaskTraker;

class SubTask extends Task{
    int idEpic;

    protected SubTask (String title, String description, StatusOfTasks statusOfTasks,int idEpic) {
        super(title, description, statusOfTasks);
        this.idEpic=idEpic;
    }
    @Override
    protected SubTask taskUpdate() {
        statusUpdate();
        return this;
    }

    protected int getIdEpic() {
        return idEpic;
    }



}
