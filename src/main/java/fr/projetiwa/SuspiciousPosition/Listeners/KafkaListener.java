package fr.projetiwa.SuspiciousPosition.Listeners;

import fr.projetiwa.SuspiciousPosition.models.SuperClassPosition;
import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import fr.projetiwa.SuspiciousPosition.repositories.SuspiciousPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class KafkaListener {

    @Autowired
    private SuspiciousPositionRepository suspiciousPositionRepository;

    @org.springframework.kafka.annotation.KafkaListener(topics = "addSusPosition", groupId = "addSusPosition")
    public void addNewSuspiciousPosition(SuperClassPosition sus) {
        SuspiciousPosition posi = (SuspiciousPosition) sus;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(posi.getPositionDate() != null && posi.getLatitude() != null && posi.getLongitude() != null) {
            SuspiciousPosition personResultAsJsonStr = restTemplate.postForObject("http://localhost:3005/suslocation", posi, SuspiciousPosition.class);
        }


       // System.out.println("Received Message in group foo: " + personResultAsJsonStr.getLatitude());
    }
}
