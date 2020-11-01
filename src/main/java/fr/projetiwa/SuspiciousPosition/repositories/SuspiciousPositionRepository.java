package fr.projetiwa.SuspiciousPosition.repositories;


import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuspiciousPositionRepository extends JpaRepository<SuspiciousPosition,Long> { }