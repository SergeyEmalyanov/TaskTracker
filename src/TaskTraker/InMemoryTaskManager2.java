package TaskTraker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class InMemoryTaskManager2 implements TaskManager2 {
    private int id;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, SubTask> subTasks;
    private final HistoryManager historyManager;

    InMemoryTaskManager2() {
        id = 0;
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
        public int addNewTask(Task task) {
            tasks.put(++id, task);
            return id;
        }

    @Override
    public void taskUpdate(int id, Task task) {
        tasks.put(id, task);
    }

    @Override
    public void taskRemove(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Map<Integer, Task> getAllTasks() {
        return tasks;
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id:tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public int addNewEpic(Epic epic) {
        epics.put(++id, epic);
        return id;
    }

    @Override
    public void EpicRemove(int id) {
        ArrayList<SubTask> subTaskOfEpic = epics.get(id).getSubTaskOfEpic();
        for (SubTask subTask : subTaskOfEpic) {
            for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
                if (subTask.equals(entry.getValue())) subTasks.remove(entry.getKey());
                historyManager.remove(entry.getKey());
            }
        }
        historyManager.remove(id);
        epics.remove(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Map<Integer, Epic> getAllEpics() {
        return epics;
    }

    @Override
    public void deleteAllEpics() {
        for (Map.Entry<Integer,Epic> entry:epics.entrySet()) {
            historyManager.remove(entry.getKey());
        }
        epics.clear();
        deleteAllSubTasks();
    }

    @Override
    public int addNewSubTask(SubTask subTask) {
        subTasks.put(++id, subTask);
        return id;
    }

    @Override
    public void subTaskUpdate(int id, SubTask subTask) {
        subTasks.put(id, subTask);
    }

    @Override
    public void subTaskRemove(int id) {
        Epic epic = subTasks.get(id).getEpicOfSubTask();
        for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
            if (epic.equals(entry.getValue())) {
                epics.get(entry.getKey()).removeSubTaskOfEpic(subTasks.get(id));
            }
        }
        historyManager.remove(id);
        subTasks.remove(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Map <Integer,SubTask> getAllSubTasks() {
        return subTasks;
    }

    @Override
    public void deleteAllSubTasks() {
        for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
            historyManager.remove(entry.getKey());
            subTasks.clear();
        }
    }

    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(Epic epic) {
        return epic.getSubTaskOfEpic();
    }
}