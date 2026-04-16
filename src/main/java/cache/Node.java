package cache;

public class Node {

    int key, value;
    Node prev, next;

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node(int key, int value) {
        this.key = key;
        this.value = value;
    }


}
