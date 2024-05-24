package bf.hsawadogo.productservice.web.integrations;

import bf.hsawadogo.productservice.mappers.ProductMapper;
import bf.hsawadogo.productservice.repositories.ProductRepository;
import bf.hsawadogo.productservice.services.dtos.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Slf4j
@ActiveProfiles("integration")
public class ProductControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

    List<ProductDTO> productDTOs;

    @Container
    @ServiceConnection
    private static MariaDBContainer mariaDBContainer = new MariaDBContainer("mariadb:latest");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDBContainer::getUsername);
        registry.add("spring.datasource.password", mariaDBContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        productDTOs = productMapper.toDTO(productRepository.findAll());
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void shouldGetAllProducts() {
        ResponseEntity<ProductDTO[]> response = restTemplate.exchange("/api/products", HttpMethod.GET, null, ProductDTO[].class);
        List<ProductDTO> content = Arrays.asList(Objects.requireNonNull(response.getBody()));
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AssertionsForClassTypes.assertThat(content.size()).isEqualTo(productDTOs.size());
        AssertionsForClassTypes.assertThat(content).usingRecursiveComparison().ignoringFields("id").isEqualTo(productDTOs);
    }

    @Test
    @Rollback
    void shouldCreateProduct() {
        ProductDTO productDTO = ProductDTO.builder().code("P004").libelle("LINUX").build();
        ResponseEntity<ProductDTO> response = restTemplate.exchange("/api/products", HttpMethod.POST, new HttpEntity<>(productDTO), ProductDTO.class);
        ProductDTO content = Objects.requireNonNull(response.getBody());
        AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        AssertionsForClassTypes.assertThat(content).usingRecursiveComparison().ignoringFields("id").isEqualTo(productDTO);
    }
}
