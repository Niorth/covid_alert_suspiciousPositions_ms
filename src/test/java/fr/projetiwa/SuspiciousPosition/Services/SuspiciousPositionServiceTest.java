package fr.projetiwa.SuspiciousPosition.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.projetiwa.SuspiciousPosition.models.Position;
import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import fr.projetiwa.SuspiciousPosition.repositories.SuspiciousPositionRepository;
import fr.projetiwa.SuspiciousPosition.util.Function;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import java.sql.Timestamp;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;



@SpringBootTest
public class SuspiciousPositionServiceTest {
    @Autowired
    private SuspiciousPositionService suspiciousPositionService;
    @MockBean
    private SuspiciousPositionRepository suspiciousPositionRepository;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    /**
     * To test the service of FindById
     */
    @Test
    @DisplayName("Test findById Success")
    void testFindByIdSuccess(){
        SuspiciousPosition mockProduct = new SuspiciousPosition(1f,new Timestamp(new Date().getTime()),1f);
        mockProduct.setPositionId(1);

        Long id = new Long(1);
        doReturn(Optional.of(mockProduct)).when(suspiciousPositionRepository).findById(id);
        Optional<SuspiciousPosition> returnedObject = suspiciousPositionService.findById(id);
        Assertions.assertTrue(returnedObject.isPresent(), "Was found");
        Assertions.assertSame(returnedObject.get(), mockProduct, "Should be the same");

    }
    /**
     * To test the service of FindByAll
     */
    @Test
    @DisplayName("Test findAll Success")
    void testFindByAllSuccess(){
        SuspiciousPosition mockProduct = new SuspiciousPosition(1f,new Timestamp(new Date().getTime()),1f);
        mockProduct.setPositionId(1);

        SuspiciousPosition mockProduct2 = new SuspiciousPosition(1f,new Timestamp(new Date().getTime()),1f);
        mockProduct2.setPositionId(2);

        doReturn(Arrays.asList(mockProduct,mockProduct2)).when(suspiciousPositionRepository).findAll();
        List<SuspiciousPosition> positions = suspiciousPositionService.findAll();
        Assertions.assertEquals(2,positions.size(), "should return 2");

    }
    /**
     * To test the service of FindById when the id is incorrect
     */
    @Test
    @DisplayName("Test findById not found")
    void testFindByIdNotFound(){
        SuspiciousPosition mockProduct = new SuspiciousPosition(1f,new Timestamp(new Date().getTime()),1f);
        mockProduct.setPositionId(1);
        Long id = new Long(1);
        doReturn(Optional.empty()).when(suspiciousPositionRepository).findById(id);
        Optional<SuspiciousPosition> returnedObject = suspiciousPositionService.findById(id);
        Assertions.assertFalse(returnedObject.isPresent(), "Should be not present");
    }
    /**
     * To test the service of adding a new suspicious position
     */
    @Test
    @DisplayName("Test save position")
    void testSave(){
        SuspiciousPosition mockProduct = new SuspiciousPosition(1f,new Timestamp(new Date().getTime()),1f);
        mockProduct.setPositionId(1);

        Long id = new Long(1);
        doReturn(mockProduct).when(suspiciousPositionRepository).save(any());
        SuspiciousPosition sus = suspiciousPositionService.save(mockProduct);
        Assertions.assertNotNull(sus,"the save shoudn't be null");
        Assertions.assertEquals(1,sus.getPositionId(),"the id should be 1");

    }


