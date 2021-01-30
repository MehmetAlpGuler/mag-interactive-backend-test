package se.maginteractive.test.controller.accounts.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import se.maginteractive.test.payload.request.AccountWithdrawRequest;
import se.maginteractive.test.payload.response.AccountResponse;
import se.maginteractive.test.payload.response.TransactionsResponse;
import se.maginteractive.test.service.AccountService;
import se.maginteractive.test.service.TransactionService;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final ModelMapper modelMapper;

    public ResponseEntity<AccountResponse> create() {
        log.info("AccountControllerImpl create: Create New Product Request");
        Account account = accountService.create();

        //return the URI to the created recording
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/account/{id}")
                .buildAndExpand(account.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(modelMapper.map(account, AccountResponse.class));
    }

    public AccountResponse findById(Long id) {
        Account product = accountService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        return modelMapper.map(product, AccountResponse.class);
    }

    public AccountResponse deposit(AccountDepositRequest request) {
        Account account = accountService.findById(request.getAccountId())
                .orElseThrow(AccountNotFountException::new);

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(request.getAmount())
                .date(ZonedDateTime.now())
                .type(TransactionType.DEPOSIT)
                .build();
        return modelMapper.map(transactionService.deposit(transaction), AccountResponse.class);
    }

    public AccountResponse withdraw(AccountWithdrawRequest request) {
        Account account = accountService.findById(request.getAccountId())
                .orElseThrow(AccountNotFountException::new);

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(request.getAmount())
                .date(ZonedDateTime.now())
                .type(TransactionType.WITHDRAW)
                .build();
        return modelMapper.map(transactionService.withdraw(transaction), AccountResponse.class);
    }

    public TransactionsResponse findAllTransactions(AccountRequest request) {
        List<TransactionDto> transactions = transactionService.findByAccountId(request.getAccountId())
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .collect(Collectors.toList());
        return TransactionsResponse.builder().transactions(transactions).build();
    }
}
