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
