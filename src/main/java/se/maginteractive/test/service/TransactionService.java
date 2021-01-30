package se.maginteractive.test.service;

import se.maginteractive.test.model.Account;
import se.maginteractive.test.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    List<Transaction> findAll();

    Optional<Transaction> findById(Long id);

    List<Transaction> findByAccountId(Long accountId);

    Transaction create(Transaction transaction);

    Transaction buyProductByAccountIdAndProductId(long accountId, long productId);

    Account deposit(Transaction transaction);

    Account withdraw(Transaction transaction);
}
