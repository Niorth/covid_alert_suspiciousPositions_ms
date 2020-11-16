package fr.projetiwa.SuspiciousPosition.Integration;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
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
    // ...
}