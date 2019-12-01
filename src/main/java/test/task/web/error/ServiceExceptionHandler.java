package test.task.web.error;

import test.task.exception.InsufficientBalanceException;
import test.task.exception.NonExistentAccountException;
import test.task.exception.RevolutException;
import test.task.model.ErrorCode;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;
import java.util.Map;

@Produces
@Singleton
public class ServiceExceptionHandler implements ExceptionHandler<RevolutException, HttpResponse> {

    private static final Map<Class, HttpStatus> errorTypeToHttpStatus = Map.of(
            NonExistentAccountException.class, HttpStatus.NOT_FOUND,
            InsufficientBalanceException.class, HttpStatus.BAD_REQUEST
    );

    @Override
    public HttpResponse handle(HttpRequest request, RevolutException exception) {
        HttpStatus status = errorTypeToHttpStatus.get(exception.getClass());
        if (null == status) {
            return defaultResponse();
        }
        return HttpResponse.status(status).body(new ErrorDto(
                exception.getMessage(), exception.getErrorCode()
        ));
    }

    private HttpResponse defaultResponse() {
        return HttpResponse.serverError(new ErrorDto(
                "Ooops, something went wrong. We are already working on it :)",
                ErrorCode.GENERAL_ERROR
        ));
    }
}
