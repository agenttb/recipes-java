package com.bintian.learn.spring.hibernate.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "STUDENT")
public class Student {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "SEX")
    private String sex;
    @Column(name = "AGE")
    private String age;
    @Column(name = "STUDENT_NBR")
    private String studentNbr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStudentNbr() {
        return studentNbr;
    }

    public void setStudentNbr(String studentNbr) {
        this.studentNbr = studentNbr;
    }
}
