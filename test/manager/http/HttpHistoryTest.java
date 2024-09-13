package manager.http;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.http.token.TaskTypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import data.Epic;
import data.SubTask;
import data.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpHistoryTest {
    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static final String BASE_URL = "http://localhost:8080/history";

    @BeforeEach
    void setUp() {
        manager.clearTasks();
        manager.clearSubTasks();
        manager.clearEpics();
        taskServer.start();
    }

    @AfterEach
    void shutDown() {
        taskServer.stop();
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        Task task1 = new Task(1, "task", Status.NEW, "description",
                LocalDateTime.of(2024, 8, 24, 20, 40), Duration.ofMinutes(15));
        Epic epic2 = new Epic(1, "epic", Status.NEW, "description",
                LocalDateTime.of(2024, 8, 24, 10, 0),
                LocalDateTime.of(2024, 8, 24, 10, 15),
                Duration.ofMinutes(15));
        SubTask subtask3 = new SubTask(4, "subtask", Status.NEW, "description",
                2, LocalDateTime.of(2024, 8, 26, 20, 40), Duration.ofMinutes(15));

        manager.addTask(task1);
        manager.addEpic(epic2);
        manager.addSubTask(subtask3);

        //для добавления в историю воспользуемся TaskManager
        manager.findEpicById(epic2.getTaskId());
        manager.findTaskById(task1.getTaskId());
        manager.findSubTaskById(subtask3.getTaskId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> historyByHttp = gson.fromJson(response.body(), new TaskTypeToken().getType());

        assertEquals(200, response.statusCode());
        assertEquals(3, historyByHttp.size());
        assertEquals(epic2.getTaskId(), historyByHttp.get(0).getTaskId());
        assertEquals(task1.getTaskId(), historyByHttp.get(1).getTaskId());
        assertEquals(subtask3.getTaskId(), historyByHttp.get(2).getTaskId());
    }
}