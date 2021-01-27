package se.maginteractive.test.controller.store.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.payload.ProductDto;
import se.maginteractive.test.payload.TransactionDto;
import se.maginteractive.test.payload.request.PurchaseRequest;
import se.maginteractive.test.payload.response.ProductsResponse;
import se.maginteractive.test.payload.response.TransactionResponse;
import se.maginteractive.test.service.TransactionService;
import se.maginteractive.test.controller.store.StoreController;
import se.maginteractive.test.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StoreControllerImpl implements StoreController {

    private final TransactionService transactionService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @GetMapping("/store/list")
    public ProductsResponse findProducts() {
        List<ProductDto> productDtos = productService.findAll()
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());

        return ProductsResponse.builder().products(productDtos).build();
    }

    @PostMapping("/store/buy")
    public TransactionResponse buy(PurchaseRequest request) {
        Transaction transaction = transactionService.buyProductByAccountIdAndProductId(request.getAccountId(), request.getProductId());

        return new TransactionResponse(modelMapper.map(transaction, TransactionDto.class));
    }
}
