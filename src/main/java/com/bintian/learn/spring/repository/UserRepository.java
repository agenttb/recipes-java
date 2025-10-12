package com.bintian.learn.spring.repository;

import com.bintian.learn.spring.hibernate.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final EntityManager em;
    public UserRepository(EntityManager em) {
        this.em = em;
    }

    public void queryList() {
        TypedQuery<Student> query = em.createQuery("from Student where name = :name", Student.class);
        query.setParameter("name", "tianbin");
        List<Student> resultList = query.getResultList();
        for (Student student : resultList) {
            System.out.println(student);
        }
    }
}
