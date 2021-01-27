package se.maginteractive.test.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDto {

    private Long id;

    @NonNull
    private String name;
    @NonNull
    @Positive
    private BigDecimal price;

    @NonNull
    @PositiveOrZero
    private Integer count;
}
