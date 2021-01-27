package se.maginteractive.test.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDepositRequest extends TransactionRequest{

    private Long accountId;

    @NonNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;
}
