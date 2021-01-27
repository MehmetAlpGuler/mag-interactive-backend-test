package se.maginteractive.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.maginteractive.test.model.Product;
import se.maginteractive.test.repository.ProductRepository;
import se.maginteractive.test.service.ProductService;
import se.maginteractive.test.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    @Transactional
    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product update(long id, Product product) {
        findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setId(id);
        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product deleteById(long id) {
        Product product = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productRepository.deleteById(id);
        return product;
    }
}
