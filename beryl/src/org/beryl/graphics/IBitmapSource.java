package org.beryl.graphics;

import org.beryl.IDisposable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;

/** Contract for representing a source of a bitmap. */
public interface IBitmapSource extends Parcelable, IDisposable {

	/** Loads the bitmap into memory. */
	boolean load(Context context);
	
	/** Loads the bitmap into memory. */
	boolean load(Context context, BitmapFactory.Options options);
	
	/** Gets the bitmap. If the bitmap is not loaded, null is returned. */
	Bitmap get();
	
	/** Returns true if the bitmap is loaded into memory. */
	boolean isAvailable();
	
	/** Gets the properties of the bitmap. */
	BitmapProperties getProperties(Context context);
}
