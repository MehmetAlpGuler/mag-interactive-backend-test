package se.maginteractive.test.service;

import se.maginteractive.test.model.Account;

import java.util.Optional;

public interface AccountService {

    Optional<Account> findById(long id);

    Account create();

    Account update(long id, Account account);
}
