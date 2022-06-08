package TaskTraker;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> browsingHistory;

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
    public List<Task> getHistory() {
        return browsingHistory;
    }
}
