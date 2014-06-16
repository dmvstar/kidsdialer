package net.sf.dvstar.kidsdialer.activities;

import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.utils.Commons;
import net.sf.dvstar.kidsdialer.utils.Log;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingsPassActivity extends Activity  implements OnClickListener {

	Button checkPin;	
	EditText textPin;	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v("Activity State: onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pass_form  );
		checkPin = (Button) findViewById(R.id.button_check_pin);
		textPin = (EditText) findViewById(R.id.edit_text_pin);
		checkPin.setOnClickListener(this);
	}

	private void launchFavoritesManager() {
		Intent intent = new Intent(this, FavoritesManagerActivity.class);
		this.startActivityForResult(intent, Commons.REQUEST_FavoritesManagerActivity );
		//this.startActivity( intent );
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		finish();
	}
	
	
	
	@Override
	public void onClick(View v) {
		launchFavoritesManager();
	}	
	
	
}
