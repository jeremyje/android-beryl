package org.beryl.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/** Thread-safe number generator. Produces a sequence of integers. */
public final class SequenceCounter {
	private final AtomicInteger counter;
	
	/** Starts the sequence counter at 0. */
	public SequenceCounter() {
		counter = new AtomicInteger(0);
	}
	
	/** Starts the sequence counter at a specific value. */
	public SequenceCounter(int startValue) {
		counter = new AtomicInteger(startValue);
	}
	
	/** Get the next number in the sequence. */
	public int next() {
		return counter.getAndAdd(1);
	}
	
	/** Peek at what the next number in the sequence is. */
	public int peek() {
		return counter.get();
	}
	
	/** Set the next number in the sequence. */
	public void set(int newValue) {
		counter.set(newValue);
	}
}
