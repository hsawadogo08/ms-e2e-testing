package bf.hsawadogo.customerservice.services;

import bf.hsawadogo.customerservice.dtos.CustomerDTO;
import bf.hsawadogo.customerservice.exceptions.CustomerNotFoundException;
import bf.hsawadogo.customerservice.exceptions.EmailAlreadyExistException;

import java.util.List;

public interface CustomerService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO) throws EmailAlreadyExistException;
    List<CustomerDTO> findAllCustomers();
    CustomerDTO findCustomerById(Long id) throws CustomerNotFoundException;
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) throws CustomerNotFoundException;
    void deleteCustomer(Long id) throws CustomerNotFoundException;
    List<CustomerDTO> searchCustomers(String keyword);
}
