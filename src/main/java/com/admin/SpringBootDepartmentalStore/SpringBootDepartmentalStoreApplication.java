package com.admin.SpringBootDepartmentalStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.admin.SpringBootDepartmentalStore.repository")
@ComponentScan("com.admin.SpringBootDepartmentalStore")
public class SpringBootDepartmentalStoreApplication {

	public static void main(final String[] args) {
		SpringApplication.run(SpringBootDepartmentalStoreApplication.class, args);
	}

}
