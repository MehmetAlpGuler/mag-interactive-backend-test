package se.maginteractive.test.controller.products;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.maginteractive.test.payload.request.ProductDeleteRequest;
import se.maginteractive.test.payload.request.ProductRequest;
import se.maginteractive.test.payload.response.ProductResponse;
import se.maginteractive.test.payload.response.ProductsResponse;

import javax.validation.Valid;

@RestController
public interface ProductController {

    @GetMapping("/product/list")
    ProductsResponse findAll(@RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "1000") Integer pageSize,
                             @RequestParam(defaultValue = "id") String sortBy);

    @GetMapping("/product/{id}")
    ProductResponse findById(@PathVariable(value = "id") Long id);

    @PostMapping("/product/create")
    ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request);

    @PostMapping("/product/update")
    ResponseEntity<ProductResponse> update(@Valid @RequestBody ProductRequest request);

    @PostMapping("/product/delete")
    ProductResponse delete(@Valid @RequestBody ProductDeleteRequest request);
}