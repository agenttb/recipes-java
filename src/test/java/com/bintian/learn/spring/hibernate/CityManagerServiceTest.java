package com.bintian.learn.spring.hibernate;

import com.bintian.learn.spring.hibernate.entity.Course;
import com.bintian.learn.spring.hibernate.entity.CourseSelection;
import com.bintian.learn.spring.hibernate.entity.Student;
import com.bintian.learn.spring.service.CityManagerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

@SpringBootTest
class CityManagerServiceTest {
    @Autowired
    private CityManagerService cityManagerService;

    @Test
    void testHibernateResultToRecordClazz() {
        Student student = new Student();
        student.setAge("12");
        student.setSex("male");
        student.setName("erick");
        student.setStudentNbr("100608101");

        Course course = new Course();
        course.setCourseName("Chinese history");
        course.setTeacherName("Jack Ma");
        course.setCourseNbr("C0089");

        CourseSelection selection = new CourseSelection();
        selection.setCourseDate(new Date(System.currentTimeMillis()));
        selection.setCourseNbr("C0089");
        selection.setStudentNbr("100608101");

        cityManagerService.addStudent(student);
        cityManagerService.addCourse(course);
        cityManagerService.addCourseSelection(selection);

        var rs = cityManagerService.getCourseSelectionInfoList();
        Assertions.assertEquals(1, rs.size());
    }
}
