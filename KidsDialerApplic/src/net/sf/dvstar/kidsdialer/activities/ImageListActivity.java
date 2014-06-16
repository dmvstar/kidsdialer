package net.sf.dvstar.kidsdialer.activities;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.apps.ThemedListActivity;
import net.sf.dvstar.kidsdialer.utils.Commons;
import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.OIFileManager;
import net.sf.dvstar.kidsdialer.utils.Utils;
import net.sf.dvstar.kidsdialer.utils.theme.ThemedResources;
import net.sf.dvstar.kidsdialer.utils.theme.DrawableItem;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class ImageListActivity extends ThemedListActivity implements Commons,
		OIFileManager {

	private ItemsAdapter adapterImages;
	private static Activity  mActivity;

	public ImageListActivity() {
		// this.mImage = image;
		mActivity = this;
	}

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		DrawableItem[] items = new ItemManager(this).getLoadedItemsArray();

		this.adapterImages = new ItemsAdapter(this, R.layout.list_image_item,
				items);

		setListAdapter(adapterImages);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		DrawableItem item = (DrawableItem) getListAdapter().getItem(position);
		Toast.makeText(this, "Selected " + item, Toast.LENGTH_LONG).show();
		Log.v("Selected item ["+position+"] "+item);

		if (item.getDrawableItemInfo().getImageId() == R.drawable.folder_172_1) {
			Intent intent = new Intent(ACTION_PICK_FILE);

			try {
				startActivityForResult(intent,
						REQUEST_CODE_PICK_FILE_OR_DIRECTORY);
			} catch (ActivityNotFoundException e) {
				// No compatible file manager was found.

				Toast toast = new Toast(v.getContext());
				Utils.showCustomToast(
						getLayoutInflater(),
						toast,
						getResources().getString(
								R.string.no_filemanager_installed));
				// Toast.makeText(this, R.string.no_filemanager_installed,
				// Toast.LENGTH_SHORT).show();
			}
		}

		Intent returnIntent = new Intent();

		returnIntent.putExtra(DrawableItem.ImageItemDescID, 	item.getDrawableItemInfo().getImageId());
		returnIntent.putExtra(DrawableItem.ImageItemDescPath, 	item.getDrawableItemInfo().getImagePath());
		
		returnIntent.putExtra(DrawableItem.ImageItemDescMode, 	item.getDrawableItemInfo().getItemMode());
		returnIntent.putExtra(DrawableItem.ImageItemDescPathMode, item.getDrawableItemInfo().getItemPathMode());
		
		returnIntent.putExtra(DrawableItem.ImageItemDescSerial, item.getSerialBitmap());
		
		returnIntent.putExtra(DrawableItem.ImageItemDesc,		(Serializable)item.getDrawableItemInfo());
				
		setResult(RESULT_OK, returnIntent);

		// mImage.setImageDrawable(item.getImage());
		this.finish();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_PICK_FILE_OR_DIRECTORY:
			if (resultCode == RESULT_OK && data != null) {
				// obtain the filename
				Uri fileUri = data.getData();
				if (fileUri != null) {
					String filePath = fileUri.getPath();
					
					Utils.showCustomToastInfo(mActivity, "selected " + filePath, R.drawable.help_about);
					
					
//					Toast.makeText(this, "selected " + filePath,
//							Toast.LENGTH_LONG).show();
				}
			}
			break;
		default:
			break;
		}
	}

//	protected void ponListItemClick1(ListView l, View v, int position, long id) {
//		this.adapterImages.getItem(position)
//				.click(this.getApplicationContext());
//	}

	private class ItemsAdapter extends ArrayAdapter<DrawableItem> {

		private DrawableItem[] items;

		public ItemsAdapter(Context context, int textViewResourceId,
				DrawableItem[] items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_image_item, null);
			}

			DrawableItem it = items[position];
			if (it != null) {
				ImageView iv = (ImageView) v.findViewById(R.id.list_image_item);
				if (iv != null) {
					iv.setImageBitmap(it.getImage());
				}

			}

			return v;
		}
	}


	private class ItemManager {

/*		
		int images[] = new int[] { R.drawable.cars_172_1,
				R.drawable.cars_172_2, R.drawable.cars_172_3,
				R.drawable.cars_172_4, R.drawable.cars_172_5,
				R.drawable.folder_172_1 };
*/
		
		//Drawable[] dimages;
		DrawableItem[] tdimages;
		
		List<DrawableItem> items = new ArrayList<DrawableItem>();
		private ThemedResources mThemedResource;
		
		public ItemManager(Activity context) {

			mThemedResource 	= new ThemedResources(context);
			tdimages 			= mThemedResource.getContactDrawables();
						
			/*
			for (int i = 0; i < images.length; i++) {
				items.add(new Item(context, images[i])); // .prepareImage(images[i]);
			}
			*/
			
			for (int i = 0; i < tdimages.length; i++) {
				items.add(
						tdimages[i]
						//new DrawableItem(tdimages[i].getImage(), tdimages[i].getIconPath())
				); // .prepareImage(images[i]);
			}			

			Resources res = getResources();
			String list[];
			try {
				list = res.getAssets().list("cars");
				if (list.length > 0) {
					Log.v("L" + 1 + ": list:" + Arrays.asList(list));

					if (list != null)
						for (int i = 0; i < list.length; ++i) {
							items.add(new DrawableItem(context, "cars/" + list[i],
									DrawableItem.ITEM_MODE_PATH_ASSET));
						}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public List<DrawableItem> getLoadedItems() {
			return items;
		}

		public DrawableItem[] getLoadedItemsArray() {
			DrawableItem[] itemsa = new DrawableItem[items.size()];
			for (int i = 0; i < items.size(); i++) {
				itemsa[i] = items.get(i);
			}
			return itemsa;
		}

	}

}

/*
 * 
 * http://stackoverflow.com/questions/459729/how-to-display-list-of-images-in-
 * listview-in-android
 * http://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
 * http://stackoverflow.com/questions/7361976/how-to-create-a-drawable-from-a-stream-without-resizing-it
 * 
 * http://stackoverflow.com/questions/2549548/load-image-in-device-independent-and-screen-independent-fashion-into-a-layout-vi
 * http://stackoverflow.com/questions/11581649/about-android-image-size-and-assets-sizes
 * 
 */