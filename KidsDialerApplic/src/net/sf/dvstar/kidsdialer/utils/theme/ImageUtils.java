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

import java.util.List;

import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.Utils;
import net.sf.dvstar.kidsdialer.utils.theme.ThemedResources.ThemeDesc;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;



public class ImageUtils {

	
	public void loadThemedBackgroundDrawable(Activity aActivity) {
		Drawable d;
		try {
			d = ThemeManager.getDrawableResourcesForTheme(aActivity,
					ThemedResources.BG_SCALE_DESC //  "scaled_background"
					//"window_bg"
					);
			if (d != null) {
				aActivity.getWindow().setBackgroundDrawable(d);
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(e);
		}
	}
	
	public void loadThemedBackgroundImage(Activity aActivity) {
		Drawable d;
		try {
			d = ThemeManager.getDrawableResourcesForTheme(aActivity,
					//"scaled_background"
					ThemedResources.BG_IMAGE_DESC // "window_bg"
					);
			if (d != null) {
				BitmapDrawable bd = new BitmapDrawable(aActivity.getResources(),
						Utils.drawableToBitmap(d));
				bd.setGravity(android.view.Gravity.CENTER);
				aActivity.getWindow().setBackgroundDrawable(bd);
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(e);
		}
	}

	void loadDirectBackgroundImage(Activity aActivity) {
		final String packName = "net.sf.dvstar.kidsdialer.theme.themeat";
		String mDrawableName = "window_bg";

		try {

			List<ThemeDesc> tdl = ThemeManager.getThemes(aActivity,
					new ThemesConfiguration());

			for (ThemeDesc td : tdl) {
				Log.v("[ThemeDesc] " + td.toString());
			}

			PackageManager manager = aActivity.getPackageManager();
			Resources mApk1Resources = manager
					.getResourcesForApplication(packName);

			int mDrawableResID = mApk1Resources.getIdentifier(mDrawableName,
					"drawable", packName);

			Log.v("loadBackgroundImage [" + mApk1Resources + "]["
					+ mDrawableName + "][" + packName + "][0x"
					+ Integer.toHexString(mDrawableResID) + "]");

			// Drawable myDrawable = mApk1Resources.getDrawable(mDrawableResID);
			//
			// if (myDrawable != null)
			// getWindow().setBackgroundDrawable(myDrawable);

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
