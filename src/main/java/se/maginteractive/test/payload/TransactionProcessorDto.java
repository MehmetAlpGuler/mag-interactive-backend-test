package se.maginteractive.test.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import se.maginteractive.test.model.Account;
import se.maginteractive.test.model.Product;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionProcessorDto {

    @NonNull
    private Account account;

    private BigDecimal amount;

    private Product product;
}
