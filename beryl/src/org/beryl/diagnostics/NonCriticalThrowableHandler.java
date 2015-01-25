package org.beryl.diagnostics;

public interface NonCriticalThrowableHandler {
	void onThrow(Throwable tr);
	void onException(Exception e);
}
