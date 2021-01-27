package se.maginteractive.test.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import se.maginteractive.test.enums.TransactionType;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDto {

    @NonNull
    private Long id;

    @NonNull
    private Long accountId;

    @Positive
    private BigDecimal amount;

    private ZonedDateTime date;

    private TransactionType type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long productId;
}
