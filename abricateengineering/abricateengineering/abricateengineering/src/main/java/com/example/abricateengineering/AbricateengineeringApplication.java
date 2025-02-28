package com.example.abricateengineering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootApplication
@EnableScheduling
public class AbricateengineeringApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbricateengineeringApplication.class, args);
	}
	@Bean
public ObjectMapper objectMapper() {
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(javaTimeModule);
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return mapper;
}


}
