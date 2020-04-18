package com.xiaobaiding.note.training_20200418.test;

import com.xiaobaiding.note.training_20200418.UnSafeThread;

class UnSafeThreadTest {

    public static void main(String[] args) throws InterruptedException {
        UnSafeThread unSafeThread = new UnSafeThread();
        Thread thread = new Thread(unSafeThread);
        thread.start();
        for (int i = 0; i < 5; i++) {
//            //不共享变量， 不影响
//            UnSafeThread thread2 = new UnSafeThread();
//            new Thread(thread2).start();
            //新的线程会共享变量
            new Thread(thread).start();
        }
        Thread.sleep(100);
        thread.stop();
    }
}