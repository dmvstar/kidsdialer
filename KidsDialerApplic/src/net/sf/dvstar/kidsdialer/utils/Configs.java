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
		return cf;
	}
	
	
	/**
	 * Save result for update contact list
	 * @param context
	 * @param dataChanged 
	 */
	public static  void saveResultForMain(Context context, boolean dataChanged) {
		ConfigParams cp = new ConfigParams(); cp.setDataChanged(dataChanged);
		saveResultForMain(context, cp);
	}
		

	public static  void saveResultForMain(Context context, ConfigParams pConfigParams) {
		SharedPreferences prefs = 
		        PreferenceManager.getDefaultSharedPreferences(context); 
		Editor editor = prefs.edit();
		editor.putBoolean(Commons.PARAM_DATACHANGED, pConfigParams.dataChanged);
		editor.commit();		
	}
	
	
	public static class ConfigParams {
		public boolean dataChanged = false;
		public void setDataChanged(boolean dataChanged) { this.dataChanged = dataChanged;}
	}
	
	
}
