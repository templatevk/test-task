package test.task.repo;

import test.task.model.Account;

public interface AccountRepository {

    Account findById(Long accountId);

    Account save(Account account);

    void clear();
}
