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
        return new ResponseEntity<SuspiciousPosition>(suspiciousPositionService.saveAndFlush(sus), HttpStatus.CREATED);
    }

    @GetMapping
    @RequestMapping("/isSuspicious")
    public String isSuspicious(@RequestHeader (name="Authorization") String token) throws JsonProcessingException {

        String payload = token.split("\\.")[1];

        try{
            String str = new String(Base64.decodeBase64(payload),"UTF-8");
            JSONObject jsonObject = new JSONObject(str);
            String personId = jsonObject.getString("sub");

        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }


        //Recuperation des positions
        String urlPositions = "http://localhost:3003/positions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(HttpHeaders.AUTHORIZATION, token);
        //System.out.println(headers);
        HttpEntity request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        String serviceUrl = urlPositions;
        ResponseEntity<String> response = restTemplate.exchange( serviceUrl, HttpMethod.GET, request, String.class, 1 );
        List<Position> positionList = new ObjectMapper().readValue(response.getBody(), new TypeReference<List<Position>>() {});

        //System.out.println(positionList.toString());

        //Recuperation des positions suspectes
        List<SuspiciousPosition> suspiciousPositionList = suspiciousPositionService.findAll();
        //List<SuperClassPosition> abc = (List<SuperClassPosition>)(SuspiciousPosition) suspiciousPositionList;
        /*SuspiciousPosition sp = new SuspiciousPosition();
        sp.setLatitude((float) 43.6016860000000000);
        sp.setLongitude((float) 3.9114860000000000);
        suspiciousPositionList.add(sp);*/
        for(Position position : positionList){
            System.out.println(suspiciousPositionList.contains(position));
        }


        //System.out.println(response.getBody());
        //return "{\"success\":1}";
        return response.getBody();
    }



}
