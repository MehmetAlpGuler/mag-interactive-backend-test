package se.maginteractive.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.maginteractive.test.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
