package se.maginteractive.test.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.module.transaction.TransactionProcessorFactory;
import se.maginteractive.test.module.transaction.impl.WithdrawProcessor;
import se.maginteractive.test.repository.AccountRepository;

import java.util.Optional;

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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

    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder().id(1L).balance(ZERO).build();
    }

    @DisplayName("Account Find By Id")
    @Test
    void findById() {
        //given
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

    @DisplayName("Account Create")
    @Test
    void create() {
        //given
        given(accountRepository.save(any(Account.class))).willReturn(account);

        //when
        Account savedAccount = service.create();

        //then
        then(accountRepository).should().save(any(Account.class));
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getBalance()).isEqualByComparingTo(ZERO);
    }

    @DisplayName("Product Update")
    @Test
    void update() {
        //given
        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));
        given(accountRepository.save(any(Account.class))).willReturn(account);

        //when
        Account savedProduct = service.update(anyLong(), account);

        //then
        then(accountRepository).should().save(any(Account.class));
        assertThat(savedProduct).isNotNull();
    }
}