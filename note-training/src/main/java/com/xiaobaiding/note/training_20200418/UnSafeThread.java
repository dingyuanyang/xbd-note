package com.xiaobaiding.note.training_20200418;

public class UnSafeThread implements Runnable {
    int a = 0;
    @Override
    public void run() {
        synchronized (""){
            String name = Thread.currentThread().getName();
            System.out.println(name + ":开始时\ta=" + a);
            a++;
            try {
                System.out.println(name+"线程内休眠1000\ta="+a);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("异常处理");
            }
            a--;
            System.out.println(name + ":结束线程后\ta=" + a);
        }
    }
}
