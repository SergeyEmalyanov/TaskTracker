package TaskTraker;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


class InMemoryTaskManager implements TaskManager {
    private int id;
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Epic> epics;
    protected final Map<Integer, SubTask> subTasks;
    protected final Set<Task> prioritizedTask;
    protected final HistoryManager historyManager;
    private final HashMap<LocalDateTime, Boolean> freeTime;

    InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
        prioritizedTask = new TreeSet<>();
        freeTime = fillingTimeIntervalTable();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public <T extends Task> int add(Integer id, T task) {
        if (task == null) return 0;
        if (intersectionsInTimeWithIntervals(task) && isIntersectionsInTime(task)) {
            System.out.println("Не добавлена: " + task);
            return -1;
        }
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
            prioritizedTask.remove(tasks.get(id));
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            List<Task> subTasks = epics.get(id).getSubTasksOfEpic();
            final int size = subTasks.size();
            SubTask subTask;
            for (int i = 0; i < size; i++) {
                subTask = (SubTask) subTasks.get(0);
                delete(subTask.getId());
            }
            prioritizedTask.remove(epics.get(id));
            epics.remove(id);
        } else if (subTasks.containsKey(id)) {
            subTasks.get(id).getEpicOfSubTask().removeSubTaskOfEpic(subTasks.get(id));
            prioritizedTask.remove(subTasks.get(id));
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

    private <T extends Task> boolean isIntersectionsInTime(T someTask) {
        if (Epic.class.equals(someTask.getClass())) return false;
        if (someTask.getStartTime().isEmpty()) return false;
        LocalDateTime startTime = someTask.getStartTime().get();
        LocalDateTime endTime = someTask.getEndTime().get();
        LocalDateTime startTimeTask, endTimeTask;
        boolean result = true;
        for (Task task : prioritizedTask) {
            if (!Epic.class.equals(task.getClass()) && task.getStartTime().isPresent()) {
                startTimeTask = task.getStartTime().get();
                endTimeTask = task.getEndTime().get();
                result &= !(startTime.isAfter(startTimeTask) && startTime.isBefore(endTimeTask) ||
                        startTime.equals(startTimeTask) ||
                        endTime.isAfter(startTimeTask) && endTime.isBefore(endTimeTask) ||
                        endTime.equals(endTimeTask) ||
                        startTime.isBefore(startTimeTask) && endTime.isAfter(endTimeTask));
            }
        }
        return !result;
    }

    private <T extends Task> boolean intersectionsInTimeWithIntervals(T task) {
        LocalDateTime localDateTimeBegin, localDateTimeEnd, localDateTimeTemp;
        boolean result = true;
        if (task.getStartTime().isPresent()) {
            localDateTimeBegin = settingMinutesFifteenMinuteInterval(task.getStartTime().get());
            localDateTimeEnd = localDateTimeBegin.plus(task.getDuration());
            localDateTimeTemp=localDateTimeBegin;
            while (localDateTimeTemp.isBefore(localDateTimeEnd)) {
                result &= freeTime.get(localDateTimeTemp);
                localDateTimeTemp = localDateTimeTemp.plusMinutes(15);
            }
        } else return false;
        if (result) {
            localDateTimeTemp=localDateTimeBegin;
            while (localDateTimeTemp.isBefore(localDateTimeEnd)) {
                freeTime.put(localDateTimeTemp, false);
                localDateTimeTemp=localDateTimeTemp.plusMinutes(15);
            }
        }
        return !result;
    }

    private HashMap<LocalDateTime, Boolean> fillingTimeIntervalTable() {
        LocalDateTime localDateTimeNow = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("GMT+3"));
        LocalDateTime localDateTimeBegin = settingMinutesFifteenMinuteInterval(localDateTimeNow);
        LocalDateTime localDateTimeEnd = localDateTimeBegin.plusYears(1);
        HashMap<LocalDateTime, Boolean> freeTime = new HashMap<>();
        while (localDateTimeBegin.isBefore(localDateTimeEnd) ||
                localDateTimeBegin.equals(localDateTimeEnd)) {
            freeTime.put(localDateTimeBegin, true);
            localDateTimeBegin = localDateTimeBegin.plusMinutes(15);
        }
        return freeTime;
    }

    private LocalDateTime settingMinutesFifteenMinuteInterval(LocalDateTime localDateTime) {
        int hours = localDateTime.getHour();
        int minutes = localDateTime.getMinute();
        if (minutes <= 7) minutes = 0;
        else if (minutes <= 22) minutes = 15;
        else if (minutes <= 37) minutes = 30;
        else if (minutes <= 52) minutes = 45;
        else {
            minutes = 0;
            hours += 1;
        }
        return localDateTime.withHour(hours).withMinute(minutes).withSecond(0).withNano(0);
    }

    void setId(int id) {
        this.id = id;
    }
}