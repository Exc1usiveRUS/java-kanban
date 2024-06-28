package TaskManager;

import Data.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final static int LIMIT_OF_HISTORY = 10;
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() >= LIMIT_OF_HISTORY) {
                history.removeFirst();
            }
            history.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
