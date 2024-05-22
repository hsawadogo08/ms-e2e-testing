package bf.hsawadogo.customerservice.mappers;

import bf.hsawadogo.customerservice.dtos.CustomerDTO;
import bf.hsawadogo.customerservice.entities.Customer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CustomerMapperTest {
    CustomerMapper customerMapper = new CustomerMapper();

    @Test
    public void shouldMapCustomerToCustomerDto() {
        Customer givenCustomer = Customer.builder().id(1L).firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build();
        CustomerDTO expected = CustomerDTO.builder().id(1L).firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build();

        CustomerDTO result = customerMapper.toDto(givenCustomer);
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void shouldMapNullCustomerToCustomerDto() {
        Customer givenCustomer = null;
        assertThatThrownBy(() -> customerMapper.toDto(givenCustomer)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldMapListCustomersToListCustomerDtos() {
        List<Customer> givenCustomers = List.of(
                Customer.builder().id(1L).firstName("Hubert").lastName("SAWADOGO").email("h.sawadogo@tic.gov.bf").build(),
                Customer.builder().id(2L).firstName("Stephanie").lastName("TONDE").email("e.tonde@tic.gov.bf").build()
        );

        List<CustomerDTO> expectedCustomers = List.of(
                CustomerDTO.builder().id(1L).firstName("Hubert").lastName("SAWADOGO").email("h.sawadogo@tic.gov.bf").build(),
                CustomerDTO.builder().id(2L).firstName("Stephanie").lastName("TONDE").email("e.tonde@tic.gov.bf").build()
        );

        List<CustomerDTO> result = customerMapper.toDto(givenCustomers);
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedCustomers);
    }

    @Test
    public void shouldMapCustomerDtoToCustomer() {
        CustomerDTO givenCustomerDto = CustomerDTO.builder().id(1L).firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build();
        Customer expected = Customer.builder().id(1L).firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build();

        Customer result = customerMapper.toEntity(givenCustomerDto);
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void shouldMapListCustomerDtosToListCustomers() {
        List<CustomerDTO> givenCustomers = List.of(
                CustomerDTO.builder().id(1L).firstName("Hubert").lastName("SAWADOGO").email("h.sawadogo@tic.gov.bf").build(),
                CustomerDTO.builder().id(2L).firstName("Stephanie").lastName("TONDE").email("e.tonde@tic.gov.bf").build()
        );

        List<Customer> expectedCustomers = List.of(
                Customer.builder().id(1L).firstName("Hubert").lastName("SAWADOGO").email("h.sawadogo@tic.gov.bf").build(),
                Customer.builder().id(2L).firstName("Stephanie").lastName("TONDE").email("e.tonde@tic.gov.bf").build()
        );

        List<Customer> result = customerMapper.toEntity(givenCustomers);
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedCustomers);
    }
}
