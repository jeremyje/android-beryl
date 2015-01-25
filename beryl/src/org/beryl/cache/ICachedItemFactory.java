package org.beryl.cache;

public interface ICachedItemFactory<T> {
	CachedItem<T> create(T value);
}
