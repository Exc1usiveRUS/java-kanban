package taskManager;

import data.Epic;
import data.SubTask;
import data.Task;
import status.Status;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    InMemoryHistoryManager history;
    private int nextId = 1;
    Map<Integer, SubTask> subTasks = new HashMap<>();
    Map<Integer, Epic> epics = new HashMap<>();
    Map<Integer, Task> tasks = new HashMap<>();

    public InMemoryTaskManager(InMemoryHistoryManager history) {
        this.history = history;
    }

    @Override
    public int generateId() {
        return nextId++;
    }

    @Override
    public void addTask(Task task) {
        int newId = generateId();
        task.setTaskId(newId);
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        int newId = generateId();
        subTask.setTaskId(newId);
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            epic.setSubTaskIds(newId);
            subTasks.put(subTask.getTaskId(), subTask);
            updateEpicStatus(epic);
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
        if ((subTask == null) || (!subTasks.containsKey(subTask.getTaskId()))) {
            return;
        }
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            return;
        }
        subTasks.replace(subTask.getTaskId(), subTask);
        updateEpicStatus(epic);
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
        if (tasks.containsKey(task.getTaskId())) {
            tasks.put(task.getTaskId(), task);
        }
    }

    public List<SubTask> findAllSubtaskByEpicId(int id) {
        ArrayList<SubTask> NewSubtasks = new ArrayList<>();
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubTaskIds()) {
                NewSubtasks.add(subTasks.get(subtaskId));
            }
        }
        return NewSubtasks;
    }

    public SubTask findSubTaskById(int id) {
        history.add(subTasks.get(id));
        return subTasks.get(id);
    }

    public Epic findEpicById(int id) {
        history.add(epics.get(id));
        return epics.get(id);
    }

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
            subTasks.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subTask : epic.getSubTaskIds()) {
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
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void clearSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            updateEpicStatus(epic);
        }
    }

    public List<Task> showAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<SubTask> showAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public List<Epic> showAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Task> getHistory() {
        return history.getHistory();
    }
}