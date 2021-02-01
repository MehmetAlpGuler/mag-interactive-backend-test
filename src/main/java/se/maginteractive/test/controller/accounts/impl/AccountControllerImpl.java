package se.maginteractive.test.controller.accounts.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import se.maginteractive.test.controller.accounts.AccountController;
import se.maginteractive.test.enums.TransactionType;
import se.maginteractive.test.exception.AccountNotFountException;
import se.maginteractive.test.exception.ResourceNotFoundException;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.payload.TransactionDto;
import se.maginteractive.test.payload.request.AccountDepositRequest;
import se.maginteractive.test.payload.request.AccountRequest;
import se.maginteractive.test.payload.response.AccountResponse;
import se.maginteractive.test.payload.response.AccountsResponse;
import se.maginteractive.test.payload.response.TransactionsResponse;
import se.maginteractive.test.service.AccountService;
import se.maginteractive.test.service.TransactionService;

import javax.validation.Valid;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final ModelMapper modelMapper;

    @Override
    public AccountsResponse findAll() {
        List<AccountResponse> accounts = accountService.findAll()
                .stream()
                .map(account -> modelMapper.map(account, AccountResponse.class))
                .collect(Collectors.toList());

        return AccountsResponse.builder().accounts(accounts).build();
    }

    public AccountResponse findById(Long id) {
        Account product = accountService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        return modelMapper.map(product, AccountResponse.class);
    }

    @Override
    public ResponseEntity<AccountResponse> create() {
        log.info("AccountControllerImpl create: Create New Product Request");
        Account account = accountService.create();

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/account/{id}")
                .buildAndExpand(account.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(modelMapper.map(account, AccountResponse.class));
    }

    @Override
    public ResponseEntity<AccountResponse> update(@NonNull Long id, @Valid AccountRequest accountRequest) {
        Account account = modelMapper.map(accountRequest, Account.class);
        account = accountService.update(id, account);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/account/{id}")
                .buildAndExpand(account.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(modelMapper.map(account, AccountResponse.class));
    }


    @Override
    public void delete(Long id) {
        accountService.deleteById(id);
    }

    @Override
    public AccountResponse deposit(Long id, AccountDepositRequest request) {
        Account account = accountService.findById(id)
                .orElseThrow(AccountNotFountException::new);

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(request.getAmount())
                .date(ZonedDateTime.now())
                .type(TransactionType.DEPOSIT)
                .build();

        return modelMapper.map(transactionService.deposit(transaction), AccountResponse.class);
    }

    @Override
    public AccountResponse withdraw(Long id, AccountDepositRequest request) {
        Account account = accountService.findById(id)
                .orElseThrow(AccountNotFountException::new);

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(request.getAmount())
                .date(ZonedDateTime.now())
                .type(TransactionType.WITHDRAW)
                .build();

        return modelMapper.map(transactionService.withdraw(transaction), AccountResponse.class);
    }

    @Override
    public TransactionsResponse findAllTransactions(Long id) {
        List<TransactionDto> transactions = transactionService.findByAccountId(id)
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .collect(Collectors.toList());

        return TransactionsResponse.builder().transactions(transactions).build();
    }
}
