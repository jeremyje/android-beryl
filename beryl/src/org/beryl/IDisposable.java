package org.beryl;

/** Contract for objects that hold unmanaged resources that must be deallocated manually. */
public interface IDisposable {
	/** Free resources. */
	void dispose();
}
