package com.xiaobaiding.note.training_20200419;


import java.util.Timer;
import java.util.TimerTask;

class SafeStopThreadByInterruptedTest {
    public static void main(String[] args) throws InterruptedException {
        SafeStopThreadByInterrupted thread = new SafeStopThreadByInterrupted();
        thread.start();
        //定义计时器，0.5秒后线程停止
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                thread.interrupt();
            }
        },500);
    }
}

class SafeStopThreadByInterrupted extends Thread {

    @Override
    public void run() {
        while (!isInterrupted()) {
            System.out.println("Running …………");
        }
    }
}