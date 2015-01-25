package org.beryl.diagnostics;

/** Contract for log writers. LogWriters are wrapped around the Log object for advanced logging output. */
public interface ILogWriter {

	/** Log an exception, with the stack trace, exception type and message. */
	void e(String tag, Exception e);

	/** Log a throwable, with the stack trace, exception type and message. */
	void e(String tag, Throwable tr);

	/** Log an error message. */
	void e(String tag, String msg);

	/** Log a debug message. */
	void d(String tag, String msg);

	/** Log an informative message. */
	void i(String tag, String msg);

	/** Log a warning message. */
	void w(String tag, String msg);
}
