package se.maginteractive.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.maginteractive.test.exception.AccountNotFountException;
import se.maginteractive.test.exception.ResourceNotFoundException;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.module.transaction.TransactionProcessorFactory;
import se.maginteractive.test.payload.TransactionProcessorDto;
import se.maginteractive.test.repository.AccountRepository;
import se.maginteractive.test.service.AccountService;

import java.util.Optional;

import static java.math.BigDecimal.ZERO;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionProcessorFactory transactionProcessorFactory;

    @Transactional(readOnly = true)
    @Override
    public Optional<Account> findById(long id) {
        return accountRepository.findById(id);
    }

    @Transactional
    @Override
    public Account create() {
        return accountRepository.save(Account.builder().balance(ZERO).build());
    }

    @Transactional
    @Override
    public Account update(long id, Account account) {
        findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        account.setId(id);
        return accountRepository.save(account);
    }

    @Transactional
    @Override
    public Account deposit(Transaction transaction) {
        return addTransaction(transaction);
    }

    @Transactional
    @Override
    public Account withdraw(Transaction transaction) {
        return addTransaction(transaction);
    }

    private Account addTransaction(Transaction transaction) {
        Account account = accountRepository.findById(transaction.getAccount().getId())
                .orElseThrow(AccountNotFountException::new);

        TransactionProcessorDto transactionProcessorDto = TransactionProcessorDto.builder()
                .account(account)
                .amount(transaction.getAmount())
                .build();

        return transactionProcessorFactory.getTransaction(transaction.getType())
                .apply(transactionProcessorDto)
                .getAccount();
    }
}
