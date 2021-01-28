package se.maginteractive.test.module.transaction;

import se.maginteractive.test.exception.InsufficientBalanceException;
import se.maginteractive.test.exception.InsufficientStockException;
import se.maginteractive.test.exception.MAGException;
import se.maginteractive.test.exception.SmallAmountException;
import se.maginteractive.test.model.Product;

import java.math.BigDecimal;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static java.math.BigDecimal.ZERO;
import static java.util.Objects.isNull;

public interface TransactionValidator {

    default void validateProductNull (Product product) {
        if (isNull(product)) {
            throw new MAGException("the product object is mandatory for this transaction type!");
        }
    }

    default void validateSmallAmount(BigDecimal amount) {
        if (isNull(amount)) {
            throw new MAGException("the amount is mandatory for this transaction type!");
        }
        if (smallAmount.test(amount)) {
            throw new SmallAmountException();
        }
    }

    default void validateInsufficientBalance (BigDecimal newBalance, BigDecimal balance, BigDecimal amount) {
        if (insufficientBalance.test(newBalance)) {
            throw new InsufficientBalanceException(balance.toString(), amount.toString());
        }
    }

    default void validateInsufficientStock (Integer stockCount) {
        if (smallCount.test(stockCount)) {
            throw new InsufficientStockException();
        }
    }

    IntPredicate smallCount = a -> (a < 1 );

    Predicate<BigDecimal> smallAmount = a -> (ZERO.compareTo(a) > -1);

    Predicate<BigDecimal> insufficientBalance = a -> (ZERO.compareTo(a) > 0);
}
