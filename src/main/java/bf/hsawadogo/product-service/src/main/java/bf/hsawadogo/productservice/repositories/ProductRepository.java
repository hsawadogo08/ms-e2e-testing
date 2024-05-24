package bf.hsawadogo.productservice.repositories;

import bf.hsawadogo.productservice.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByCode(String code);
    List<Product> findAllByLibelleContainingIgnoreCase(String libelle);
}
