package Data;

import Status.Status;

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
