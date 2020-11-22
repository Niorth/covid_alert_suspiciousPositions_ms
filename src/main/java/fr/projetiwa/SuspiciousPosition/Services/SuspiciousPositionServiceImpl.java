package fr.projetiwa.SuspiciousPosition.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.projetiwa.SuspiciousPosition.models.Position;
import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import fr.projetiwa.SuspiciousPosition.repositories.SuspiciousPositionRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SuspiciousPositionServiceImpl implements SuspiciousPositionService{

    @Autowired
    private RestTemplate restTemplate;

    private final SuspiciousPositionRepository suspiciousPositionRepository;

    public SuspiciousPositionServiceImpl(SuspiciousPositionRepository suspiciousPositionRepository){
        this.suspiciousPositionRepository = suspiciousPositionRepository;
    }
    @Override
    public Optional<SuspiciousPosition> findById(Long id) {
        return suspiciousPositionRepository.findById(id);
    }

    @Override
    public List<SuspiciousPosition> findAll() {
        return suspiciousPositionRepository.findAll();
    }

    @Override
    public boolean existsById(long id) {
        return suspiciousPositionRepository.existsById(id);
    }

    @Override
    public SuspiciousPosition getOne(long id) {
        return suspiciousPositionRepository.getOne(id);
    }

    @Override
    public SuspiciousPosition saveAndFlush(SuspiciousPosition sus) {
        return suspiciousPositionRepository.saveAndFlush(sus);
    }


    @Override
    public SuspiciousPosition save(SuspiciousPosition suspiciousPosition) {

        return suspiciousPositionRepository.save(suspiciousPosition);
    }

    @Override
    public List<Position> getUserPosition(String token) throws JsonProcessingException {
        String urlPositions = "http://localhost:3003/positions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(HttpHeaders.AUTHORIZATION, token);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange( urlPositions, HttpMethod.GET, request, String.class, 1 );
        List<Position> positionList = new ObjectMapper().readValue(response.getBody(), new TypeReference<List<Position>>() {});
        return positionList;
    }


    /*
    Return True if the response success equals 1
     */
    @Override
    public Boolean setUserAsCasContact(String token) {
        String urlPersonState = "http://localhost:3002/personState/update";
        HttpHeaders headersPersonState = new HttpHeaders();
        headersPersonState.setContentType(MediaType.APPLICATION_JSON);
        headersPersonState.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headersPersonState.add(HttpHeaders.AUTHORIZATION, token);
        String body = "{\"stateId\":0}";
        HttpEntity requestPersonState = new HttpEntity(body,headersPersonState);
        ResponseEntity<String> response = restTemplate.exchange( urlPersonState, HttpMethod.POST, requestPersonState, String.class, 1 );
        JSONObject jsonObject = new JSONObject(response.getBody());
        Integer success = jsonObject.getInt("success");
        return success.equals(1);
    }
}
