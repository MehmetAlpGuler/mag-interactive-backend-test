package se.maginteractive.test.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.maginteractive.test.exception.AccountNotFountException;
import se.maginteractive.test.exception.ResourceNotFoundException;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.model.Product;
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.module.transaction.TransactionProcessorFactory;
import se.maginteractive.test.payload.TransactionProcessorDto;
import se.maginteractive.test.repository.TransactionRepository;
import se.maginteractive.test.service.AccountService;
import se.maginteractive.test.service.ProductService;
import se.maginteractive.test.service.TransactionService;

import java.util.List;
import java.util.Optional;

import static se.maginteractive.test.enums.TransactionType.PURCHASE;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final ProductService productService;
    private final TransactionProcessorFactory transactionProcessorFactory;

    @Transactional(readOnly = true)
    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Transaction>  findByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @Transactional
    @Override
    public Transaction create(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Transactional
    @Override
    public Transaction buyProductByAccountIdAndProductId(long accountId, long productId) {
        Account account = accountService.findById(accountId)
                .orElseThrow(AccountNotFountException::new);

        Product product = productService.findById(productId)
                .filter(p -> p.getCount().compareTo(0) > 0)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock ", "id", productId));

        TransactionProcessorDto transactionProcessorDto = TransactionProcessorDto.builder()
                .account(account)
                .product(product)
                .build();

        return transactionProcessorFactory.getTransaction(PURCHASE)
                .apply(transactionProcessorDto);
    }
}
