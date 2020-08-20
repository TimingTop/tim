package com.timing.im.netty.util.executor;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultEventExecutorChooserFactory implements  EventExecutorChooserFactory {

    public static final DefaultEventExecutorChooserFactory INSTANCE
            = new DefaultEventExecutorChooserFactory();

    private DefaultEventExecutorChooserFactory() {}


    @Override
    public EventExecutorChooser newChooser(EventExecutor[] executors) {
        if (isPowerOfTwo(executors.length)) {
            return new PowerOfTwoEventExecutorChooser(executors);
        } else {
            return new GenericEventExecutorChooser(executors);
        }
    }


    // 2 指数，
    // 使用 负数 & 操作，
    //  4 =  00100   ,负数是所有位取反 + 1 = 补码
    // -4 =  11011 + 1 = 11100
    // & 之后
    private boolean isPowerOfTwo(int val) {
        return (val & -val) == val;
    }


    private static final class PowerOfTwoEventExecutorChooser implements EventExecutorChooser {
        private final AtomicInteger idx = new AtomicInteger();
        private final EventExecutor[] executors;

        PowerOfTwoEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }
        @Override
        public EventExecutor next() {
            // 能进来的肯定是 2 的指数，先计算 减法， 后面的位数 肯定是 1111 结尾，所以可以提高遍历速度
            // 最后的效果也是 0 1 2 3 4 0 1 2 3 4 遍历，因为是位运算，所以应该比取模快
            return executors[idx.getAndIncrement() & executors.length - 1];
        }
    }

    private static final class GenericEventExecutorChooser implements EventExecutorChooser {
        private final AtomicInteger idx = new AtomicInteger();
        private final EventExecutor[] executors;

        GenericEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }

        @Override
        public EventExecutor next() {
            return executors[Math.abs(idx.getAndIncrement() % executors.length)];
        }
    }







    public static void main(String[] args) {
        int a = 4;
        System.out.println(-a);
        System.out.println(a & -a);

        System.out.println(1&8-1);
        System.out.println(2&8-1);
        System.out.println(3&8-1);
        System.out.println(4&8-1);
        System.out.println(5 & 8-1);
        System.out.println(6&8-1);
        System.out.println(7&8-1);
        System.out.println(8&8-1);
        System.out.println(9&8-1);
        System.out.println(10&8-1);
        System.out.println(11&8-1);
        System.out.println(12&8-1);
        System.out.println(13&8-1);
        System.out.println(14&8-1);
        System.out.println(15&8-1);
    }
}
