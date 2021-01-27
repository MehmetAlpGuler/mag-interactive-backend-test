package se.maginteractive.test.controller.store;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.maginteractive.test.payload.request.PurchaseRequest;
import se.maginteractive.test.payload.response.ProductsResponse;
import se.maginteractive.test.payload.response.TransactionResponse;

@RestController
public interface StoreController {

    @GetMapping("/store/list")
    ProductsResponse findProducts();

    @PostMapping("/store/buy")
    TransactionResponse buy(@RequestBody PurchaseRequest request);
}
