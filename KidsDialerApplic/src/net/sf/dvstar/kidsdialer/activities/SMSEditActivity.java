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

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.data.ContactItem;
import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SMSEditActivity extends Activity {

	private ContactItem mContact;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v("SMSEditActivity Activity State: onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_edit);

		String phoneNumber = getIntent().getExtras().getString( "phone_number" );
		Intent intent = getIntent();
		mContact = (ContactItem) intent.getSerializableExtra(ContactItem.Desc);
		
		Resources res = getResources();
		String[] sms_template_key = res.getStringArray(R.array.sms_template_key);
		
		// адаптер
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				//R.layout.spinner_item_text,
				sms_template_key);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		TextView tvMyPhone = (TextView) findViewById(R.id.tvMyPhone);
		tvMyPhone.setText( "+3"+Utils.getMyPhoneNumber( this ) );
		
		EditText editTextSMS = (EditText)findViewById(R.id.editTextSMS);
		editTextSMS.setText("");
		
		Spinner spinner = (Spinner) findViewById(R.id.spTemplateSMS);
		spinner.setAdapter(adapter);
		// заголовок
		spinner.setPrompt("SMS");
		// выделяем элемент
		//spinner.setSelection(2);
		// устанавливаем обработчик нажатия
/*
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// показываем позиция нажатого элемента
				Toast.makeText(getBaseContext(), "Position = " + position,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
*/
	}
	
	
	/**
	 * Make SMS to selected contacts phone number
	 * @param view from call 
	 */
	public void sendSMS(View view) {
		//super.onBackPressed();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date date = new Date();
		
		System.out.println(dateFormat.format(date));		
		
		Spinner spinner = (Spinner) findViewById(R.id.spTemplateSMS);
		EditText editTextSMS = (EditText)findViewById(R.id.editTextSMS);
		
		String phoneNumber = getIntent().getExtras().getString("phone_number");
		String message = spinner.getSelectedItem().toString();
		message +=  
				"\n"+ editTextSMS.getText().toString() +
				"\n\n" + "(+3"+Utils.getMyPhoneNumber( this )+")"+
				"\n\n" + dateFormat.format(date);
 		
		SmsManager sms = SmsManager.getDefault();
	    sms.sendTextMessage(phoneNumber, null, message, null, null);		
		
		finish();
	}
	
	public void cancelSMS(View view) {
		finish();
	}

}
