package test.task.exception;

import test.task.model.ErrorCode;

public class NonExistentAccountException extends RevolutException {

    public NonExistentAccountException(String message) {
        super(message);
        setErrorCode(ErrorCode.NON_EXISTENT_ACCOUNT);
    }
}
