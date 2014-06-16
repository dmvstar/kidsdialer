package net.sf.dvstar.kidsdialer.activities;

import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.utils.Log;
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
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
	}	
	
	public void makeDonate(View v) {
		
	}
	
	
}
