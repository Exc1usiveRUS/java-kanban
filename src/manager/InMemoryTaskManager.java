package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import exceptions.ManagerSaveException;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    static InMemoryHistoryManager history;
    protected int nextId = 1;
    protected Map<Integer, SubTask> subTasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Set<Task> prioritized = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager(InMemoryHistoryManager history) {
        InMemoryTaskManager.history = history;
    }

    public InMemoryTaskManager() {
        history = new InMemoryHistoryManager();
    }

    public void updateEpicTime(Epic epic) {
        List<Task> subTasksList = getPrioritizedTasks().stream()
                .filter(task -> task.getType().equals(TaskType.SUBTASK))
                .filter(task -> ((SubTask) task).getEpicId() == epic.getTaskId())
                .toList();

        if (subTasksList.isEmpty()) {
            return;
        }

        Duration duration = Duration.ofMinutes(0);
        for (Task subTask : subTasksList) {
            duration = duration.plus(subTask.getDuration());
        }

        LocalDateTime startTime = subTasksList.getFirst().getStartTime();
        LocalDateTime endTime = subTasksList.getLast().getEndTime();

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);
    }

    public void addPrioritized(Task task) {
        if (task.getType().equals(TaskType.EPIC)) return;
        List<Task> taskList = getPrioritizedTasks();
        if (task.getStartTime() != null && task.getEndTime() != null) {
            for (Task task1 : taskList) {
                if (task1.getTaskId() == task.getTaskId()) prioritized.remove(task1);
                if (checkForIntersection(task, task1)) {
                    return;
                }
            }
            prioritized.add(task);
        }
    }

    private boolean checkForIntersection(Task task1, Task task2) {
        return !task1.getEndTime().isBefore(task2.getStartTime()) &&
                !task1.getStartTime().isAfter(task2.getEndTime());
    }

    private void validatePrioritized(Task task) {
        if (task == null || task.getStartTime() == null) return;
        List<Task> taskList = getPrioritizedTasks();

        for (Task someTask : taskList) {
            if (someTask == task) {
                continue;
            }
            boolean taskIntersection = checkForIntersection(task, someTask);

            if (taskIntersection) {
                throw new ManagerSaveException("Задачи - " + task.getTaskId() + " и + " + someTask.getTaskId()
                        + "пересекаются");
            }
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritized);
    }

    @Override
    public int generateId() {
        return nextId++;
    }

    @Override
    public void addTask(Task task) {
        int newId = generateId();
        task.setTaskId(newId);
        validatePrioritized(task);
        addPrioritized(task);
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        int newId = generateId();
        subTask.setTaskId(newId);
        validatePrioritized(subTask);
        addPrioritized(subTask);
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            epic.setSubTaskIds(newId);
            subTasks.put(subTask.getTaskId(), subTask);
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        int newId = generateId();
        epic.setTaskId(newId);
        epics.put(epic.getTaskId(), epic);
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        if (!epics.containsKey(epic.getTaskId())) {
            return;
        }

        List<SubTask> epicSubTasks = new ArrayList<>();

        int countNew = 0;
        int countDone = 0;

        for (int i = 0; i < epic.getSubTaskIds().size(); i++) {
            epicSubTasks.add(subTasks.get(epic.getSubTaskIds().get(i)));
        }

        for (SubTask subtask : epicSubTasks) {
            if (subtask.getTaskStatus().equals(Status.DONE)) {
                countDone++;
            } else if (subtask.getTaskStatus().equals(Status.NEW)) {
                countNew++;
            } else {
                epic.setTaskStatus(Status.IN_PROGRESS);
                return;
            }

            if (countNew == epicSubTasks.size()) {
                epic.setTaskStatus(Status.NEW);
            } else if (countDone == epicSubTasks.size()) {
                epic.setTaskStatus(Status.DONE);
            } else {
                epic.setTaskStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        validatePrioritized(subTask);
        addPrioritized(subTask);
        if ((subTask == null) || (!subTasks.containsKey(subTask.getTaskId()))) {
            return;
        }
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            return;
        }
        subTasks.replace(subTask.getTaskId(), subTask);
        updateEpicStatus(epic);
        updateEpicTime(epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getTaskId())) {
            Epic actualEpic = epics.get(epic.getTaskId());
            actualEpic.setTaskName(epic.getTaskName());
            actualEpic.setTaskDescription(epic.getTaskDescription());
        }
    }

    @Override
    public void updateTask(Task task) {
        validatePrioritized(task);
        addPrioritized(task);
        if (tasks.containsKey(task.getTaskId())) {
            tasks.put(task.getTaskId(), task);
        }
    }

    public List<SubTask> findAllSubtaskByEpicId(int id) {
        ArrayList<SubTask> newSubtasks = new ArrayList<>();
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubTaskIds()) {
                newSubtasks.add(subTasks.get(subtaskId));
            }
        }
        return newSubtasks;
    }

    @Override
    public SubTask findSubTaskById(int id) {
        history.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic findEpicById(int id) {
        history.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Task findTaskById(int id) {
        history.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void removeSubTaskById(Integer id) {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            Epic epic = epics.get(subTask.getEpicId());
            epic.removeSubTask(id);
            updateEpicStatus(epic);
            updateEpicTime(epic);
            prioritized.remove(subTask);
            subTasks.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subTask : epic.getSubTaskIds()) {
                prioritized.remove(subTasks.get(subTask));
                subTasks.remove(subTask);
            }
        }
        epics.remove(id);
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void clearTasks() {
        for (Task task : tasks.values()) {
            history.remove(task.getTaskId());
            prioritized.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        for (SubTask subTask : subTasks.values()) {
            history.remove(subTask.getTaskId());
            prioritized.remove(subTask);
        }

        for (Epic epic : epics.values()) {
            history.remove(epic.getTaskId());
        }
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void clearSubTasks() {
        prioritized.removeAll(epics.values());
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }
    }

    @Override
    public List<Task> showAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> showAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> showAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }
}