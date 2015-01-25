package org.beryl.cache;

/** Contract for an object that holds cached data. */
public interface ICachedItem {
	/** Removes the data from the holder. */
	void clear();
	/** Returns true if the data is still available. */
	boolean isValid();
}
