package data;

public class SubTask extends Task {
    private final int epicId;

    public SubTask(String taskDescription, String taskName, int epicId) {
        super(taskDescription, taskName);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
