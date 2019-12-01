package test.task.service;

import test.task.dto.TransferRequestDto;
import test.task.model.Account;
import test.task.repo.AccountRepository;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class AccountServiceIntegrationTest {

    static final int CONCURRENT_TRANSFERS = 100;
    static final long INITIAL_BALANCE = 50000L;
    static final long TRANSFER_AMOUNT = 100L;

    @Inject
    AccountService service;
    @Inject
    AccountRepository accountRepository;

    @Test
    void shouldProduceCorrectBalanceOnConcurrentTransfers() throws Exception {
        var accountFrom = accountRepository.save(Account.builder().balance(INITIAL_BALANCE).build());
        var accountTo = accountRepository.save(Account.builder().balance(INITIAL_BALANCE).build());

        transferAllBalanceConcurrently(accountFrom, accountTo);

        Assertions.assertEquals(0L, accountRepository.findById(accountFrom.getId()).getBalance());
        Assertions.assertEquals(INITIAL_BALANCE * 2, accountRepository.findById(accountTo.getId()).getBalance());
    }

    void transferAllBalanceConcurrently(Account accountFrom, Account accountTo) throws InterruptedException {
        var executorService = Executors.newFixedThreadPool(CONCURRENT_TRANSFERS);

        var transferOperations = INITIAL_BALANCE / TRANSFER_AMOUNT;
        LongStream.range(0, transferOperations).forEach(i ->
                executorService.submit(() -> service.transfer(
                        new TransferRequestDto(accountFrom.getId(), accountTo.getId(), TRANSFER_AMOUNT)
                ))
        );
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }
}