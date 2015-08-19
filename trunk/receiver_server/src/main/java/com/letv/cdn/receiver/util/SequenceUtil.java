package com.letv.cdn.receiver.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 原子序号器
 * 
 * @author kk
 */
public class SequenceUtil{
    
    private AtomicLong counter;
    
    public SequenceUtil() {
    
        this(-1);
    }
    
    public SequenceUtil(long l) {
    
        counter = new AtomicLong(l);
    }
    
    public long next() {
    
        return next(1);
    }
    
    public long next(int n) {
    
        if (n < 1) {
            throw new IllegalArgumentException("n must be > 0");
        }
        
        long current;
        long next;
        
        do {
            current = counter.get();
            next = current + n;
            
            if (counter.compareAndSet(current, next)) {
                break;
            }
        } while (true);
        return next;
    }
    
    public boolean compareAndSet(long expectedValue, long newValue) {
    
        return counter.compareAndSet(expectedValue, newValue);
    }
    
    public long getCurrent() {
    
        return counter.get();
    }
}
