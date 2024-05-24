package bf.hsawadogo.productservice.services.impl;

import bf.hsawadogo.productservice.entities.Product;
import bf.hsawadogo.productservice.exceptions.ProductWithCodeAlreadyExistException;
import bf.hsawadogo.productservice.exceptions.ProductNotFoundException;
import bf.hsawadogo.productservice.mappers.ProductMapper;
import bf.hsawadogo.productservice.repositories.ProductRepository;
import bf.hsawadogo.productservice.services.ProductService;
import bf.hsawadogo.productservice.services.dtos.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) throws ProductWithCodeAlreadyExistException {
        log.info("Request to create new product: {}", productDTO);
        Optional<Product> optionalProduct = productRepository.findByCode(productDTO.getCode());
        if (optionalProduct.isPresent()) {
            throw new ProductWithCodeAlreadyExistException();
        }
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDTO(product);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) throws ProductNotFoundException {
        log.info("Request to update product: {}", productDTO);
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException();
        }
        Product productToUpdate = productMapper.toEntity(productDTO);
        productToUpdate.setId(id);
        productToUpdate = productRepository.save(productToUpdate);
        return productMapper.toDTO(productToUpdate);
    }

    @Override
    public ProductDTO findProductById(Long id) throws ProductNotFoundException {
        log.info("Request to find product by id: {}", id);
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException();
        }
        return productMapper.toDTO(optionalProduct.get());
    }

    @Override
    public ProductDTO findProductByCode(String code) throws ProductNotFoundException {
        log.info("Request to find product by code: {}", code);
        Optional<Product> optionalProduct = productRepository.findByCode(code);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException();
        }
        return productMapper.toDTO(optionalProduct.get());
    }

    @Override
    public List<ProductDTO> findAllProducts() {
        log.info("Request to find all products");
        List<Product> products = productRepository.findAll();
        return productMapper.toDTO(products);
    }

    @Override
    public List<ProductDTO> searchProducts(String keyword) {
        log.info("Request to search products by keyword: {}", keyword);
        List<Product> products = productRepository.findAllByLibelleContainingIgnoreCase(keyword);
        return productMapper.toDTO(products);
    }

    @Override
    public void deleteProduct(Long id) throws ProductNotFoundException {
        log.info("Request to delete product: {}", id);
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException();
        }
        productRepository.deleteById(id);
    }
}