    @Test
    @DisplayName("Test getUserPosition")
    void testGetUserPosition() throws JsonProcessingException {
        RestGatewaySupport gateway = new RestGatewaySupport();
        gateway.setRestTemplate(restTemplate);
        mockServer = MockRestServiceServer.createServer(gateway);


        List<Position> mockProduct = new ArrayList<Position>();
        Position mockPosition = new Position(10.0563f,new Timestamp(new Date().getTime()),60.0563f,"5030a8a3-bb8c-42d0-87e2-030d7181a0cd", 30.0f);
        mockProduct.add(mockPosition);

        mockServer.expect(once(), requestTo("http://localhost:3003/positions"))
                .andRespond(withSuccess(Function.asJsonString(mockProduct), MediaType.APPLICATION_JSON));

        String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI1VmxhaXlBMGVaZW9rVnl4STFMT25tc1FEM3pjME1wajVPYUdGbnZHYmZFIn0.eyJleHAiOjE2MDU4OTgxNDQsImlhdCI6MTYwNTg5Nzg0NCwiYXV0aF90aW1lIjoxNjA1ODk3ODQzLCJqdGkiOiIwMzA5MTBmYy03YTE4LTQ3ZmQtOTc0Yi0zY2FhMmViZTA0MTUiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC9yZWFsbXMvY292aWQtYWxlcnQiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiNTAzMGE4YTMtYmI4Yy00MmQwLTg3ZTItMDMwZDcxODFhMGNkIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidXNlci1hcHAiLCJub25jZSI6IjliYTU0ZmFjLTE4OGQtNDA3NC04MzI2LTIwMTk0ODY4MjZhNyIsInNlc3Npb25fc3RhdGUiOiJkZDEwNzFmZS1hZDdlLTQ3MzktODQ0Ni05MWQxNjNkZjRjZWIiLCJhY3IiOiIwIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoiZm9vIn0.bajbyUvmnAp9fLVMAmQrs_EIdoSv4rWtNKATuVHJmpC4TZgvUsSSGs7fhJQHjE6Dp5FaCwbvFaKDw2GlPjVwxv4VgvaBQ2G4cBLy4-qj1kJQK5sV541mDu9B4pbYa68J2kl2cZHNobcVKCmMV96Av6YXLH5CLsYB0YHe7Q5fpzLVrbooSHXYiZKpso3LfRBYh1G2VFLMTCSp1b74AjCTZ_3tOweBov1YwVRuByDwlIZxnShdAmihUJinu_zYL06MaI24vjUjlCla7xvdeV5JpYsCrUd70Ah4eit1Bic-O_jLQbhWOLbj5EJMHV2yQHHTs_m1swtgpyrR5YsoEmO_mQ";
        //doReturn(mockProduct).when(suspiciousPositionService).getUserPosition(token);
        List<Position> positionList = suspiciousPositionService.getUserPosition(token);

        Position assertPosition = new Position(10.0652f,new Timestamp(new Date().getTime()),60.0564f,"5030a8a3-bb8c-42d0-87e2-030d7181a0cd", 30.0f);
        Assertions.assertEquals(1,positionList.size());
        Assertions.assertEquals("5030a8a3-bb8c-42d0-87e2-030d7181a0cd",positionList.get(0).getUserId());
    }

    @Test
    @DisplayName("Test setUserAsCasContact")
    void testSetUserAsCasContact(){
        RestGatewaySupport gateway = new RestGatewaySupport();
        gateway.setRestTemplate(restTemplate);
        mockServer = MockRestServiceServer.createServer(gateway);

        mockServer.expect(once(), requestTo("http://localhost:3002/personState/update"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json("{\"stateId\":0}"))
                .andRespond(withSuccess("{\"success\":1}", MediaType.APPLICATION_JSON));

        String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI1VmxhaXlBMGVaZW9rVnl4STFMT25tc1FEM3pjME1wajVPYUdGbnZHYmZFIn0.eyJleHAiOjE2MDU4OTgxNDQsImlhdCI6MTYwNTg5Nzg0NCwiYXV0aF90aW1lIjoxNjA1ODk3ODQzLCJqdGkiOiIwMzA5MTBmYy03YTE4LTQ3ZmQtOTc0Yi0zY2FhMmViZTA0MTUiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC9yZWFsbXMvY292aWQtYWxlcnQiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiNTAzMGE4YTMtYmI4Yy00MmQwLTg3ZTItMDMwZDcxODFhMGNkIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidXNlci1hcHAiLCJub25jZSI6IjliYTU0ZmFjLTE4OGQtNDA3NC04MzI2LTIwMTk0ODY4MjZhNyIsInNlc3Npb25fc3RhdGUiOiJkZDEwNzFmZS1hZDdlLTQ3MzktODQ0Ni05MWQxNjNkZjRjZWIiLCJhY3IiOiIwIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoiZm9vIn0.bajbyUvmnAp9fLVMAmQrs_EIdoSv4rWtNKATuVHJmpC4TZgvUsSSGs7fhJQHjE6Dp5FaCwbvFaKDw2GlPjVwxv4VgvaBQ2G4cBLy4-qj1kJQK5sV541mDu9B4pbYa68J2kl2cZHNobcVKCmMV96Av6YXLH5CLsYB0YHe7Q5fpzLVrbooSHXYiZKpso3LfRBYh1G2VFLMTCSp1b74AjCTZ_3tOweBov1YwVRuByDwlIZxnShdAmihUJinu_zYL06MaI24vjUjlCla7xvdeV5JpYsCrUd70Ah4eit1Bic-O_jLQbhWOLbj5EJMHV2yQHHTs_m1swtgpyrR5YsoEmO_mQ";
        Assertions.assertEquals(suspiciousPositionService.setUserAsCasContact(token),Boolean.TRUE);
    }

}
