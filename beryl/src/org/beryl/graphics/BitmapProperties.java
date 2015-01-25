package org.beryl.graphics;

import org.beryl.app.AndroidVersion;
import org.beryl.util.Memory;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;

/** Holder for attributes and metrics of a Bitmap object. */
public class BitmapProperties {

	public static final long RGBA8888_BYTES_PER_PIXEL = 4;
	public static final long ARGB444_BYTES_PER_PIXEL = 2;
	public static final long RGB565_BYTES_PER_PIXEL = 2;
	public static final long ALPHA8_BYTES_PER_PIXEL = 4;
	
	public long width = 0;
	public long height = 0;
	public long bytesPerPixel = RGBA8888_BYTES_PER_PIXEL;
	
	public BitmapProperties() {
		
	}
	
	public BitmapProperties(Options options) {
		fromBitmapOptions(options);
	}
	
	public BitmapProperties(Bitmap bitmap) {
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		this.bytesPerPixel = bitmapConfigToBytesPerPixel(bitmap.getConfig());
	}
	
	public void fromBitmapOptions(Options options) {
		width = options.outWidth;
		height = options.outHeight;
	}
	
	public long getNumPixels() {
		return width * height;
	}
	
	public long getBytesPerPixel() {
		return bytesPerPixel;
	}
	
	public long getSizeInBytes() {
		return getNumPixels() * getBytesPerPixel();
	}
	
	public long getSizeInBytes(int sampleSize) {
		return getMemoryFootprint(this.width / sampleSize,  this.height / sampleSize, this.bytesPerPixel);
	}
	
	public float getSizeInMegaBytes() {
		return Memory.bytesToMegabytes(getSizeInBytes());
	}
	
	public boolean isSafeToLoad() {
		return isSafeToLoad(Memory.getReasonableMemoryCushion(), 1);
	}

	public boolean isSafeToLoad(long withMemoryRemaining, int sampleSize) {
		long freeMem = Memory.freeMemory();
		long imageSize = getSizeInBytes();
		return freeMem - withMemoryRemaining - imageSize / sampleSize > 0;
	}
	
	/** Gets the recommended sample size to load this bitmap at based on the memory constraint. */
	public int getRecommendedSampleSize(long maxMemorySize) {
		int sampleSize = 1;

		while(getSizeInBytes(sampleSize) > maxMemorySize) {
			sampleSize *= 2;
		}

		return sampleSize;
	}
	
	/** Gets the bytes-per-pixel based on the Bitmap.Config value. */
	public static long bitmapConfigToBytesPerPixel(Config config) {
		switch(config) {
			case ALPHA_8: return ALPHA8_BYTES_PER_PIXEL;
			case ARGB_4444: return ARGB444_BYTES_PER_PIXEL;
			case ARGB_8888: return RGBA8888_BYTES_PER_PIXEL;
			case RGB_565: return RGB565_BYTES_PER_PIXEL;
			default: return RGBA8888_BYTES_PER_PIXEL;
		}
	}
	
	/** Gets the number of pixels based on the width and height. */
	public static long getNumberOfPixels(final long width, final long height) {
		return width * height;
	}

	/** Gets the memory size based on an RGBA_8888 (32-bit) bitmap. */
	public static long getMemoryFootprintForRgba8888(final long width, final long height) {
		return getMemoryFootprint(width, height, RGBA8888_BYTES_PER_PIXEL);
	}
	
	/** Gets the memory used based on the parameters. */
	public static long getMemoryFootprint(final long width, final long height, final long bytesPerPixel) {
		return getNumberOfPixels(width, height) * bytesPerPixel;
	}

	/** Gets the size in bytes of the bitmap. */
	public static long getSizeInBytes(final Bitmap bitmap) {
		if(bitmap == null) {
			return 0;
		} else if(bitmap.isRecycled()) {
			return 0;
		} else if(AndroidVersion.isHoneycombMr1OrHigher()) {
			return bitmap.getByteCount();
		} else {
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
	}
	
	/** Gets the load options for a decode-only load when calling BitmapFactory.decode*() */
	public static Options getDecodeOnlyOptions() {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		return options;
	}
	
	/** Gets load options for the highest quality bitmap. */
	public static Options getRecommendedOptions() {
		Options options = new Options();

		if(AndroidVersion.isGingerbreadMr1OrHigher()) {
			options.inPreferQualityOverSpeed = true;
		}
		options.inPreferredConfig = Config.ARGB_8888;
		
		return options;
	}
}
