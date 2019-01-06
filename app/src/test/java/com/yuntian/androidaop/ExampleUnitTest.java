package com.yuntian.androidaop;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test1() {
        boolean isCan = false;
        long startTime = System.currentTimeMillis();
        System.out.println("打电话送煤气");
        try {
            Thread.sleep(3000);
            Random random = new Random();
            int result = random.nextInt(100);
            if (result % 2 == 0) {
                isCan = true;
                System.out.println("煤气送到了");
            } else {
                System.out.println("煤气没送到了");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Person person = new Person();
        person.buy();

        person.cook(isCan);
        System.out.println("总共用时" + (System.currentTimeMillis() - startTime) + "ms");
    }


    @Test
    public void test2() {
        final boolean[] isCan = {false};
        long startTime = System.currentTimeMillis();
        System.out.println("打电话送煤气");
        Thread threadGas = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Random random = new Random();
                    int result = random.nextInt(2);
                    if (result == 0) {
                        isCan[0] = true;
                        System.out.println("煤气送到了");
                    } else {
                        isCan[0] = false;
                        System.out.println("煤气没送到了");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadGas.start();
        Person person = new Person();
        person.buy();
        try {
            threadGas.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //无法晓得煤气是否送到
        person.cook(isCan[0]);
        System.out.println("总共用时" + (System.currentTimeMillis() - startTime) + "ms");

    }

    @Test
    public void test3() {
        boolean isCan = false;
        long startTime = System.currentTimeMillis();
        System.out.println("打电话送煤气");
        FutureTask<Boolean> futureTask = new FutureTask<Boolean>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Thread.sleep(2000);
                    Random random = new Random();
                    int result = random.nextInt(2);
                    if (result == 0) {
                        System.out.println("煤气送到了");
                        return true;
                    } else {
                        System.out.println("煤气没送到了");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("煤气没送到了");
                }
                return false;
            }
        });
        new Thread(futureTask).start();
        Person person = new Person();
        person.buy();
        try {
            isCan = futureTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        person.cook(isCan);
        System.out.println("总共用时" + (System.currentTimeMillis() - startTime) + "ms");

    }
}