package se.maginteractive.test.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.maginteractive.test.exception.AccountNotFountException;
import se.maginteractive.test.exception.ResourceNotFoundException;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.repository.AccountRepository;
import se.maginteractive.test.service.AccountService;

import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

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
    public Account deleteById(long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(AccountNotFountException::new);
        accountRepository.deleteById(id);

        return account;
    }
}
