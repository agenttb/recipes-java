package com.bintian.learn.spring.service;

import com.bintian.learn.spring.vo.PersonVO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;

@Component
@Slf4j
public class CityManagerService {

    private final SessionFactory sessionFactory;

    @Qualifier("mysql0Ds")
    @Autowired
    private  DataSource dataSource;

    public CityManagerService(@Qualifier("mysql0Fc") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void getConnection() {
        Connection connection = DataSourceUtils.getConnection(dataSource);
    }

    @Transactional(transactionManager = "mysql0TM", propagation = Propagation.REQUIRED)
    public void addCity(PersonVO personVO) {
        Session session = sessionFactory.getCurrentSession();

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
