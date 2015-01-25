package org.beryl.media;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.webkit.MimeTypeMap;

/**
 * Android Service that offloads the scanning of new media in a new thread.
 * This parallels the functionality of the SDK's MediaScannerConnection.scanFile method.
<h4>The 2 advantages of this class are:</h4>
<ul>
	<li>MediaScanner runs in a separate thread.</li>
	<li>Service is self-contained and will no leak on close of application.</li>
</ul>

<h3>To use this class you will need to register it in the AndroidManifest.xml.</h3>
<pre class="code"><code class="xml">
&lt;service android:name="org.beryl.media.RegisterMediaIntentService" /&gt;
</code></pre>
 */
public class RegisterMediaIntentService extends IntentService {

	private static final String EXTRA_FilePath = "EXTRA_FilePath";
	private static final String EXTRA_MimeType = "EXTRA_MimeType";

	public RegisterMediaIntentService() {
		this("RegisterMediaIntentService");
	}

	public RegisterMediaIntentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		final String filePath = intent.getStringExtra(EXTRA_FilePath);
		final String mimeType = intent.getStringExtra(EXTRA_MimeType);

		MediaScannerClient client = new MediaScannerClient();
		MediaScannerConnection scanner = new MediaScannerConnection(this, client);
		client.setScanner(scanner);
		client.queueMediaScan(filePath, mimeType);
		scanner.connect();
		client.waitForScanComplete();
	}

	class MediaScannerClient implements MediaScannerConnectionClient {

		private MediaScannerConnection scanner;
		private String filePath;
		private String mimeType;
		private final AtomicBoolean hasScanCompleted = new AtomicBoolean(false);

		public void setScanner(MediaScannerConnection scanner) {
			this.scanner = scanner;
		}

		public void waitForScanComplete() {
			while(! hasScanCompleted.get()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// This will probably happen. Not a big deal. Let the thread resume.
				}
			}
		}

		public void queueMediaScan(String filePath, String mimeType) {
			this.filePath = filePath;
			this.mimeType = mimeType;
		}

		public void onMediaScannerConnected() {
			scanner.scanFile(filePath, mimeType);
		}

		public void onScanCompleted(String path, Uri uri) {
			if(hasScanCompleted.get() == false) {
				hasScanCompleted.set(true);
				scanner.disconnect();
				notifyScanCompleted(path, uri);
			}
		}
	}

	public static final void addFileToAndroidMediaCollection(Context context, String filePath) {
		final MimeTypeMap mimeMap = MimeTypeMap.getSingleton();
		final String extension = getFileExtension(filePath);
		final String mimeType = mimeMap.getMimeTypeFromExtension(extension);
		addFileToAndroidMediaCollection(context, filePath, mimeType);
	}

	public void notifyScanCompleted(String path, Uri uri) {
		MediaRegisteredReceiver.sendBroadcast(this, path, uri);
	}

	public static final void addFileToAndroidMediaCollection(Context context, String filePath, String mimeType) {
		final Intent startScannerService = new Intent(context, RegisterMediaIntentService.class);
		startScannerService.putExtra(EXTRA_FilePath, filePath);
		startScannerService.putExtra(EXTRA_MimeType, mimeType);
		context.startService(startScannerService);
	}

	// TODO: Create a beryl file utils class.
	public static String getFileExtension(String filePath) {
		return filePath.substring(filePath.lastIndexOf(".") + 1);
	}
}
