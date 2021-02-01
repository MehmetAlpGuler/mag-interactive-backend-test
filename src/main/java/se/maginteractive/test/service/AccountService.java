package se.maginteractive.test.service;

import se.maginteractive.test.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<Account> findAll();

    Optional<Account> findById(long id);

    Account create();

    Account update(long id, Account account);

    Account deleteById(long id);
}
