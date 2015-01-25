package org.beryl.graphics;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class UriBitmapSource extends AbstractBitmapSource {

	private Uri uri;
	
	public UriBitmapSource(final Uri uri) {
		this.uri = uri;
	}
	
	public UriBitmapSource(Parcel in) {
		readFromParcel(in);
	}
	
	public boolean load(Context context, Options options) {
		boolean success = false;
		if(! isAvailable() || options.inJustDecodeBounds) {
			try {
				final InputStream is = context.getContentResolver().openInputStream(this.uri);
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

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
    	dest.writeParcelable(uri, flags);
    	dest.writeParcelable(bitmap, flags);
    }

    public void readFromParcel(Parcel in) {
    	uri = in.readParcelable(null);
    	bitmap = in.readParcelable(null);
    }

    public static final Parcelable.Creator<UriBitmapSource> CREATOR = new Parcelable.Creator<UriBitmapSource>() {
        public UriBitmapSource createFromParcel(Parcel in) {
            return new UriBitmapSource(in);
        }

        public UriBitmapSource[] newArray(int size) {
            return new UriBitmapSource[size];
        }
    };
}
