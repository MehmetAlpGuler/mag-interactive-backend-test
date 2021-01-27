package se.maginteractive.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException() {
        super("Product stock is not enough!");
    }
}
