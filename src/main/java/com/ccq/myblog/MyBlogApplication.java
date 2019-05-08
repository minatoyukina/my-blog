package com.ccq.myblog;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyBlogApplication {
    @Bean
    public Module dataTypeHibernateModule() {
        return new Hibernate5Module();
    }

    public static void main(String[] args) {
        SpringApplication.run(MyBlogApplication.class, args);
    }


}

