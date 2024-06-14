package Data;

import java.util.ArrayList;
import TaskManager.TaskManager;

public class Epic extends Task {
    ArrayList<Integer> subTaskIds;
    TaskManager taskManager = new TaskManager();

    public Epic(String taskDescription, String taskName) {
        super(taskDescription, taskName);
        subTaskIds = new ArrayList<>();
    }

    public void setSubTaskIds(int id) {
        subTaskIds.add(id);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }


    public void clearSubTasks() {
        subTaskIds.clear();
        taskManager.clearSubTasks();
    }

    public void removeSubTaskById(int id) {
        if (subTaskIds.contains(id)) {
            subTaskIds.remove(id);
            taskManager.removeSubTaskById(id);
        }
    }
}

