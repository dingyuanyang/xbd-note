package com.xiaobaiding.note.training_20200414;

/**
 * 测试String 的执行结果
 */
public class StringEquals {

    public static void main(String[] args) {

        String x = "string";
        String y = "str"+"ing";
        String z = new String("string");
        System.out.println("-------------------");
        System.out.println("x=" + x);
        System.out.println("y=" + y);
        System.out.println("z=" + z);
        System.out.println("-------------------");
        System.out.println(x == y);
        System.out.println(x == z);
        System.out.println(x.equals(y));
        System.out.println(x.equals(z));
        System.out.println(x.compareTo(z));
    }
}
