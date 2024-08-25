package data;

import manager.TaskType;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int epicId;

    public SubTask(String taskDescription, String taskName, Duration duration, LocalDateTime startTime, int epicId) {
        super(taskDescription, taskName, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(String taskName, String taskDescription, Status taskStatus, int epicId) {
        super(taskName, taskDescription, taskStatus);
        this.epicId = epicId;
    }

    public SubTask(int taskId, String taskName, Status taskStatus, String taskDescription, int epicId,
                   LocalDateTime startTime, Duration duration) {
        super(taskId, taskName, taskStatus, taskDescription, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String taskDescription, String taskName, int epicId) {
        super(taskDescription, taskName);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }
}
