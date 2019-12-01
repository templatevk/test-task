package test.task.service;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Singleton
public class LockServiceImpl implements LockService {

    private Map<Set<Long>, Lock> locks = new ConcurrentHashMap<>();

    @Override
    public Lock createTransferLock(Long from, Long to) {
        return locks.computeIfAbsent(Set.of(from, to), x -> new ReentrantLock());
    }
}
