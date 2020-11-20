package fr.projetiwa.SuspiciousPosition.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.projetiwa.SuspiciousPosition.Services.SuspiciousPositionService;
import fr.projetiwa.SuspiciousPosition.models.Position;
import fr.projetiwa.SuspiciousPosition.models.SuperClassPosition;
import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import fr.projetiwa.SuspiciousPosition.repositories.SuspiciousPositionRepository;
import org.apache.commons.codec.binary.Base64;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/suslocation")
public class SuspiciousPositionController {

    @Autowired
    KafkaConsumer<String,SuspiciousPosition> consumer;

    @Autowired
    private HttpServletRequest request;


    @Autowired
    private SuspiciousPositionService suspiciousPositionService;



    @Autowired
    KafkaTemplate<String,SuspiciousPosition> kafkaTemplate;

    private static final String TOPIC = "blabla";



    @GetMapping
    public ResponseEntity<List<SuspiciousPosition>> list () {
        return new ResponseEntity<List<SuspiciousPosition>>(suspiciousPositionService.findAll(), HttpStatus.OK);}
    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<SuspiciousPosition> get ( @PathVariable Long id ) {
        if(!suspiciousPositionService.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<SuspiciousPosition>(suspiciousPositionService.getOne(id), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<SuspiciousPosition> create(@RequestBody SuspiciousPosition sus) {
        System.out.println(sus);
        return new ResponseEntity<SuspiciousPosition>(suspiciousPositionService.saveAndFlush(sus), HttpStatus.CREATED);
    }

    @GetMapping
    @RequestMapping("/isSuspicious")
    public String isSuspicious(@RequestHeader (name="Authorization") String token) throws JsonProcessingException {
        Boolean isSuspicious =Boolean.FALSE;

        //Recuperation des positions
        String urlPositions = "http://localhost:3003/positions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(HttpHeaders.AUTHORIZATION, token);
        HttpEntity request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange( urlPositions, HttpMethod.GET, request, String.class, 1 );
        List<Position> positionList = new ObjectMapper().readValue(response.getBody(), new TypeReference<List<Position>>() {});

        //Recuperation des positions suspectes
        List<SuspiciousPosition> suspiciousPositionList = suspiciousPositionService.findAll();
        ListIterator<Position> positionListIterator = positionList.listIterator();

        //Verification entre les position suspectes et les positions de l'utilisateur
        while(positionListIterator.hasNext() && !isSuspicious) {
            isSuspicious = suspiciousPositionList.contains(positionListIterator.next());
        }

        //Changement du personState si isSuspicious = True
        if(isSuspicious==Boolean.TRUE){
            String urlPersonState = "http://localhost:3002/personState/update";
            HttpHeaders headersPersonState = new HttpHeaders();
            headersPersonState.setContentType(MediaType.APPLICATION_JSON);
            headersPersonState.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headersPersonState.add(HttpHeaders.AUTHORIZATION, token);
            String body = "{\"stateId\":0}";
            HttpEntity requestPersonState = new HttpEntity(body,headersPersonState);
            RestTemplate restTemplatePersonState = new RestTemplate();
            ResponseEntity<String> response2 = restTemplatePersonState.exchange( urlPersonState, HttpMethod.POST, requestPersonState, String.class, 1 );
        }

        return "{\"success\":1,\"isSuspicious\":"+isSuspicious+"}";
    }



}
