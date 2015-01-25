package org.beryl.cache;

public interface ICache<TData> extends ICacheController {
	
	/** Adds the value into the cache. */
	void add(String key, TData value);
	
	/** Removes an item from the cache, if still available the value is returned. */
	TData remove(String key);
	
	/** Retrieves the item from the cache. */
	TData get(String key);
}
