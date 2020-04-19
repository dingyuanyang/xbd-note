package com.xiaobaiding.note.training_20200419;

public class InterruptTest {

    public static void main(String[] args) {
        Thread thread  = new Thread(){
            @Override
            public void run() {
                //线程不停止
               while (!isInterrupted()){
                   System.out.println("Running …………");
               }
            }
        };
        thread.start();
        System.out.println(thread.isInterrupted());
        thread.interrupt();
        System.out.println(thread.isInterrupted());
    }
}
