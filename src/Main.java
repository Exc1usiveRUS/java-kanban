import Data.Epic;
import Data.SubTask;
import Status.Status;
import TaskManager.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();


        Epic epic1 = new Epic("Эпик 1", "Нужно сделать");
        taskManager.addEpic(epic1);
        System.out.println("Добавили эпик");
        System.out.println(epic1);


        SubTask subtask1 = new SubTask("Subtask1 создания ",
                "Написать что то ", 1);
        taskManager.addSubTask(subtask1);
        System.out.println("Добавили первый сабтаск");
        System.out.println(subtask1);
        SubTask subtask2 = new SubTask("Subtask2 создания ",
                "Написать что то ", 1);
        taskManager.addSubTask(subtask2);
        System.out.println("Добавили второй сабтаск");
        System.out.println(subtask2);

        subtask1.setTaskStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subtask1);
        System.out.println("Поменяли статус первого сабтаска на В ПРОЦЕССЕ");
        System.out.println(subtask1);
        System.out.println(subtask2);

        System.out.println("Проверяем статус эпика после изменения статуса его сабтаска");
        System.out.println(epic1);


        subtask2.setTaskStatus(Status.DONE);
        taskManager.updateSubTask(subtask2);
        System.out.println("Поменяли статус второго сабтаска на ВЫПОЛНЕН");
        System.out.println(subtask1);
        System.out.println(subtask2);

        System.out.println("Проверяем статус эпика после изменения статуса второго сабтаска");
        System.out.println(epic1);


        subtask1.setTaskStatus(Status.DONE);
        taskManager.updateSubTask(subtask1);
        System.out.println("Выставили статус ВЫПОЛНЕН первому сабтаску" + subtask1.getTaskStatus());
        System.out.println(subtask1);
        System.out.println(subtask2);

        System.out.println("Проверяем статус первого эпика после выполнения всех его сабтасков" + epic1.getTaskStatus());
        System.out.println(epic1);
        System.out.println("проверяем обновление эпика");
        taskManager.updateEpic(epic1, "новое название", "новое описание");
        System.out.println(epic1);

        System.out.println("Проверяем очистились ли сабтаски");
        System.out.println(taskManager.showAllSubTasks());
        System.out.println(subtask1);
        System.out.println(subtask2);

    }
}
