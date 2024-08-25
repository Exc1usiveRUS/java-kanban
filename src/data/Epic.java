package data;

import manager.TaskType;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subTaskIds;
    private LocalDateTime endTime;

    public Epic(String taskDescription, String taskName, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(taskDescription, taskName, duration, startTime);
        this.endTime = endTime;
        subTaskIds = new ArrayList<>();
    }

    public Epic(String taskDescription, String taskName) {
        super(taskDescription, taskName);
        subTaskIds = new ArrayList<>();
    }

    public Epic(String taskName, String taskDescription, Status taskStatus) {
        super(taskName, taskDescription, taskStatus);
        subTaskIds = new ArrayList<>();
    }

    public Epic(int taskId, String taskName, Status taskStatus, String taskDescription,
                LocalDateTime startTime,
                LocalDateTime endTime, Duration duration) {
        super(taskId, taskName, taskStatus, taskDescription, startTime, duration);
        subTaskIds = new ArrayList<>();
        this.endTime = endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}

