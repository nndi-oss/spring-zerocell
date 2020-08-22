package com.nndi_tech.spring.web.zerocell.examples;

import com.nndi_tech.spring.web.zerocell.SpringZerocellSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ImportAutoConfiguration(SpringZerocellSupport.class)
public class SpringZerocellExamplesApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringZerocellExamplesApplication.class, args);
	}
}
