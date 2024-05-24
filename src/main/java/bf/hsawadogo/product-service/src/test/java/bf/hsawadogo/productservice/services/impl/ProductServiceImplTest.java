package bf.hsawadogo.productservice.services.impl;

import bf.hsawadogo.productservice.entities.Product;
import bf.hsawadogo.productservice.exceptions.ProductNotFoundException;
import bf.hsawadogo.productservice.exceptions.ProductWithCodeAlreadyExistException;
import bf.hsawadogo.productservice.mappers.ProductMapper;
import bf.hsawadogo.productservice.repositories.ProductRepository;
import bf.hsawadogo.productservice.services.dtos.ProductDTO;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductServiceImpl  productService;

    List<Product> products;
    List<ProductDTO> productDTOs;
    Product product;
    ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        products = List.of(
                Product.builder().id(1L).code("P001").libelle("HP").build(),
                Product.builder().id(2L).code("P002").libelle("ASYS").build(),
                Product.builder().id(3L).code("P003").libelle("MACOS").build()
        );
        productDTOs = List.of(
                ProductDTO.builder().id(1L).code("P001").libelle("HP").build(),
                ProductDTO.builder().id(2L).code("P002").libelle("ASYS").build(),
                ProductDTO.builder().id(3L).code("P003").libelle("MACOS").build()
        );
        productDTO = ProductDTO.builder().id(1L).code("P001").libelle("HP").build();
        product = Product.builder().id(1L).code("P001").libelle("HP").build();
    }

    @Test
    void shouldCreateProduct() {
        Mockito.when(productRepository.findByCode(productDTO.getCode())).thenReturn(Optional.empty());
        Mockito.when(productMapper.toEntity(productDTO)).thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.when(productMapper.toDTO(product)).thenReturn(productDTO);
        ProductDTO result = productService.createProduct(productDTO);
        assertNotNull(result);
        assertThat(result).usingRecursiveComparison().isEqualTo(productDTO);
    }

    @Test
    void shouldNotCreateProductWhenCodeIsExist() {
        Mockito.when(productRepository.findByCode(productDTO.getCode())).thenReturn(Optional.of(product));
        assertThatThrownBy(() -> productService.createProduct(productDTO)).isInstanceOf(ProductWithCodeAlreadyExistException.class);
    }

    @Test
    void shouldUpdateProduct() {
        Mockito.when(productRepository.findById(productDTO.getId())).thenReturn(Optional.of(product));
        Mockito.when(productMapper.toEntity(productDTO)).thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.when(productMapper.toDTO(product)).thenReturn(productDTO);
        ProductDTO result = productService.updateProduct(productDTO.getId(), productDTO);
        assertNotNull(result);
        assertThat(result).usingRecursiveComparison().isEqualTo(productDTO);
    }

    @Test
    void shouldNotUpdateProductWhenIdIsNotFound() {
        Mockito.when(productRepository.findById(productDTO.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productService.updateProduct(productDTO.getId(), productDTO)).isInstanceOf(ProductNotFoundException.class);
    }
}
