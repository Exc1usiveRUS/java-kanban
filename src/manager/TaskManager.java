package manager;

import data.Epic;
import data.SubTask;
import data.Task;

import java.util.List;

public interface TaskManager {
    int generateId();

    void addTask(Task task);

    void addSubTask(SubTask subTask);

    void addEpic(Epic epic);

    void updateEpicStatus(Epic epic);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void updateTask(Task task);

    SubTask findSubTaskById(int id);

    Task findTaskById(int id);

    Epic findEpicById(int id);

    List<SubTask> findAllSubtaskByEpicId(int id);

    List<Task> showAllTasks();

    List<SubTask> showAllSubTasks();

    List<Epic> showAllEpics();

    void removeSubTaskById(Integer id);

    void removeEpicById(int id);

    void removeTaskById(int id);

    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    List<Task> getPrioritizedTasks();

    List<Task> getHistory();

}
