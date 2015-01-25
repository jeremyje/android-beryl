package org.beryl.graphics;

import org.beryl.util.Memory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class BitmapWrapper implements IBitmapSource {

	public static final BitmapWrapper Empty = new BitmapWrapper(
			new EmptyBitmapSource());
	protected IBitmapSource source;

	public BitmapWrapper(Parcel in) {
		readFromParcel(in);
	}

	public BitmapWrapper(IBitmapSource source) {
		this.source = source;
	}

	public BitmapWrapper(Uri uri) {
		this.source = createSource(uri);
	}

	public BitmapWrapper(Bitmap bitmap) {
		this.source = createSource(bitmap);
	}

	public boolean load(final Context context) {
		return source.load(context);
	}

	public boolean load(final Context context, final Options options) {
		return source.load(context, options);
	}

	public boolean conservativeLoad(final Context context) {
		return conservativeLoad(context, 4);
	}

	public boolean conservativeLoad(final Context context,
			final int numInstances) {
		BitmapProperties properties = this.getProperties(context);
		final long sizeLimit = Memory.getSizeLimit(numInstances);
		final int sampleSize = properties.getRecommendedSampleSize(sizeLimit);
		Options options = BitmapProperties.getRecommendedOptions();
		options.inSampleSize = sampleSize;
		return load(context, options);
	}

	public boolean isAvailable() {
		return source.isAvailable();
	}

	public Bitmap get() {
		return source.get();
	}

	public Bitmap rotate(float angle) {
		return rotateBitmap(get(), angle);
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, float angle) {
		int width;
		int height;

		width = bitmap.getWidth();
		height = bitmap.getHeight();

		Matrix rotationMatrix = new Matrix();
		rotationMatrix.setRotate(angle);
		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				rotationMatrix, true);
		return rotatedBitmap;
	}

	public void dispose() {
		source.dispose();
	}

	public BitmapProperties getProperties(final Context context) {
		return source.getProperties(context);
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(source, flags);
	}

	public void readFromParcel(Parcel in) {
		source = in.readParcelable(this.getClass().getClassLoader());
	}

	public static final Parcelable.Creator<BitmapWrapper> CREATOR = new Parcelable.Creator<BitmapWrapper>() {
		public BitmapWrapper createFromParcel(Parcel in) {
			return new BitmapWrapper(in);
		}

		public BitmapWrapper[] newArray(int size) {
			return new BitmapWrapper[size];
		}
	};

	public static BitmapWrapper create(Uri uri) {
		return new BitmapWrapper(uri);
	}

	public static BitmapWrapper create(Bitmap bitmap) {
		return new BitmapWrapper(bitmap);
	}

	public static IBitmapSource createSource(Uri uri) {

		if (uri.getScheme().toLowerCase().startsWith("http")) {
			return new HttpBitmapSource(uri);
		} else {
			return new UriBitmapSource(uri);
		}
	}

	public static IBitmapSource createSource(Bitmap bitmap) {
		return new BitmapBitmapSource(bitmap);
	}

	public static boolean isUsable(Bitmap bitmap) {
		return bitmap != null && !bitmap.isRecycled();
	}

	public static void dispose(Bitmap bitmap) {
		if (isUsable(bitmap)) {
			bitmap.recycle();
		}
	}
}
