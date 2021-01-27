package se.maginteractive.test.module.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.maginteractive.test.enums.TransactionType;

import java.util.List;

@Component
public class TransactionProcessorFactory {

    @Autowired
    private List<TransactionProcessor> transactions;

    public TransactionProcessor getTransaction(TransactionType transactionType) {
        return transactions.stream()
                .filter(transaction -> transaction.getType() == transactionType)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(" Invalid Transaction Type - %s", transactionType)));
    }
}