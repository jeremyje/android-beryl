package org.beryl.util;

public class Exceptions {

	/** Some exceptions themselves act as wrappers. To keep things uniform to {@link WrappedException}, use this method to wrap
	 * the cause rather than the {@link Exception} itself.
	 * If the cause is null then the Exception itself will be wrapped.
	 * If the Exception is a WrappedException then it will just be thrown.
	 */
	public static void wrapCauseAndThrow(Exception e) {
		if(e instanceof WrappedException)
			throw (WrappedException)e;
		else {
			final Throwable t = e.getCause();
			if(t != null) {
				if(t instanceof WrappedException) {
					throw (WrappedException)t;
				} else if (t instanceof Exception) {
					throw new WrappedException((Exception)t);
				}
			}

			throw new WrappedException(e);
		}
	}

	/** Auto-wrap an exception and throw it. Check out how {@link WrappedException} works. */
	public static void wrapAndThrow(Exception e) {
		throw WrappedException.wrap(e);
	}
}
