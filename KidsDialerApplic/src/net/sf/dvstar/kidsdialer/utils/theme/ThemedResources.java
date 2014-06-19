/* Copyright (c) 2014, Dmitry Starzhynskyi
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package net.sf.dvstar.kidsdialer.utils.theme;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.data.SerializableBitmap;
import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.TypedValue;

public class ThemedResources {

	public static final String BG_IMAGE_DESC = "window_bg";
	public static final String BG_SCALE_DESC = "scaled_background";
	public static final String LS_COLOR_DESC = "list_item_text";
	public static final String IC_ITEMS_DESC = "cars_172_";
	public static final int IC_ITEMS_CNT = 5;

	Drawable backgroundDrawable;

	int ic_carsDrawableId[] = new int[] { R.drawable.cars_172_1,
			R.drawable.cars_172_2, R.drawable.cars_172_3,
			R.drawable.cars_172_4, R.drawable.cars_172_5 };

	//private Drawable[] ic_carsDrawable = new Drawable[IC_ITEMS_CNT];
	private DrawableItem[] ic_carsThemedDrawable = new DrawableItem[IC_ITEMS_CNT];;
	private Activity mActivity;
	private boolean DEBUG = true;

	public ThemedResources(Activity aActivity) {
		Log.v("ThemedResources " + PreferenceManager.getThemePackage(aActivity));
		mActivity = aActivity;
		prepareThemedResources();
	}

	void prepareThemedResources() {
		try {
			Drawable ic_carsDrawable;
			backgroundDrawable = ThemeManager.getDrawableResourcesForTheme(
					mActivity, ThemedResources.BG_SCALE_DESC);
			if (backgroundDrawable == null)
				backgroundDrawable = mActivity.getResources().getDrawable(
						R.drawable.scaled_background);

			for (int i = 0; i < IC_ITEMS_CNT; i++) {
				int j = i + 1;
				ic_carsDrawable = ThemeManager.getDrawableResourcesForTheme(
						mActivity, ThemedResources.IC_ITEMS_DESC + j);

				// if(DEBUG)
				if (ic_carsDrawable == null) {
					ic_carsDrawable = mActivity.getResources().getDrawable(
							ic_carsDrawableId[i]);

				}
				
				{
					ic_carsThemedDrawable[i] = new DrawableItem(
							ic_carsDrawable,
							ThemeManager.getDrawableResourceNameForTheme(mActivity,
									ThemedResources.IC_ITEMS_DESC + j),
									DrawableItem.ITEM_MODE_PATH_NTRES		
							);
				}
				Log.v(ThemedResources.IC_ITEMS_DESC + j + " "
						+ ic_carsThemedDrawable[i]);
			}

		} catch (NameNotFoundException e) {
			Log.d(e);
		}
	}

	public Bitmap getRandomContactDrawable(int i) {
		if (i < ic_carsThemedDrawable.length)
			return ic_carsThemedDrawable[i].getImage();
		else
			return ic_carsThemedDrawable[0].getImage();
	}

	public DrawableItem[] getContactDrawables() {
		return ic_carsThemedDrawable;
	}

	public static class ThemeDesc {
		public final boolean mDetached;
		public String mTitle;
		public String mPackageName;
		public int mId;

		public ThemeDesc(boolean detached) {
			mDetached = detached;
		}

		public String toString() {
			String ret = "[" + mId + "][" + mTitle + "][" + mPackageName + "]["
					+ mDetached + "]";
			return ret;
		}
	}
	
/*
	public static class ThemedDrawable {
		public ThemedDrawable(Drawable drawable,
				String drawableNameResourcesForTheme) {
			this.mDrawable = drawable;
			this.mResName = drawableNameResourcesForTheme;
		}
		public String mResName;
		public Drawable mDrawable;
	}
*/
	
	
	
}
