package bf.hsawadogo.customerservice.services.impl;

import bf.hsawadogo.customerservice.dtos.CustomerDTO;
import bf.hsawadogo.customerservice.entities.Customer;
import bf.hsawadogo.customerservice.exceptions.EmailAlreadyExistException;
import bf.hsawadogo.customerservice.mappers.CustomerMapper;
import bf.hsawadogo.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class) // Mockito va nous permettre de générer des mocks
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    public void shouldSaveCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder().firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build();
        Customer customer = Customer.builder().firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build();
        Customer savedCustomer = Customer.builder().id(1L).firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build();
        CustomerDTO expected = CustomerDTO.builder().id(1L).firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build();

        Mockito.when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.empty());
        Mockito.when(customerMapper.toEntity(customerDTO)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(savedCustomer);
        Mockito.when(customerMapper.toDto(savedCustomer)).thenReturn(expected);
        CustomerDTO result = customerService.saveCustomer(customerDTO);
        assertNotNull(result);
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    public void shouldNotSaveCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder().firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build();
        Customer customer = Customer.builder().id(1L).firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build();

        Mockito.when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.of(customer));
        assertThatThrownBy(() -> customerService.saveCustomer(customerDTO)).isInstanceOf(EmailAlreadyExistException.class);
    }

}
