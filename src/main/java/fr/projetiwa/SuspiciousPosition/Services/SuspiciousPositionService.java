package fr.projetiwa.SuspiciousPosition.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.projetiwa.SuspiciousPosition.models.Position;
import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import fr.projetiwa.SuspiciousPosition.repositories.SuspiciousPositionRepository;
import org.springframework.stereotype.Service;

import javax.swing.plaf.SeparatorUI;
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

    void setUserAsCasContact(String token);


}
