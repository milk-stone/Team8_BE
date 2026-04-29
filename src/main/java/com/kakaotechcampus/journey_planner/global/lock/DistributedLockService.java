package com.kakaotechcampus.journey_planner.global.lock;

import com.kakaotechcampus.journey_planner.global.exception.BusinessException;
import com.kakaotechcampus.journey_planner.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributedLockService {

    private final RedissonClient redissonClient;

    private static final long WAIT_TIME = 3L;
    private static final long LEASE_TIME = 5L;

    public <T> T executeWithLock(String lockKey, Supplier<T> operation) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (!lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS)) {
                throw new BusinessException(ErrorCode.NODE_LOCK_FAILED);
            }
            log.debug("[LOCK] 획득 lockKey={}", lockKey);
            return operation.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.NODE_LOCK_FAILED);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.debug("[LOCK] 해제 lockKey={}", lockKey);
            }
        }
    }
}
