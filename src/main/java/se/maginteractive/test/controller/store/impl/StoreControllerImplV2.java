package se.maginteractive.test.controller.store.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import se.maginteractive.test.controller.store.StoreController;
import se.maginteractive.test.payload.ProductDto;
import se.maginteractive.test.payload.request.PurchaseRequest;
import se.maginteractive.test.payload.response.ProductsResponse;
import se.maginteractive.test.payload.response.TransactionResponse;

import java.math.BigDecimal;
import java.util.List;

// This class is useless, just I created for different version
// It can call like below
//   /api/v2/store/list
//   /api/v2/store/list

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class StoreControllerImplV2 implements StoreController {

    public ProductsResponse findProducts() {
        ProductDto product = ProductDto.builder()
                .id(5L)
                .name("Rubber Duck")
                .count(200)
                .price(BigDecimal.valueOf(300L))
                .build();

        return ProductsResponse.builder()
                .products(List.of(product))
                .build();
    }

    public TransactionResponse buy(PurchaseRequest request) {
        return null;
    }
}