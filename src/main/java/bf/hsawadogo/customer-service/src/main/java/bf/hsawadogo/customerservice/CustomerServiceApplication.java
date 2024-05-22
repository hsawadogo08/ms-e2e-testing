package bf.hsawadogo.customerservice;

import bf.hsawadogo.customerservice.entities.Customer;
import bf.hsawadogo.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    @Profile("!test") // Pour dire que ce bean ne s'éxecute pas lorsqu'il s'agit des tests
    CommandLineRunner commandLineRunner(CustomerRepository repository) {
        return args -> {
             repository.save(Customer.builder().firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build());
             repository.save(Customer.builder().firstName("Hubert").lastName("SAWADOGO").email("h.sawadogo@tic.gov.bf").build());
             repository.save(Customer.builder().firstName("Stephanie").lastName("TONDE").email("e.tonde@tic.gov.bf").build());
        };
    }
}
