package com.timing.serialize.core;


import org.junit.Test;

import java.io.Serializable;

public class ObjectUtilTest {

    class Student implements Serializable {
        private String name;
        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    public void test() {

        Student student = new Student();
        student.setName("Timing");

        byte[] bytes = ObjectUtil.serialize(student);
        System.out.println("serialize: " + new String(bytes));

        Student des = ObjectUtil.unserialize(bytes);
        System.out.println("deseric: " + des.getName());


    }
}