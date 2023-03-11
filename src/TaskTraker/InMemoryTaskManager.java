package TaskTraker;

import java.time.LocalDateTime;
import java.util.*;

class InMemoryTaskManager implements TaskManager {
    private int id;
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Epic> epics;
    protected final Map<Integer, SubTask> subTasks;
    protected final HistoryManager historyManager;
    private final Set<Task> prioritizedTask;

    InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
        prioritizedTask = new TreeSet<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public <T extends Task> int add(Integer id, T task) {
        if (task == null) return 0;
        if (isIntersectionsByTime(task)) return -1;
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
        prioritizedTask.add(task);
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
    public void delete(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            List<Task> subTasks = epics.get(id).getSubTasksOfEpic();
            final int size = subTasks.size();
            SubTask subTask;
            for (int i = 0; i < size; i++) {
                subTask = (SubTask) subTasks.get(0);
                delete(subTask.getId());
            }
            epics.remove(id);
        } else if (subTasks.containsKey(id)) {
            subTasks.get(id).getEpicOfSubTask().removeSubTaskOfEpic(subTasks.get(id));
            subTasks.remove(id);
        } else {
            throw new IllegalArgumentException("Задачи с таким ID не существует");
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
        prioritizedTask.clear();
    }

    @Override
    public List<Task> getSubTasksOfEpic(Epic epic) {
        List<Task> epicAndSubTask = new ArrayList<>();
        epicAndSubTask.add(epic);
        epicAndSubTask.addAll(epic.getSubTasksOfEpic());
        for (Task task : epicAndSubTask) {
            System.out.println(task);
        }
        return epicAndSubTask;
    }

    @Override
    public List<Task> getPrioritizedTask() {
        return new ArrayList<>(prioritizedTask);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int incCurrentId() {
        return ++id;
    }

    private <T extends Task> boolean isIntersectionsByTime(T someTask) {
        if (Epic.class.equals(someTask.getClass())) return false;
        LocalDateTime startTime = someTask.getStartTime().orElse(LocalDateTime.MAX);
        LocalDateTime endTime = someTask.getEndTime().orElse(LocalDateTime.MIN);
        LocalDateTime startTimeTask, endTimeTask;
        boolean result = true;
        for (Task task : prioritizedTask) {
            if (!Epic.class.equals(task.getClass())) {
                startTimeTask = task.getStartTime().orElse(LocalDateTime.MAX);
                endTimeTask = task.getEndTime().orElse(LocalDateTime.MIN);
                result &= !(startTime.isAfter(startTimeTask) && startTime.isBefore(endTimeTask) ||
                        endTime.isAfter(startTimeTask) && endTime.isBefore(endTimeTask) ||
                        startTime.isBefore(startTimeTask) && endTime.isAfter(endTimeTask));
            }
        }
        return !result;
    }

    void setId(int id) {
        this.id = id;
    }
}