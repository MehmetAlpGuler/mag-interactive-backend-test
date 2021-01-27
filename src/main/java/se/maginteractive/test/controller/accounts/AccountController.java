package se.maginteractive.test.controller.accounts;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.maginteractive.test.payload.request.AccountDepositRequest;
import se.maginteractive.test.payload.request.AccountRequest;
import se.maginteractive.test.payload.request.AccountWithdrawRequest;
import se.maginteractive.test.payload.response.AccountResponse;
import se.maginteractive.test.payload.response.TransactionsResponse;

import javax.validation.Valid;

@RestController
public interface AccountController {

    //@PostMapping("/accounts")  It can be enough like this.
    @PostMapping("/account/create")
    ResponseEntity<AccountResponse> create();

    @GetMapping("/account/{id}")
    AccountResponse findById(@PathVariable(value = "id") Long id);

    //@PostMapping("/account/{id}/deposit")
    @PostMapping("/account/deposit")
    AccountResponse  deposit(@RequestBody AccountDepositRequest request);

    //@PostMapping("/account/{id}/withdraw")
    @PostMapping("/account/withdraw")
    AccountResponse withdraw(@RequestBody AccountWithdrawRequest request);

    //I would prefer like below. But It can be more secure with POST and request body style.
    //@GetMapping("/account/{id}/listTransactions")
    @PostMapping("/account/listTransactions")
    TransactionsResponse findAllTransactions(@Valid @RequestBody AccountRequest request);

}
