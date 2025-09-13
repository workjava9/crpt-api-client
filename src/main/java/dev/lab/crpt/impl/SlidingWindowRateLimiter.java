package dev.lab.crpt.impl;

import dev.lab.crpt.core.RateLimiter;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SlidingWindowRateLimiter implements RateLimiter {

    private final long aLong;
    private final int limit;
    private final Deque<Long> timestamps = new ArrayDeque<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition spaceAvailable = lock.newCondition();

    public SlidingWindowRateLimiter(int limit, TimeUnit unit) {
        if (limit <= 0) throw new IllegalArgumentException("limit must be > 0");
        this.limit = limit;
        this.aLong = unit.toNanos(1);
    }


    @Override
    public void acquire() throws InterruptedException {
        long now;
        lock.lock();
        try {
            while (true) {
                now = System.nanoTime();
                while (!timestamps.isEmpty() && now - timestamps.peekFirst() >= aLong) {
                    timestamps.removeFirst();
                }
                if (timestamps.size() < limit) {
                    timestamps.addLast(now);
                    spaceAvailable.signalAll();
                    return;
                }
                long waitNanos = aLong - (now - timestamps.peekFirst());
                if (waitNanos > 0) {
                    spaceAvailable.awaitNanos(waitNanos);
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
