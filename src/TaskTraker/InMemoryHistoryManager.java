package TaskTraker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    final Map<Integer, Node<Task>> browsingHistory;
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    protected InMemoryHistoryManager() {
        browsingHistory = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        /*System.out.print("Начало ");////
        printNumHistory();////
        System.out.println(" \n"+size);////
        System.out.println();////*/

        if (browsingHistory.containsKey(task.getId())) {
            remove(task.getId());
            browsingHistory.put(task.getId(), linkLast(task));
            size++;
        } else if (size == 10) {
            removeNodeHead();
            size--;
        } else {
            browsingHistory.put(task.getId(), linkLast(task));
            size++;
        }
        /*System.out.print("Конец ");////
        printNumHistory();////
        System.out.println(" \n"+size);
        System.out.println();////*/
    }

    @Override
    public void remove(int id) {
        if (id < 0) {
            browsingHistory.clear();
            head = null;
            tail = null;
            size = 0;
        } else {
            removeNode(browsingHistory.get(id));
            browsingHistory.remove(id);
            size--;
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    Node<Task> linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        return newNode;
    }

    List<Task> getTasks() {
        List<Task> browsingHistoryList = new ArrayList<>();
        Node<Task> someNode = head;
        for (int i = 1; i <= size; i++) {
            browsingHistoryList.add(someNode.data);
            someNode = someNode.next;
        }
        return browsingHistoryList;
    }

    void removeNode(Node<Task> node) {
        if (node.prev == null) {
            removeNodeHead();
        } else if (node.next == null) {
            removeNodeTail();
        } else {
            final Node<Task> prevNode = node.prev;
            final Node<Task> nextNode = node.next;
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
    }

    void removeNodeHead() {
        if (head.next == null) {
            head = null;
            tail = null;
        } else {
            final Node<Task> newHead = head.next;
            newHead.prev = null;
            head = newHead;
        }
    }

    void removeNodeTail() {
        final Node<Task> newTail = tail.prev;
        newTail.next = null;
        tail = newTail;
    }
    /////////////////////////////
    /*void printNumHistory (){
        for (Integer num: browsingHistory.keySet()) {
            System.out.print(num+" ");
        }
    }*/
}