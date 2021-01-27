package se.maginteractive.test.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionRequest {

    private Long accountId;

    @NonNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;
}
