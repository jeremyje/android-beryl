package org.beryl.cache.policies;

/** Contract for the expiration of an object. */
public interface IExpirationPolicy {

	/** Force the expiration of the object. Calling this method is not reversible. */
	public void expire();

	/** Returns true if the object is expired. */
	public boolean isExpired();
}