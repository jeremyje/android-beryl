package org.beryl.diagnostics;

/** A LogWriter that prints to the Android LogCat. */
public class LogCatLogWriter implements ILogWriter {

	public LogCatLogWriter() {

	}

	public void d(String tag, String msg) {
		if(tag == null) tag = "";
		if(msg == null) msg = "";
		android.util.Log.d(tag, msg);
	}

	public void e(String tag, Exception e) {
		if(tag == null) tag = "";
		if(e == null) e = new Exception("");
		android.util.Log.e(tag, e.getMessage(), e);
	}

	public void e(String tag, Throwable tr) {
		if(tag == null) tag = "";
		if(tr == null) tr = new Throwable("");
		android.util.Log.e(tag, tr.getMessage(), tr);
	}

	public void e(String tag, String msg) {
		if(tag == null) tag = "";
		if(msg == null) msg = "";
		android.util.Log.e(tag, msg);
	}

	public void i(String tag, String msg) {
		if(tag == null) tag = "";
		if(msg == null) msg = "";
		android.util.Log.i(tag, msg);
	}

	public void w(String tag, String msg) {
		if(tag == null) tag = "";
		if(msg == null) msg = "";
		android.util.Log.w(tag, msg);
	}
}
