package bf.hsawadogo.productservice.repositories;

import bf.hsawadogo.productservice.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    List<Product> products;

    @BeforeEach
    void setUp() {
        products = List.of(
                Product.builder().code("P001").libelle("HP").build(),
                Product.builder().code("P002").libelle("ASYS").build(),
                Product.builder().code("P003").libelle("MACOS").build()
        );
        productRepository.saveAll(products);
    }

    @Test
    void shouldFindProductByCode() {
        String code = "P001";
        Optional<Product> productOptional = productRepository.findByCode(code);
        assertThat(productOptional).isPresent();
        assertThat(productOptional.get()).isNotNull();
        assertThat(productOptional.get()).usingRecursiveComparison().ignoringFields("id").isEqualTo(products.getFirst());
    }

    @Test
    void shouldNotFindProductByCode() {
        String code = "PXXX";
        Optional<Product> productOptional = productRepository.findByCode(code);
        assertThat(productOptional).isEmpty();
    }

    @Test
    void shouldFindAllByLibelleContainingIgnoreCase() {
        String keyword = "A";
        List<Product> expectedProducts = List.of(
                Product.builder().code("P002").libelle("ASYS").build(),
                Product.builder().code("P003").libelle("MACOS").build()
        );
        List<Product> response = productRepository.findAllByLibelleContainingIgnoreCase(keyword);
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(expectedProducts.size());
        assertThat(response).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedProducts);
    }

    @Test
    void shouldNotFindProductByLibelleContainingIgnoreCase() {
        String keyword = "AAAA";
        List<Product> response = productRepository.findAllByLibelleContainingIgnoreCase(keyword);
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(0);
    }
}
