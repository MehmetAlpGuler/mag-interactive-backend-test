package se.maginteractive.test.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.maginteractive.test.exception.AccountNotFountException;
import se.maginteractive.test.exception.ResourceNotFoundException;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.model.Product;
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.module.transaction.TransactionProcessorFactory;
import se.maginteractive.test.module.transaction.impl.WithdrawProcessor;
import se.maginteractive.test.payload.TransactionProcessorDto;
import se.maginteractive.test.repository.TransactionRepository;
import se.maginteractive.test.service.AccountService;
import se.maginteractive.test.service.ProductService;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static se.maginteractive.test.enums.TransactionType.DEPOSIT;
import static se.maginteractive.test.enums.TransactionType.PURCHASE;
import static se.maginteractive.test.enums.TransactionType.WITHDRAW;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private ProductService productService;

    @Mock
    private TransactionProcessorFactory transactionProcessorFactory;

    @Mock
    private WithdrawProcessor withdrawProcessorBean;

    @InjectMocks
    private TransactionServiceImpl service;

    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(1000))
                .build();
    }

    @DisplayName("Transactions Find All")
    @Test
    void findAll() {
        //given
        given(transactionRepository.findAll()).willReturn(List.of(new Transaction()));

        //when
        List<Transaction> transactionList = service.findAll();

        //then
        then(transactionRepository).should().findAll();
        assertThat(transactionList).hasSize(1);
    }

    @DisplayName("Transaction Find By Id")
    @Test
    void findById() {
        //given
        given(transactionRepository.findById(anyLong())).willReturn(Optional.of(new Transaction()));

        //when
        Optional<Transaction> foundTransaction = service.findById(anyLong());

        //then
        then(transactionRepository).should().findById(anyLong());
        assertThat(foundTransaction).isPresent();
    }

    @DisplayName("Transaction Find By Account Id")
    @Test
    void findByAccountId() {
        //given
        given(transactionRepository.findByAccountId(anyLong())).willReturn(List.of(new Transaction()));

        //when
        List<Transaction> foundTransaction = service.findByAccountId(anyLong());

        //then
        then(transactionRepository).should().findByAccountId(anyLong());
        assertThat(foundTransaction).isNotNull();
    }

    @DisplayName("Transactions Create")
    @Test
    void create() {
        //given
        given(transactionRepository.save(any(Transaction.class))).willReturn(new Transaction());

        //when
        Transaction savedTransaction = service.create(new Transaction());

        //then
        then(transactionRepository).should().save(any(Transaction.class));
        assertThat(savedTransaction).isNotNull();
    }

    @DisplayName("Buy Product With AccountId And ProductId")
    @Test
    void buyProductByAccountIdAndProductId() {
        //given
        Product product = Product.builder()
                .id(1L)
                .price(BigDecimal.valueOf(200))
                .count(10)
                .name("Rubber Duck")
                .build();

        Transaction transactionResult = Transaction.builder()
                .account(Account.builder()
                        .id(1L)
                        .balance(BigDecimal.valueOf(800))
                        .build())
                .amount(BigDecimal.valueOf(200))
                .type(PURCHASE)
                .date(ZonedDateTime.now())
                .build();

        TransactionProcessorDto transactionProcessorDto = TransactionProcessorDto.builder()
                .account(account)
                .product(product)
                .build();

        given(accountService.findById(anyLong())).willReturn(Optional.of(account));
        given(productService.findById(anyLong())).willReturn(Optional.of(product));
        given(transactionProcessorFactory.getTransaction(PURCHASE)).willReturn(withdrawProcessorBean);
        given(withdrawProcessorBean.apply(transactionProcessorDto)).willReturn(transactionResult);
        given(transactionRepository.save(any())).willReturn(transactionResult);

        //when
        Transaction savedTransaction = service.buyProductByAccountIdAndProductId(1L, 1L);

        //then
        then(accountService).should().findById(anyLong());
        then(productService).should().findById(anyLong());
        then(transactionProcessorFactory).should().getTransaction(any());
        then(withdrawProcessorBean).should().apply(any());
        then(transactionRepository).should().save(any());
        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(200));
        assertThat(savedTransaction.getAccount().getBalance()).isEqualByComparingTo(BigDecimal.valueOf(800));
    }


    @DisplayName("Purchase Account Not Found")
    @Test
    void purchase_account_not_found() {
        //given
        given(accountService.findById(anyLong())).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(
                AccountNotFountException.class,
                () -> service.buyProductByAccountIdAndProductId(1L, 1L));

        //then
        then(accountService).should().findById(anyLong());
        assertEquals("Account not found!", exception.getMessage());
    }

    @DisplayName("Purchase Product Not Found")
    @Test
    void purchase_product_not_found() {
        //given
        given(accountService.findById(anyLong())).willReturn(Optional.of(account));
        given(productService.findById(anyLong())).willReturn(Optional.empty());


        //when
        assertThrows(
                ResourceNotFoundException.class,
                () -> service.buyProductByAccountIdAndProductId(1L, 1L));

        //then
        then(accountService).should().findById(anyLong());
        then(productService).should().findById(anyLong());
    }

    @DisplayName("Deposit Success")
    @Test
    void deposit_success() {
        //given
        account.setBalance(BigDecimal.valueOf(1000));

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

        given(accountService.findById(anyLong())).willReturn(Optional.of(account));
        given(transactionProcessorFactory.getTransaction(DEPOSIT)).willReturn(withdrawProcessorBean);
        given(withdrawProcessorBean.apply(transactionProcessorDto)).willReturn(transactionResult);

        //when
        Account savedAccount = service.deposit(transaction);

        //then
        then(accountService).should().findById(anyLong());
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1100L));
    }

    @DisplayName("Deposit Account Not Found")
    @Test
    void deposit_account_not_found() {
        //given
        account.setBalance(BigDecimal.valueOf(1000));

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(BigDecimal.valueOf(100))
                .type(DEPOSIT)
                .date(ZonedDateTime.now())
                .build();

        given(accountService.findById(anyLong())).willReturn(Optional.empty());

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
        account.setBalance(BigDecimal.valueOf(1000));

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

        given(accountService.findById(anyLong())).willReturn(Optional.of(account));
        given(transactionProcessorFactory.getTransaction(WITHDRAW)).willReturn(withdrawProcessorBean);
        given(withdrawProcessorBean.apply(transactionProcessorDto)).willReturn(transactionResult);

        //when
        Account savedAccount = service.withdraw(transaction);

        //then
        then(accountService).should().findById(anyLong());
        then(transactionProcessorFactory).should().getTransaction(any());
        then(withdrawProcessorBean).should().apply(any());
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(900));
    }

    @DisplayName("Withdraw Account Not Found")
    @Test
    void withdraw_account_not_found() {
        //given
        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(BigDecimal.valueOf(100))
                .type(WITHDRAW)
                .date(ZonedDateTime.now())
                .build();

        given(accountService.findById(anyLong())).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(
                AccountNotFountException.class,
                () -> service.withdraw(transaction));

        //then
        then(accountService).should().findById(anyLong());
        assertEquals("Account not found!", exception.getMessage());
    }

    @Test
    void addTransaction() {
        //given
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

        given(accountService.findById(anyLong())).willReturn(Optional.of(account));
        given(transactionProcessorFactory.getTransaction(WITHDRAW)).willReturn(withdrawProcessorBean);
        given(withdrawProcessorBean.apply(transactionProcessorDto)).willReturn(transactionResult);

        //when
        Account savedAccount = service.addTransaction(transaction);

        //then
        then(accountService).should().findById(anyLong());
        then(transactionProcessorFactory).should().getTransaction(any());
        then(withdrawProcessorBean).should().apply(any());
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(900));
    }
}