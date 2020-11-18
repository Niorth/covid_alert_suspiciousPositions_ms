package fr.projetiwa.SuspiciousPosition.Repository;


import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import fr.projetiwa.SuspiciousPosition.repositories.SuspiciousPositionRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({DBUnitExtension.class,
        SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
public class SuspiciousRepositoryTest {
    @Autowired
    private DataSource dataSource;
    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }
    @Autowired
    private SuspiciousPositionRepository repository;
    @Test
    @DataSet("suspiciousPosition.yml")
    void testFindAll() {
        List<SuspiciousPosition> suspiciousPositions =
                Lists.newArrayList(repository.findAll());
        Assertions.assertEquals(2, suspiciousPositions.size(),
                "Expected 2 positions in the database");
    }
    @Test
    @DataSet("suspiciousPosition.yml")
    void testFindOne() {
        Long id = new Long(1);
        SuspiciousPosition suspiciousPosition = repository.getOne(id);
        assertThat(suspiciousPosition).isNotNull();
    }
    @Test
    @DataSet("suspiciousPosition.yml")
    void testFindAllNotEqual() {
        List<SuspiciousPosition> suspiciousPositions =
                Lists.newArrayList(repository.findAll());
        assertThat( suspiciousPositions.size()== 1).isFalse();

    }
}