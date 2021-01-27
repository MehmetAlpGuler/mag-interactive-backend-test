package se.maginteractive.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class SmallAmountException extends RuntimeException {
    public SmallAmountException() {
        super("Amount can not be zero or less!");
    }
}
