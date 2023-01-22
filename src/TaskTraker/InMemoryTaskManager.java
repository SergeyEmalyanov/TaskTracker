package TaskTraker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InMemoryTaskManager implements TaskManager {
    protected int id;
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
    public <T extends Task> int add(T task) {
        String classTask = task.getClass().getName();
        switch (classTask) {
            case "Task":
                tasks.put(++id, task);
                task.setId(id);
                break;

            case "Epic":
                epics.put(++id, (Epic) task);
                task.setId(id);
                break;

            case "SubTask":
                subTasks.put(++id, (SubTask) task);
                task.setId(id);
                subTasks.get(id).getEpicOfSubTask().addSubTaskOfEpic(subTasks.get(id));
                break;

        }
        return id;
    }

    @Override
    public void remove(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            ArrayList<SubTask> subTasks = epics.get(id).getSubTasksOfEpic();
            for (SubTask subTask : subTasks) {
                remove(subTask.getId());
            }
            epics.remove(id);
        } else if (subTasks.containsKey(id)) {
            subTasks.get(id).getEpicOfSubTask().removeSubTaskOfEpic(subTasks.get(id));
            subTasks.remove(id);
        }
    }

    @Override
    public Task get(int id) {
        Task task = null;
        if (tasks.containsKey(id)) task = tasks.get(id);
        else if (epics.containsKey(id)) task = epics.get(id);
        else if (subTasks.containsKey(id)) task = subTasks.get(id);
        historyManager.add(task);
        return task;
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
    }

    @Override
    public List<Task> getSubTasksOfEpic(Epic epic) {
        List<Task> epicAndSubTask = new ArrayList<>();
        epicAndSubTask.add(epic);
        epicAndSubTask.addAll(epic.getSubTasksOfEpic());
        return epicAndSubTask;
    }
}