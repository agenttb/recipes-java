package com.bintian.learn.spring.service;

import com.bintian.learn.spring.vo.PersonVO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Component
@Slf4j
public class CityManagerService {

    private final SessionFactory sessionFactory;

    public CityManagerService(@Qualifier("mysql0Fc") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(transactionManager = "mysql0TM", propagation = Propagation.REQUIRED)
    public void addCity(PersonVO personVO) {
        Session session = sessionFactory.openSession();

        try {
            Serializable save = session.save(personVO);
            log.info(save.toString());
        }  catch (Exception e) {
            log.error("Insert person error", e);
        } finally {
            session.close();
        }
    }
}
