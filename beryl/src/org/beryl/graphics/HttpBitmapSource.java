package org.beryl.graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.SocketException;

import org.beryl.diagnostics.ExceptionReporter;
import org.beryl.net.SimpleFileDownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class HttpBitmapSource extends AbstractBitmapSource {

	private Uri remoteUri;
	private String localPath = null;
	
	public HttpBitmapSource(final Uri uri) {
		this.remoteUri = uri;
	}
	
	public HttpBitmapSource(Parcel in) {
		readFromParcel(in);
	}
	
	public boolean load(Context context, Options options) {
		boolean success = false;
		if(localPath == null) {
			downloadFile(context);
		}
		
		if((! isAvailable() || options.inJustDecodeBounds) && localPath != null) {
			try {
				final InputStream is = new FileInputStream(this.localPath);
				final Bitmap tempBitmap = BitmapFactory.decodeStream(is, null, options);
				
				if(! options.inJustDecodeBounds) {
					dispose();
					this.bitmap = tempBitmap;
				}
				
				is.close();
				success = true;
			} catch(Exception e) {
				dispose();
				success = false;
			}
		}
		
		return success;
	}

	private void downloadFile(final Context context) {
		SimpleFileDownloader downloader = new SimpleFileDownloader();
		try {
			File dest = downloader.download(context, remoteUri.toString());
			localPath = dest.getAbsolutePath();
		} catch(SocketException e) {
			ExceptionReporter.report(e);
			localPath = null;
		}
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
    	dest.writeParcelable(remoteUri, flags);
    	dest.writeString(localPath);
    	dest.writeParcelable(bitmap, flags);
    }

    public void readFromParcel(Parcel in) {
    	remoteUri = in.readParcelable(null);
    	localPath = in.readString();
    	bitmap = in.readParcelable(null);
    }

    public static final Parcelable.Creator<HttpBitmapSource> CREATOR = new Parcelable.Creator<HttpBitmapSource>() {
        public HttpBitmapSource createFromParcel(Parcel in) {
            return new HttpBitmapSource(in);
        }

        public HttpBitmapSource[] newArray(int size) {
            return new HttpBitmapSource[size];
        }
    };
}
