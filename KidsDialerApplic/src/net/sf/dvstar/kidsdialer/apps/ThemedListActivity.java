package net.sf.dvstar.kidsdialer.apps;

import android.os.Bundle;
import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.theme.ImageUtils;

public class ThemedListActivity extends android.app.ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ImageUtils imageUtils = new ImageUtils();
		imageUtils.loadThemedBackgroundDrawable(this);
		super.onCreate(savedInstanceState);
		Log.v("Activity State: onCreate()");
	}

}
