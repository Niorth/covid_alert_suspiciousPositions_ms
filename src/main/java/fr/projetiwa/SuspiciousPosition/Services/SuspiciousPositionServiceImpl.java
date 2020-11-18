package fr.projetiwa.SuspiciousPosition.Services;

import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import fr.projetiwa.SuspiciousPosition.repositories.SuspiciousPositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuspiciousPositionServiceImpl implements SuspiciousPositionService{
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
}
