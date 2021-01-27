package se.maginteractive.test.module.transaction;

import lombok.NonNull;
import se.maginteractive.test.enums.TransactionType;
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.payload.TransactionProcessorDto;

public interface TransactionProcessor extends TransactionValidator{

    Transaction apply(@NonNull TransactionProcessorDto transactionProcessorDto);

    TransactionType getType();
}
