package se.maginteractive.test.module.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.maginteractive.test.enums.TransactionType;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionProcessorFactory {

    private final List<TransactionProcessor> transactions;

    public TransactionProcessor getTransaction(TransactionType transactionType) {
        return transactions.stream()
                .filter(transaction -> transaction.getType() == transactionType)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(" Invalid Transaction Type - %s", transactionType)));
    }
}