package fr.projetiwa.SuspiciousPosition.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.projetiwa.SuspiciousPosition.models.Position;
import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SuspiciousPositionService {
    Optional<SuspiciousPosition> findById(Long id);

    List<SuspiciousPosition> findAll();

    boolean existsById(long id);

    SuspiciousPosition getOne(long id);
    SuspiciousPosition saveAndFlush(SuspiciousPosition sus);

    SuspiciousPosition save(SuspiciousPosition suspiciousPosition);

    List<Position> getUserPosition(String token) throws JsonProcessingException;

    Boolean setUserAsCasContact(String token);


}
