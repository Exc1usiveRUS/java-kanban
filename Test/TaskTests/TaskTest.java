package TaskTests;

import Data.Task;
import TaskManager.InMemoryTaskManager;
import TaskManager.TaskManager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TaskTest {

    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void tasksEqualsIfIdEquals() {

        Task task1 = new Task("Тестовое описание", "Тестовое имя");
        Task task2 = new Task("Второе описание", "Второе имя");

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        task1.setTaskId(task2.getTaskId());
        assertEquals(task1, task2, "Ошибка, тест не пройден");

    }
}