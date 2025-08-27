package com.rca.mis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
}, scanBasePackages = {
    "com.rca.mis.config",
    "com.rca.mis.util",
    "com.rca.mis.controller",
    "com.rca.mis.service.impl"
})
@EnableJpaRepositories(basePackages = "com.rca.mis.repository")
@EntityScan(basePackages = "com.rca.mis.model")
public class RwandaCodingAcademyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RwandaCodingAcademyApplication.class, args);

	}

}
