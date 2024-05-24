package bf.hsawadogo.productservice.web;

import bf.hsawadogo.productservice.services.ProductService;
import bf.hsawadogo.productservice.services.dtos.ProductDTO;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@WebMvcTest(ProductController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerTest {
    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    List<ProductDTO> productDTOs;

    @BeforeEach
    void setUp() {
        productDTOs = List.of(
                ProductDTO.builder().id(1L).code("P001").libelle("HP").build(),
                ProductDTO.builder().id(2L).code("P002").libelle("ASYS").build(),
                ProductDTO.builder().id(3L).code("P003").libelle("MACOS").build()
        );
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductDTO productDTO = productDTOs.getFirst();
        Mockito.when(productService.createProduct(Mockito.any())).thenReturn(productDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO))
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(productDTO)));
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        Mockito.when(productService.findAllProducts()).thenReturn(productDTOs);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(productDTOs)));
    }

    @Test
    void souldFindProductByCode() throws Exception {
        Mockito.when(productService.findProductByCode(Mockito.anyString())).thenReturn(productDTOs.getFirst());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/search?code=" + productDTOs.getFirst().getCode()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(productDTOs.getFirst())));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        Long productId = productDTOs.getFirst().getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
