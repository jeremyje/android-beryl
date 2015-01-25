package org.beryl.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;

/** Recommended base class for implementing an IBitmapSource. */
public abstract class AbstractBitmapSource implements IBitmapSource {

	protected Bitmap bitmap = null;
	
	public void dispose() {
		if(bitmap != null) {
			if(! bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
	}
	
	public boolean load(Context context) {
		return load(context, BitmapProperties.getRecommendedOptions());
	}
	
	public Bitmap get() {
		return this.bitmap;
	}

	public boolean isAvailable() {	
		return (this.bitmap != null && ! this.bitmap.isRecycled());
	}
	
	public BitmapProperties getProperties(final Context context) {
		BitmapProperties properties = null;
		Options options = BitmapProperties.getDecodeOnlyOptions();
		final boolean success = load(context, options);
		if(success) {
			properties = new BitmapProperties(options);
		}
		
		return properties;
	}
}
