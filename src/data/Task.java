package data;

import manager.TaskType;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int taskId;
    private String taskName;
    private String taskDescription;
    private Status taskStatus;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String taskDescription, String taskName, Duration duration, LocalDateTime startTime) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        taskStatus = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int taskId, String taskName, Status taskStatus, String taskDescription,
                LocalDateTime startTime, Duration duration) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.taskDescription = taskDescription;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String taskDescription, String taskName) {
        this.taskDescription = taskDescription;
        this.taskName = taskName;
        taskStatus = Status.NEW;
    }

    public Task(String taskName, String taskDescription, Status taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }

    public LocalDateTime getEndTime() {
        if (duration == null) {
            return startTime;
        }
        return startTime.plus(duration);
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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
