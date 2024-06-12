package Data;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String taskDescription, String taskName, int epicId) {
        super(taskDescription, taskName);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
