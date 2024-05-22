package bf.hsawadogo.customerservice.services.impl;

import bf.hsawadogo.customerservice.dtos.CustomerDTO;
import bf.hsawadogo.customerservice.entities.Customer;
import bf.hsawadogo.customerservice.exceptions.CustomerNotFoundException;
import bf.hsawadogo.customerservice.exceptions.EmailAlreadyExistException;
import bf.hsawadogo.customerservice.mappers.CustomerMapper;
import bf.hsawadogo.customerservice.repository.CustomerRepository;
import bf.hsawadogo.customerservice.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) throws EmailAlreadyExistException {
        log.info("Request to save customer : {}", customerDTO);
        Optional<Customer> customerByEmail = customerRepository.findByEmail(customerDTO.getEmail());
        if (customerByEmail.isPresent()) {
            log.info("This email is '{}' already in use", customerDTO.getEmail());
            throw new EmailAlreadyExistException();
        }

        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    @Override
    public List<CustomerDTO> findAllCustomers() {
        log.info("Request to get all customers");
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toDto(customers);
    }

    @Override
    public CustomerDTO findCustomerById(Long id) throws CustomerNotFoundException {
        log.info("Request to get Customer by id : {}", id);
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(customerMapper::toDto).orElseThrow(CustomerNotFoundException::new);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) throws CustomerNotFoundException {
        log.info("Request to update Customer by id : {}", id);
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Customer customerToUpdate = customerMapper.toEntity(customerDTO);
        customerToUpdate.setId(id);
        customerToUpdate = customerRepository.save(customerToUpdate);
        return customerMapper.toDto(customerToUpdate);
    }

    @Override
    public void deleteCustomer(Long id) throws CustomerNotFoundException {
        log.info("Request to delete Customer by id : {}", id);
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        customerRepository.deleteById(id);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        log.info("Request to search customers by keyword : {}", keyword);
        List<Customer> customers = customerRepository.findByFirstNameContainsIgnoreCase(keyword);
        return customerMapper.toDto(customers);
    }
}
