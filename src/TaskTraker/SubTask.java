package TaskTraker;

public class SubTask extends Tasks{
    private int affiliationOfEpic;

    public SubTask (String title, String description, StatusOfTasks statusOfTasks, int affiliationOfEpic) {
        super(title,description,statusOfTasks);
        this.affiliationOfEpic=affiliationOfEpic;
    }

    public int getAffiliationOfEpic() {
        return affiliationOfEpic;
    }

    public void setAffiliationOfEpic(int affiliationOfEpic) {
        this.affiliationOfEpic = affiliationOfEpic;
    }
}
