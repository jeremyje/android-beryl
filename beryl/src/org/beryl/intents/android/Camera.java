package org.beryl.intents.android;

import org.beryl.diagnostics.ExceptionReporter;
import org.beryl.graphics.BitmapWrapper;
import org.beryl.intents.IActivityResultHandler;
import org.beryl.intents.IIntentBuilderForResult;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class Camera {


	// For the future allow camera captures to not have to register in the ContentProvider.
	// TODO: http://stackoverflow.com/questions/2729267/android-camera-intent
	// TODO: http://www.monkeycancode.com/taking-a-picture-with-camera-using-built-in-intent
	// TODO: http://achorniy.wordpress.com/2010/04/26/howto-launch-android-camera-using-intents/
	public static class CaptureRequest implements IIntentBuilderForResult {
		public static final int PICTUREQUALITY_Lowest = 0;
		public static final int PICTUREQUALITY_Highest = 1;

		public String title = "Camera Picture";
		public String displayName = "";
		public String fileName = "";
		public String description = "Camera Picture";
		public int pictureQualityMode = PICTUREQUALITY_Highest;

		private Uri imageUri;

		private String getDisplayName() {
			if(displayName != null && displayName.length() > 0) {
				return displayName;
			} else {
				return title;
			}
		}

		public void prepareIntent(Context context) {
			final ContentValues values = new ContentValues();
			values.put(MediaStore.Images.Media.TITLE, title);
			values.put(MediaStore.Images.Media.DESCRIPTION, description);
			values.put(MediaStore.Images.Media.DISPLAY_NAME, getDisplayName());

			try {
				final ContentResolver cr = context.getContentResolver();
				this.imageUri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} catch(Exception e) {
				ExceptionReporter.report(e);
				this.imageUri = null;
			}
		}

		public Intent getIntent() {
			final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, this.imageUri);
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, pictureQualityMode);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			return intent;
		}

		public Bundle getResultBundle() {
			Bundle bundle = new Bundle();
			bundle.putParcelable("captureImageUri", imageUri);
			return bundle;
		}

		public boolean isChoosable() {
			return false;
		}

		public CharSequence getChooserTitle() {
			return "";
		}

		public boolean isValid() {
			return this.imageUri != null;
		}
	}

	public abstract static class GetCameraCaptureResult implements IActivityResultHandler {

		public BitmapWrapper bitmapResult = BitmapWrapper.Empty;

		public void prepareResult(Context context, Bundle resultBundle, int resultCode, Intent data) {
			Uri imageUri = null;

			if(resultBundle != null && resultBundle.containsKey("captureImageUri")) {
				imageUri = resultBundle.getParcelable("captureImageUri");
			} else if(data != null) {
				imageUri = data.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
			}

			if(resultCode == Activity.RESULT_OK) {
				bitmapResult = Gallery.loadBitmapFromUri(context, imageUri);
			} else if(resultCode == Activity.RESULT_CANCELED) {
				Gallery.deleteImageUri(context, imageUri);
			}
		}
	}
}
