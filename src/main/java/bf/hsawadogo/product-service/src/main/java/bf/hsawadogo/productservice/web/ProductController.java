package bf.hsawadogo.productservice.web;

import bf.hsawadogo.productservice.services.ProductService;
import bf.hsawadogo.productservice.services.dtos.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Service
@RequiredArgsConstructor
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        log.info("REST Request to create a new product : {}", productDTO);
        ProductDTO product = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> findAllProducts() {
        log.info("REST Request to find all products");
        List<ProductDTO> products = productService.findAllProducts();
        log.info("Found {} products", products.size());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/search")
    public ResponseEntity<ProductDTO> findProductByCode(@RequestParam("code") String code) {
        log.info("REST Request to find product by code : {}", code);
        ProductDTO product = productService.findProductByCode(code);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("REST Request to delete product : {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
