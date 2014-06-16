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

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sf.dvstar.kidsdialer.data.SerializableBitmap;
import net.sf.dvstar.kidsdialer.utils.Utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.TypedValue;

public class DrawableItem implements Serializable//, Parcelable 
{
	private static final long serialVersionUID = 946485355973233758L;
	
	public static final String ImageItemDesc 	= "ImageItem";
	public static final String ImageItemDescID 	= "ImageItemId";
	public static final String ImageItemDescPath = "ImageItemPath";
	public static final String ImageItemDescRes = "ImageItemRes";
	public static final String ImageItemDescSerial = "ImageItemSerial";
	public static final String ImageItemDescMode = "ImageItemMode";
	public static final String ImageItemDescPathMode = "ImageItemPathMode";

	public static final int ITEM_MODE_NONE = 0;
	public static final int ITEM_MODE_REID = 1;
	public static final int ITEM_MODE_DRAW = 2;
	public static final int ITEM_MODE_PATH = 3;
	public static final int ITEM_MODE_NRES = 4;
	public static final int ITEM_MODE_PATH_ASSET = 31;
	public static final int ITEM_MODE_PATH_FULLS = 32;
	public static final int ITEM_MODE_PATH_NTRES = 33;
	
	private Bitmap mBitmap;
	private SerializableBitmap mSerialBitmap;
	private DrawableItemInfo mDrawableItemInfo;

	//private transient Drawable mDrawableIcon;
	
	public DrawableItem(Context context, int imageId) {
		setDrawableItemInfo(new DrawableItemInfo(ITEM_MODE_REID, ITEM_MODE_NONE, imageId, ""));
		prepareImage(context, getDrawableItemInfo());
	}

	public DrawableItem(Context context, String imagePath, int aItemPathMode) {
		setDrawableItemInfo(new DrawableItemInfo(ITEM_MODE_PATH, aItemPathMode));
		getDrawableItemInfo().setImagePath(imagePath != null ? imagePath : "");
		prepareImage(context, getDrawableItemInfo());
	}
	
	public DrawableItem(Drawable drawable, String imagePath) {
		setDrawableItemInfo(new DrawableItemInfo(ITEM_MODE_DRAW, ITEM_MODE_NONE));
		getDrawableItemInfo().setImagePath(imagePath != null ? imagePath : "");
		prepareImage(drawable);
	}

	public DrawableItem(Drawable drawable, String imagePath, int aItemPathMode) {
		setDrawableItemInfo(new DrawableItemInfo(ITEM_MODE_NRES, aItemPathMode));
		getDrawableItemInfo().setImagePath(imagePath != null ? imagePath : "");
		prepareImage(drawable);
	}
	
	public SerializableBitmap getSerialBitmap() {
		return mSerialBitmap;
	}

	public void setSerialBitmap(SerializableBitmap mSerialBitmap) {
		this.mSerialBitmap = mSerialBitmap;
	}
	
	public Bitmap getImage() {
		//return new BitmapDrawable(mBitmap);
		return mBitmap;
	}

	/*	
	public int getImageId() {
		return mIconId;
	}

	public void click(Context applicationContext) {

	}

	public String getIconPath() {
		return mIconPath;
	}

	public void setIconPath(String mIconPath) {
		this.mIconPath = mIconPath;
	}
*/
	public void prepareImage(String path, int pathMode) {

	}

	public void prepareImage(Drawable drawable) {
		Drawable mDrawableIcon = drawable;
		mBitmap = drawableToBitmap(drawable);
		mSerialBitmap = new SerializableBitmap(mBitmap, getDrawableItemInfo().getImagePath());
	}

