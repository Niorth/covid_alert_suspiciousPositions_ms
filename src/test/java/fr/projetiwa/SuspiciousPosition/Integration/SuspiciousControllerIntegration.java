package fr.projetiwa.SuspiciousPosition.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
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
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.is;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Date;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@Profile("test")
@AutoConfigureMockMvc
public class SuspiciousControllerIntegration {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private DataSource dataSource;
    public ConnectionHolder getConnectionHolder() {
        // Return a function that retrieves a connection
        // from our data source
        return () -> dataSource.getConnection();
    }
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
    @Test
    @DisplayName("GET /suslocation/1000 - Not Found")
    @DataSet("suspiciousPosition.yml")
    void testGetPositionByIdNotFound() throws Exception {
        mockMvc.perform(get("/suslocation/{id}",1000))
                // Validate the status code and content type
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("POST /suslocation - Success")
    @DataSet("suspiciousPosition.yml")
    void testCreatePosition() throws Exception{
        SuspiciousPosition sus = new SuspiciousPosition();
        sus.setLongitude(1f);
        sus.setLatitude(1f);
        sus.setPosition_date(new Timestamp(new Date().getTime()));
        mockMvc.perform(post("/suslocation/").contentType(APPLICATION_JSON).content(asJsonString(sus)))
                .andExpect(status().isCreated()).andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.positionId", is(3)))
                .andExpect(jsonPath("$.latitude", is(1.0)))
                .andExpect(jsonPath("$.longitude",is(1.0)));


    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}