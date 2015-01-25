package org.beryl.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.beryl.io.DirectoryUtils;
import org.beryl.io.FileUtils;

import android.content.Context;

/** Downloads a file from a web server to a file.
 * As the class implies it is a simple implementation. It does not support resuming or chunking of data. */
public class SimpleFileDownloader {

	/** Download to a random file in the public application directory. */
	public File download(Context context, String urlPath) throws ConnectException {
		return download(context, urlPath, null);
	}
	
	/** Download to a random file in the public application directory with a specific extension. */
	public File download(Context context, String urlPath, String extension) throws ConnectException {
		File result = null;
		if(! NetworkUtils.isConnected(context)) {
			throw new ConnectException("No connection is available to download.");
		}
		try {
			File targetDirectory = DirectoryUtils.getPublicApplication(context, "downloads");
			result = File.createTempFile("download", extension, targetDirectory);
			
			boolean success = download(result, urlPath);
			if(! success)
				throw new Exception("Download failed");
		} catch(Exception e) {
			if(result != null) {
				// Delete the file if the download failed.
				FileUtils.delete(result);
				result = null;
			}
		}
		return result;
	}
	
	/** Download the file to a specific location. */
	public boolean download(File fileDesc, String urlPath) {
		HttpURLConnection connection = null;
		FileOutputStream fos = null;
		InputStream is = null;
		boolean success = false;
		
		try {
			URL url = new URL(urlPath);
			connection = (HttpURLConnection) url.openConnection();
			
			fos = new FileOutputStream(fileDesc);
			is = connection.getInputStream();
			copyStream(is, fos);
			success = true;
		} catch (Exception e) {
		} finally {
			try {
				if(is != null) {
					is.close();
				}
			} catch(Exception e) {}
			
			try {
				if(fos != null) {
					fos.close();
				}
			} catch(Exception e) {}
			
			if (connection != null) {
				connection.disconnect();
			}
		}
		
		return success;
	}
	
	// TODO: Consider moving this into some other class as this can be helpful in other places.
	private void copyStream(InputStream is, OutputStream os) throws IOException {
		byte[] buffer = new byte[1024];
		int readLen = 0;
		while ((readLen = is.read(buffer)) != -1) {
			os.write(buffer, 0, readLen);
		}
	}
}
