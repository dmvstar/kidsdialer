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

package net.sf.dvstar.kidsdialer;

import net.sf.dvstar.kidsdialer.activities.AboutActivity;
import net.sf.dvstar.kidsdialer.activities.ConfigActivity;
import net.sf.dvstar.kidsdialer.activities.ContactAdderActivity;
import net.sf.dvstar.kidsdialer.adapters.ContactPrimaryCursorAdapter;
import net.sf.dvstar.kidsdialer.apps.ThemedActivity;
import net.sf.dvstar.kidsdialer.data.ContactPrimaryCursorData;
import net.sf.dvstar.kidsdialer.utils.Commons;
import net.sf.dvstar.kidsdialer.utils.Configs;
import net.sf.dvstar.kidsdialer.utils.Configs.ConfigParams;
import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public final class KidsDialerMainActivity extends ThemedActivity implements Commons {

/*	
	private Button mAddAccountButton;
	private boolean mShowInvisible;
*/	
	private ListView mContactList;
	private ConfigParams mConfigParams;
	
	/**
	 * Called when the activity is first created. Responsible for initializing
	 * the UI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		mParentActivity = this;
		
		Log.v("KidsDialerMainActivity Activity State: onCreate()");
		Log.v("KidsDialerMainActivity Activity State: onCreate()" + getTheme());
		//ThemesManagerHelper.setThemeForActivity( this );
		super.onCreate(savedInstanceState);
		Log.v("KidsDialerMainActivity Activity State: onCreate()" + getTheme());
		
		setContentView(R.layout.contact_manager_main);

		// Obtain handles to UI objects
		//mAddAccountButton = (Button) findViewById(R.id.addContactButton);
		//mShowInvisibleControl = (CheckBox) findViewById(R.id.showInvisible);
		mContactList = (ListView) findViewById( R.id.contactList );
		
		
		// Initialize class properties
		//mShowInvisible = false;
		//mShowInvisibleControl.setChecked(mShowInvisible);

		// Register handler for UI elements
		/*
		mAddAccountButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "mAddAccountButton clicked");
				launchContactAdder();
			}
		});
		
		mShowInvisibleControl
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Log.d(TAG, "mShowInvisibleControl changed: "
								+ isChecked);
						mShowInvisible = isChecked;
						populateContactList();
					}
				});
		*/
		// Populate the contact list
		populateContactList();
		//dataChanged = false;
	}

	@Override
	public void onResume() {
		mConfigParams = Configs.readResultForMain(this);
		if(mConfigParams.dataChanged) populateContactList();
		Configs.saveResultForMain(this, false);
		super.onResume();
		Log.v("Activity State: onResume() "+this);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.v("onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.kids_dialer_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(this,
				item.getItemId() + " onOptionsItemSelected" + item.getTitle(),
				Toast.LENGTH_SHORT).show();
		
		switch (item.getItemId()) {
		case R.id.main_menu_settings: {
			launchSettings();
		}
			break;
			
		case R.id.main_menu_about: {
			launchAbout();
		}
			break;
			
			
		case R.id.main_menu_exit: {
			finish();
		}
			break;
			
		}
		
		return true;
	}
	
	private void launchSettings() {
/*
		PendingIntent pi;
		Intent intent = new Intent(this, SettingsPassActivity.class);
		//this.startActivityForResult(intent, Commons.REQUEST_FavoritesManagerActivity );
		
		pi = createPendingResult(REQUEST_FavoritesManagerActivity, intent, 0);
		
		intent.putExtra(PARAM_PINTENT, pi);
		
		this.startActivity(intent);
*/
		
		Intent intent = new Intent(this, ConfigActivity.class );
		this.startActivity(intent);

	}
	
	private void launchAbout() {
		Intent intent = new Intent(this, AboutActivity.class);
		this.startActivity(intent);
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(" onActivityResult ["+requestCode+"]["+resultCode+"]("+data+")");
		
		switch(requestCode) {
			case REQUEST_ContactActivity: {
				if (resultCode == Activity.RESULT_OK) {
					
				}
			
			} break;
		
		}
//		if (resultCode == Activity.RESULT_OK) {
//			dataChanged = data.getExtras().getBoolean("dataChanged");
//			if(dataChanged) populateContactList();
//		}
	}

	
	/**
	 * Populate the contact list based on account currently selected in the
	 * account spinner.
	 */
	private void populateContactList() {
       
        ContactPrimaryCursorData curData = new ContactPrimaryCursorData(
    			this,
            	new int[] {R.id.contact_entry_l, R.id.contact_entry_r},
            	CONTACT_COLUMNS_PREFS,
            	CONTACT_COLUMNS_NAMES
    		);
        
        //curData.getContactCursor();
        Utils.printCursorData( curData.getContactCursor() );
               
        ContactPrimaryCursorAdapter mContactListAdapterP = new ContactPrimaryCursorAdapter(
        		this, 
//        		R.layout.contact_entry_two, 
        		R.layout.contact_entry_two_img, 
        		curData, //.getContactCursor(),
        		CONTACT_COLUMNS_PREFS, 
                new int[] {
        				R.id.contact_entry_l, R.id.contact_entry_r		
        		},
        		CONTACT_FIELD_NAMES, 
                new int[] {
        				R.id.contactEntryText,R.id.contactPhoneText
        		}
        );
  
        mContactList.setAdapter(mContactListAdapterP);
        
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    Log.v("onConfigurationChanged "+newConfig.orientation);
/*	    
	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        setContentView(R.layout.contact_entry_two);
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	        setContentView(R.layout.contact_entry_two_port);
	    }
*/	    
	}	
	
	
	

	/**
	 * Launches the ContactAdder activity to add a new contact to the selected
	 * accont.
	 */
	protected void launchContactAdder() {
		Intent i = new Intent(this, ContactAdderActivity.class);
		startActivity(i);
	}
	
/*	
	@Override
	public Resources getResources() {
		return ThemesManagerHelper.getResourcesForActivity(this, super.getResources());
	}

	@Override
	public Resources.Theme getTheme() {
		return ThemesManagerHelper.getThemeForActivity(this, super.getTheme());
	}
*/	
}
