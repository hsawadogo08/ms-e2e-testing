package bf.hsawadogo.customerservice.web;

import bf.hsawadogo.customerservice.dtos.CustomerDTO;
import bf.hsawadogo.customerservice.services.CustomerService;
import bf.hsawadogo.customerservice.utils.AbstractIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;



@ActiveProfiles("test")
@WebMvcTest(CustomerController.class) // Préciser le controller que l'on veut tester
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerControllerTest {
    @MockBean // À utiliser pour le cas des services
    private CustomerService customerService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    List<CustomerDTO> customers;

    @BeforeEach
    void setUp() {
        customers = List.of(
                CustomerDTO.builder().id(1L).firstName("Wendinso").lastName("SAWADOGO").email("w.sawadogo@tic.gov.bf").build(),
                CustomerDTO.builder().id(2L).firstName("Hubert").lastName("SAWADOGO").email("h.sawadogo@tic.gov.bf").build(),
                CustomerDTO.builder().id(3L).firstName("Stephanie").lastName("TONDE").email("e.tonde@tic.gov.bf").build()
        );
    }

    @Test
    void shouldGetAllCustomers() throws Exception {
        Mockito.when(customerService.findAllCustomers()).thenReturn(customers);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers)));
    }

    @Test
    void shouldGetCustomerById() throws Exception {
        Long id = 1L;
        Mockito.when(customerService.findCustomerById(id)).thenReturn(customers.get(0));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/{id}", customers.get(0).getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers.get(0))));
    }

    @Test
    void shouldSaveCustomer() throws Exception {
        CustomerDTO customerDTO = customers.get(0);
        Mockito.when(customerService.saveCustomer(Mockito.any())).thenReturn(customerDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customerDTO)));
    }

    @Test
    void shouldUpdateCustomer() throws Exception {
        CustomerDTO customerDTO = customers.get(0);
        Mockito.when(customerService.updateCustomer(Mockito.anyLong(), Mockito.any())).thenReturn(customerDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/customers/{id}", customerDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customerDTO)));
    }

    @Test
    void shouldSearchCustomerByKeyword() throws Exception {
        String keyword = "e";
        Mockito.when(customerService.searchCustomers(Mockito.anyString())).thenReturn(customers);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/search?query={keyword}", keyword))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(customers.size())))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers)));
    }

    @Test
    void shouldDeleteCustomer() throws Exception {
        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
