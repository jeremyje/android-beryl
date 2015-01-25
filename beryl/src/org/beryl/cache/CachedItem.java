package org.beryl.cache;

import java.lang.ref.SoftReference;

/** Container for a cached item. */
public class CachedItem<T> implements ICachedItem {

	protected final SoftReference<T> value;
	
	public CachedItem(T value) {
		this.value = new SoftReference<T>(value);
	}
	
	/** Gets the cached data. If the data has been dropped then null is returned. */
	public T get() {
		return this.value.get();
	}
	
	/** Clears the value. Successive get calls will return null. */
	public void clear() {
		this.value.clear();
	}
	
	/** Returns true if there's a value. Otherwise false is returned. */
	public boolean isValid() {
		return this.value.get() != null;
	}
	
	// TODO: Should hashCode and equals be implemented as well?
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public String toString() {
		T value = get();
		if(value != null) {
			return value.toString();
		} else {
			return "<null>, cached value dropped.";
		}
	}
}
