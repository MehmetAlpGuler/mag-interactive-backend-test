package se.maginteractive.test.controller.products.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import se.maginteractive.test.controller.products.ProductController;
import se.maginteractive.test.exception.ResourceNotFoundException;
import se.maginteractive.test.model.Product;
import se.maginteractive.test.payload.ProductDto;
import se.maginteractive.test.payload.request.ProductDeleteRequest;
import se.maginteractive.test.payload.request.ProductRequest;
import se.maginteractive.test.payload.response.ProductResponse;
import se.maginteractive.test.payload.response.ProductsResponse;
import se.maginteractive.test.service.ProductService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    public ProductsResponse findAll(Integer pageNo,
                                    Integer pageSize,
                                    String sortBy) {

        List<ProductDto> products = productService.findAll(pageNo, pageSize, sortBy)
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());

        return ProductsResponse.builder().products(products).build();
    }

    public ProductResponse findById(Long id) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        return modelMapper.map(product, ProductResponse.class);
    }

    public ResponseEntity<ProductResponse> create(ProductRequest request) {
        Product product = modelMapper.map(request.getProduct(), Product.class);
        product = productService.create(product);

        //return getURI that created record
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/product/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(modelMapper.map(product, ProductResponse.class));
    }

    public ResponseEntity<ProductResponse> update(ProductRequest request) {
        Product product = modelMapper.map(request.getProduct(), Product.class);
        product = productService.update(request.getProduct().getId(), product);

        //return getURI that created record
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/product/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(modelMapper.map(product, ProductResponse.class));
    }

    public ProductResponse delete(ProductDeleteRequest request) {
        Product product = productService.deleteById(request.getId());
        return modelMapper.map(product, ProductResponse.class);
    }
}