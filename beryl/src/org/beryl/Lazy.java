package org.beryl;

/** (thread-safe) Support class for lazy initialization. */
public abstract class Lazy<T> {

	private volatile T data = null;
	
	/** Returns true if the variable has been initialized. */
	public boolean isValid() {
		return this.data != null;
	}
	
	/** (thread-safe) Gets the lazy initialized value. */
	public T get() {
		
		T result = this.data;
		if(result == null) {
			synchronized(this) {
				result = this.data;
				if(result == null) {
					this.data = result = onSet();
				}
			}
		}
		
		return result;
	}
	
	/** Callback method to set the lazy-initialized variable. */
	protected abstract T onSet();
}
