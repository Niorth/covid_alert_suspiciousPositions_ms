package fr.projetiwa.SuspiciousPosition.Services;

import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import fr.projetiwa.SuspiciousPosition.repositories.SuspiciousPositionRepository;
import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class SuspiciousPositionServiceTest {
    @Autowired
    private SuspiciousPositionService suspiciousPositionService;
    @MockBean
    private SuspiciousPositionRepository suspiciousPositionRepository;

    @Test
    @DisplayName("Test findById Success")
    void testFindByIdSuccess(){
        SuspiciousPosition mockProduct = new SuspiciousPosition();
        mockProduct.setPositionId(1);
        mockProduct.setPosition_date(new Timestamp(new Date().getTime()));
        mockProduct.setLatitude(1f);
        mockProduct.setLongitude(1f);
        Long id = new Long(1);
        doReturn(Optional.of(mockProduct)).when(suspiciousPositionRepository).findById(id);
        Optional<SuspiciousPosition> returnedObject = suspiciousPositionService.findById(id);
        Assertions.assertTrue(returnedObject.isPresent(), "Was found");
        Assertions.assertSame(returnedObject.get(), mockProduct, "Should be the same");

    }
    @Test
    @DisplayName("Test findAll Success")
    void testFindByAllSuccess(){
        SuspiciousPosition mockProduct = new SuspiciousPosition();
        mockProduct.setPositionId(1);
        mockProduct.setPosition_date(new Timestamp(new Date().getTime()));
        mockProduct.setLatitude(1f);
        mockProduct.setLongitude(1f);
        SuspiciousPosition mockProduct2 = new SuspiciousPosition();
        mockProduct2.setPositionId(2);
        mockProduct2.setPosition_date(new Timestamp(new Date().getTime()));
        mockProduct2.setLatitude(1f);
        mockProduct2.setLongitude(1f);

        doReturn(Arrays.asList(mockProduct,mockProduct2)).when(suspiciousPositionRepository).findAll();
        List<SuspiciousPosition> positions = suspiciousPositionService.findAll();
        Assertions.assertEquals(2,positions.size(), "should return 2");

    }
    @Test
    @DisplayName("Test findById not found")
    void testFindByIdNotFound(){
        SuspiciousPosition mockProduct = new SuspiciousPosition();
        mockProduct.setPositionId(1);
        mockProduct.setPosition_date(new Timestamp(new Date().getTime()));
        mockProduct.setLatitude(1f);
        mockProduct.setLongitude(1f);
        Long id = new Long(1);
        doReturn(Optional.empty()).when(suspiciousPositionRepository).findById(id);
        Optional<SuspiciousPosition> returnedObject = suspiciousPositionService.findById(id);
        Assertions.assertFalse(returnedObject.isPresent(), "Should be not present");
    }

    @Test
    @DisplayName("Test save position")
    void testSave(){
        SuspiciousPosition mockProduct = new SuspiciousPosition();
        mockProduct.setPositionId(1);
        mockProduct.setPosition_date(new Timestamp(new Date().getTime()));
        mockProduct.setLatitude(1f);
        mockProduct.setLongitude(1f);
        Long id = new Long(1);
        doReturn(mockProduct).when(suspiciousPositionRepository).save(any());
        SuspiciousPosition sus = suspiciousPositionService.save(mockProduct);
        Assertions.assertNotNull(sus,"the save shoudn't be null");
        Assertions.assertEquals(1,sus.getPositionId(),"the id should be 1");

    }

}
