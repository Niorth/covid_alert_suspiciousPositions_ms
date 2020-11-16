package fr.projetiwa.SuspiciousPosition.Services;

import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class KafkaTest {

    @Autowired
    KafkaConsumer<String,SuspiciousPosition> consumer;
    @Autowired
    KafkaTemplate<String,SuspiciousPosition> kafkaTemplate;

    @Test
    public void test() throws Exception{
        long randomNum = 1 + (long)(Math.random() * 20);
        SuspiciousPosition sus = new SuspiciousPosition();
        sus.setLongitude(1f);
        sus.setLatitude(1f);
        sus.setPositionId(randomNum);

        sus.setPosition_date(new Timestamp((new Date().getTime())));
        kafkaTemplate.send("testKafka",sus);
        consumer.subscribe(Arrays.asList("testKafka"));
        List<SuspiciousPosition> list = new ArrayList<>();
            ConsumerRecords<String, SuspiciousPosition> records = consumer.poll(5000);
            for (ConsumerRecord<String, SuspiciousPosition> record : records)
                list.add(record.value());

        assertThat(list.get(list.size()-1).getPositionId() == sus.getPositionId()).isTrue();








    }

}
