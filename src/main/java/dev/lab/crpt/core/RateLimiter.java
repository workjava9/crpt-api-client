package dev.lab.crpt.core;

public interface RateLimiter {
    void acquire() throws InterruptedException;
}
