package data;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.InMemoryHistoryManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    TaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    @Test
    public void subTasksEqualsIfIdEquals() {

       Epic epic = new Epic("Тестовое описание", "Тестовое имя");

        SubTask subTask1 = new SubTask("Тестовое описание", "Тестовое имя", epic.getTaskId());
        SubTask subTask2 = new SubTask("Второе описание", "Второе имя", epic.getTaskId());

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        subTask1.setTaskId(subTask2.getTaskId());
        assertEquals(subTask1, subTask2, "Ошибка, тест не пройден");

    }
}