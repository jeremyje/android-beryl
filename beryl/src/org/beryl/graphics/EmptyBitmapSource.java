package org.beryl.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

class EmptyBitmapSource implements IBitmapSource {

	private final BitmapProperties props = new BitmapProperties();
	
	public EmptyBitmapSource() {
	}
	
	public EmptyBitmapSource(final Uri uri) {
	}
	
	public EmptyBitmapSource(Parcel in) {
		readFromParcel(in);
	}
	
	public boolean load(Context context, Options options) {
		return false;
	}
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
    }

    public void readFromParcel(Parcel in) {
    }

    public static final Parcelable.Creator<EmptyBitmapSource> CREATOR = new Parcelable.Creator<EmptyBitmapSource>() {
        public EmptyBitmapSource createFromParcel(Parcel in) {
            return new EmptyBitmapSource(in);
        }

        public EmptyBitmapSource[] newArray(int size) {
            return new EmptyBitmapSource[size];
        }
    };

	public void dispose() {
	}

	public boolean load(Context context) {
		return false;
	}

	public Bitmap get() {
		return null;
	}

	public boolean isAvailable() {
		return false;
	}

	public BitmapProperties getProperties(Context context) {
		return props;
	}
}
