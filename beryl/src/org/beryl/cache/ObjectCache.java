package org.beryl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.beryl.cache.policies.IEvictionPolicy;

public class ObjectCache<TData> implements ICache<TData> {

	public static final <T> CachedItemFactory<T> defaultFactory() {
		return new CachedItemFactory<T>();
	}
	
	private final ConcurrentHashMap<String, CachedItem<TData>> cache;
	private final CachedItemFactory<TData> cacheItemFactory;
	
	public ObjectCache() {
		this.cache = new ConcurrentHashMap<String, CachedItem<TData>>();
		this.cacheItemFactory = defaultFactory();
	}
	
	public ObjectCache(CachedItemFactory<TData> cacheItemFactory) {
		this.cache = new ConcurrentHashMap<String, CachedItem<TData>>();
		this.cacheItemFactory = cacheItemFactory;
	}
	
	public void add(String key, TData value) {
		final CachedItem<TData> cacheValue = this.cacheItemFactory.create(value);
		cache.put(key, cacheValue);
	}
	
	public TData remove(String key) {
		final CachedItem<TData> cacheValue = this.cache.remove(key);
		return cacheValue.get();
	}
	
	public TData get(String key) {
		return cache.get(key).get();
	}
	
	public void clear() {
		this.cache.clear();
	}

	class GcEvictionPolicy implements IEvictionPolicy {
		
		public void evict() {
			List<String> items = findDeadItems();
			
			if(items.isEmpty()) {
				String evictTarget = null;
				for(String key : cache.keySet()) {
					evictTarget = key;
					break;
				}
				
				if(evictTarget != null) {
					items.add(evictTarget);
				}
			}
			
			for(String evictTarget : items) {
				remove(evictTarget);
			}
		}
	}
	
	protected List<String> findDeadItems() {
		List<String> deadItems = new ArrayList<String>();
		
		for(Entry<String, CachedItem<TData>> entry : cache.entrySet()) {
			if(! entry.getValue().isValid()) {
				deadItems.add(entry.getKey());
			}
		}
		
		return deadItems;
	}
	
	public void evict() {
		String evictTarget = null;
		for(String key : cache.keySet()) {
			evictTarget = key;
			break;
		}
		
		if(evictTarget != null) {
			remove(evictTarget);
		}
	}
	
	/*
	 * Caching Features
	 * - Cache Any Object
	 * - Clear Cache
	 * - Thread Safety
	 * - Remove
	 * - Imposed Limits (size)
	 * - General Expiration Policy
	 * - Different ways: LruCache, GC Managed, Weighted (hits against + Lru)
	 */
}
