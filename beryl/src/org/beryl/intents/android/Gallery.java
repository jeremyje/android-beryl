package org.beryl.intents.android;

import org.beryl.graphics.BitmapWrapper;
import org.beryl.intents.IActivityResultHandler;
import org.beryl.intents.IIntentBuilderForResult;
import org.beryl.intents.IntentHelper;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

public class Gallery {
	private static final int NUM_INSTANCES = 4;

	public static final BitmapWrapper loadBitmapFromUri(Context context, Uri fileUri) {
		BitmapWrapper result = new BitmapWrapper(fileUri);
		result.conservativeLoad(context, NUM_INSTANCES);
		return result;
	}

	// http://stackoverflow.com/questions/5944282/retrieve-picasa-image-for-upload-from-gallery
	public static class GetImage implements IIntentBuilderForResult {
		public String TypeFilter = "image/*";
		public boolean ForceDefaultHandlers = false;
		public Bitmap.CompressFormat CompressFormat = Bitmap.CompressFormat.PNG;

		private Intent intent = null;

		public void prepareIntent(Context context) {
			createGenericGetImageIntent();
		}

		private void createGenericGetImageIntent() {
			intent = IntentHelper.getContentByType("image/*");
		}

		public boolean isValid() {
			return intent != null;
		}

		public Intent getIntent() {
			return intent;
		}

		public Bundle getResultBundle() {
			Bundle data = new Bundle();
			return data;
		}

		public boolean isChoosable() {
			return false;
		}

		public CharSequence getChooserTitle() {
			return "Which Gallery?";
		}
	}

	public static final Intent getImage() {
		return getImage("image/*");
	}

	public static final Intent getImageDefaultOnly() {
		return getImageDefaultOnly("image/*");
	}

	public static final Intent getImage(String imageType) {
		return IntentHelper.getContentByType(imageType);
	}

	public static final Intent getImageDefaultOnly(String imageType) {
		final Intent intent = IntentHelper.getContentByType(imageType);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		return intent;
	}

	public static final Intent sendImage(final String filePath) {
		final Uri uri = Uri.parse(filePath);
		return sendImage(uri);
	}

	public static final Intent sendImage(final Uri contentUri) {
		final String mimeType = IntentHelper.getMimeTypeFromUrl(contentUri);
		final Intent intent = new Intent(Intent.ACTION_SEND);

		intent.putExtra(Intent.EXTRA_STREAM, contentUri);
		intent.setType(mimeType);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		return intent;
	}

	public abstract static class SendImageResult implements IActivityResultHandler {

		public BitmapWrapper bitmapResult = BitmapWrapper.Empty;

		public void prepareResult(Context context, Bundle resultBundle, int resultCode, Intent data) {

			Uri dataUri = data.getParcelableExtra(Intent.EXTRA_STREAM);
			if(dataUri == null) {
				try {
					dataUri = Uri.parse(data.getStringExtra(Intent.EXTRA_TEXT));
				} catch (Exception e) {}
			}
			if(dataUri != null) {
				bitmapResult = Gallery.loadBitmapFromUri(context, dataUri);
			}
		}
	}

	public abstract static class GetImageResult implements IActivityResultHandler {

		public BitmapWrapper bitmapResult = BitmapWrapper.Empty;

		public void prepareResult(Context context, Bundle resultBundle, int resultCode, Intent data) {
			bitmapResult = null;
			Uri imageUri = null;

			if(data != null) {
				imageUri = data.getData();
			}

			try {
				if(imageUri != null) {
					bitmapResult = BitmapWrapper.create(imageUri);
					bitmapResult.conservativeLoad(context, NUM_INSTANCES);
				}
			} catch(Exception e) {
				if(bitmapResult != null) {
					bitmapResult.dispose();
				}
			}
		}
	}

	public static void deleteImageUri(Context context, Uri imageUri) {
		final ContentResolver cr = context.getContentResolver();
		cr.delete(imageUri, null, null);
	}
}
