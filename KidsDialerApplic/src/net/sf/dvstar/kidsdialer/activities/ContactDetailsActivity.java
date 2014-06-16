package net.sf.dvstar.kidsdialer.activities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.apps.ThemedActivity;
import net.sf.dvstar.kidsdialer.data.ContactItem;
import net.sf.dvstar.kidsdialer.data.FavoriteDataProvider;
import net.sf.dvstar.kidsdialer.data.SerializableBitmap;
import net.sf.dvstar.kidsdialer.utils.Commons;
import net.sf.dvstar.kidsdialer.utils.Configs;
import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.Utils;
import net.sf.dvstar.kidsdialer.utils.theme.DrawableItem.DrawableItemInfo;
import net.sf.dvstar.kidsdialer.utils.theme.ThemeManager;
import net.sf.dvstar.kidsdialer.utils.theme.ThemedResources;
import net.sf.dvstar.kidsdialer.utils.theme.DrawableItem;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetailsActivity extends ThemedActivity implements Commons {

	private String mSelectedPhoneNumber;
	private ContactItem mContact;
	private boolean firstStart = true;

	private TextView mContactNameTextView;
	private ImageView mContactImageView;
	private Spinner mContactPhonesSpinner;
	private boolean orientationChanged;
	private int lastOrientation;

	private SharedPreferences mSettings;
	private SharedPreferences.Editor mEditor;
	private String KEY_PHONE_BY_UID;
	private ThemedResources mThemedResource;

	public ContactDetailsActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mThemedResource = new ThemedResources(this);
		Intent intent = getIntent();
		mContact = (ContactItem) intent.getSerializableExtra(ContactItem.Desc);
		mSettings = this.getPreferences(MODE_PRIVATE);
		lastOrientation = getResources().getConfiguration().orientation;
		setContentBasedOnLayout(lastOrientation);
		Log.v("[ContactDetailsActivity] onCreate " + lastOrientation);
		orientationChanged = false;
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.v("[ContactDetailsActivity] onStart" + lastOrientation);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.
	}

	/**
	 * Fill form values from contact info 
	 */
	private void fillFormFieldValues() {

		loadPreferences();

		Log.v("[ContactDetailsActivity] fillFieldValues "
				+ getResources().getConfiguration().orientation);
		Log.v("[ContactDetailsActivity] fillFieldValues " + mContact);

		mContactNameTextView = (TextView) findViewById(R.id.contactEntryText);
		mContactImageView = (ImageView) findViewById(R.id.contactImage);

		// mContactPhonesNltView = (TextView)
		// findViewById(R.id.contactEntryText);
		mContactPhonesSpinner = (Spinner) findViewById(R.id.contactPhoneListSpinner);

		Bitmap vContactImage = mThemedResource.getRandomContactDrawable(0);

		if (mContact.getContactImagePath() != null && mContact.getContactImagePath().length() > 0) {
			try {
				Drawable vContactImageDrawable = ThemeManager
						.getDrawableResourcesForTheme(this,
								mContact.getContactImagePath());
				mContactImageView.setImageDrawable(vContactImageDrawable);
			} catch (NameNotFoundException e) {
			}
		} else {
			mContactImageView.setImageBitmap(vContactImage);
		}

		setSafeText(R.id.contactEntryText, mContact.getContactName());
		setSafeText(R.id.contactPhoneText, mContact.getContactPhonesNl());

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item,
				R.layout.spinner_item_text, mContact.getContactPhonesList());
		adapter.setDropDownViewResource(
		// android.R.layout.simple_spinner_dropdown_item
		R.layout.spinner_dropdown_item);

		mContactPhonesSpinner.setPrompt(getResources().getString(
				R.string.promptPhones));

		if (mContactPhonesSpinner != null
				&& mContact.getContactPhonesList() != null
				&& mContact.getContactPhonesList().size() > 0) {
			mContactPhonesSpinner.setAdapter(adapter);
			int position = adapter.getPosition(mSettings.getString(
					KEY_PHONE_BY_UID, ""));
			if (position >= 0)
				mContactPhonesSpinner.setSelection(position);

			mContactPhonesSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							// показываем позицию нажатого элемента
							/*
							 * Toast.makeText(getBaseContext(), "Position = " +
							 * position + " " + getSafeText(view),
							 * Toast.LENGTH_SHORT).show();
							 */
							setSelectedPhoneNumber(getSafeText(view));
							savePreferences();
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

			setSelectedPhoneNumber(mContactPhonesSpinner.getSelectedItem()
					.toString());

			savePreferences();
		}
	}

	
	/**
	 * @todo create preference class
	 */
	private void loadPreferences() {
		KEY_PHONE_BY_UID = LAST_PHONE_NUMBER + "_" + mContact.getContactId();
		Log.v("[ContactDetailsActivity] loadPreferences "
				+ mSettings.getString(KEY_PHONE_BY_UID, "000-00-00"));
	}

	private void savePreferences() {
		KEY_PHONE_BY_UID = LAST_PHONE_NUMBER + "_" + mContact.getContactId();
		mEditor = mSettings.edit();
		mEditor.putString(KEY_PHONE_BY_UID, getSelectedPhoneNumber());
		mEditor.commit();
		Log.v("[ContactDetailsActivity] savePreferences "
				+ mSettings.getString(LAST_PHONE_UID, "000") + "-"
				+ mSettings.getString(KEY_PHONE_BY_UID, "000-00-00"));
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
			setContentBasedOnLayout(newConfig.orientation);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
			setContentBasedOnLayout(newConfig.orientation);
		}
	}

	private void setContentBasedOnLayout(int vorientation) {

		// WindowManager winMan = (WindowManager)
		// getSystemService(Context.WINDOW_SERVICE);
		int orientation = getResources().getConfiguration().orientation;

		if (vorientation < 0)
			orientation = Utils.getOrientationRotation(this);

		// if (winMan != null)
		{

			// orientation = getOrientation(this);

			if (orientation == Configuration.ORIENTATION_PORTRAIT) {
				// Portrait
				setContentView(R.layout.contact_caller_port);
				if (lastOrientation != orientation)
					orientationChanged = true;
				lastOrientation = orientation;
			}

			else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				// Landscape
				setContentView(R.layout.contact_caller_land);
				if (lastOrientation != orientation)
					orientationChanged = true;
				lastOrientation = orientation;
			}

			Log.v("[ContactDetailsActivity] setContentBasedOnLayout "
					+ orientation + "-" + getSafeText(mContactNameTextView));

			if (!checkName() || firstStart || orientationChanged) {
				fillFormFieldValues();
				firstStart = false;
			}
		}
	}

	private boolean checkName() {
		boolean ret = false;
		String s = getSafeText(mContactNameTextView);
		if (s.length() > 0)
			ret = true;
		return ret;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.kids_contact_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(this,
				item.getItemId() + " onOptionsItemSelected" + item.getTitle(),
				Toast.LENGTH_SHORT).show();

		switch (item.getItemId()) {
		case R.id.menu_contact_details: {
			launchContactDetails();
		}
			break;
		}

		return true;
	}

	private void launchContactDetails() {
		// PendingIntent pi;

		Intent intent = new Intent(this, ContactAdderActivity.class);

		intent.putExtra(PARAM_CONTACT_EDIT_MODE, CONTACT_EDIT_MODE_EDIT);
		intent.putExtra(PARAM_CONTACT_EDIT_ID, mContact.getContactId());
		intent.putExtra(ContactItem.Desc, mContact);

		this.startActivityForResult(intent,
				Commons.REQUEST_FavoritesManagerActivity);
	}

	private void setSelectedPhoneNumber(String safeText) {
		mSelectedPhoneNumber = safeText;
	}

	private String getSelectedPhoneNumber() {
		return mSelectedPhoneNumber;
	}

	public void closeActivity(View v) {
		this.finish();
	}

	public void getImagesIdentifiers(View v) {
		// listFiles();
		// final AssetManager mgr = getAssets();
		// displayFiles(mgr, "", 0);
		listDir();
		Intent intent = new Intent(this, ImageListActivity.class);
		intent.putExtra(ContactItem.Desc, mContact);
		this.startActivityForResult(intent, Commons.REQUEST_ImageListActivity);
	}

	private void listDir() {
		Resources res = getResources();

		String uriStr = "android.resource://" + getPackageName() + "/res/"
				+ "raw";
		Log.v(uriStr);
		Uri uri = Uri.parse(uriStr);
		File rawFile = new File(uri.toString());
		String s[] = rawFile.list();

		// ContentResolver cr = getContentResolver();
		// InputStream is = cr.openInputStream(imageUri);

		// displayFiles(res.getAssets(), "cars", 0);

	}

	private void listFiles() {

		/*
		 * String pathstring = "/res/drawable"; File folder; folder = new
		 * File(pathstring, "/"); File[] images = folder.listFiles();
		 * 
		 * Log.v("  -----"+images.length);
		 */

		final R.drawable drawableResources = new R.drawable();
		final Class<R.drawable> c = R.drawable.class;
		final Field[] fields = c.getDeclaredFields();

		for (int i = 0, max = fields.length; i < max; i++) {
			final int resourceId;
			try {
				resourceId = fields[i].getInt(drawableResources);
				Log.v("  getImagesIdentifiers [" + resourceId + "]["
						+ fields[i].getName() + "][]");
			} catch (Exception e) {
				continue;
			}
			/* make use of resourceId for accessing Drawables here */
		}

	}

	void displayFiles(AssetManager mgr, String path, int level) {

		Log.v("enter displayFiles(" + path + ")");
		try {
			String list[] = mgr.list(path);
			if (list.length > 0) {
				Log.v("L" + level + ": list:" + Arrays.asList(list));

				if (list != null)
					for (int i = 0; i < list.length; ++i) {
						if (level >= 1) {
							displayFiles(mgr, path + "/" + list[i], level + 1);
						} else {
							displayFiles(mgr, list[i], level + 1);
						}
					}
			}
		} catch (IOException e) {
			Log.v("List error: can't list" + path);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {

			int imageId = data.getExtras().getInt(DrawableItem.ImageItemDescID);
			String imagePath = data.getExtras().getString(
					DrawableItem.ImageItemDescPath);
			int itemMode = data.getExtras().getInt(
					DrawableItem.ImageItemDescMode);
			int itemPathMode = data.getExtras().getInt(
					DrawableItem.ImageItemDescPathMode);

			SerializableBitmap sBitmap = (SerializableBitmap) data
					.getSerializableExtra(DrawableItem.ImageItemDescSerial);
			ImageView imagev = (ImageView) findViewById(R.id.contactImage);

			// DrawableItem item = (DrawableItem)
			// data.getSerializableExtra(DrawableItem.ImageItemDesc);
			DrawableItemInfo item = (DrawableItemInfo) data
					.getSerializableExtra(DrawableItem.ImageItemDesc);
			
			ContactItem contact = (ContactItem) data
					.getSerializableExtra(ContactItem.Desc);

			saveFavoriteImageInfo(item);

			if (imageId > 0) {
				if (imagev != null) {
					Drawable image = getResources().getDrawable(imageId);
					imagev.setImageDrawable(image);
					// Toast.makeText(this,
					// "Image: ["+imageId+"]["+image.getBounds()+"]",
					// Toast.LENGTH_SHORT).show();
					Log.v("Image: [" + imagePath + "][" + image.getBounds()
							+ "]");
				}
			} else {
				if (sBitmap != null && sBitmap.getBitmap() != null) {
					if (imagev != null) {
						Drawable image = new BitmapDrawable(getResources(),
								sBitmap.getBitmap());
						imagev.setImageDrawable(image);
					}
				} else {

					if (itemMode == DrawableItem.ITEM_MODE_PATH
							&& itemPathMode == DrawableItem.ITEM_MODE_PATH_ASSET) {
						Drawable image;
						try {
							image = Drawable.createFromStream(
									getAssets().open(imagePath), null);
							imagev.setImageDrawable(image);
							imagev.setImageBitmap(Utils
									.getNormalBitmapFromAssetsPath(this,
											imagePath));
							// Toast.makeText(this,
							// "Image: ["+imagePath+"]["+image.getBounds()+"]",
							// Toast.LENGTH_SHORT).show();
							Log.v("Image: [" + imagePath + "]["
									+ image.getBounds() + "]");
						} catch (IOException e) {
							Log.e(e);
						}
					}

					if (itemMode == DrawableItem.ITEM_MODE_NRES
							&& itemPathMode == DrawableItem.ITEM_MODE_PATH_NTRES) {
						Drawable ic_carsDrawable = null;
						try {
							ic_carsDrawable = ThemeManager
									.getDrawableResourcesForTheme(this,
											imagePath);
							imagev.setImageDrawable(ic_carsDrawable);
							// Toast.makeText(this,
							// "Image: ["+imagePath+"]["+ic_carsDrawable.getBounds()+"]",
							// Toast.LENGTH_SHORT).show();
							Log.v("Image: [" + imagePath + "]["
									+ ic_carsDrawable.getBounds() + "]");
						} catch (NameNotFoundException e) {
							Log.e(e);
						}
					}

				}
			}

		}
	}

	private void saveFavoriteImageInfo(DrawableItemInfo item) {
		Log.v("[ContactDetailsActivity][saveFavoriteImageInfo]" + item);

		String cimp = mContact.getContactImagePath();
		String iimp = item.getImagePath();
		Log.v("saveFavoriteImageInfo compare cimp["+cimp+"] iimp["+iimp+"]");
		if(cimp==null || !cimp.equalsIgnoreCase(iimp)) {
			mContact.setContactImageInfo(item);

			FavoriteDataProvider mFavoriteDataProvider = new FavoriteDataProvider(
				this);
			mFavoriteDataProvider.updatedFavorites(mContact.getContentValues());
			Configs.saveResultForMain(this, true);
		}

	}

	/**
	 * Make Call to selected contacts phone number
	 * 
	 * @param view
	 *            from call
	 */
	public void makeCall(View view) {

		String number = "tel:" + getSelectedPhoneNumber().trim();
		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
		// callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		EndCallListener callListener = new EndCallListener();
		TelephonyManager mTM = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);

		startActivity(callIntent);

	}

	/**
	 * Make SMS to selected contacts phone number
	 * 
	 * @param view
	 *            from call
	 */
	public void makeSMS(View view) {

		String number = "tel:" + getSelectedPhoneNumber().trim();

		Intent smsIntent = new Intent(this, SMSEditActivity.class);
		smsIntent.putExtra("phone_number", getSelectedPhoneNumber().trim());
		smsIntent.putExtra("contact_id", mContact.getContactId());
		smsIntent.putExtra(ContactItem.Desc, mContact);
		this.startActivity(smsIntent);

		/*
		 * TypedArray appearance =
		 * getTheme().obtainStyledAttributes(R.style.AppTheme, new int[]
		 * {android.R.attr.windowBackground}); int attributeResourceId =
		 * appearance.getResourceId(0, 0); Drawable drawable =
		 * getResources().getDrawable(attributeResourceId);
		 * appearance.recycle(); Intent smsIntent = new
		 * Intent(android.provider.Settings.ACTION_SETTINGS);
		 * startActivity(smsIntent);
		 */

	}

	/**
	 * Set text to TextView
	 * 
	 * @param resId
	 *            id
	 * @param value
	 *            text
	 */
	private void setSafeText(int resId, String value) {
		TextView textView = (TextView) findViewById(resId);
		if (textView != null) {
			textView.setText(value);
		}
	}

	/**
	 * @param view
	 *            current view
	 * @return text при повороте экрана view = null
	 */
	private String getSafeText(View view) {
		String ret = "";
		if (view != null)
			ret = ((TextView) view).getText().toString();
		return ret;
	}

	private class EndCallListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (TelephonyManager.CALL_STATE_RINGING == state) {
				Log.v("RINGING, number: " + incomingNumber);
			}
			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				// wait for phone to go offhook (probably set a boolean flag) so
				// you know your app initiated the call.
				Log.v("OFFHOOK");
			}
			if (TelephonyManager.CALL_STATE_IDLE == state) {
				// when this state occurs, and your flag is set, restart your
				// app
				Log.v("IDLE");
				finish();
			}
		}
	}

}

/*
 * 
 * http://www.mkyong.com/android/how-to-make-a-phone-call-in-android/
 * http://stackoverflow
 * .com/questions/13081767/how-to-return-back-to-my-application
 * -after-calling-external-intent-calendar-ap
 * http://android.bigresource.com/Android
 * -return-to-original-activity-after-call-ends--grFJjkZRt.html
 */
