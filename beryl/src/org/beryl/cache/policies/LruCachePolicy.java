package org.beryl.cache.policies;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.beryl.cache.ICacheEventListener;
import org.beryl.cache.ICachedItem;

public class LruCachePolicy implements ICacheEventListener, IEvictionPolicy {

	// TODO: http://www.ebaytechblog.com/2011/08/30/high-throughput-thread-safe-lru-caching/
	private final Queue<String> items = new ConcurrentLinkedQueue<String>();
	
	public void onReset() {
		items.clear();
	}

	public void onGet(String key, ICachedItem item) {
		items.remove(key);
		items.add(key);
	}

	public void onAdd(String key, ICachedItem item) {
		items.add(key);
	}

	public void onRemove(String key, ICachedItem item) {
		items.remove(key);
	}

	public void evict() {
	}
}
