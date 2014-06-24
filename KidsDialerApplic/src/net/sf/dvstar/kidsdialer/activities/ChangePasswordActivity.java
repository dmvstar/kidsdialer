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
import net.sf.dvstar.kidsdialer.utils.Commons;
import net.sf.dvstar.kidsdialer.utils.Configs;
import net.sf.dvstar.kidsdialer.utils.Configs.ConfigParams;
import net.sf.dvstar.kidsdialer.utils.Log;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Change or create password
 * @author dmvstar
 *
 */
public class ChangePasswordActivity extends Activity  implements OnClickListener {

	private Button changePin;
	private EditText textOldPin;
	private EditText textNewPin1;
	private EditText textNewPin2;
	private ConfigParams configParams;
	private boolean newPinPass=false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v("Activity State: onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pass_change  );
		Bundle bundle = getIntent().getExtras();

		
		textOldPin = (EditText) findViewById(R.id.edt_old_pin);
		textNewPin1 = (EditText) findViewById(R.id.edt_new_pin_1);
		textNewPin2 = (EditText) findViewById(R.id.edt_new_pin_2);
		changePin = (Button)  findViewById(R.id.button_check_pin);
		
		changePin.setOnClickListener(this);
   

        if(bundle.getBoolean(Commons.PARAM_NEWPINPASS,false)==true)
        {
        	textOldPin.setEnabled(false);
        	newPinPass = true;
        }
	}
	
	@Override
	public void onClick(View v) {
		String savPin, oldPin, newPin1, newPin2;
		
		configParams = Configs.readResultForMain(this);
		Toast.makeText(this,
				"ChangePassActivity onClick ["+configParams.pinPass+"]["+textOldPin.getText().toString()+"]["+textNewPin1.getText().toString()+"]["+textNewPin2.getText().toString()+"]",
				Toast.LENGTH_SHORT).show();
		if(newPinPass){
			newPin1 = textNewPin1.getText().toString();
			newPin2 = textNewPin2.getText().toString();

			if( newPin1.equals(newPin2) ){
				configParams.pinPass = textNewPin1.getText().toString();
				Configs.saveResultForMain(this, configParams);
				Toast.makeText(this,
						"ChangePassActivity onClick - Save Pin ["+configParams.pinPass+"]",
						Toast.LENGTH_SHORT).show();
			}  else {
				Toast.makeText(this,
						"ChangePassActivity onClick - Pin is Wrong - not same !",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			savPin 	= configParams.pinPass;
			oldPin 	= textOldPin.getText().toString();
			newPin1 = textNewPin1.getText().toString();
			newPin2 = textNewPin2.getText().toString();
			if( newPin1.equals(newPin2) && savPin.equals(oldPin)){
				configParams.pinPass = textNewPin1.getText().toString();
				Configs.saveResultForMain(this, configParams);
				Toast.makeText(this,
						"ChangePassActivity onClick - Change Pin ["+configParams.pinPass+"]",
						Toast.LENGTH_SHORT).show();
			}  else {
				Toast.makeText(this,
						"ChangePassActivity onClick - Pin is Wrong - not same !",
						Toast.LENGTH_SHORT).show();
			}
		}
		this.finish();
	}

}
