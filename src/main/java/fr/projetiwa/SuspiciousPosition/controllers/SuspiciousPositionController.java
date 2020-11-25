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

    /**
     * Return the list of suspicious position
     * @return list of suspicious position
     */
    @GetMapping
    public ResponseEntity<List<SuspiciousPosition>> list () {
        return new ResponseEntity<List<SuspiciousPosition>>(suspiciousPositionService.findAll(), HttpStatus.OK);}

    /**
     *  get a suspicious position by his id
      * @param id is the id of a suspicious position
     * @return a suspicious position
     */
    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<SuspiciousPosition> get ( @PathVariable Long id ) {
        if(!suspiciousPositionService.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<SuspiciousPosition>(suspiciousPositionService.getOne(id), HttpStatus.OK);
    }

    /**
     * Create a new suspicious position
     * @param sus is the objet created by the request body content
     * @return the new suspicious position created
     */
    @PostMapping()
    public ResponseEntity<SuspiciousPosition> create(@RequestBody SuspiciousPosition sus) {
        System.out.println(sus);
        return new ResponseEntity<SuspiciousPosition>(suspiciousPositionService.saveAndFlush(sus), HttpStatus.CREATED);
    }

    /**
     * Check if the user with the token has positions identical to suspicious positions
     * @param token
     * @return Success 1 or 0, isSuspicious Boolean True or False
     * @throws JsonProcessingException
     */
    @GetMapping
    @RequestMapping("/isSuspicious")
    public String isSuspicious(@RequestHeader (name="Authorization") String token) throws JsonProcessingException {
        Boolean isSuspicious =Boolean.FALSE;

        //Recuperation des positions
        List<Position> positionList = suspiciousPositionService.getUserPosition(token);

        //Recuperation des positions suspectes
        List<SuspiciousPosition> suspiciousPositionList = suspiciousPositionService.findAll();
        ListIterator<Position> positionListIterator = positionList.listIterator();

        //Verification entre les position suspectes et les positions de l'utilisateur
        while(positionListIterator.hasNext() && !isSuspicious) {
            isSuspicious = suspiciousPositionList.contains(positionListIterator.next());
        }

        //Changement du personState si isSuspicious = True
        if(isSuspicious==Boolean.TRUE){
            Boolean success = suspiciousPositionService.setUserAsCasContact(token);
        }

        return "{\"success\":1,\"isSuspicious\":"+isSuspicious+"}";
    }



}
