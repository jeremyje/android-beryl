package org.beryl;

/** Contract to retrieve the size in bytes that the object consumes. */
public interface ISizeGettable {
	/** Returns the size (in bytes) that this object holds. */
	long getSizeInBytes();
}
