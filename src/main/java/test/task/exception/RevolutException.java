package test.task.exception;

import test.task.model.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class RevolutException extends RuntimeException {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private ErrorCode errorCode;

    public RevolutException(String message) {
        super(message);
        this.errorCode = ErrorCode.GENERAL_ERROR;
    }

    public RevolutException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
