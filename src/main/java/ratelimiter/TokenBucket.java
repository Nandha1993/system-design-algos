package ratelimiter;

import ratelimiter.utils.Bucket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucket {

    private int tokens;
    private long refillRate;
    private Map<String, Bucket> userLogs;

    public TokenBucket(int tokens, long refillRate) {
        this.tokens = tokens;
        this.refillRate = refillRate;
        this.userLogs = new ConcurrentHashMap<>();
    }

    public boolean allowUserRequest(String userId){

        long now = System.currentTimeMillis();

        userLogs.putIfAbsent(userId, new Bucket(tokens, now));

        Bucket tokenBucket = userLogs.get(userId);


        long elapsedTime = now - tokenBucket.getLastRefillTime();

        int tokensToRefill = (int) (elapsedTime * refillRate/1000);

        if(tokensToRefill > 0){
            tokenBucket.setTokens(Math.min(tokensToRefill+tokenBucket.getTokens(), tokens));
            tokenBucket.setLastRefillTime(now);
        }

        if (tokenBucket.getTokens() > 0){
            tokenBucket.setTokens(tokenBucket.getTokens()-1);
            userLogs.put(userId, tokenBucket);
            return true;
        }

        return false;

    }

    public static void main(String[] args) {
        TokenBucket tokenBucket = new TokenBucket(5, 5);
        tokenBucket.allowUserRequest("user1");
        tokenBucket.allowUserRequest("user2");
        tokenBucket.allowUserRequest("user1");
        tokenBucket.allowUserRequest("user1");
        tokenBucket.allowUserRequest("user1");
        tokenBucket.allowUserRequest("user1");
        tokenBucket.allowUserRequest("user1");
        tokenBucket.allowUserRequest("user1");
        tokenBucket.allowUserRequest("user1");
        tokenBucket.allowUserRequest("user1");
        tokenBucket.allowUserRequest("user1");
        tokenBucket.allowUserRequest("user1");
        tokenBucket.allowUserRequest("user1");
        tokenBucket.allowUserRequest("user1");

    }

}
