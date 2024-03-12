package com.bintian.learn.spring.hibernate.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name= "COURSE")
@Data
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "COURSE_NAME")
    private String courseName;

    @Column(name = "TEACHER_NAME")
    private String teacherName;

    @Column(name = "COURSE_NBR")
    private String courseNbr;
}
