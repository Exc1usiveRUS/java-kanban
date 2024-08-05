package manager;

import static org.junit.jupiter.api.Assertions.*;
import status.Status;
import data.Task;
import data.SubTask;
import data.Epic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.File;

class FileBackedTaskManagerTest {

    File file;
    Task task;
    Epic epic;
    SubTask subTask;

    @BeforeEach
    void beforeEach() throws IOException {
        file = File.createTempFile("test", ".csv");
        task = new Task("testTask", "testTaskDescription", Status.IN_PROGRESS);
        epic = new Epic("testEpic", "testEpicDescription", Status.IN_PROGRESS);
        subTask = new SubTask("testSub", "testSubDescription", Status.IN_PROGRESS, 2);
    }

    @Test
    void testLoadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        fileBackedTaskManager.addTask(task);
        fileBackedTaskManager.addEpic(epic);
        fileBackedTaskManager.addSubTask(subTask);

        assertEquals(1, fileBackedTaskManager.tasks.size());
        assertEquals(1, fileBackedTaskManager.epics.size());
        assertEquals(1, fileBackedTaskManager.subTasks.size());




        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(fileBackedTaskManager.showAllTasks(), fileManager.showAllTasks());
        assertEquals(fileBackedTaskManager.showAllEpics(), fileManager.showAllEpics());
        assertEquals(fileBackedTaskManager.showAllSubTasks(), fileManager.showAllSubTasks());

    }
}