	public void prepareImage(Context context, DrawableItemInfo mDrawableItemInfo) {
		switch(mDrawableItemInfo.mItemMode) {
			case ITEM_MODE_REID: {
				Drawable mDrawableIcon = context.getResources().getDrawable(mDrawableItemInfo.mImageId);
				mBitmap = BitmapFactory.decodeResource(context.getResources(),
					mDrawableItemInfo.mImageId);
				mBitmap.setDensity(Bitmap.DENSITY_NONE);
				mDrawableIcon = new BitmapDrawable(context.getResources(),mBitmap);
				mSerialBitmap = new SerializableBitmap(mBitmap, mDrawableItemInfo.mImagePath);
			} break;
		
			case ITEM_MODE_PATH:{
				InputStream bitmapIS = null;
			
				
				
				switch (mDrawableItemInfo.mItemPathMode) {

				case ITEM_MODE_PATH_ASSET: {
					try {

						TypedValue typedValue = new TypedValue();
						//density none divides by the density, density default keeps the original size
						typedValue.density = TypedValue.DENSITY_DEFAULT;
						
						Drawable mDrawableIcon = Drawable.createFromStream(context.getAssets().open(mDrawableItemInfo.getImagePath()), null);
						
						mDrawableIcon = Drawable.createFromResourceStream(context.getResources(), typedValue, context.getAssets().open(mDrawableItemInfo.getImagePath()), "icon");
						
						//mBitmap = BitmapFactory.decodeStream(context.getAssets().open(path));
						
						mBitmap = Utils.getNormalBitmapFromAssetsPath(context, mDrawableItemInfo.getImagePath());
						
						//Utils.showCustomToastInfo(this, "W "+mBitmap.getWidth(), R.drawable.help_about);
						
						
						//mBitmap.setDensity(Bitmap.DENSITY_NONE);
						mDrawableIcon = new BitmapDrawable(null, mBitmap);
						
						
						/*
						 * bitmapIS = context.getAssets().open(path); mBitmap =
						 * BitmapFactory.decodeStream(bitmapIS); mIcon = new
						 * BitmapDrawable(context.getResources(), mBitmap);
						 */
						mSerialBitmap = new SerializableBitmap(
								drawableToBitmap(mDrawableIcon), mDrawableItemInfo.mImagePath);

					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (bitmapIS != null)
							try {
								bitmapIS.close();
							} catch (IOException e) {
							}
					}
				}
					break;
				case ITEM_MODE_PATH_FULLS: {

				}
					break;

				}
				
				
				
				
				
			}break;
		
		}
		
	}


//	public void prepareImage(Context context, String path, int pathMode) {
//		mIconPath = path;
//
//		InputStream bitmapIS = null;
//
//		switch (pathMode) {
//
//		case ITEM_MODE_PATH_ASSET: {
//			try {
//
//				TypedValue typedValue = new TypedValue();
//				//density none divides by the density, density default keeps the original size
//				typedValue.density = TypedValue.DENSITY_DEFAULT;
//				
//				Drawable mDrawableIcon = Drawable.createFromStream(context.getAssets().open(path), null);
//				
//				mDrawableIcon = Drawable.createFromResourceStream(context.getResources(), typedValue, context.getAssets().open(path), "icon");
//				
//				//mBitmap = BitmapFactory.decodeStream(context.getAssets().open(path));
//				
//				mBitmap = Utils.getNormalBitmapFromAssetsPath(context, path);
//				
//				//Utils.showCustomToastInfo(this, "W "+mBitmap.getWidth(), R.drawable.help_about);
//				
//				
//				//mBitmap.setDensity(Bitmap.DENSITY_NONE);
//				mDrawableIcon = new BitmapDrawable(null, mBitmap);
//				
//				
//				/*
//				 * bitmapIS = context.getAssets().open(path); mBitmap =
//				 * BitmapFactory.decodeStream(bitmapIS); mIcon = new
//				 * BitmapDrawable(context.getResources(), mBitmap);
//				 */
//				mSerialBitmap = new SerializableBitmap(
//						drawableToBitmap(mDrawableIcon), mIconPath);
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				if (bitmapIS != null)
//					try {
//						bitmapIS.close();
//					} catch (IOException e) {
//					}
//			}
//		}
//			break;
//		case ITEM_MODE_PATH_FULLS: {
//
//		}
//			break;
//
//		}
//
//	}

	public Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	public String toString(){
		String ret = getDrawableItemInfo()+" ("+mBitmap+")"; 
		return ret;		
	}
	
	public DrawableItemInfo getDrawableItemInfo() {
		return mDrawableItemInfo;
	}

	private void setDrawableItemInfo(DrawableItemInfo mDrawableItemInfo) {
		this.mDrawableItemInfo = mDrawableItemInfo;
	}

	public static class DrawableItemInfo implements Serializable{

		private static final long serialVersionUID = 2382436315011343011L;
		private int mItemMode 		= ITEM_MODE_NONE;
		private int mItemPathMode 	= ITEM_MODE_NONE;
		private int mImageId;
		private String mImagePath = "";
		
		public DrawableItemInfo(int aItemMode) {
			mItemMode = aItemMode;
		}

		public void setImagePath(String aImagePath) {
			mImagePath = aImagePath;
		}
		public String getImagePath() {
			return mImagePath;
		}

		public DrawableItemInfo(int aItemMode, int aItemPathMode) {
			mItemMode = aItemMode;
			mItemPathMode = aItemPathMode;
		}
		
		public DrawableItemInfo(int aItemMode, int aItemPathMode, int aImageId, String aImagePath){
			this.mItemMode 	= aItemMode;
			this.mItemPathMode = aItemPathMode; 
			this.mImageId = aImageId;
			this.mImagePath = aImagePath;
		}
		
		public String toString(){
			String ret = "DrawableItemInfo [mItemMode="+mItemMode+"][mIconId="+mImageId+"][mItemPathMode="+mItemPathMode+"]("+mImagePath+")"; 
			return ret;		
		}

		public int getImageId() {
			return mImageId;
		}

		public int getItemMode() {
			return mItemMode;
		}

		public int getItemPathMode() {
			return mItemPathMode;
		}
		
	}
	
}




