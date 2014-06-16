package net.sf.dvstar.kidsdialer.utils;

import java.io.File;
import java.io.IOException;

import net.sf.dvstar.kidsdialer.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Utils implements Commons {

	public static void printCursorData(Cursor cursor) {

		String columnNames[] = cursor.getColumnNames();
		int count = 1;
		cursor.moveToFirst();

		while (cursor.moveToNext()) {
			String result = "[" + count + "]";
			int k = 0;

			for (int i = 0; i < columnNames.length; i++) {
				int pos = cursor.getColumnIndex(columnNames[i]);
				String val = "";
				val = cursor.getString(i);
				result += "[" + pos + "][" + columnNames[i] + "]" + "(" + val
						+ ") ";
			}

			Log.v(result);
			count++;
		}

	}

	public static void showCustomToast(LayoutInflater layoutInflater,
			Toast toast, String message) {
		TextView toastText;
		View toastLayout;
		toastLayout = layoutInflater.inflate(R.layout.toast_layout, null);
		// (ViewGroup) findViewById(R.id.toast_layout_root));
		toastText = (TextView) toastLayout.findViewById(R.id.text);
		toastText.setText(message);
		toast.setView(toastLayout);
		toast.show();
	}

	public static void showCustomToastInfo(Activity context, String message,
			int iconId) {
		Toast toast = new Toast(context);
		LayoutInflater layoutInflater = context.getLayoutInflater();
		TextView toastText;
		ImageView toastImage;
		View toastLayout;
		toastLayout = layoutInflater.inflate(R.layout.toast_layout, null);
		// (ViewGroup) findViewById(R.id.toast_layout_root));
		toastText = (TextView) toastLayout.findViewById(R.id.text);
		if (iconId > 0) {
			toastImage = (ImageView) toastLayout.findViewById(R.id.toastImage);
			toastImage.setImageDrawable(context.getResources().getDrawable(
					iconId));
		}
		toastText.setText(message);
		toast.setView(toastLayout);
		toast.show();
	}

	public static String convertMediaUriToPath(Context context, Uri uri) {
		String path = "";
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, proj, null,
				null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			path = cursor.getString(column_index);
			cursor.close();
		}
		return path;
	}

	public static Uri getImageContentUri(Context context, File imageFile) {
		String filePath = imageFile.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID },
				MediaStore.Images.Media.DATA + "=? ",
				new String[] { filePath }, null);
		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor
					.getColumnIndex(MediaStore.MediaColumns._ID));
			Uri baseUri = Uri.parse("content://media/external/images/media");
			return Uri.withAppendedPath(baseUri, "" + id);
		} else {
			if (imageFile.exists()) {
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.DATA, filePath);
				return context.getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				return null;
			}
		}
	}

	public static Bitmap getNormalBitmapFromAssetsPath(Context context, String aPath) throws IOException {

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = true;
		opts.inDensity = DisplayMetrics.DENSITY_MEDIUM;
		Rect padding = new Rect();
		opts.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
		Bitmap bm = BitmapFactory.decodeStream(
				context.getAssets().open(aPath), padding, opts);
		return bm;
	}



	public static String getMyPhoneNumber(Context context){
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
		return mTelephonyMgr.getLine1Number();
	}


	
	/*
	public static int getOrientation(Context context) {
		final int orientation = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getOrientation();
		return orientation;
	}
	*/

	public static int getScreenOrientation(Activity activity)
	{
	    Display display = activity.getWindowManager().getDefaultDisplay();
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
	    Point size = new Point();
	    display.getSize(size);
	    
	    if(size.x == size.y){
	        orientation = Configuration.ORIENTATION_SQUARE;
	    } else{ 
	        if(size.x < size.y){
	            orientation = Configuration.ORIENTATION_PORTRAIT;
	        }else { 
	            orientation = Configuration.ORIENTATION_LANDSCAPE;
	        }
	    }
	    return orientation;
	}	
	
	public static int getOrientationRotation(Context context) {
		final int rotation = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getRotation();
		switch (rotation) {
		case Surface.ROTATION_0:
			return Configuration.ORIENTATION_PORTRAIT; // "portrait";
		case Surface.ROTATION_90:
			return Configuration.ORIENTATION_LANDSCAPE; // "landscape";
		case Surface.ROTATION_180:
			return Configuration.ORIENTATION_PORTRAIT; // "reverse portrait";
		default:
			return Configuration.ORIENTATION_LANDSCAPE; // "reverse landscape";
		}
	}
	
	public static Bitmap getBitmapImage(Resources res, int resId){
		/* Set the options */
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inDither = true;
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inScaled = false; /* Flag for no scalling */ 


		/* Load the bitmap with the options */
		Bitmap bitmapImage = BitmapFactory.decodeResource(res,
				resId, opts);
		
		return bitmapImage;
	}
	
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable == null) {
	    	return null;
	    }
	    
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    int width = drawable.getIntrinsicWidth();
	    width = width > 0 ? width : 1;
	    int height = drawable.getIntrinsicHeight();
	    height = height > 0 ? height : 1;

	    Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}

}
