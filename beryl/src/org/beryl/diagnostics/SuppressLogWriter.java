package org.beryl.diagnostics;

/** Suppresses all logging. */
public class SuppressLogWriter implements ILogWriter {

	public SuppressLogWriter() {
	}

	public void d(String tag, String msg) {
	}

	public void e(String tag, Exception e) {
	}

	public void e(String tag, String msg) {
	}

	public void i(String tag, String msg) {
	}

	public void w(String tag, String msg) {
	}

	public void e(String tag, Throwable tr) {
	}
}
