package bf.hsawadogo.productservice.mappers;

import bf.hsawadogo.productservice.entities.Product;
import bf.hsawadogo.productservice.services.dtos.ProductDTO;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

class ProductMapperTest {
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Test
    void shouldMapProductToProductDto() {
        Product product = Product.builder().id(1L).code("P001").libelle("HP").build();
        ProductDTO expected = ProductDTO.builder().id(1L).code("P001").libelle("HP").build();
        ProductDTO result = productMapper.toDTO(product);
        AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void shouldMapProductsToProductDtos() {
        List<Product> products = List.of(
                Product.builder().id(1L).code("P001").libelle("HP").build(),
                Product.builder().id(2L).code("P002").libelle("ASYS").build()
        );
        List<ProductDTO> expectedProductDTOs = List.of(
                ProductDTO.builder().id(1L).code("P001").libelle("HP").build(),
                ProductDTO.builder().id(2L).code("P002").libelle("ASYS").build()
        );
        List<ProductDTO> result = productMapper.toDTO(products);
        AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expectedProductDTOs);
    }

    @Test
    void shouldMapProductDTOToProduct() {
        ProductDTO productDTO = ProductDTO.builder().id(1L).code("P001").libelle("HP").build();
        Product expected = Product.builder().id(1L).code("P001").libelle("HP").build();
        Product result = productMapper.toEntity(productDTO);
        AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void shouldMapProductDTOsToProducts() {
        List<ProductDTO> productDTOS = List.of(
                ProductDTO.builder().id(1L).code("P001").libelle("HP").build(),
                ProductDTO.builder().id(2L).code("P002").libelle("ASYS").build()
        );
        List<Product> expectedProducts = List.of(
                Product.builder().id(1L).code("P001").libelle("HP").build(),
                Product.builder().id(2L).code("P002").libelle("ASYS").build()
        );
        List<Product> result = productMapper.toEntity(productDTOS);
        AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expectedProducts);
    }
}
