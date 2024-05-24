package bf.hsawadogo.productservice;

import bf.hsawadogo.productservice.entities.Product;
import bf.hsawadogo.productservice.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.List;

@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    @Profile("integration")
    CommandLineRunner commandLineRunner(ProductRepository productRepository) {
        return args -> {
            List<Product> products = List.of(
                    Product.builder().code("P001").libelle("HP").build(),
                    Product.builder().code("P002").libelle("ASYS").build(),
                    Product.builder().code("P003").libelle("MACOS").build()
            );
            productRepository.saveAll(products);

        };
    }
}
