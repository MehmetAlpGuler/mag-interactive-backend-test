package se.maginteractive.test.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.maginteractive.test.exception.AccountNotFountException;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.module.transaction.TransactionProcessorFactory;
import se.maginteractive.test.module.transaction.impl.WithdrawProcessor;
import se.maginteractive.test.payload.TransactionProcessorDto;
import se.maginteractive.test.repository.AccountRepository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static se.maginteractive.test.enums.TransactionType.DEPOSIT;
import static se.maginteractive.test.enums.TransactionType.WITHDRAW;

@DisplayName("Account Service Tests")
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionProcessorFactory transactionProcessorFactory;

    @Mock
    private WithdrawProcessor withdrawProcessorBean;

    @InjectMocks
    private AccountServiceImpl service;

    @DisplayName("Account Create")
    @Test
    void create() {
        //given
        Account account = Account.builder()
                .id(1L)
                .balance(ZERO)
                .build();
        given(accountRepository.save(any(Account.class))).willReturn(account);

        //when
        Account savedAccount = service.create();

        //then
        then(accountRepository).should().save(any(Account.class));
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getBalance()).isEqualByComparingTo(ZERO);
    }

    @DisplayName("Account Find By Id")
    @Test
    void findById() {
        //given
        Account account = Account.builder()
                .id(1L)
                .balance(ZERO)
                .build();
        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));

        //when
        Optional<Account> foundAccount = service.findById(anyLong());

        //then
        then(accountRepository).should().findById(anyLong());
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getId()).isEqualByComparingTo(1L);
    }

    @DisplayName("Product Find By Id Not Found")
    @Test
    void findById_not_found() {
        //given
        given(accountRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        Optional<Account> foundAccount = service.findById(anyLong());

        //then
        then(accountRepository).should().findById(anyLong());
        assertThat(foundAccount).isNotPresent();
    }

    @DisplayName("Deposit Success")
    @Test
    void deposit_success() {
        //given
        Account account = Account.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(1000))
                .build();

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(BigDecimal.valueOf(100))
                .type(DEPOSIT)
                .date(ZonedDateTime.now())
                .build();

        Transaction transactionResult = Transaction.builder()
                .account(Account.builder()
                        .id(1L)
                        .balance(BigDecimal.valueOf(1100))
                        .build())
                .amount(BigDecimal.valueOf(100))
                .type(WITHDRAW)
                .date(ZonedDateTime.now())
                .build();

        TransactionProcessorDto transactionProcessorDto = TransactionProcessorDto.builder()
                .account(account)
                .amount(transaction.getAmount())
                .build();

        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));
        given(transactionProcessorFactory.getTransaction(DEPOSIT)).willReturn(withdrawProcessorBean);
        given(withdrawProcessorBean.apply(transactionProcessorDto)).willReturn(transactionResult);

        //when
        Account savedAccount = service.deposit(transaction);

        //then
        then(accountRepository).should().findById(anyLong());
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1100L));
    }

    @DisplayName("Deposit Account Not Found")
    @Test
    void deposit_account_not_found() {
        //given
        Account account = Account.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(1000))
                .build();

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(BigDecimal.valueOf(100))
                .type(DEPOSIT)
                .date(ZonedDateTime.now())
                .build();

        given(accountRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(
                RuntimeException.class,
                () -> service.deposit(transaction));

        //then
        assertEquals("Account not found!", exception.getMessage());
    }

    @DisplayName("Withdraw Success")
    @Test
    void withdraw_success() {
        //given
        Account account = Account.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(1000))
                .build();

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(BigDecimal.valueOf(100))
                .type(WITHDRAW)
                .date(ZonedDateTime.now())
                .build();

        Transaction transactionResult = Transaction.builder()
                .account(Account.builder()
                        .id(1L)
                        .balance(BigDecimal.valueOf(900))
                        .build())
                .amount(BigDecimal.valueOf(100))
                .type(WITHDRAW)
                .date(ZonedDateTime.now())
                .build();


        TransactionProcessorDto transactionProcessorDto = TransactionProcessorDto.builder()
                .account(account)
                .amount(transaction.getAmount())
                .build();

        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));
        given(transactionProcessorFactory.getTransaction(WITHDRAW)).willReturn(withdrawProcessorBean);
        given(withdrawProcessorBean.apply(transactionProcessorDto)).willReturn(transactionResult);

        //when
        Account savedAccount = service.withdraw(transaction);

        //then
        then(accountRepository).should().findById(anyLong());
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(900));
    }

    @DisplayName("Withdraw Account Not Found")
    @Test
    void withdraw_account_not_found() {
        //given
        Account account = Account.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(1000))
                .build();

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(BigDecimal.valueOf(100))
                .type(WITHDRAW)
                .date(ZonedDateTime.now())
                .build();

        given(accountRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(
                AccountNotFountException.class,
                () -> service.withdraw(transaction));

        //then
        assertEquals("Account not found!", exception.getMessage());
    }
}