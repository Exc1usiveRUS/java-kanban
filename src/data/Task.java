package data;

import manager.TaskType;
import status.Status;

import java.util.Objects;

public class Task {
    private int taskId;
    private String taskName;
    private String taskDescription;
    private Status taskStatus;

    public Task(String taskDescription, String taskName) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        taskStatus = Status.NEW;
    }

    public Task(int taskId, String taskName, Status taskStatus, String taskDescription) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskDescription = taskDescription;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskID) {
        this.taskId = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return taskId == task.taskId && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, taskStatus);
    }

    @Override
    public String toString() {
        return "Data.Task{" +
                "taskDescription='" + taskDescription + '\'' +
                ", taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
