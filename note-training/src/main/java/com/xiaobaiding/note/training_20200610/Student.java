package com.xiaobaiding.note.training_20200610;

public class Student extends Person {
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student(String name) {
        super(name);
    }

    public Student(String name, int id) {
        super(name);
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Student){
            Student stu = (Student)obj;
            return super.equals(obj) && stu.getId() == id;
        }
        return super.equals(obj);
    }

    public static void main(String[] args) {
        Person p1 = new Person("小白丁");
        Student s1 = new Student("小白丁",1);
        Student s2 = new Student("小白丁",123);
        System.out.println(p1.equals(s1));
        System.out.println(p1.equals(s2));
        System.out.println(s1.equals(s2));
    }
}
