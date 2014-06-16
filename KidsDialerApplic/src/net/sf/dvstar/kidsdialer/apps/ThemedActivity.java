package net.sf.dvstar.kidsdialer.apps;

import java.util.List;

import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.Utils;
import net.sf.dvstar.kidsdialer.utils.theme.ImageUtils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class ThemedActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
/*
		Drawable d = ThemeManager.getDrawableResourcesForTheme(this,
				"window_bg");
		if (d != null) {
			BitmapDrawable bd = new BitmapDrawable(getResources(),
					Utils.drawableToBitmap(d));
			bd.setGravity(android.view.Gravity.CENTER);
			getWindow().setBackgroundDrawable(bd);
		}
*/		
		//loadDirectBackgroundImage();
		//loadThemedBackgroundImage();
		ImageUtils imageUtils = new ImageUtils(); 
		imageUtils.loadThemedBackgroundDrawable( this );
		super.onCreate(savedInstanceState);
		Log.v("Activity State: onCreate()");
	}

}
