package TaskManager;

import Data.Epic;
import Data.SubTask;
import Data.Task;
import Status.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int nextId = 1;
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Task> tasks = new HashMap<>();

    public void addTask(Task task) {
        task.setTaskId(nextId);
        nextId++;
        tasks.put(task.getTaskId(), task);
    }

    public void addSubTask(SubTask subTask) {
        subTask.setTaskId(nextId);
        Epic epic = epics.get(subTask.getEpicId());
        epic.setSubTaskIds(nextId);
        nextId++;
        subTasks.put(subTask.getTaskId(), subTask);
        updateEpicStatus(epic);
    }

    public void addEpic(Epic epic) {
        epic.setTaskId(nextId);
        nextId++;
        epics.put(epic.getTaskId(), epic);
    }

    private void updateEpicStatus(Epic epic) {
        if (!epics.containsKey(epic.getTaskId())) {
            return;
        }

        ArrayList<SubTask> epicSubTasks = new ArrayList<>();

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

    private void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getTaskId())) {
            epics.replace(epic.getTaskId(), epic);
            updateEpicStatus(epic);
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getTaskId())) {
            tasks.put(task.getTaskId(), task);
        }
    }

    public ArrayList<SubTask> findAllSubtaskByEpicId(int id) {
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
        return subTasks.get(id);
    }

    public Epic findEpicById(int id) {
        return epics.get(id);
    }

    public Task findTaskById(int id) {
        return tasks.get(id);
    }

    public void removeSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            Epic epic = epics.get(subTask.getEpicId());
            epic.getSubTaskIds().remove(id);
            updateEpicStatus(epic);
            subTasks.remove(id);

        }
    }

    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subTask : epic.getSubTaskIds()) {
                subTasks.remove(subTask);
            }
            epic.getSubTaskIds().clear();
        }

        epics.remove(id);
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        subTasks.clear();
        epics.clear();
    }

    public void clearSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTaskIds().clear();
            updateEpicStatus(epic);
        }
    }

    public ArrayList<Task> showAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> showAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> showAllEpics() {
        return new ArrayList<>(epics.values());
    }

}