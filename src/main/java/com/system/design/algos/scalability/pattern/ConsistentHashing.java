package com.system.design.algos.scalability.pattern;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Arrays;

public class ConsistentHashing {
    // Number of virtual nodes per server
    private final int numReplicas;
    // Hash ring storing virtual nodes
    private final TreeMap<Long, String> ring;
    // Set of physical servers
    private final Set<String> servers;

    /**
     * Consistency hashing otherwise called distributed hashing technique
     * that overcomes the issue of traditional hashing used in
     * distributed systems.
     *
     * @param servers
     * @param numReplicas
     */
    public ConsistentHashing(List<String> servers, int numReplicas) {
        this.numReplicas = numReplicas;
        this.ring = new TreeMap<>();
        this.servers = new HashSet<>();

        // Add each server to the hash ring
        for (String server : servers) {
            addServer(server);
        }
    }

    /**
     *
     * @param key
     * @return
     */
    private long hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            return ((long) (digest[0] & 0xFF) << 24) |
                    ((long) (digest[1] & 0xFF) << 16) |
                    ((long) (digest[2] & 0xFF) << 8) |
                    ((long) (digest[3] & 0xFF));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    public void addServer(String server) {
        //Set that holds physical servers
        servers.add(server);
        /*
        Concept of virtual node used in ring to distribute load
        evenly across all nodes in ring
        */
        for (int i = 0; i < numReplicas; i++) {
            // Unique hash for each virtual node
            long hash = hash(server + "-" + i);
            ring.put(hash, server);
        }
    }

    public void removeServer(String server) {
        if (servers.remove(server)) {
            for (int i = 0; i < numReplicas; i++) {
                long hash = hash(server + "-" + i);
                ring.remove(hash);
            }
        }
    }

    public String getServer(String key) {
        if (ring.isEmpty()) {
            return null; // No servers available
        }

        long hash = hash(key);
        // Find the closest server in a clockwise direction
        Map.Entry<Long, String> entry = ring.ceilingEntry(hash);
        if (entry == null) {
            // If we exceed the highest node, wrap around to the first node
            entry = ring.firstEntry();
        }
        return entry.getValue();
    }

    public static void main(String[] args) {
        List<String> servers = Arrays.asList("S0", "S1", "S2", "S3", "S4", "S5");
        ConsistentHashing ch = new ConsistentHashing(servers, 3);

        // Step 2: Assign requests (keys) to servers
        System.out.println("UserA is assigned to: " + ch.getServer("UserA"));
        System.out.println("UserB is assigned to: " + ch.getServer("UserB"));

        // Step 3: Add a new server dynamically
        ch.addServer("S6");
        System.out.println("UserA is now assigned to: " + ch.getServer("UserA"));

        // Step 4: Remove a server dynamically
        ch.removeServer("S2");
        System.out.println("UserB is now assigned to: " + ch.getServer("UserB"));
    }
}

