package test.task.exception;

import test.task.model.ErrorCode;

public class InsufficientBalanceException extends RevolutException {

    public InsufficientBalanceException(String message) {
        super(message);
        setErrorCode(ErrorCode.INSUFFICIENT_BALANCE);
    }
}
