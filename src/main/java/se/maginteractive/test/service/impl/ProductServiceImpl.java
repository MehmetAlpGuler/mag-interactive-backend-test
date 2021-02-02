package se.maginteractive.test.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public List<Product> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Product> pagedResult = productRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return List.of();
        }
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
