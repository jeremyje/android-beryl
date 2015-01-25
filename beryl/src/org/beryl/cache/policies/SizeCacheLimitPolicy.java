package org.beryl.cache.policies;

import java.util.concurrent.atomic.AtomicLong;

import org.beryl.ISizeGettable;
import org.beryl.cache.ICacheController;
import org.beryl.cache.ICacheEventListener;
import org.beryl.cache.ICachedItem;
import org.beryl.util.Memory;

public class SizeCacheLimitPolicy implements ICacheEventListener {

	private AtomicLong memorySize = new AtomicLong(0);
	private final ICacheController cache;
	private final long memoryLimit;
	
	private static final long UNKNOWN_SIZE_GUESS = 256 * 1024; // Guess 256 KB per item.
	private static final double DEFAULT_RATIO = 1 / 5.0;
	
	public SizeCacheLimitPolicy(ICacheController cache) {
		this(cache, -1);
	}
	
	public SizeCacheLimitPolicy(ICacheController cache, long memoryLimit) {
		if(memoryLimit <= 0) {
			memoryLimit = (long) (Memory.memoryLimit() * DEFAULT_RATIO);
		}
		
		this.cache = cache;
		this.memoryLimit = memoryLimit;
	}
	
	public void onGet(String key, ICachedItem item) {
	}
	
	public void onAdd(String key, ICachedItem item) {
		final long itemSize = getItemSizeInBytes(item);
		long currentSize = memorySize.addAndGet(itemSize);
		
		while(currentSize > memoryLimit) {
			cache.evict();
			currentSize = memorySize.get();
		}
	}

	private long getItemSizeInBytes(ICachedItem item) {
		if(item instanceof ISizeGettable) {
			return ((ISizeGettable) item).getSizeInBytes();
		} else {
			return UNKNOWN_SIZE_GUESS;
		}
	}

	public void onRemove(String key, ICachedItem item) {
		final long itemSize = getItemSizeInBytes(item);
		memorySize.addAndGet(-itemSize);
	}

	public void onReset() {
		memorySize.set(0);
	}

	

}
