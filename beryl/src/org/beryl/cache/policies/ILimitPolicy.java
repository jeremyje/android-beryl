package org.beryl.cache.policies;

import org.beryl.cache.ICachedItem;

public interface ILimitPolicy {
	void reset();
	void add(ICachedItem item);
	void onRemove(ICachedItem item);
}
