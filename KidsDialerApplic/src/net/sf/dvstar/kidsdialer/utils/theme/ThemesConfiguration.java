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

import java.util.ArrayList;
import java.util.List;

import net.sf.dvstar.kidsdialer.utils.theme.ThemedResources.ThemeDesc;


import android.content.Context;

public class ThemesConfiguration {

	private List<ThemeDesc> packagesList = new ArrayList<ThemeDesc>();
	private int mDefaultThemeId = -1;
	private int mTempDefaultThemeId = -1;
	private String mDetachedThemeName = "DetachedTheme";
	private String mPackagePrefixName = "";
	
	List<ThemeDesc> getInternalThemes(Context context) {
	    
	    if(mDefaultThemeId == -1) {
	        ThemeDesc theme1 =  new ThemeDesc(false);
	        theme1.mPackageName = "";
	        theme1.mTitle = "Default";
	        theme1.mId = ThemeManager.getMainThemeId(context);
	        packagesList.add(0, theme1);
	    }
		return packagesList;
	}
	
	public ThemesConfiguration addNameForDetachedTheme(String detachedThemeName) {
		mDetachedThemeName = detachedThemeName;
		return this;
	}
	
	public ThemesConfiguration addPackagePrefixName(String packagePrefixName) {
		mPackagePrefixName = packagePrefixName;
		return this;
	}
	
	
	String getNameForDetachedTheme() {
		return mDetachedThemeName;
	}
	
	String getPackagePrefixName(Context ctx) {
		if(!mPackagePrefixName.equals("")) {
			return mPackagePrefixName;
		} else {
			return ctx.getPackageName();
		}
	}
	
	public ThemesConfiguration addInnerTheme(int thmemeId, String themeTitle) {
		if(mDefaultThemeId == -1) {
			mTempDefaultThemeId = thmemeId;
		}
		addInnerTheme(thmemeId, themeTitle, false);
		return this;
	}
	
	public ThemesConfiguration addDefaultInnerTheme(int thmemeId, String themeTitle) {
		addInnerTheme(thmemeId, themeTitle, true);
		return this;
	}
	
	void addInnerTheme(int thmemeId, String themeTitle, boolean isDefault) {
		ThemeDesc theme1 =  new ThemeDesc(false);
        theme1.mPackageName = "";
        theme1.mTitle = themeTitle;
        theme1.mId = thmemeId;
        packagesList.add(theme1);
        if(isDefault) {
        	mDefaultThemeId = thmemeId;
        }
	}
	
	int getDefaultThemeId(Context context) {
		if(mDefaultThemeId == -1) {
			if(mTempDefaultThemeId == -1) {
		         return ThemeManager.getMainThemeId(context);
			} else {
				return mTempDefaultThemeId;
			}
		} else {
			return mDefaultThemeId;
		}
	}

}
