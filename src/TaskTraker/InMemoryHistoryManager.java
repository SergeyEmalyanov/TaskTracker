package TaskTraker;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> browsingHistory;

    protected InMemoryHistoryManager() {
        browsingHistory = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (browsingHistory.size() == 10) {
            browsingHistory.remove(1);
        }
        browsingHistory.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return browsingHistory;
    }
}
