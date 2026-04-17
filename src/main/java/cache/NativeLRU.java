package cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class NativeLRU<K,V> extends LinkedHashMap<K,V> {

    private int capacity;

    public NativeLRU(int capacity) {
        super(capacity, 0.75F, true);
        this.capacity = capacity;
    }

    @Override
    public boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > capacity;
    }

    public static void main(String[] args) {
        NativeLRU<Integer, String> cache = new NativeLRU<>(2);
        cache.put(0,"a");
        cache.put(1,"b");
        cache.get(0);
        cache.put(2,"c");
        cache.get(0);
        cache.put(3,"d");
        System.out.println(cache);
    }

}
