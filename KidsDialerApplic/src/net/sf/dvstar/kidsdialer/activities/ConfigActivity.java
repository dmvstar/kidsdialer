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

package net.sf.dvstar.kidsdialer.activities;

import java.util.List;

import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.apps.ThemedActivity;
import net.sf.dvstar.kidsdialer.utils.Commons;
import net.sf.dvstar.kidsdialer.utils.Configs;
import net.sf.dvstar.kidsdialer.utils.Configs.ConfigParams;
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

	private ConfigParams configParams;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// mParentActivity = this;
		// ThemesManagerHelper.setThemeForActivity( this );

		// Log.v("Activity State: onCreate() [R.string.button_change_theme="
		// +R.string.button_change_theme+"]("
		// +getResources().getString(R.string.button_change_theme)
		// +")");

		// getApplicationContext().setTheme(
		// ThemesManagerHelper.getThemeResourceId());

		// getApplicationContext().getPackageName();

		super.onCreate(savedInstanceState);

		// LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		//
		// if (ThemesManagerHelper.getThemeResourceId() > 0) {
		//
		// Context mtContext = new ContextThemeWrapper(this,
		// ThemesManagerHelper.getThemeResourceId());
		// mLayoutInflater = LayoutInflater.from(mtContext);
		//
		// }

		// View view = mLayoutInflater.inflate(R.layout.config, null);
		// setContentView(view);
		configParams = Configs.readResultForMain(this);
		setContentView(R.layout.config);

		Log.v("Activity State: onCreate()");
	}

	public void launchChangeFavorites(View v) {
		PendingIntent pi;
		configParams = Configs.readResultForMain(this);
		
		if (firstStart()) {
			Intent intent = new Intent(this, ChangePassActivity.class);
			intent.putExtra(PARAM_NEWPINPASS, true);
			pi = createPendingResult(REQUEST_ChanepasswordActivity, intent,
					0);
			intent.putExtra(PARAM_PINTENT, pi);
			this.startActivityForResult(intent, REQUEST_ChanepasswordActivity);
		} else {
			Intent intent = new Intent(this, SettingsPassActivity.class);
			// this.startActivityForResult(intent,
			// Commons.REQUEST_FavoritesManagerActivity );

			pi = createPendingResult(REQUEST_FavoritesManagerActivity, intent,
					0);

			intent.putExtra(PARAM_PINTENT, pi);
			this.startActivity(intent);
		}
	}

	private boolean firstStart() {
		boolean ret = true;
		if (configParams.pinPass.length() > 0)
			ret = false;
		return ret;
	}

	public void launchChangeTheme(View v) {
		this.startActivity(new Intent(this, SwitchThemeActivity.class));
	}

	public void launchChangePassword(View v) {
		PendingIntent pi;
		Intent intent = new Intent(this, ChangePassActivity.class);
		intent.putExtra(PARAM_NEWPINPASS, false);
		pi = createPendingResult(REQUEST_ChanepasswordActivity, intent,
				0);
		intent.putExtra(PARAM_PINTENT, pi);
		this.startActivityForResult(intent, REQUEST_ChanepasswordActivity);
	}
	
	public void launchAbout(View v) {
		this.startActivity(new Intent(this, AboutActivity.class));
	}

}
