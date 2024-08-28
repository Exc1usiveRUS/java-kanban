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
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    File file;

    FileBackedTaskManagerTest() throws IOException {
        file = File.createTempFile("test", ".csv");
        taskManager = new FileBackedTaskManager(file);
    }


    @Test
    void testLoadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(20));
        fileBackedTaskManager.addTask(task);

        fileBackedTaskManager.addEpic(epic);

        subTask.setStartTime(LocalDateTime.now().plusMinutes(30));
        subTask.setDuration(Duration.ofMinutes(20));
        fileBackedTaskManager.addSubTask(subTask);

        assertEquals(1, fileBackedTaskManager.tasks.size());
        assertEquals(1, fileBackedTaskManager.epics.size());
        assertEquals(1, fileBackedTaskManager.subTasks.size());

        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(fileBackedTaskManager.showAllTasks(), fileManager.showAllTasks());
        assertEquals(fileBackedTaskManager.showAllEpics(), fileManager.showAllEpics());
        assertEquals(fileBackedTaskManager.showAllSubTasks(), fileManager.showAllSubTasks());
    }

    @Test
    void loadFromEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("emptyTest", ".csv");
        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile(emptyFile);
        assertNotNull(loadFile); //попробовал создать объект класса на основе пустого файла и, соответственно, сравнить с null
        assertEquals(0, loadFile.epics.size());
        assertEquals(0, loadFile.subTasks.size());
        assertEquals(0, loadFile.tasks.size());
    }
}