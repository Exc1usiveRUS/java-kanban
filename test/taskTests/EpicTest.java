package taskTests;

import data.Epic;
import taskManager.InMemoryTaskManager;
import taskManager.TaskManager;
import taskManager.InMemoryHistoryManager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EpicTest {

    TaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    @Test
    public void epicsEqualsIfIdEquals() {

        Epic epic1 = new Epic("Тестовое описание", "Тестовое имя");
        Epic epic2 = new Epic("Второе описание", "Второе имя");

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        epic1.setTaskId(epic2.getTaskId());
        assertEquals(epic1, epic2, "Ошибка, тест не пройден");

    }
}