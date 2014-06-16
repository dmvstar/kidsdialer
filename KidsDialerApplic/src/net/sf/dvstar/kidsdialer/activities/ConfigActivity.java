package net.sf.dvstar.kidsdialer.activities;

import java.util.List;

import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.apps.ThemedActivity;
import net.sf.dvstar.kidsdialer.utils.Commons;
import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.Utils;
import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ConfigActivity extends ThemedActivity implements Commons {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// mParentActivity = this;
		// ThemesManagerHelper.setThemeForActivity( this );

		// Log.v("Activity State: onCreate() [R.string.button_change_theme="
		// +R.string.button_change_theme+"]("
		// +getResources().getString(R.string.button_change_theme)
		// +")");

//		getApplicationContext().setTheme(
//				ThemesManagerHelper.getThemeResourceId());

		// getApplicationContext().getPackageName();

		super.onCreate(savedInstanceState);

//		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
//		
//		if (ThemesManagerHelper.getThemeResourceId() > 0) {
//
//			Context mtContext = new ContextThemeWrapper(this,
//					ThemesManagerHelper.getThemeResourceId());
//			mLayoutInflater = LayoutInflater.from(mtContext);
//
//		}

		// View view = mLayoutInflater.inflate(R.layout.config, null);
		// setContentView(view);

		setContentView(R.layout.config);

		Log.v("Activity State: onCreate()");
	}


	public void launchChangeFavorites(View v) {
		PendingIntent pi;
		Intent intent = new Intent(this, SettingsPassActivity.class);
		// this.startActivityForResult(intent,
		// Commons.REQUEST_FavoritesManagerActivity );

		pi = createPendingResult(REQUEST_FavoritesManagerActivity, intent, 0);

		intent.putExtra(PARAM_PINTENT, pi);

		this.startActivity(intent);
	}

	public void launchChangeTheme(View v) {
		this.startActivity(new Intent(this, SwitchThemeActivity.class));
	}

	public void launchChangePassword(View v) {

	}

}
