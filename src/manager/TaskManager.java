package manager;

import data.Epic;
import data.SubTask;
import data.Task;

public interface TaskManager {
    int generateId();

    void addTask(Task task);

    void addSubTask(SubTask subTask);

    void addEpic(Epic epic);

    void updateEpicStatus(Epic epic);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void updateTask(Task task);

    void removeSubTaskById(Integer id);

    void removeEpicById(int id);

    void removeTaskById(int id);

    void clearTasks();

    void clearEpics();

    void clearSubTasks();

}
