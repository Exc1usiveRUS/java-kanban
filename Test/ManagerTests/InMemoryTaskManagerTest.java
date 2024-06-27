package ManagerTests;

import TaskManager.InMemoryTaskManager;
import Data.Epic;
import Data.SubTask;
import Data.Task;
import TaskManager.TaskManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager;
    Epic epic;
    SubTask subTask;
    Task task;

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
        epic = new Epic("Описание эпика", "Имя эпика");
        subTask = new SubTask("Описание сабтаска", "Имя сабтаска", epic.getTaskId());
        task = new Task("Описание таска", "Имя таска");
    }

    @Test
    void shouldAddTasksAndFindById() {
        taskManager.addTask(task);

        assertNotNull(taskManager.findTaskById(task.getTaskId()));
        assertNotNull(taskManager.showAllTasks());

    }

    @Test
    void shouldAddEpicAndFindById() {
        taskManager.addEpic(epic);

        assertNotNull(taskManager.findEpicById(epic.getTaskId()));
        assertNotNull(taskManager.showAllEpics());
    }

    @Test
    void shouldAddSubtaskAndFindById() {
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Описание", "Имя", epic.getTaskId());
        taskManager.addSubTask(subTask1);

        assertNotNull(taskManager.findSubTaskById(subTask1.getTaskId()));
        assertNotNull(taskManager.showAllSubTasks());
    }

    @Test
    void shouldEqualsTasksWithSameId() {
        Task task1 = new Task("Описание 1", "Имя 1");
        Task task2 = new Task("Описание 2", "Имя 2");

        task2.setTaskId(task1.getTaskId());
        assertEquals(task1, task2);
    }
}