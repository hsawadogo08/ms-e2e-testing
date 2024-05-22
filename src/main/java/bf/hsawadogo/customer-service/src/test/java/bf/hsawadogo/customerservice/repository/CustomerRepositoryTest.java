package bf.hsawadogo.customerservice.repository;

import bf.hsawadogo.customerservice.entities.Customer;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // Precise le profil correspondand des tests
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        customerRepository.save(Customer.builder().firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build());
        customerRepository.save(Customer.builder().firstName("Hubert").lastName("SAWADOGO").email("h.sawadogo@tic.gov.bf").build());
        customerRepository.save(Customer.builder().firstName("Stephanie").lastName("TONDE").email("e.tonde@tic.gov.bf").build());
    }

    @Test
    void shouldFindCustomerByEmail() {
        String givenEmail = "w.sawadogo@tic.gov.bf";
        Optional<Customer> customer = customerRepository.findByEmail(givenEmail);
        assertThat(customer).isPresent();
    }

    @Test
    void shouldNotFindCustomerByEmail() {
        String givenEmail = "wendison.sawadogo@tic.gov.bf";
        Optional<Customer> customer = customerRepository.findByEmail(givenEmail);
        assertThat(customer).isEmpty();
    }

    @Test
    void shouldFindCustomersByFirstNameContainsIgnoreCase() {
        String keyword = "t";
        List<Customer> expectedCustomers = List.of(
                Customer.builder().firstName("Hubert").lastName("SAWADOGO").email("h.sawadogo@tic.gov.bf").build(),
                Customer.builder().firstName("Stephanie").lastName("TONDE").email("e.tonde@tic.gov.bf").build()
        );
        List<Customer> result = customerRepository.findByFirstNameContainsIgnoreCase(keyword);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(expectedCustomers.size());
        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedCustomers);
    }
}
