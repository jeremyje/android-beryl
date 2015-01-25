package org.beryl.util;

/** In Java-land there are checked exceptions which sometimes are annoying to use.
 * Although a novel idea, they can break encapsulation.
 * Sometimes they are not appropriate to use since the exception handling may occur many levels above where the exception is thrown.
 * WrappedExceptions basically wrapper any {@link Exception} into a {@link RuntimeException} this relaxes the need to have
 * throws clauses in method signatures.
 *
 * <h2>Notes</h2> You cannot instantiate this class directly. Use {@link WrappedException#wrap(Exception)} or {@link Exceptions#wrapAndThrow(Exception)}.
 *
<h2>Recommended Usage</h2>
<pre class="code"><code class="java">

// Throw-Rethrowing
catch(Exception e) {
	Exceptions.wrapAndThrow(e);
}

// Catching
catch(WrappedException e) {
	final Exception ex = Exception.unwrap(e);
	if(ex instanceof IOException e) {
	} else if (ex instanceof ...) {}
}
</code></pre>
 */
public final class WrappedException extends RuntimeException {

	public static WrappedException wrap(Exception e) {
		if (e instanceof WrappedException)
			return (WrappedException)e;
		else
			return new WrappedException(e);
	}

	public static Exception unwrap(Exception e) {
		if(e instanceof WrappedException) {
			return (Exception)e.getCause();
		} else {
			return e;
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -5957347492438373249L;

	WrappedException(Exception e) {
		super(e);
	}
}
