package com.xiaobaiding.note.training_20200610;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o!=null && o.getClass() == this.getClass()) {
            Person person = (Person) o;
            if (person.getName() == null || name == null) {
                return false;
            } else {
                return name.equalsIgnoreCase(person.getName().trim());
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Person p1 = new Person("小白丁");
        Person p2 = new Person(null);
        List<Person> list = new ArrayList<Person>(1);
        list.add(p1);
        list.add(p2);
        System.out.println("是否包含小白丁：" + list.contains(p1));
        System.out.println("是否包含小白丁：" + list.contains(p2));

    }
}
