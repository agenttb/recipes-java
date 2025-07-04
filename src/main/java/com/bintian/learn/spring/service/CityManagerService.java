package com.bintian.learn.spring.service;

import com.bintian.learn.spring.vo.PersonVO;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.Statement;


@Component
@Slf4j
public class CityManagerService {

    private final SessionFactory sessionFactory;
    private TransactionTemplate template;
    private final EntityManager entityManager;
    public CityManagerService(@Qualifier("h2Fc") SessionFactory sessionFactory, EntityManager entityManager) {
        this.sessionFactory = sessionFactory;
        this.entityManager = entityManager;
    }

    @Transactional(transactionManager = "h2TM", propagation = Propagation.REQUIRED)
    public void addCity(PersonVO personVO) {

        //entityManager.
        Session session = null;
        boolean active = TransactionSynchronizationManager.isActualTransactionActive();
        if (active) {
            session = sessionFactory.getCurrentSession();
            Transaction transaction = session.beginTransaction();
            transaction.commit();
            transaction.rollback();
            session.close();
//            Connection unwrap = session.unwrap(Connection.class);
//            var statemen = unwrap.createStatement();
//            String s = unwrap.nativeSQL("");
//            Statement statement = unwrap.createStatement();
//            statemen.ex
//        } else {
            session = sessionFactory.openSession();
        }

        try {
            session.persist(personVO);
            log.info(personVO.getPersonId() + "");
        }  catch (Exception e) {
            log.error("Insert person error", e);
        } finally {
            session.close();
        }
    }
}
