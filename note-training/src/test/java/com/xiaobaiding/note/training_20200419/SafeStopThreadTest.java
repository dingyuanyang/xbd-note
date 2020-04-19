package com.xiaobaiding.note.training_20200419;


class SafeStopThreadTest {
    public static void main(String[] args) throws InterruptedException {
        SafeStopThread safeStopThread = new SafeStopThread();
        Thread thread = new Thread(safeStopThread);
        thread.start();
        for (int i = 0; i < 5; i++) {
//            //不共享变量， 不影响
//            UnSafeThread thread2 = new UnSafeThread();
//            new Thread(thread2).start();
            //新的线程会共享变量
            new Thread(thread).start();
        }
        Thread.sleep(100);
        safeStopThread.terminated();
    }
}