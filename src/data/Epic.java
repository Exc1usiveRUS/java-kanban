package data;

import manager.TaskType;
import status.Status;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subTaskIds;

    public Epic(String taskDescription, String taskName) {
        super(taskDescription, taskName);
        subTaskIds = new ArrayList<>();
    }

    public Epic(String taskName, String taskDescription, Status taskStatus) {
        super(taskName, taskDescription, taskStatus);
        subTaskIds = new ArrayList<>();
    }

    public Epic(int taskId, String taskName, Status taskStatus, String taskDescription) {
        super(taskId, taskName, taskStatus, taskDescription);
        subTaskIds = new ArrayList<>();
    }

    public void setSubTaskIds(int id) {
        subTaskIds.add(id);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

     public void removeSubTask(Integer id) {
         subTaskIds.remove(id);
     }

     public void clearSubTasks() {
        subTaskIds.clear();
     }

     public TaskType getType() {
        return TaskType.EPIC;
     }

}

