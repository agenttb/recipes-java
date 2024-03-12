package com.bintian.learn.spring.service;

import com.bintian.learn.spring.hibernate.entity.Course;
import com.bintian.learn.spring.hibernate.entity.CourseSelection;
import com.bintian.learn.spring.hibernate.entity.Student;
import com.bintian.learn.spring.vo.PersonVO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CityManagerService {
    private static final Logger logger = LoggerFactory.getLogger(CityManagerService.class);
    private final SessionFactory sessionFactory;


    public CityManagerService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void initData() {

    }

    //@Transactional(transactionManager = "mysql0TM", propagation = Propagation.REQUIRED)
    public void addCity(PersonVO personVO) {
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
        try {
            session.persist(personVO);
            log.info(personVO.toString());
        }  catch (Exception e) {
            log.error("Insert person error", e);
        } finally {
            session.close();
        }
    }

    public void addStudent(Student student) {
        Session session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.persist(student);
        logger.info("Saved, id = {}", student.getId());
        session.flush();
        transaction.commit();
        session.close();
    }

    public void addCourse(Course course) {
        Session session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.persist(course);
        logger.info("Saved, id = {}", course.getId());
        session.flush();
        transaction.commit();
        session.close();
    }

    public void addCourseSelection(CourseSelection selection) {
        Session session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.persist(selection);
        logger.info("Saved, id = {}", selection.getId());
        session.flush();
        transaction.commit();
        session.close();
    }

    public List<CourseSelection.CourseSelectionInfo> getCourseSelectionInfoList() {
        Session session = sessionFactory.openSession();
        String sql = "select t1.name,  t3.courseName from Student as t1 join CourseSelection as t2 on t1.studentNbr = t2.studentNbr join Course as t3 on t2.courseNbr = t3.courseNbr";
        var query = session.createSelectionQuery(sql, CourseSelection.CourseSelectionInfo.class);
        var resList = query.getResultList();
        return resList;
    }
}
