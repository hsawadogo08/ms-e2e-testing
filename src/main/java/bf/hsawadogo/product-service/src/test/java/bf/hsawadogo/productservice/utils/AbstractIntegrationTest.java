package bf.hsawadogo.productservice.utils;


import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractIntegrationTest {

    private static final MariaDBContainer mariadb;

    static {
        mariadb = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"))
                .withDatabaseName("productdb")
                .withUsername("hsawaodogo")
                .withPassword("0987654321");
        mariadb.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariadb::getJdbcUrl);
        registry.add("spring.datasource.username", mariadb::getUsername);
        registry.add("spring.datasource.password", mariadb::getPassword);
    }



}
