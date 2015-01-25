package org.beryl.diagnostics;

/** Handler for manually caught exceptions that probably need to be reported somewhere.
 * Use this class for situations where exceptions can be thrown that should at least be mentioned somewhere.
 * This allows for easy swapping of generic exception handling.
 * The default is no action but all non-critical exceptions can be reported by calling enable(). */
public class ExceptionReporter {

	public static final NonCriticalThrowableHandler suppressExceptionHandler = new DoNothingHandler();
	public static final NonCriticalThrowableHandler logcatExceptionHandler = new LogEHandler();

	public static NonCriticalThrowableHandler globalHandler = suppressExceptionHandler;

	public static void disable() {
		globalHandler = suppressExceptionHandler;
	}

	public static void enable() {
		globalHandler = logcatExceptionHandler;
	}

	public static void setCustomHandler(NonCriticalThrowableHandler handler) {
		globalHandler = handler;
	}

	public static void report(Throwable tr) {
		globalHandler.onThrow(tr);
	}
	
	public static void report(String message, Throwable tr) {
		globalHandler.onException(new Exception(message));
		globalHandler.onThrow(tr);
	}

	public static void report(Exception e) {
		globalHandler.onException(e);
	}

	public static void report(String message, Exception e) {
		globalHandler.onException(new Exception(message));
		globalHandler.onException(e);
	}

	static class DoNothingHandler implements NonCriticalThrowableHandler {
		public void onThrow(Throwable tr) {
		}

		public void onException(Exception e) {
		}
	}

	static class LogEHandler implements NonCriticalThrowableHandler {
		private final Log logger;

		public LogEHandler() {
			logger = new Log(new LogCatLogWriter());
			logger.setTag("NonCritical");
		}

		public void onThrow(Throwable tr) {
			logger.e(tr);
		}

		public void onException(Exception e) {
			logger.e(e);
		}
	}
}
