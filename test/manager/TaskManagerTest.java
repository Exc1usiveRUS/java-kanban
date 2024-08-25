package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import exceptions.ManagerSaveException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;

    @BeforeEach
    void beforeEach() {
        task = new Task("Описание", "Имя");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(20));

        epic = new Epic("Описание эпика", "Имя эпика");

        subTask = new SubTask("Описание сабтаска", "Имя сабтаска", 2);
        subTask.setStartTime(LocalDateTime.now().plusHours(1));
        subTask.setDuration(Duration.ofMinutes(20));
    }

    @Test
    void addTask() {
        taskManager.addTask(task);
        assertNotNull(taskManager.showAllTasks());
        assertEquals(task, taskManager.showAllTasks().getFirst());
        assertEquals(1, taskManager.showAllTasks().size());

    }

    @Test
    void addEpic() {
        taskManager.addEpic(epic);
        assertNotNull(taskManager.showAllEpics());
        assertEquals(epic, taskManager.showAllEpics().getFirst());
        assertEquals(1, taskManager.showAllEpics().size());
    }

    @Test
    void addSubTask() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        assertNotNull(taskManager.showAllSubTasks());
        assertEquals(subTask, taskManager.showAllSubTasks().getFirst());
        assertEquals(1, taskManager.showAllSubTasks().size());
    }

    @Test
    void findTaskById() {
        taskManager.addTask(task);
        assertEquals(task, taskManager.findTaskById(task.getTaskId()));
    }

    @Test
    void findEpicById() {
        taskManager.addEpic(epic);
        assertEquals(epic, taskManager.findEpicById(epic.getTaskId()));
    }

    @Test
    void findSubTaskById() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);
        assertEquals(subTask, taskManager.findSubTaskById(subTask.getTaskId()));
    }

    @Test
    void checkOfRemovingOfTask() {
        taskManager.addTask(task);
        assertNotNull(taskManager.findTaskById(task.getTaskId()));

        taskManager.removeTaskById(task.getTaskId());
        assertEquals(0, taskManager.showAllTasks().size());
    }

    @Test
    void checkOfRemovingOfSubTask() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addTask(subTask);

        taskManager.removeSubTaskById(subTask.getTaskId());
        assertEquals(0, taskManager.showAllSubTasks().size());
    }

    @Test
    void checkOfRemovingOfEpic() {
        taskManager.addEpic(epic);
        assertNotNull(taskManager.findEpicById(epic.getTaskId()));

        taskManager.removeEpicById(epic.getTaskId());
        assertEquals(0, taskManager.showAllEpics().size());
    }

    @Test
    void checkOfPrioritized() {
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(20));
        taskManager.addTask(task);

        taskManager.addEpic(epic);

        subTask.setStartTime(LocalDateTime.now().minusMinutes(60));
        subTask.setDuration(Duration.ofMinutes(15));
        subTask.setTaskStatus(Status.NEW);
        taskManager.addSubTask(subTask);

        assertEquals(taskManager.getPrioritizedTasks(), List.of(subTask, task));
    }

    @Test
    void checkForIntersection() {
        epic.setStartTime(LocalDateTime.of(2024, 8, 25, 18, 0));
        epic.setDuration(Duration.ofMinutes(30));
        taskManager.addEpic(epic);

        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(60));
        taskManager.addTask(task);

        subTask.setStartTime(LocalDateTime.now().plusMinutes(30));
        subTask.setDuration(Duration.ofMinutes(60));

        assertThrows(ManagerSaveException.class, () -> taskManager.addSubTask(subTask));
    }

    @Test
    void checkOfHistory() {
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);

        taskManager.findEpicById(epic.getTaskId());
        taskManager.findTaskById(task.getTaskId());

        assertEquals(taskManager.getHistory(), List.of(epic, task));
    }

    @Test
    void shouldEqualsTasksWithSameId() {
        Task task1 = new Task("Описание 1", "Имя 1");
        Task task2 = new Task("Описание 2", "Имя 2");

        task2.setTaskId(task1.getTaskId());
        assertEquals(task1, task2);
    }
}
