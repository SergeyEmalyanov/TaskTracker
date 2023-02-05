package TaskTraker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InMemoryTaskManager implements TaskManager {
    private int id;
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Epic> epics;
    protected final Map<Integer, SubTask> subTasks;
    protected final HistoryManager historyManager;

    InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public <T extends Task> int add(Integer id, T task) {
        if (task == null) return 0;
        if (id == null || id == 0) id = incCurrentId();
        if (Task.class.equals(task.getClass())) {
            tasks.put(id, task);
        } else if (Epic.class.equals(task.getClass())) {
            epics.put(id, (Epic) task);
        } else if (SubTask.class.equals(task.getClass())) {
            subTasks.put(id, (SubTask) task);
            subTasks.get(id).getEpicOfSubTask().addSubTaskOfEpic(subTasks.get(id));
        }
        task.setId(id);
        return id;
    }

    @Override
    public Task get(Integer id) {
        Task task = null;
        if (tasks.containsKey(id)) task = tasks.get(id);
        else if (epics.containsKey(id)) task = epics.get(id);
        else if (subTasks.containsKey(id)) task = subTasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void remove(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            List<Task> subTasks = epics.get(id).getSubTasksOfEpic();
            for (Task subTask : subTasks) {
                remove(subTask.getId());
            }
            epics.remove(id);
        } else if (subTasks.containsKey(id)) {
            subTasks.get(id).getEpicOfSubTask().removeSubTaskOfEpic(subTasks.get(id));
            subTasks.remove(id);
        }
        historyManager.remove(id);
    }

    @Override
    public List<Task> getAll() {
        List<Task> all = new ArrayList<>();
        all.addAll(tasks.values());
        all.addAll(epics.values());
        all.addAll(subTasks.values());
        return all;
    }

    @Override
    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        id = 0;
        historyManager.remove(-1);
    }

    @Override
    public List<Task> getSubTasksOfEpic(Epic epic) {
        List<Task> epicAndSubTask = new ArrayList<>();
        epicAndSubTask.add(epic);
        epicAndSubTask.addAll(epic.getSubTasksOfEpic());
        for (Task task: epicAndSubTask) {
            System.out.println(task);
        }
        return epicAndSubTask;
    }

    private int incCurrentId() {
        return ++id;
    }

    @Override
    public void getHistory() {
        List<Task> history = historyManager.getHistory();
        System.out.println("HISTORY");
        for (Task task : history) {
            System.out.println(task.toString());
        }
    }
}