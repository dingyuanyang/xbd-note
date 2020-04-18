package com.xiaobaiding.note.training_20200418;

public class ThreadStopTest_1 {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("线程进入休眠1000");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("异常处理");
                }
                //这里的代码不被执行
                System.out.println("如果这里的代码是相当重要的逻辑处理，是不会被执行的");
            }
        };
        thread.start();
        System.out.println("线程开始");
        //因为外层休眠时间小于内部时间，意外停止，导致后续代码不能执行
        Thread.sleep(100);
        System.out.println("线程休眠100");
        thread.stop();
        System.out.println("线程停止");
    }
}
