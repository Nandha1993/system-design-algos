package cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class LRUCache {

    private final Node head;
    private final Node tail;
    private final int capacity;
    private Map<Integer,Node> map;
    private ReentrantLock lock;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>(capacity);
        head = new Node(0,0);
        tail = new Node(0,0);
        head.next = tail;
        tail.prev = head;
        lock = new ReentrantLock();
    }

    public int get(int key) {
        try {
            lock.lock();
            if (!map.containsKey(key)) {
                System.out.println("No key found");
                return -1;
            }
            Node node = map.get(key);
            remove(node);
            insert(node);
            System.out.println(node.key);
            return node.value;
        } finally {
            lock.unlock();
        }

    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            remove(node);
        }
        Node node = new Node(key, value);
        map.put(key, node);
        insert(node);

        if (map.size() > capacity) {
            Node lru = tail.prev;
            remove(lru);
            map.remove(lru.key);
        }
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void insert(Node node) {
        node.next = head.next;
        node.prev = head;

        head.next.prev = node;
        head.next = node;
    }

    public static void main(String[] args) {
        LRUCache lruCache = new LRUCache(3);
        lruCache.put(1, 1);
        lruCache.get(1);
        lruCache.get(2);
        lruCache.put(2, 2);
        lruCache.put(3, 3);
        lruCache.put(4, 4);
        lruCache.put(5, 5);
        lruCache.get(2);
        lruCache.put(6, 6);
        lruCache.get(5);
    }

}
