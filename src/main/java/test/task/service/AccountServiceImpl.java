package test.task.service;

import test.task.dto.CreateAccountRequestDto;
import test.task.dto.TransferRequestDto;
import test.task.dto.TransferResponseDto;
import test.task.exception.InsufficientBalanceException;
import test.task.exception.NonExistentAccountException;
import test.task.model.Account;
import test.task.repo.AccountRepository;
import lombok.RequiredArgsConstructor;

import javax.inject.Singleton;
import java.util.concurrent.locks.Lock;

@Singleton
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final LockService lockService;

    @Override
    public Account getBalance(Long accountId) {
        return retrieveAccount(accountId);
    }

    @Override
    public Account create(CreateAccountRequestDto request) {
        return accountRepository.save(Account.builder().balance(request.getBalance()).build());
    }

    @Override
    public TransferResponseDto transfer(TransferRequestDto request) {
        Lock lock = lockService.createTransferLock(request.getAccountIdFrom(), request.getAccountIdTo());
        try {
            lock.lock();
            var accountFrom = retrieveAccount(request.getAccountIdFrom());
            var accountTo = retrieveAccount(request.getAccountIdTo());

            validateBalance(request, accountFrom);
            updateBalance(request, accountFrom, accountTo);

            return new TransferResponseDto(accountFrom.getBalance(), accountTo.getBalance());
        } finally {
            lock.unlock();
        }
    }

    private void updateBalance(TransferRequestDto request, Account accountFrom, Account accountTo) {
        accountFrom.setBalance(accountFrom.getBalance() - request.getAmount());
        accountTo.setBalance(accountTo.getBalance() + request.getAmount());

        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
    }

    private void validateBalance(TransferRequestDto request, Account accountFrom) {
        if (accountFrom.getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException(String.format(
                    "Account with id %d has insufficient balance to perform transfer operation.",
                    accountFrom.getId()
            ));
        }
    }

    private Account retrieveAccount(Long accountId) {
        var account = accountRepository.findById(accountId);
        if (null == account) {
            throw new NonExistentAccountException(String.format("Account with id %d is not registered.", accountId));
        }
        return account;
    }
}
