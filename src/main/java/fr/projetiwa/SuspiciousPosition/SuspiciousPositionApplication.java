package fr.projetiwa.SuspiciousPosition;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SuspiciousPositionApplication {

	public static void main(String[] args) {

		SpringApplication.run(SuspiciousPositionApplication.class, args);
	}

	@Bean
	public StringJsonMessageConverter jsonConverter() {
		return new StringJsonMessageConverter();
	}

}
