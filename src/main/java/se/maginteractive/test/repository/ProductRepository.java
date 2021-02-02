package se.maginteractive.test.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import se.maginteractive.test.model.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

}
