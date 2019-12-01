package test.task.web;

import io.micronaut.http.client.exceptions.HttpClientResponseException;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.fail;

public class WebClientUtils {

    private WebClientUtils() {
    }

    public static void expectErrorResponse(Runnable httpClientInvocation, Consumer<HttpClientResponseException> errorConsumer) {
        try {
            httpClientInvocation.run();
            fail();
        } catch (HttpClientResponseException e) {
            errorConsumer.accept(e);
        }
    }
}
