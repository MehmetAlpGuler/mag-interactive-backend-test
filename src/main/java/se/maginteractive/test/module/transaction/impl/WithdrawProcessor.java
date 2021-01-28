package se.maginteractive.test.module.transaction.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.maginteractive.test.enums.TransactionType;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.module.transaction.TransactionProcessor;
import se.maginteractive.test.payload.TransactionProcessorDto;
import se.maginteractive.test.service.TransactionService;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static se.maginteractive.test.enums.TransactionType.WITHDRAW;

@Component
@RequiredArgsConstructor
public class WithdrawProcessor implements TransactionProcessor {

    private final TransactionService transactionService;

    @Transactional
    @Override
    public Transaction apply(TransactionProcessorDto transactionProcessorDto) {
        validate(transactionProcessorDto);

        Account account = transactionProcessorDto.getAccount();
        account.setBalance(account.getBalance().subtract(transactionProcessorDto.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(transactionProcessorDto.getAmount());
        transaction.setType(getType());
        transaction.setDate(ZonedDateTime.now());
        transactionService.create(transaction);

        return transaction;
    }

    private void validate(TransactionProcessorDto transactionProcessorDto) {
        Account account = transactionProcessorDto.getAccount();
        BigDecimal newBalance = account.getBalance().subtract(transactionProcessorDto.getAmount());

        validateSmallAmount(transactionProcessorDto.getAmount());
        validateInsufficientBalance(newBalance, account.getBalance(), transactionProcessorDto.getAmount());
    }


    @Override
    public TransactionType getType() {
        return WITHDRAW;
    }
}
