package se.maginteractive.test.controller.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.maginteractive.test.controller.accounts.impl.AccountControllerImpl;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.payload.request.AccountDepositRequest;
import se.maginteractive.test.payload.request.AccountRequest;
import se.maginteractive.test.payload.request.AccountWithdrawRequest;
import se.maginteractive.test.service.AccountService;
import se.maginteractive.test.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AccountControllerImpl controller;

    private static final ObjectMapper om = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void findById() throws Exception {
        //given
        given(accountService.findById(1L)).willReturn(Optional.of(new Account()));

        //when
        mockMvc.perform(get("/api/v1/accounts/{id}", 1L))
                .andExpect(status().isOk());

        //then
        then(accountService).should().findById(1L);
    }

    @Test
    void create() throws Exception {
        //given
        given(accountService.create()).willReturn(new Account());

        //when
        mockMvc.perform(post("/api/v1/accounts"))
                .andExpect(status().isCreated());

        //then
        then(accountService).should().create();
    }

    @Test
    void deposit() throws Exception {
        //given
        AccountDepositRequest accountDepositRequest = AccountDepositRequest.builder()
                .accountId(1L)
                .amount(BigDecimal.valueOf(100L))
                .build();

        given(accountService.findById(anyLong())).willReturn(Optional.of(new Account()));
        given(transactionService.deposit(any())).willReturn(new Account());

        //when
        mockMvc.perform(post("/api/v1/accounts/{id}/deposit", anyLong())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(accountDepositRequest)))
                .andExpect(status().isOk());

        //then
        then(transactionService).should().deposit(any());
    }

    @Test
    void withdraw() throws Exception {
        //given
        AccountWithdrawRequest accountWithdrawRequest = AccountWithdrawRequest.builder()
                .accountId(1L)
                .amount(BigDecimal.valueOf(100L))
                .build();


        given(accountService.findById(anyLong())).willReturn(Optional.of(new Account()));
        given(transactionService.withdraw(any())).willReturn(new Account());

        //when
        mockMvc.perform(post("/api/v1/accounts/{id}/withdraw", anyLong())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(accountWithdrawRequest)))
                .andExpect(status().isOk());

        //then
        then(transactionService).should().withdraw(any());
    }

    @Test
    void listTransactions() throws Exception {
        //given
        AccountRequest accountRequest = AccountRequest.builder().accountId(1L).build();
        given(transactionService.findByAccountId(anyLong())).willReturn(List.of(new Transaction()));

        //when
        mockMvc.perform(post("/api/v1/accounts/{id}/transactions", anyLong())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(accountRequest)))
                .andExpect(status().isOk());

        //then
        then(transactionService).should().findByAccountId(anyLong());
    }
}