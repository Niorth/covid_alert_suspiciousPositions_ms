package fr.projetiwa.SuspiciousPosition.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import fr.projetiwa.SuspiciousPosition.models.Position;
import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import fr.projetiwa.SuspiciousPosition.util.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.is;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.*;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@Profile("test")
@AutoConfigureMockMvc
public class SuspiciousControllerIntegration {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private Function service;
    @Autowired
    private DataSource dataSource;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;


    public ConnectionHolder getConnectionHolder() {
        // Return a function that retrieves a connection
        // from our data source
        return () -> dataSource.getConnection();
    }

    /**
     * Test if the route /suslocation worked and return a list of suspicious position
     * @throws Exception
     */
    @Test
    @DisplayName("GET /suslocation - Found")
    @DataSet("suspiciousPosition.yml")
    void testGetPositionsFound() throws Exception {

        mockMvc.perform(get("/suslocation",1))
                // Validate the status code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.length()", is(2)));
    }

    /**
     * To test if the route return a suspicious position item when we give an id
     * @throws Exception
     */
    @Test
    @DisplayName("GET /suslocation/1 - Found")
    @DataSet("suspiciousPosition.yml")
    void testGetPositionByIdFound() throws Exception {

        mockMvc.perform(get("/suslocation/{id}",1))
                // Validate the status code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.positionId", is(1)))
                .andExpect(jsonPath("$.latitude", is(10.0)))
                .andExpect(jsonPath("$.longitude",is(10.0)));

    }

    /**
     * To test if there is an error if we give a random id not likend with a suspicious position
     * @throws Exception
     */
    @Test
    @DisplayName("GET /suslocation/1000 - Not Found")
    @DataSet("suspiciousPosition.yml")
    void testGetPositionByIdNotFound() throws Exception {
        mockMvc.perform(get("/suslocation/{id}",1000))
                // Validate the status code and content type
                .andExpect(status().isNotFound());
    }

    /**
     * To test if we can add a new suspicious position to the database
     * @throws Exception
     */
    @Test
    @DisplayName("POST /suslocation - Success")
    @DataSet("suspiciousPosition.yml")
    void testCreatePosition() throws Exception{
        SuspiciousPosition sus = new SuspiciousPosition(1f,new Timestamp(new Date().getTime()),1f);
        mockMvc.perform(post("/suslocation/").contentType(APPLICATION_JSON).content(service.asJsonString(sus)))
                .andExpect(status().isCreated()).andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.positionId", is(3)))
                .andExpect(jsonPath("$.latitude", is(1.0)))
                .andExpect(jsonPath("$.longitude",is(1.0)));


    }

    /**
     * Test isSuspicious
     * Simulation of Position,CovidState microservice with MockServer
     * Retrieves user positions from mockServer and retrieves susPositions from yml, compare both and return if al least one user position is Suspicious
     * Use of suspicious position from yml file
     * @throws Exception
     */
    @Test
    @DisplayName("GET /suslocation/isSuspicious - Success")
    @DataSet("suspiciousPosition2.yml")
    void testIsSuspicious() throws Exception {
        RestGatewaySupport gateway = new RestGatewaySupport();
        gateway.setRestTemplate(restTemplate);
        mockServer = MockRestServiceServer.createServer(gateway);


        List<Position> mockProduct = new ArrayList<Position>();
        Calendar cal = Calendar.getInstance();
        cal.set(2020, Calendar.OCTOBER, 10, 14, 30, 00); //Year, month, day of month, hours, minutes and seconds
        Date date = cal.getTime();
        Position mockPosition = new Position(10.0563f,new Timestamp(date.getTime()),60.0563f,"5030a8a3-bb8c-42d0-87e2-030d7181a0cd", 30.0f);
        mockProduct.add(mockPosition);

        mockServer.expect(once(), requestTo("http://localhost:3003/positions"))
                .andRespond(withSuccess(Function.asJsonString(mockProduct), MediaType.APPLICATION_JSON));

        mockServer.expect(once(), requestTo("http://localhost:3002/personState/update"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"success\":1}", MediaType.APPLICATION_JSON));

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        header.add(HttpHeaders.AUTHORIZATION, "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI1VmxhaXlBMGVaZW9rVnl4STFMT25tc1FEM3pjME1wajVPYUdGbnZHYmZFIn0.eyJleHAiOjE2MDU4OTgxNDQsImlhdCI6MTYwNTg5Nzg0NCwiYXV0aF90aW1lIjoxNjA1ODk3ODQzLCJqdGkiOiIwMzA5MTBmYy03YTE4LTQ3ZmQtOTc0Yi0zY2FhMmViZTA0MTUiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC9yZWFsbXMvY292aWQtYWxlcnQiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiNTAzMGE4YTMtYmI4Yy00MmQwLTg3ZTItMDMwZDcxODFhMGNkIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidXNlci1hcHAiLCJub25jZSI6IjliYTU0ZmFjLTE4OGQtNDA3NC04MzI2LTIwMTk0ODY4MjZhNyIsInNlc3Npb25fc3RhdGUiOiJkZDEwNzFmZS1hZDdlLTQ3MzktODQ0Ni05MWQxNjNkZjRjZWIiLCJhY3IiOiIwIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoiZm9vIn0.bajbyUvmnAp9fLVMAmQrs_EIdoSv4rWtNKATuVHJmpC4TZgvUsSSGs7fhJQHjE6Dp5FaCwbvFaKDw2GlPjVwxv4VgvaBQ2G4cBLy4-qj1kJQK5sV541mDu9B4pbYa68J2kl2cZHNobcVKCmMV96Av6YXLH5CLsYB0YHe7Q5fpzLVrbooSHXYiZKpso3LfRBYh1G2VFLMTCSp1b74AjCTZ_3tOweBov1YwVRuByDwlIZxnShdAmihUJinu_zYL06MaI24vjUjlCla7xvdeV5JpYsCrUd70Ah4eit1Bic-O_jLQbhWOLbj5EJMHV2yQHHTs_m1swtgpyrR5YsoEmO_mQ");

        mockMvc.perform(post("/suslocation/isSuspicious").headers(header))
                .andExpect(jsonPath("$.isSuspicious", is(Boolean.TRUE)));

    }


}