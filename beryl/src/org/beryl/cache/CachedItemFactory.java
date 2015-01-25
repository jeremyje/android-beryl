package org.beryl.cache;

public class CachedItemFactory<T> {

	public CachedItem<T> create(T value) {
		return new CachedItem<T>(value);
	}
}
