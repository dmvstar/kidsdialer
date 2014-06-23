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

package net.sf.dvstar.kidsdialer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Incapsulate config
 * @author dmvstar
 *
 */
public class Configs {

	/**
	 * Read result for update contact list
	 * @link http://stackoverflow.com/questions/4878159/android-whats-the-best-way-to-share-data-between-activities
	 * @param context app context
	 * @return ConfigParams 
	 */
	public static ConfigParams readResultForMain(Context context) {
		SharedPreferences prefs = 
		        PreferenceManager.getDefaultSharedPreferences(context); 
		ConfigParams cf = new ConfigParams();
		cf.dataChanged = prefs.getBoolean(Commons.PARAM_DATACHANGED, false);
		cf.pinPass = prefs.getString(Commons.PARAM_PINPASS, "");
		return cf;
	}
	
	
	/**
	 * Save result for update contact list
	 * @param context
	 * @param dataChanged 
	 */
	public static  void saveResultForMain(Context context, boolean dataChanged) {
		ConfigParams cp = readResultForMain(context);
		cp.setDataChanged(dataChanged);
		saveResultForMain(context, cp);
	}
		

	public static  void saveResultForMain(Context context, ConfigParams pConfigParams) {
		SharedPreferences prefs = 
		        PreferenceManager.getDefaultSharedPreferences(context); 
		Editor editor = prefs.edit();
		editor.putBoolean(Commons.PARAM_DATACHANGED, pConfigParams.dataChanged);
		editor.putString(Commons.PARAM_PINPASS, pConfigParams.pinPass);
		editor.commit();		
	}
	
	
	public static class ConfigParams {
		public boolean dataChanged = false;
		public String pinPass = "";
		public void setDataChanged(boolean dataChanged) { this.dataChanged = dataChanged;}
	}
	
	
}
