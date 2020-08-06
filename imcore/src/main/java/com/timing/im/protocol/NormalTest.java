package com.timing.im.protocol;

public class NormalTest {

    public static void main(String[] args) {
        Context context = new Context();



    }

    static class Context {
        public void say() {
            System.out.println("hehe");
        }
    }

    static class Consumer extends Thread {

        private Context context;
        public Consumer(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            context.say();
        }
    }

    static class Producer extends Thread {
        private Context context;
        public Producer(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            context = new Context();
        }
    }
}
