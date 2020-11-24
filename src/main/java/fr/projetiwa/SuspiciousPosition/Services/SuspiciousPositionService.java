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
    /**
     * Return the suspicious position linked with the id / maybe empty
     * @param id is the suspicious position Id
     * @return the suspicious position
     */
    Optional<SuspiciousPosition> findById(Long id);

    /**
     * Return all suspicious position
     * @return a list of suspicious position
     */
    List<SuspiciousPosition> findAll();

    /**
     * Verify if a suspicious position exist with this id
     * @param id is the identifier of a suspicious position
     * @return true if the id is liked with a suspicious position
     */
    boolean existsById(long id);

    /**
     * Return the suspicious position linked with the id (must exist)
     * @param id id of the suspicious position
     * @return
     */
    SuspiciousPosition getOne(long id);

    /**
     * To save the new suspicious position on the DB and flush
     * @param sus is the suspicious position to save
     * @return the suspicious position saved
     */
    SuspiciousPosition saveAndFlush(SuspiciousPosition sus);
    /**
     * To save the new suspicious position on the DB
     * @param suspiciousPosition is the suspicious position to save
     * @return the suspicious position saved
     */
    SuspiciousPosition save(SuspiciousPosition suspiciousPosition);


    List<Position> getUserPosition(String token) throws JsonProcessingException;

    Boolean setUserAsCasContact(String token);


}
