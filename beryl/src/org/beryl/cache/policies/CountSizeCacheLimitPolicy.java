package org.beryl.cache.policies;

import java.util.concurrent.atomic.AtomicInteger;

import org.beryl.cache.ICacheController;
import org.beryl.cache.ICacheEventListener;
import org.beryl.cache.ICachedItem;

/** Limits the cache to a set amount of items. */
public class CountSizeCacheLimitPolicy implements ICacheEventListener {

	private AtomicInteger count = new AtomicInteger(0);
	private final ICacheController cache;
	private final int maxItems;
	
	public CountSizeCacheLimitPolicy(ICacheController cache) {
		this(cache, 25);
	}
	
	public CountSizeCacheLimitPolicy(ICacheController cache, int maxItems) {
		this.cache = cache;
		this.maxItems = maxItems;
	}

	public void onGet(String key, ICachedItem item) {
	}
	
	public void onAdd(String key, ICachedItem item) {
		final int numItems = count.incrementAndGet();
		while(numItems > maxItems) {
			this.cache.evict();
		}
	}
	
	public void onRemove(String key, ICachedItem item) {
		count.decrementAndGet();
	}

	public void onReset() {
		count.set(0);
	}
}
