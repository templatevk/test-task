package test.task.web.controller;

import test.task.dto.CreateAccountRequestDto;
import test.task.dto.TransferRequestDto;
import test.task.dto.TransferResponseDto;
import test.task.model.Account;
import test.task.service.AccountService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@Controller("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Get("/{accountId}")
    public Account get(@PathVariable Long accountId) {
        return accountService.getBalance(accountId);
    }

    @Post
    public Account create(@Valid @Body CreateAccountRequestDto request) {
        return accountService.create(request);
    }

    @Post("/transfer")
    public TransferResponseDto transfer(@Valid @Body TransferRequestDto request) {
        return accountService.transfer(request);
    }
}
