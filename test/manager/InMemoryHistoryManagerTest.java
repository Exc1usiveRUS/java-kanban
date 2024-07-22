package manager;

import java.util.List;

import data.Task;
import data.Epic;
import data.SubTask;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager history = new InMemoryHistoryManager();
    static Task task;
    static Epic epic;
    static SubTask subTask;

    @BeforeAll
    static void beforeAll() {
        task = new Task("Описание", "Имя");
        epic = new Epic("Описание эпика", "Имя эпика");
        subTask = new SubTask("Описание саба", "Имя саба", 3);

        task.setTaskId(1);
        epic.setTaskId(2);
        subTask.setTaskId(3);
    }

    @Test
    void add() {

        history.add(task);

        assertNotNull(history.getHistory());
        assertEquals(history.getHistory().getFirst(), task);
    }

    @Test
    void removeFirstTaskFromHistory() {
        history.add(task);
        history.add(epic);
        history.add(subTask);

        history.remove(task.getTaskId());

        assertEquals(history.getHistory(), List.of(epic, subTask));
    }

    @Test
    void removeLastFromTaskHistory() {
        history.add(task);
        history.add(epic);
        history.add(subTask);

        history.remove(subTask.getTaskId());

        assertEquals(history.getHistory(), List.of(task, epic));
    }

    @Test
    void checkForUniquenessInList() {
        history.add(task);
        history.add(epic);
        history.add(subTask);
        history.add(task);

        assertNotEquals(task, history.getHistory().getFirst());
    }
}