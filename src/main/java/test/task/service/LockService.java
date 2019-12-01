package test.task.service;

import java.util.concurrent.locks.Lock;

public interface LockService {

    Lock createTransferLock(Long from, Long to);
}
