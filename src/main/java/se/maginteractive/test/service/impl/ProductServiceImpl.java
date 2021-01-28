package se.maginteractive.test.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.maginteractive.test.exception.ResourceNotFoundException;
import se.maginteractive.test.model.Product;
import se.maginteractive.test.repository.ProductRepository;
import se.maginteractive.test.service.ProductService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

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
