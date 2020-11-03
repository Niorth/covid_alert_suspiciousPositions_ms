package fr.projetiwa.SuspiciousPosition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SuspiciousPositionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuspiciousPositionApplication.class, args);
	}

}
