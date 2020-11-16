package fr.projetiwa.SuspiciousPosition.controllers;


import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import fr.projetiwa.SuspiciousPosition.repositories.SuspiciousPositionRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/suslocation")
public class SuspiciousPositionController {

    @Autowired
    KafkaConsumer<String,SuspiciousPosition> consumer;



    @Autowired
    private SuspiciousPositionRepository susRepository ;



    @Autowired
    KafkaTemplate<String,SuspiciousPosition> kafkaTemplate;

    private static final String TOPIC = "blabla";



    @GetMapping
    public ResponseEntity<List<SuspiciousPosition>> list () {
        return new ResponseEntity<List<SuspiciousPosition>>(susRepository.findAll(), HttpStatus.OK);}
    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<SuspiciousPosition> get ( @PathVariable Long id ) {
        if(!susRepository.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<SuspiciousPosition>(susRepository.getOne(id), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<SuspiciousPosition> create(@RequestBody SuspiciousPosition sus) {
        return new ResponseEntity<SuspiciousPosition>(susRepository.saveAndFlush(sus), HttpStatus.OK);}


}
