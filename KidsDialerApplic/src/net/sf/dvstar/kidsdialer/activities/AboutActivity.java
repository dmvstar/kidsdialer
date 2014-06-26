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

import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.Utils;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends Activity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v("Activity State: onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about );
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String version = pInfo.versionName;
			TextView v = (TextView) findViewById(R.id.lbVersionNum);
			v.setText(pInfo.versionName);
			
			v = (TextView) findViewById(R.id.lbHintOfDay);
			v.setText(getSeasonTip());
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
	}	
	
	private String getSeasonTip(){
		String ret = getResources().getString(R.string.hint_of_day);
		
		String[] seasonTips = getResources().getStringArray(R.array.array_hint_of_day);
		
		ret = seasonTips[Utils.getSeasonId()];
		
		return ret;
	}
	
	public void makeDonate(View v) {
		
	}
	
	
}
