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
import net.sf.dvstar.kidsdialer.apps.ThemedActivity;
import net.sf.dvstar.kidsdialer.utils.Commons;
import net.sf.dvstar.kidsdialer.utils.Configs;
import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.Configs.ConfigParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Verify password from saved
 * @author dmvstar
 */
public class VerifyPasswordActivity extends ThemedActivity  implements OnClickListener {

	private Button checkPin;	
	private EditText textPin;	
	private ConfigParams configParams;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v("Activity State: onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pass_form  );
		checkPin = (Button) findViewById(R.id.button_check_pin);
		textPin = (EditText) findViewById(R.id.edt_old_pin);
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
		if(passIsValid()) {
			launchFavoritesManager();
		} else {
			this.finish();
		}
	}

	private boolean passIsValid() {
		boolean ret = false;
		String savedPass, readPass;
		configParams = Configs.readResultForMain(this);
		savedPass = configParams.pinPass; 
		readPass = textPin.getText().toString(); 
		if(readPass.equals(savedPass)) ret= true;
		else {
			Toast.makeText(this,
					"VerifyPasswordActivity onClick - Pin is Wrong ["+readPass+"]["+savedPass+"] !",
					Toast.LENGTH_SHORT).show();
		}
		return ret;
	}	
	
	
}
