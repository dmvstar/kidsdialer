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

package net.sf.dvstar.kidsdialer.utils.theme;

import android.content.Context;
import android.content.SharedPreferences;

class PreferenceManager {

	final static String PREF_THEME_TYPE = "PREF_THEME_TYPE";
	final static String PREF_THEME_ID = "PREF_THEME_ID";
	final static String PREF_THEME_PACKAGE = "PREF_THEME_PACKAGE";

	// theme type
	static void setThemeType(Context context, boolean value) {
		SharedPreferences pref = context.getSharedPreferences("PREF_NAME",
				Context.MODE_PRIVATE);
		pref.edit().putBoolean(PREF_THEME_TYPE, value).commit();
	}

	static boolean getThemeType(Context context) {
		SharedPreferences pref = context.getSharedPreferences("PREF_NAME",
				Context.MODE_PRIVATE);
		return pref.getBoolean(PREF_THEME_TYPE, false);
	}

	// theme id
	static void setThemeId(Context context, int value) {
		SharedPreferences pref = context.getSharedPreferences("PREF_NAME",
				Context.MODE_PRIVATE);
		pref.edit().putInt(PREF_THEME_ID, value).commit();
	}

	static int getThemeId(Context context, int defaultThemeId) {
		SharedPreferences pref = context.getSharedPreferences("PREF_NAME",
				Context.MODE_PRIVATE);
		return pref.getInt(PREF_THEME_ID, defaultThemeId);
	}

	// theme package
	static void setThemePackage(Context context, String value) {
		SharedPreferences pref = context.getSharedPreferences("PREF_NAME",
				Context.MODE_PRIVATE);
		pref.edit().putString(PREF_THEME_PACKAGE, value).commit();
	}

	static String getThemePackage(Context context) {
		SharedPreferences pref = context.getSharedPreferences("PREF_NAME",
				Context.MODE_PRIVATE);
		String ret = pref.getString(PREF_THEME_PACKAGE, "");  
		return ret;
	}
}
