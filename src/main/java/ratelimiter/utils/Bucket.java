package ratelimiter.utils;

public class Bucket {

    private int tokens;
    private long lastRefillTime;

    public Bucket(int tokens, long lastRefillRate) {
        this.tokens = tokens;
        this.lastRefillTime = lastRefillTime;
    }

    public int getTokens() {
        return tokens;
    }
    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public long getLastRefillTime() {
        return lastRefillTime;
    }

    public void setLastRefillTime(long lastRefillTime) {
        this.lastRefillTime = lastRefillTime;
    }
}
