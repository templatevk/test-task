package test.task.service;

import test.task.dto.CreateAccountRequestDto;
import test.task.dto.TransferRequestDto;
import test.task.dto.TransferResponseDto;
import test.task.model.Account;

public interface AccountService {

    Account getBalance(Long accountId);

    TransferResponseDto transfer(TransferRequestDto request);

    Account create(CreateAccountRequestDto balance);
}
