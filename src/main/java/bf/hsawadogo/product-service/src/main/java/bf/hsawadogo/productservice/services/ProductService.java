package bf.hsawadogo.productservice.services;

import bf.hsawadogo.productservice.exceptions.ProductWithCodeAlreadyExistException;
import bf.hsawadogo.productservice.exceptions.ProductNotFoundException;
import bf.hsawadogo.productservice.services.dtos.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO) throws ProductWithCodeAlreadyExistException;
    ProductDTO updateProduct(Long id, ProductDTO productDTO) throws ProductNotFoundException;
    ProductDTO findProductById(Long id) throws ProductNotFoundException;
    ProductDTO findProductByCode(String code) throws ProductNotFoundException;
    List<ProductDTO> findAllProducts();
    List<ProductDTO> searchProducts(String keyword);
    void deleteProduct(Long id) throws ProductNotFoundException;
}
