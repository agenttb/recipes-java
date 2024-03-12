package com.bintian.learn.spring.hibernate.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "COURSE_SELECTION")
@Data
public class CourseSelection {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name= "STUDENT_NBR")
    private String studentNbr;

    @Column(name = "COURSE_NBR")
    private String courseNbr;

    @Column(name = "COURSE_DATE")
    private Date courseDate;

    public record CourseSelectionInfo(String studentName, String courseName) {

    }

}

