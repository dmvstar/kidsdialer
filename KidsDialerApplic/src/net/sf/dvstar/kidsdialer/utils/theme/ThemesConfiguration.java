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
