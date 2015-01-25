package org.beryl.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.os.Parcel;
import android.os.Parcelable;

/** A bitmap source that is a wrapper for an already in memory bitmap. */
public class BitmapBitmapSource extends AbstractBitmapSource {

	public BitmapBitmapSource(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public BitmapBitmapSource(Parcel in) {
		readFromParcel(in);
	}
	
	public boolean load(Context context, Options options) {
		if(isAvailable()) {
			options.outWidth = bitmap.getWidth();
			options.outHeight = bitmap.getHeight();
			return true;
		}
		
		return false;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
    	dest.writeParcelable(bitmap, flags);
    }

    public void readFromParcel(Parcel in) {
    	bitmap = in.readParcelable(null);
    }

    public static final Parcelable.Creator<BitmapBitmapSource> CREATOR = new Parcelable.Creator<BitmapBitmapSource>() {
        public BitmapBitmapSource createFromParcel(Parcel in) {
            return new BitmapBitmapSource(in);
        }

        public BitmapBitmapSource[] newArray(int size) {
            return new BitmapBitmapSource[size];
        }
    };
}
