package fr.projetiwa.SuspiciousPosition;

import fr.projetiwa.SuspiciousPosition.controllers.SuspiciousPositionController;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SuspiciousApplicationTests {

	@Autowired
	private SuspiciousPositionController suspiciousPositionController;

	@Test
	void contextLoads()  throws Exception{
		assertThat(suspiciousPositionController).isNotNull();

	}

}
