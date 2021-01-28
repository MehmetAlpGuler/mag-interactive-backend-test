package se.maginteractive.test.module.transaction.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.maginteractive.test.enums.TransactionType;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.model.Product;
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.module.transaction.TransactionProcessor;
import se.maginteractive.test.payload.TransactionProcessorDto;
import se.maginteractive.test.service.TransactionService;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static se.maginteractive.test.enums.TransactionType.PURCHASE;

@Component
@RequiredArgsConstructor
public class PurchaseProcessor implements TransactionProcessor {

    private final TransactionService transactionService;

    @Transactional
    @Override
    public Transaction apply(TransactionProcessorDto transactionProcessorDto) {
        Account account = transactionProcessorDto.getAccount();
        Product product = transactionProcessorDto.getProduct();
        validateProductNull(product);

        BigDecimal newBalance = account.getBalance().subtract(product.getPrice());

        validateInsufficientStock(product.getCount());
        validateInsufficientBalance(newBalance, account.getBalance(), product.getPrice());

        account.setBalance(newBalance);

        product.setCount(product.getCount() - 1);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(product.getPrice());
        transaction.setType(getType());
        transaction.setDate(ZonedDateTime.now());
        transaction.setProduct(product);
        transactionService.create(transaction);

        return transaction;
    }

    @Override
    public TransactionType getType() {
        return PURCHASE;
    }
}
