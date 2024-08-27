package manager;

import data.Epic;
import data.SubTask;
import data.Task;
import exceptions.ManagerSaveException;
import status.Status;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private static final String HEADER = "id,type,name,description,start,end,duration,epic\n";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy,HH:mm");
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public FileBackedTaskManager(File file) {
        super(new InMemoryHistoryManager());
        this.file = file;
    }


    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = bufferedReader.readLine();

            while (bufferedReader.ready()) {
                line = bufferedReader.readLine();

                Task task = taskFromString(line);
                if (task.getType().equals(TaskType.EPIC)) {
                    fileManager.epics.put(task.getTaskId(), (Epic) task);
                } else if (task.getType().equals(TaskType.SUBTASK)) {
                    fileManager.subTasks.put(task.getTaskId(), (SubTask) task);
                    fileManager.epics.get(((SubTask) task).getEpicId()).setSubTaskIds(task.getTaskId());
                } else {
                    fileManager.tasks.put(task.getTaskId(), task);
                }
                if (fileManager.nextId < task.getTaskId()) {
                    fileManager.nextId = task.getTaskId();
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось загрузить данные из файла");
        }
        return fileManager;
    }

    public void save() {
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException io) {
            throw new ManagerSaveException("Не удалось найти файл");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write(HEADER);
            for (Task task : showAllTasks()) {
                bufferedWriter.write(taskToString(task));
            }
            for (Epic epic : showAllEpics()) {
                bufferedWriter.write(taskToString(epic));
            }
            for (SubTask subTask : showAllSubTasks()) {
                bufferedWriter.write(taskToString(subTask));
            }
        } catch (IOException io) {
            throw new ManagerSaveException("Не удалось сохранить файл");
        }

    }

    private static String taskToString(Task task) {

        String startTime = task.getStartTime() != null ? task.getStartTime().format(formatter) : "";
        String endTime = task.getEndTime() != null ? task.getEndTime().format(formatter) : "";
        String duration = task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "";

        return task.getTaskId() + "," +
                task.getType() + "," +
                task.getTaskName() + "," +
                task.getTaskStatus() + "," +
                task.getTaskDescription() + "," +
                startTime + "," +
                endTime + "," +
                duration + "," +
                getEpicId(task) + "\n";

    }

    private static Task taskFromString(String value) {
        String[] lines = value.split(",");
        int id = Integer.parseInt(lines[0]);
        String taskType = lines[1];
        String name = lines[2];
        Status status = Status.valueOf(lines[3]);
        String description = lines[4];
        LocalDateTime startTime = LocalDateTime.of(LocalDate.parse(lines[5], dateFormatter),
                LocalTime.parse(lines[6], timeFormatter));
        Duration duration = Duration.ofMinutes(Long.parseLong(lines[9]));

        if (taskType.equals("EPIC")) {
            LocalDateTime endEpicTime = LocalDateTime.of(LocalDate.parse(lines[7], dateFormatter),
                    LocalTime.parse(lines[8], timeFormatter));
            return new Epic(id, name, status, description, startTime, endEpicTime, duration);
        } else if (taskType.equals("SUBTASK")) {
            int epicId = Integer.parseInt(lines[10]);
            return new SubTask(id, name, status, description, epicId, startTime, duration);
        } else {
            return new Task(id, name, status, description, startTime, duration);
        }
    }

    private static String getEpicId(Task task) {
        if (task.getType().equals(TaskType.SUBTASK)) {
            return Integer.toString(((SubTask) task).getEpicId());
        } else {
            return "";
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubTaskById(Integer id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }
}
