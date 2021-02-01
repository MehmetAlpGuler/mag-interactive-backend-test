package se.maginteractive.test.controller.accounts;

import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.maginteractive.test.payload.request.AccountDepositRequest;
import se.maginteractive.test.payload.request.AccountRequest;
import se.maginteractive.test.payload.response.AccountResponse;
import se.maginteractive.test.payload.response.AccountsResponse;
import se.maginteractive.test.payload.response.TransactionsResponse;

import javax.validation.Valid;

@RestController
public interface AccountController  {

    @GetMapping("/accounts")
    AccountsResponse findAll();

    @GetMapping("/accounts/{id}")
    AccountResponse findById(@NonNull @PathVariable(value = "id") Long id);

    @PostMapping("/accounts")
    ResponseEntity<AccountResponse> create();

    @PutMapping("/accounts/{id}")
    ResponseEntity<AccountResponse> update(@NonNull @PathVariable(value = "id") Long id,@Valid AccountRequest accountRequest);

    @DeleteMapping("/accounts/{id}")
    void delete(@PathVariable(value = "id") Long id);

    @PostMapping("/accounts/{id}/deposit")
    AccountResponse deposit(@NonNull @PathVariable(value = "id") Long id, @RequestBody AccountDepositRequest request);

    @PostMapping("/accounts/{id}/withdraw")
    AccountResponse withdraw(@NonNull @PathVariable(value = "id") Long id, @RequestBody AccountDepositRequest request);

    @PostMapping("/accounts/{id}/transactions")
    TransactionsResponse findAllTransactions(@NonNull @PathVariable(value = "id") Long id);

}
