package manager;

import data.Task;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList history = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getTaskId());
        history.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return history.getTaskList();
    }

    @Override
    public void remove(int id) {
        history.removeNode(history.innerMap.remove(id));
    }

    private static class Node {
        private Node prev;
        private final Task task;
        private Node next;

        private Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private static class CustomLinkedList {
        private final Map<Integer, Node> innerMap = new HashMap<>();
        private Node head;
        private Node tail;

        private void linkLast(Task task) {
            Node newNode = new Node(task, tail, null);

            if (tail == null) {
                head = newNode;
            } else {
                tail.next = newNode;
            }
            tail = newNode;
            innerMap.put(task.getTaskId(), newNode);
        }

        private void removeNode(Node node) {
            if (node == null) {
                return;
            }
                innerMap.remove(node.task.getTaskId());
                Node prev = node.prev;
                Node next = node.next;

                if (prev != null) {
                    prev.next = next;
                }
                if (next != null) {
                    next.prev = prev;
                }

                node.prev = null;
                node.next = null;

                if (node == head) {
                    head = next;
                }
                if (node == tail) {
                    tail = prev;
                }

        }

        private List<Task> getTaskList() {
            List<Task> list = new ArrayList<>();
            Node elem = head;
            while (elem != null) {
                list.add(elem.task);
                elem = elem.next;
            }
            return list;
        }
    }
}