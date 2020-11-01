package fr.projetiwa.SuspiciousPosition.controllers;


import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import fr.projetiwa.SuspiciousPosition.repositories.SuspiciousPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suslocation")
public class SuspiciousPositionController {

    @Autowired
    private SuspiciousPositionRepository susRepository ;

    @GetMapping
    public List<SuspiciousPosition> list () {
        return susRepository.findAll(); }
    @GetMapping
    @RequestMapping("{id}")
    public SuspiciousPosition get ( @PathVariable Long id ) {
        return susRepository.getOne(id); }
    @PostMapping
    @ResponseStatus ( HttpStatus.CREATED )
    public SuspiciousPosition create(@RequestBody final SuspiciousPosition sus) {
        return susRepository.saveAndFlush(sus); }


}
