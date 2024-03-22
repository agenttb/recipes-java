package com.bintian.learn.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.TimeUnit;

@SpringBootApplication(scanBasePackages = "com.bintian.learn.spring", exclude = {DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class})
public class SpringDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDemoApplication.class, args);
	}

}
