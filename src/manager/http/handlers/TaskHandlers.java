package manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerSaveException;
import manager.TaskManager;
import data.Task;

import java.io.IOException;

public class TaskHandlers extends BaseHttpHandler {
    public TaskHandlers(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        String[] split = exchange.getRequestURI().getPath().split("/");

        try {
            switch (endpoint) {
                case GET ->
                    sendText(exchange, gson.toJson(taskManager.showAllTasks()));
                case GET_BY_ID ->
                    sendText(exchange, gson.toJson(taskManager.findTaskById(Integer.parseInt(split[2]))));
                case POST -> {
                    task = gson.fromJson(getTaskFromRequestBody(exchange), Task.class);
                    taskManager.addTask(task);
                    writeResponse(exchange, 201, "Задача добавлена");
                }
                case POST_BY_ID -> {
                    task = gson.fromJson(getTaskFromRequestBody(exchange), Task.class);
                    taskManager.updateTask(task);
                    sendText(exchange, "");
                }
                case DELETE_BY_ID -> {
                    task = taskManager.findTaskById(Integer.parseInt(split[2]));
                    taskManager.removeTaskById(task.getTaskId());
                    writeResponse(exchange, 204, "");
                }
                case UNKNOWN ->
                    sendNotFound(exchange);
            }
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        } catch (ManagerSaveException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            writeResponse(exchange, 500, "");
        }
    }
}
