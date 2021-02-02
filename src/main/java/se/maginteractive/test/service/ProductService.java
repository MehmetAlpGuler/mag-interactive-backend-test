package se.maginteractive.test.service;

import se.maginteractive.test.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll(Integer pageNo, Integer pageSize, String sortBy);

    Optional<Product> findById(long id);

    Product create(Product product);

    Product update(long id, Product product);

    Product deleteById(long id);
}
