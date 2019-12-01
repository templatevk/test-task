package test.task.repo;

import test.task.model.Account;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
@NotThreadSafe
public class InMemoryAccountRepository implements AccountRepository {

    private static final AtomicLong ID_SEQUENCE = new AtomicLong();

    private final Map<Long, Account> storage = new HashMap<>();

    @Override
    public Account findById(Long accountId) {
        var account = storage.get(accountId);
        return account == null ? null : Account.copyOf(account);
    }

    @Override
    public Account save(Account account) {
        if (null == account.getId()) {
            account.setId(ID_SEQUENCE.incrementAndGet());
        }
        storage.put(account.getId(), account);
        return account;
    }

    @Override
    public void clear() {
        storage.clear();
    }
}
