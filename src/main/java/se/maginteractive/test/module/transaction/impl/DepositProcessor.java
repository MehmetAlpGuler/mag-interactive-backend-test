package se.maginteractive.test.module.transaction.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.maginteractive.test.enums.TransactionType;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.module.transaction.TransactionProcessor;
import se.maginteractive.test.payload.TransactionProcessorDto;

import java.time.ZonedDateTime;

import static se.maginteractive.test.enums.TransactionType.DEPOSIT;

@Component
public class DepositProcessor implements TransactionProcessor {

    @Transactional
    @Override
    public Transaction apply(TransactionProcessorDto transactionProcessorDto) {
        validate(transactionProcessorDto);

        Account account = transactionProcessorDto.getAccount();
        account.setBalance(account.getBalance().add(transactionProcessorDto.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(transactionProcessorDto.getAmount());
        transaction.setType(getType());
        transaction.setDate(ZonedDateTime.now());
        return transaction;
    }

    private void validate(TransactionProcessorDto transactionProcessorDto) {
        validateSmallAmount(transactionProcessorDto.getAmount());
    }

    @Override
    public TransactionType getType() {
        return DEPOSIT;
    }
}