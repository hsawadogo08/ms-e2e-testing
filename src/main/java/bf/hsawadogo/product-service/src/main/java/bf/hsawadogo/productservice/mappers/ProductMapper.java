package bf.hsawadogo.productservice.mappers;

import bf.hsawadogo.productservice.entities.Product;
import bf.hsawadogo.productservice.services.dtos.ProductDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductDTO productDTO);
    List<Product> toEntity(List<ProductDTO> productDTOs);
    ProductDTO toDTO(Product product);
    List<ProductDTO> toDTO(List<Product> products);
}
