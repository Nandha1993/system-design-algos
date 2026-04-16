package ratelimiter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowLog {

    private int window;
    private int limit;
    private Map<String, Deque<Long>> userLogs;

    public SlidingWindowLog(int window, int limit) {
        this.window = window;
        this.limit = limit;
        userLogs = new ConcurrentHashMap<>();
    }

    public boolean allowRequest(String userId) {

        Long now = System.currentTimeMillis();

        userLogs.putIfAbsent(userId, new ArrayDeque<>());

        Deque<Long> logs = userLogs.get(userId);

        // discard expired timestamps

        while (!logs.isEmpty() && now - logs.peekFirst() > window) {
            logs.pollFirst();
        }

        if(logs.size()<limit) {
            logs.addLast(now);
            userLogs.put(userId, logs);
            return true;
        }

     return false;

    }

    public static void main(String[] args) {
        SlidingWindowLog slidingWindowLog = new SlidingWindowLog(3, 2);
        slidingWindowLog.allowRequest("user1");
        slidingWindowLog.allowRequest("user2");
        slidingWindowLog.allowRequest("user1");
        slidingWindowLog.allowRequest("user1");
    }

}
