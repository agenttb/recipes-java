package com.bintian.learn.spring.repository;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepoTest {
    @Autowired
    EntityManager em;
    @Test
    void testQueryList() {
        UserRepository userRepository = new UserRepository(em);
        userRepository.queryList();

    }
}
