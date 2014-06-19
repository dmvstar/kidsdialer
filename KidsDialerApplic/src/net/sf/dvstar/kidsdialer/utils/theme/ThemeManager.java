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

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ThemeManager {

	public static final String LTAG="ThemeManager";
	
	private static ThemesConfiguration mThemesConfiguration = null;
	private static int themeResourceId = 0;

	static int getMainThemeId(Context context) {
		int id_st = -1;
		try {
			id_st = context.getResources().getIdentifier("MainTheme", "style",
					context.getPackageName());
		} catch (NullPointerException ex) {
			Log.e(LTAG, ex.getMessage());
			throw new IllegalArgumentException(
					"There is no default theme with name \"MainTheme\"");
		}
		return id_st;
	}

	static void init(Context ctx) {
		if (isThemeDetached(ctx)) {

			final PackageManager pm = ctx.getPackageManager();
			List<PackageInfo> packages = pm.getInstalledPackages(0);
			boolean packageDeleted = true;
			String themePackage = PreferenceManager.getThemePackage(ctx);
			for (PackageInfo pi : packages) {
				String pnm = pi.applicationInfo.packageName;
				if (pnm.equals(themePackage)) {
					packageDeleted = false;
				}
			}
			if (packageDeleted) {
				setInnerTheme(ctx, getMainThemeId(ctx));
			}
		}
	}

	/**
	 * Get themes list
	 * 
	 * @param ctx
	 *            context
	 * @param cfg
	 *            configuration
	 * @return list of themes descriptions
	 */
	public static List<ThemeDesc> getThemes(Context ctx, ThemesConfiguration cfg) {
		mThemesConfiguration = cfg;
		List<ThemeDesc> packagesList = new ArrayList<ThemeDesc>();
		final PackageManager pm = ctx.getPackageManager();

		packagesList.addAll(mThemesConfiguration.getInternalThemes(ctx));

		int id = 8003;

		List<PackageInfo> packages = pm.getInstalledPackages(0);

		for (PackageInfo pi : packages) {
			String pnm = pi.applicationInfo.packageName;
			if (pnm.startsWith(mThemesConfiguration.getPackagePrefixName(ctx))
					&& (!ctx.getPackageName().equals(pnm))) {
				try {
					Resources res = pm.getResourcesForApplication(pnm);
					ThemeDesc theme = new ThemeDesc(true);
					theme.mPackageName = pnm;
					theme.mTitle = res.getString(pi.applicationInfo.labelRes);
					theme.mId = id;
					id++;
					packagesList.add(theme);

				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		return packagesList;
	}

	private static void setInnerTheme(Context ctx, int id) {
		PreferenceManager.setThemeType(ctx, false);
		PreferenceManager.setThemeId(ctx, id);
		PreferenceManager.setThemePackage(ctx, "");
	}

	public static void setTheme(Context ctx, ThemeDesc theme) {
		if (theme.mDetached) {
			PreferenceManager.setThemeType(ctx, true);
			PreferenceManager.setThemePackage(ctx, theme.mPackageName);
		} else {
			setInnerTheme(ctx, theme.mId);
		}
	}

	static boolean isThemeDetached(Context context) {
		return PreferenceManager.getThemeType(context);
	}

	static int getThemeId(Context context) {
		return PreferenceManager.getThemeId(context, getMainThemeId(context));
	}

	static int getThemeResourceId() {
		return themeResourceId;
	}

	private static String getStringResourceByName(Resources res,
			String aPackageName, String aString) {
		int resId = res.getIdentifier(aString, "string", aPackageName);
		return res.getString(resId);
	}

	static String getCurrentThemePackage(Context ctx) {
		String themePackage = PreferenceManager.getThemePackage(ctx);
		return themePackage;
	}
	
	static Resources.Theme getTheme(Context ctx, Resources.Theme superTheme) {
		final PackageManager pm = ctx.getPackageManager();
		Resources res = null;
		try {
			String themePackage = PreferenceManager.getThemePackage(ctx);
			res = pm.getResourcesForApplication(themePackage);
			// Log.v("ThemeManager", "themePackage = "+themePackage );
			// Log.v("ThemeManager",
			// "Activity State: onCreate() [R.string.button_change_theme="
			// +getStringResourceByName(res, themePackage,
			// "button_change_theme") );

			Resources.Theme rs = res.newTheme();
			// superTheme.dump(0, "ST", "1");
			rs.setTo(superTheme);
			// rs.dump(0, "DT", "2");

			int id_st = 0;
			themeResourceId = id_st;
			try {
				// TODO: mConfig.getNameForDetachedTheme()
				id_st = res.getIdentifier(
						// mThemesConfiguration.getNameForDetachedTheme(),
						"DetachedTheme", "style",
						PreferenceManager.getThemePackage(ctx));
				themeResourceId = id_st;
			} catch (NullPointerException ex) {
				Log.e("ThemeManager", ex.getMessage());
			}
			rs.applyStyle(id_st, true);
			return rs;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int _getStyleIdValue(Context ctx, String attrStyleValue,
			int dafaultAttrStyleValue) {
		if (isThemeDetached(ctx)) {
			final PackageManager pm = ctx.getPackageManager();
			Resources res = null;
			try {
				res = pm.getResourcesForApplication(PreferenceManager
						.getThemePackage(ctx));
				int id_st = res.getIdentifier(attrStyleValue, "attr",
						PreferenceManager.getThemePackage(ctx));
				return id_st;
				// return dafaultAttrStyleValue;

			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}

		return dafaultAttrStyleValue;
	}

	public static Resources getDetachedResources(Context ctx) {
		final PackageManager pm = ctx.getPackageManager();
		Resources res = null;
		try {
			String pn = PreferenceManager.getThemePackage(ctx);
			res = pm.getResourcesForApplication(pn);
			return res;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get full name for themed resource
	 * @param ctx
	 * @param resName
	 * @return
	 */
	public static String getDrawableResourceNameForTheme(Context ctx,
			String resName) {
		String ret = null;
		String pn = PreferenceManager.getThemePackage(ctx);
		if (resName.contains(pn))
			ret = resName;
		else
			ret = pn + ":drawable/" + resName;
		
		Log.v(LTAG,
				"1 getDrawableResourceNameForTheme resName[" + resName + "][" + ret+ "]");
		return ret;
	}

	/**
	 * Get id for themed resource
	 * 
	 * @param ctx
	 *            Application Context
	 * @param resName
	 *            Resource name like as im_item_01
	 *            net.sf.dvstar.kidsdialer.theme.transformers:drawable/im_item_01
	 * @return resource id
	 * @throws NameNotFoundException
	 */
	public static int getDrawableResourceIdForTheme(Context ctx, String resName)
			//throws NameNotFoundException 
		{
		int ret = 0;
		Resources res = getDetachedResources(ctx);
		if (res != null) {
			
			ret = res.getIdentifier(
					getDrawableResourceNameForTheme(ctx, resName), null, null);
			
			Log.v(LTAG,
					"2 getDrawableResourceIdForTheme resName [0x" + Integer.toHexString(ret) +"][" + resName + "]["
							+ getDrawableResourceNameForTheme(ctx, resName)
							);
			// ret = res.getIdentifier(resName, "drawable", pn );
			// Log.v("ThemeManager","getDrawableIdResourcesForTheme resName["+resName+"]["+
			// getDrawableNameResourcesForTheme(ctx, resName) +"][0x"+
			// Integer.toHexString(ret)+"]");
		} else {
			ret = 0;
		}
//			throw new NameNotFoundException("Not found "
//					+ getDrawableResourceNameForTheme(ctx, resName) + " for "
//					+ resName);
		return ret;
	}

	/**
	 * Get resource name from path
	 * @param resName full resource path 
	 * @return part of full resource path
	 */
	private static String getResourceNameFromPath(String resName) {
		String ret = resName;
		int index = 0;
		if((index=resName.indexOf("drawable/"))>0){
			index+=9;
			ret = resName.substring(index);
		}
		return ret;
	}

	/**
	 * Get drawable themed resource
	 * @param ctx
	 * @param resName
	 * @return
	 * @throws NameNotFoundException
	 */
	public static Drawable getDrawableResourcesForTheme(Context ctx,
			String resName) throws NameNotFoundException {
		Drawable ret = null;
		if (resName == null)
			return ret;
		// int id = res.getIdentifier(resName, "drawable", pn );
		String pn = PreferenceManager.getThemePackage(ctx);
		Resources res = getDetachedResources(ctx);
		int id = getDrawableResourceIdForTheme(ctx, resName);
		if (id > 0) {
			Log.v("ThemeManager", "3 getDrawableResourcesForTheme resName[0x"+Integer.toHexString(id)+"][" + resName
					+ "]\nt[" + pn + "]\ns["+ctx.getPackageName()+"]"+res);
			ret = res.getDrawable(id);
		}	
		else {
			/**
			 * try get resource from application
			 */
			res = ctx.getResources();
			id = res.getIdentifier(ctx.getPackageName() + ":drawable/" + getResourceNameFromPath(resName),
						null, null);
			Log.v("ThemeManager", "3 getDrawableResourcesForTheme resName[0x"+Integer.toHexString(id)+"][" + getResourceNameFromPath(resName)
					+ "]\nt[" + pn + "]\ns["+ctx.getPackageName()+"]"+res);
			if (id > 0)
				ret = ctx.getResources().getDrawable(id);
		}
		return ret;
	}
	
	public static int getColorResourcesForTheme(Context ctx,
			String resName) throws NameNotFoundException {
		int ret=0;
		if (resName == null)
			return ret;
		
		String pn = PreferenceManager.getThemePackage(ctx);
		Resources tr = getDetachedResources(ctx);
		int resId = -1;
		
		resId = tr.getIdentifier(resName, "color", pn );
		
		if(resId>0) {
			ret = tr.getColor(resId);
		}
		
		Log.v(LTAG, "["+pn+"]["+resName+"]["+Integer.toHexString(resId)+"]["+Integer.toHexString(ret)+"]");
		
		return ret;
	}	

}
