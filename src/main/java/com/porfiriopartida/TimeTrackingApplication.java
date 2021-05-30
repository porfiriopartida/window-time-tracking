package com.porfiriopartida;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.porfiriopartida")
@ComponentScan("com.porfiriopartida")
public class TimeTrackingApplication  {
	public static void main(String[] args) {
		SpringApplication.run(TimeTrackingApplication.class, args);
	}
}
