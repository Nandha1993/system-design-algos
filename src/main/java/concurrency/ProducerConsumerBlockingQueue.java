package concurrency;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ProducerConsumerBlockingQueue {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        Thread producerThread = new Thread((new Runnable() {
            int count = 0;
            @Override
            public void run() {
                while(true){
                    queue.offer(count++);
                    System.out.println("Producer produced : "+queue.size());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }));
        Thread consumerThread = new Thread((new Runnable() {
            @Override
            public void run() {
                while(true){
                    queue.poll();
                    System.out.println("Consumer consumed : "+queue.size());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }));
        producerThread.start();
        consumerThread.start();

    }

}
