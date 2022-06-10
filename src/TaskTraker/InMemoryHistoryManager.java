package TaskTraker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    Map<Integer, Node<Task>> browsingHistory;
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    protected InMemoryHistoryManager() {
        browsingHistory = new HashMap();
    }

    @Override
    public void add(int id, Task task) {
        if (browsingHistory.size() == 10) {
            browsingHistory.remove(1);
        }
        browsingHistory.add(task);
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory;
    }

    void linkLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        size++;
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
    void removeNodeHead (){
        final Node<Task> newHead = head.next;
            newHead.prev=null;
            head=newHead;
    }
    void removeNode (Node<Task> node){
        final Node <Task> prevNode = node.prev;
        final Node <Task> nextNode = node.next;
        prevNode.next=nextNode;
        nextNode.prev=prevNode;
    }
}

