package org.beryl.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/** Reference counter implementation that runs the assigned delegate once the reference goes to 0. */
public class ReferenceCounter {

	final Runnable onZeroCountDelegate;
	final AtomicInteger counter = new AtomicInteger(0);
	
	public ReferenceCounter(Runnable onZeroCountDelegate) {
		this.onZeroCountDelegate = onZeroCountDelegate;
	}
	
	/** Increase the counter. */
	public void up() {
		counter.incrementAndGet();
	}
	
	/** Decrease the counter. */
	public void down() {
		final int count = counter.decrementAndGet();
		if(count <= 0) {
			onZeroCountDelegate.run();
		}
	}
}
