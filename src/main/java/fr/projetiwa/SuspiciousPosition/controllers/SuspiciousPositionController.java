package fr.projetiwa.SuspiciousPosition.controllers;


import fr.projetiwa.SuspiciousPosition.models.Person;
import fr.projetiwa.SuspiciousPosition.models.SuperClassPosition;
import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import fr.projetiwa.SuspiciousPosition.repositories.SuspiciousPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.Console;
import java.util.*;

@RestController
@RequestMapping("/suslocation")
public class SuspiciousPositionController {

    @Autowired
    private SuspiciousPositionRepository susRepository ;



    String serviceUrl = "http://localhost:3000/person";

    public String callGetService(int id) {

        List<Person> obj=  new RestTemplate().getForObject(serviceUrl, List.class);

// create headers
        HttpHeaders headers = new HttpHeaders();

// set `Content-Type` and `Accept` headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

// example of custom header
        headers.set("X-Request-Source", "Desktop");

// build the request
        HttpEntity request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();



        ResponseEntity<Person[]> response = restTemplate.exchange(
                serviceUrl,
                HttpMethod.GET,
                request,
                Person[].class,
                1
        );

        Person[] objects = response.getBody();
        List<Person> listObjects = Arrays.asList(objects);
        System.out.println(listObjects.get(0));
        return "test";
    }

    @GetMapping
    public List<SuspiciousPosition> list () {
        //callGetService(1);
        return susRepository.findAll(); }
    @GetMapping
    @RequestMapping("{id}")
    public SuspiciousPosition get ( @PathVariable Long id ) {
        return susRepository.getOne(id); }
    @PostMapping
    @ResponseStatus ( HttpStatus.CREATED )
    public SuspiciousPosition create(@RequestBody final SuspiciousPosition sus) {
        return susRepository.saveAndFlush((SuspiciousPosition)sus); }


}
