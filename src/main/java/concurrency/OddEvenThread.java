package concurrency;

public class OddEvenThread {

    static int counter =1;

    public static void main(String[] args) throws InterruptedException {

        final Object lock = new Object();

        /**
         * OddThread Prints Odd number
         */
        Thread oddThread = new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (lock) {
                    while (true){
                        while(counter % 2 == 0) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {

                            }
                        }
                        System.out.println(counter++);

                        lock.notify();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } );

        /**
         * Even Thread Prints even number
         */
        Thread evenThread = new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (lock) {
                    while (true){
                        while(counter % 2 !=0) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.println(counter++);
                        lock.notify();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        });
        oddThread.start();
        evenThread.start();

    }
}
