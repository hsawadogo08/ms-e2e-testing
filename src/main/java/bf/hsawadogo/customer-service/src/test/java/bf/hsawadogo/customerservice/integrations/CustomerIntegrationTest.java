package bf.hsawadogo.customerservice.integrations;

import bf.hsawadogo.customerservice.dtos.CustomerDTO;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CustomerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer postgreSQLContainer=new PostgreSQLContainer("postgres:16");


    List<CustomerDTO> customers;

    @BeforeEach
    void setUp() {
        customers = List.of(
                CustomerDTO.builder().id(1L).firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build(),
                CustomerDTO.builder().id(2L).firstName("Hubert").lastName("SAWADOGO").email("h.sawadogo@tic.gov.bf").build(),
                CustomerDTO.builder().id(3L).firstName("Stephanie").lastName("TONDE").email("e.tonde@tic.gov.bf").build()
        );
    }

    @Test
    void shouldFindAllCustomers() {
        ResponseEntity<CustomerDTO[]> response = restTemplate.exchange("/api/customers", HttpMethod.GET, null, CustomerDTO[].class);
        List<CustomerDTO> content = Arrays.asList(Objects.requireNonNull(response.getBody()));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(content.size()).isEqualTo(3);
        assertThat(content).usingRecursiveComparison().isEqualTo(customers);
    }

    @Test
    @Rollback
    void shouldSaveValidCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder().firstName("Wendinso").lastName("SAWADOGO").email("wendison.sawadogo@tic.gov.bf").build();
        ResponseEntity<CustomerDTO> response = restTemplate.exchange("/api/customers", HttpMethod.POST, new HttpEntity<>(customerDTO), CustomerDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(customerDTO);
    }

    @Test
    @Rollback
    void shouldDeleteCustomer(){
        Long customerId = 3L;
        ResponseEntity<String> response = restTemplate.exchange("/api/customers/"+customerId, HttpMethod.DELETE, null, String.class);
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
