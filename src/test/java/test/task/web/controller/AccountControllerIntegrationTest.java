package test.task.web.controller;

import test.task.dto.CreateAccountRequestDto;
import test.task.dto.TransferRequestDto;
import test.task.dto.TransferResponseDto;
import test.task.model.Account;
import test.task.repo.AccountRepository;
import test.task.web.error.ErrorDto;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import test.task.web.WebClientUtils;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class AccountControllerIntegrationTest {

    @Inject
    AccountRepository accountRepository;
    @Inject
    @Client("/account")
    HttpClient client;

    @AfterEach
    void tearDown() {
        accountRepository.clear();
    }

    @Test
    void shouldGetAccount() throws Exception {
        Account account = createAccount();

        var request = HttpRequest.GET("/" + account.getId());
        var response = client.toBlocking().retrieve(request, Account.class);

        assertEquals(account.getBalance(), response.getBalance());
        assertEquals(account.getId(), response.getId());
    }

    @Test
    void shouldCreateAccount() throws Exception {
        var request = HttpRequest.POST("/", new CreateAccountRequestDto(10L));
        var response = client.toBlocking().retrieve(request, Account.class);

        var account = accountRepository.findById(response.getId());

        assertEquals(account.getBalance(), response.getBalance());
        assertEquals(account.getId(), response.getId());
    }

    @Test
    void shouldReturn400OnInvalidCreateAccountPayload() throws Exception {
        var request = HttpRequest.POST("/", new CreateAccountRequestDto());
        WebClientUtils.expectErrorResponse(
                () -> client.toBlocking().retrieve(request, Account.class),
                e -> assertEquals(HttpStatus.BAD_REQUEST, e.getStatus())
        );
    }

    @Test
    void shouldReturn404OnAccountDoesNotExist() throws Exception {
        var request = HttpRequest.GET("/" + 1);
        WebClientUtils.expectErrorResponse(
                () -> client.toBlocking().retrieve(request),
                e -> assertEquals(HttpStatus.NOT_FOUND, e.getStatus())
        );
    }

    @Test
    void shouldTransferAccountBalance() throws Exception {
        var accountFrom = createAccount();
        var accountTo = createAccount();
        var transferAmount = 10L;

        var requestBody = TransferRequestDto.builder()
                .accountIdFrom(accountFrom.getId())
                .accountIdTo(accountTo.getId())
                .amount(transferAmount)
                .build();
        var request = HttpRequest.POST("/transfer", requestBody);

        var response = client.toBlocking().retrieve(request, TransferResponseDto.class);

        assertResponseData(accountFrom, accountTo, transferAmount, response);
        assertStorageState(accountFrom, accountTo, response);
    }

    @Test
    void shouldReturn400OnInvalidTransferPayload() throws Exception {
        var request = HttpRequest.POST("/transfer", new TransferRequestDto());
        WebClientUtils.expectErrorResponse(
                () -> client.toBlocking().retrieve(request, TransferResponseDto.class),
                e -> assertEquals(HttpStatus.BAD_REQUEST, e.getStatus())
        );
    }

    @Test
    void shouldReturn400OnTransferInsufficientAccountFromBalance() throws Exception {
        var accountFrom = createAccount();
        var accountTo = createAccount();
        var transferAmount = 20L;

        var requestBody = TransferRequestDto.builder()
                .accountIdFrom(accountFrom.getId())
                .accountIdTo(accountTo.getId())
                .amount(transferAmount)
                .build();
        var request = HttpRequest.POST("/transfer", requestBody);

        WebClientUtils.expectErrorResponse(
                () -> client.toBlocking().exchange(request, Argument.of(TransferResponseDto.class), Argument.of(ErrorDto.class)),
                e -> assertEquals(HttpStatus.BAD_REQUEST, e.getResponse().getStatus())
        );
    }

    void assertStorageState(Account accountFrom, Account accountTo, TransferResponseDto response) {
        assertEquals(response.getAccountFromBalance(), accountRepository.findById(accountFrom.getId()).getBalance());
        assertEquals(response.getAccountToBalance(), accountRepository.findById(accountTo.getId()).getBalance());
    }

    void assertResponseData(Account accountFrom, Account accountTo, long transferAmount, TransferResponseDto response) {
        assertEquals(accountFrom.getBalance() - transferAmount, response.getAccountFromBalance());
        assertEquals(accountTo.getBalance() + transferAmount, response.getAccountToBalance());
    }

    Account createAccount() {
        return accountRepository.save(Account.builder().balance(10L).build());
    }
}