package concurrency;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {

    Queue<Integer> queue;
    int size;
    public ProducerConsumer(int size) {
        queue = new LinkedList<>();
        this.size = size;
    }
    final Object lock = new Object();

    Thread producer = new Thread(new Runnable() {
        @Override
        public void run() {
            int count = 0;
            while(true) {
                try {
                    synchronized (lock) {
                        while(queue.size() == size) {
                            lock.wait();
                        }
                        queue.offer(++count);
                        System.out.println("Producer Thread" + ": Producing " + count + " to queue");
                        lock.notify();
                        Thread.sleep(1000);
                    }
                } catch(InterruptedException e) {
                    throw  new RuntimeException();
                }
            }
        }
    });

    Thread consumer = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                try {
                    synchronized (lock) {
                        while(queue.size() == 0) {
                            lock.wait();
                        }
                        queue.poll();
                        System.out.println("Consumer Thread" + ": Consuming " + queue.size() + " from queue");
                        lock.notify();
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {

                }
            }
        }
    });

    public void runProducerConsumer() {
        producer.start();
        consumer.start();
    }

    public static void main(String[] args) throws InterruptedException {
        ProducerConsumer producerConsumer = new ProducerConsumer(5);
        producerConsumer.runProducerConsumer();
    }

}
