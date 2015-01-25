package org.beryl.cache;

/** Contract of the control methods of a cache. */
public interface ICacheController {

	/** Removes all items from the cache. */
	void clear();
	
	/** Removes the least desirable item from the cache. */
	void evict();
}
