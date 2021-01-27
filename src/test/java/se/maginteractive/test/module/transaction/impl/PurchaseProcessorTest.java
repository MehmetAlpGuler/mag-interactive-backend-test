package se.maginteractive.test.module.transaction.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.maginteractive.test.exception.InsufficientBalanceException;
import se.maginteractive.test.exception.InsufficientStockException;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.model.Product;
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.payload.TransactionProcessorDto;
import se.maginteractive.test.service.TransactionService;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static se.maginteractive.test.enums.TransactionType.PURCHASE;

@DisplayName("Purchase Processor Test")
@ExtendWith(MockitoExtension.class)
class PurchaseProcessorTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private PurchaseProcessor service;

    private TransactionProcessorDto transactionProcessorDto;
    private Account account;
    private Product product;

    @BeforeEach
    void setUp() {
        account = Account.builder().id(1L).balance(BigDecimal.valueOf(1000)).build();
        product = Product.builder().id(1L).price(BigDecimal.valueOf(300)).count(200).name("Rubber Duck").build();
        transactionProcessorDto = TransactionProcessorDto.builder()
                .account(account)
                .amount(BigDecimal.valueOf(300))
                .product(product)
                .build();
    }

    @Test
    void apply() {
        //given
        given(transactionService.create(any(Transaction.class))).willReturn(new Transaction());

        //when
        Transaction savedTransaction = service.apply(transactionProcessorDto);

        //then
        then(transactionService).should().create(any(Transaction.class));
        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(300));
        assertThat(savedTransaction.getAccount().getBalance()).isEqualByComparingTo(BigDecimal.valueOf(700));
        assertThat(product.getCount()).isEqualByComparingTo(199);
    }

    @DisplayName("Purchase Small Amount Exception")
    @Test
    void purchase_not_enough_stock_exception() {
        //given
        product.setCount(0);

        //when
        assertThrows(
                InsufficientStockException.class,
                () -> service.apply(transactionProcessorDto));
    }

    @DisplayName("Purchase Insufficient Balance Exception")
    @Test
    void purchase_insufficient_balance_exception() {
        //given
        account.setBalance(ONE);
        //when
        assertThrows(
                InsufficientBalanceException.class,
                () -> service.apply(transactionProcessorDto));
    }

    @Test
    void getType() {
        assertEquals(PURCHASE, service.getType());
    }
}