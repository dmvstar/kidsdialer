package net.sf.dvstar.kidsdialer.activities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.data.ContactItem;
import net.sf.dvstar.kidsdialer.data.FavoriteDataProvider;
import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.OIFileManager;
import net.sf.dvstar.kidsdialer.utils.Utils;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public final class ContactAdderActivity extends Activity implements
		OnAccountsUpdateListener, OIFileManager {

	public static final String ACCOUNT_NAME = "net.sf.dvstar.kidsdialer.ContactsAdder.ACCOUNT_NAME";
	public static final String ACCOUNT_TYPE = "net.sf.dvstar.kidsdialer.ContactsAdder.ACCOUNT_TYPE";

	private ArrayList<AccountData> mAccounts;
	private AccountAdapter mAccountAdapter;
	private Spinner mAccountSpinner;
	private EditText mContactEmailEditText;
	private ArrayList<Integer> mContactEmailTypes;
	private Spinner mContactEmailTypeSpinner;
	private EditText mContactNameEditText;
	private EditText mContactPhoneEditText;
	private ArrayList<Integer> mContactPhoneTypes;
	private Spinner mContactPhoneTypeSpinner;
	private Button mContactSaveButton;
	private Button mSelectRingtoneButton;
	private Button mRingtonePlayButton;

	private AccountData mSelectedAccount;
	private ContactItem mContact;
	private EditText mSelectRingtoneEditText;

	private Spinner mContactPhonesSpinner;
	private ImageView mContactPhonesImageView_1;
	private ImageView mContactPhonesImageView_2;
	private ImageView mContactPhonesImageView_3;

	private MediaPlayer mMediaPlayer = new MediaPlayer();
	private Activity mContext;

	/**
	 * Called when the activity is first created. Responsible for initializing
	 * the UI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v("Activity State: onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_adder);
		this.mContext = this;

		/*
		 * intent.putExtra(PARAM_CONTACT_EDIT_MODE, CONTACT_EDIT_MODE_EDIT);
		 * intent.putExtra(PARAM_CONTACT_EDIT_ID, contact.getContactId());
		 * intent.putExtra(PARAM_CONTACT, contact);
		 */

		// Obtain handles to UI objects
		mAccountSpinner = (Spinner) findViewById(R.id.accountSpinner);
		mContactNameEditText = (EditText) findViewById(R.id.contactNameEditText);
		mContactPhoneEditText = (EditText) findViewById(R.id.contactPhoneEditText);
		mContactEmailEditText = (EditText) findViewById(R.id.contactEmailEditText);
		mContactPhoneTypeSpinner = (Spinner) findViewById(R.id.contactPhoneTypeSpinner);
		mContactEmailTypeSpinner = (Spinner) findViewById(R.id.contactEmailTypeSpinner);
		mContactSaveButton = (Button) findViewById(R.id.contactSaveButton);
		mSelectRingtoneButton = (Button) findViewById(R.id.contact_button_ringtone);
		mSelectRingtoneEditText = (EditText) findViewById(R.id.contact_ringtone_path);

		mRingtonePlayButton = (Button) findViewById(R.id.contact_button_play);

		mContactPhonesSpinner = (Spinner) findViewById(R.id.contactPhonesListSpinner);

		mContactPhonesImageView_1 = (ImageView) findViewById(R.id.contactImageView);
		mContactPhonesImageView_2 = (ImageView) findViewById(R.id.contactImageViewAdd);
		mContactPhonesImageView_3 = (ImageView) findViewById(R.id.contactImageViewAss);

		mContactPhonesImageView_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.showCustomToastInfo(mContext, "1 Image : ["
						+ ((ImageView) v).getDrawable().getBounds() + "]",
						R.drawable.help_about);
			};
		});
		mContactPhonesImageView_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Utils.showCustomToastInfo(mContext, "2 Image : ["
						+ ((ImageView) v).getDrawable().getBounds() + "]",
						R.drawable.help_about);

			};
		});
		mContactPhonesImageView_3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Utils.showCustomToastInfo(mContext, "3 Image : ["
						+ ((ImageView) v).getDrawable().getBounds() + "]",
						R.drawable.help_about);

			};
		});

		Drawable imageDraw_2 = getResources()
				.getDrawable(R.drawable.cars_172_3);
		mContactPhonesImageView_2.setImageDrawable(imageDraw_2);

		Drawable imageDraw_3;
		try {
			imageDraw_3 = Drawable.createFromStream(
					getAssets().open("cars/cars_43.png"), null);
			mContactPhonesImageView_3.setImageDrawable(imageDraw_3);

			mContactPhonesImageView_3.setImageBitmap(Utils.getNormalBitmapFromAssetsPath(this, "cars/cars_43.png"));

		} catch (IOException e) {
		}

		// Prepare list of supported account types
		// Note: Other types are available in ContactsContract.CommonDataKinds
		// Also, be aware that type IDs differ between Phone and Email, and MUST
		// be computed
		// separately.
		mContactPhoneTypes = new ArrayList<Integer>();
		mContactPhoneTypes
				.add(ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
		mContactPhoneTypes
				.add(ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
		mContactPhoneTypes
				.add(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
		mContactPhoneTypes
				.add(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER);

		mContactEmailTypes = new ArrayList<Integer>();
		mContactEmailTypes
				.add(ContactsContract.CommonDataKinds.Email.TYPE_HOME);
		mContactEmailTypes
				.add(ContactsContract.CommonDataKinds.Email.TYPE_WORK);
		mContactEmailTypes
				.add(ContactsContract.CommonDataKinds.Email.TYPE_MOBILE);
		mContactEmailTypes
				.add(ContactsContract.CommonDataKinds.Email.TYPE_OTHER);

		// Prepare model for account spinner
		mAccounts = new ArrayList<AccountData>();
		mAccountAdapter = new AccountAdapter(this, mAccounts);
		mAccountSpinner.setAdapter(mAccountAdapter);

		// Populate list of account types for phone
		ArrayAdapter<String> adapter;
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Iterator<Integer> iter;
		iter = mContactPhoneTypes.iterator();
		while (iter.hasNext()) {
			adapter.add(ContactsContract.CommonDataKinds.Phone.getTypeLabel(
					this.getResources(), iter.next(),
					getString(R.string.undefinedTypeLabel)).toString());
		}
		mContactPhoneTypeSpinner.setAdapter(adapter);
		mContactPhoneTypeSpinner.setPrompt(getString(R.string.selectLabel));

		// Populate list of account types for email
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		iter = mContactEmailTypes.iterator();
		while (iter.hasNext()) {
			adapter.add(ContactsContract.CommonDataKinds.Email.getTypeLabel(
					this.getResources(), iter.next(),
					getString(R.string.undefinedTypeLabel)).toString());
		}
		mContactEmailTypeSpinner.setAdapter(adapter);
		mContactEmailTypeSpinner.setPrompt(getString(R.string.selectLabel));

		// Prepare the system account manager. On registering the listener
		// below, we also ask for
		// an initial callback to pre-populate the account list.
		AccountManager.get(this).addOnAccountsUpdatedListener(this, null, true);

		// Register handlers for UI elements
		mAccountSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long i) {
				updateAccountSelection();
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// We don't need to worry about nothing being selected, since
				// Spinners don't allow
				// this.
			}
		});

		mContactSaveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onSaveButtonClicked();
			}
		});

		mSelectRingtoneButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onSelectRingtoneClicked();
			}
		});

		mRingtonePlayButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mSelectRingtoneEditText.getText() != null)
					onPlayRingtoneClicked(mSelectRingtoneEditText.getText()
							.toString());
			}

		});

		Intent intent = getIntent();
		mContact = (ContactItem) intent.getSerializableExtra(ContactItem.Desc);

		fillFormValues();

	}

	@Override
	public void onResume() {
		Log.v("Activity State: onResume()");
		super.onResume();
	}

	/**
	 * Fill form values
	 */
	private void fillFormValues() {

		mContactNameEditText.setText(mContact.getContactName());

		String[] PROJECTION = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER,
				ContactsContract.Data.CUSTOM_RINGTONE };

		Uri contactData = ContactsContract.Contacts.CONTENT_URI;
		String contactId = contactData.getLastPathSegment();

		Cursor localCursor = managedQuery(contactData, PROJECTION, "_ID="
				+ mContact.getContactId(), null, null);

		Log.v("Query to contactData");
		if (localCursor.moveToNext()) {
			String Id = localCursor
					.getString(localCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
			String Name = localCursor
					.getString(localCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String Ring = localCursor
					.getString(localCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CUSTOM_RINGTONE));

			String rpath = "";
			if (Ring != null) {
				Log.v("Id=" + Id + " Name=" + Name + " Ring=" + Ring);
				android.net.Uri u = Uri.parse(Ring);
				rpath = Utils.convertMediaUriToPath(this, u);
				mSelectRingtoneEditText.setText(rpath);
				Log.v("Id=" + Id + " Name=" + Name + " Ring=" + Ring + " Path="
						+ rpath);

			} else {
				mSelectRingtoneEditText.setText("");
				Log.v("Id=" + Id + " Name=" + Name);
			}

			mContact.setContactRingtone(rpath);

		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item,
				R.layout.spinner_item_text, mContact.getContactPhonesList());
		adapter.setDropDownViewResource(
		// android.R.layout.simple_spinner_dropdown_item
		R.layout.spinner_dropdown_item);

		// mContactPhonesSpinner.setPrompt(getResources().getString(R.string.promptPhones));

		if (mContactPhonesSpinner != null
				&& mContact.getContactPhonesList() != null
				&& mContact.getContactPhonesList().size() > 0) {
			mContactPhonesSpinner.setAdapter(adapter);
		}

		// localCursor.close(); !!!!!!!!!
	}

	private void saveRingtoneToContact() {

		if (mContact.getContactRingtone() == null)
			return;

		FavoriteDataProvider dataProvider = new FavoriteDataProvider(this);

		Log.v("[ContactAdderActivity] saveRingtoneToContact (" + mContact
				+ ")(" + mContact.getContactRingtone() + ")");

		dataProvider.updatedFavorites(mContact.getContentValues());

		String[] PROJECTION = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER,
				ContactsContract.Data.CUSTOM_RINGTONE };

		Uri contactData = ContactsContract.Contacts.CONTENT_URI;
		String contactId = contactData.getLastPathSegment();

		Cursor localCursor = managedQuery(contactData, PROJECTION, "_ID="
				+ mContact.getContactId(), null, null);

		// localCursor.move(120); // CONTACT ID NUMBER

		// String str1 = localCursor.getString(localCursor
		// .getColumnIndexOrThrow("_id"));
		// String str2 = localCursor.getString(localCursor
		// .getColumnIndexOrThrow("display_name"));
		Uri localUri = Uri.withAppendedPath(
				ContactsContract.Contacts.CONTENT_URI, mContact.getContactId());

		ContentValues localContentValues = new ContentValues();

		localContentValues.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);
		localContentValues.put(ContactsContract.Data.CUSTOM_RINGTONE,
				mContact.getContactRingtone());

		getContentResolver().update(localUri, localContentValues, null, null);
		// ????
	}

	/**
	 * Actions for when the Save button is clicked. Creates a contact entry and
	 * terminates the activity.
	 */
	private void onSaveButtonClicked() {
		Log.v("Save button clicked");
		createContactEntry();

		saveRingtoneToContact();

		finish();
	}

	private void onPlayRingtoneClicked(String filePath) {
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			mMediaPlayer.release();

			mRingtonePlayButton.setText(getResources().getString(
					R.string.button_play_ringtone));
			mMediaPlayer = new MediaPlayer();

		} else {
			try {
				mMediaPlayer.setDataSource(filePath);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
				mRingtonePlayButton.setText(getResources().getString(
						R.string.button_stop_ringtone));
			} catch (Exception e) {
				Toast toast = new Toast(this);
				Utils.showCustomToast(getLayoutInflater(), toast,
						e.getLocalizedMessage());
			}
		}
	}

	private void onStopRingtoneClicked(String filePath) {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
		}
	}

	private void onSelectRingtoneClicked() {
		Log.v("Select ringtone button clicked");

		Intent intent = new Intent(ACTION_PICK_FILE);
		intent.putExtra(EXTRA_FILTER_FILETYPE, ".mp3");
		try {
			startActivityForResult(intent, REQUEST_CODE_PICK_FILE_OR_DIRECTORY);
		} catch (ActivityNotFoundException e) {
			// No compatible file manager was found.
			Toast toast = new Toast(this);
			Utils.showCustomToast(getLayoutInflater(), toast, getResources()
					.getString(R.string.no_filemanager_installed));
		}
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
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
					Toast toast = new Toast(this);
					Utils.showCustomToast(getLayoutInflater(), toast,
							"selected " + filePath);
					mSelectRingtoneEditText.setText(filePath);
					Log.v("[ContactAdderActivity] onActivityResult " + filePath);

					Uri uuu = Utils
							.getImageContentUri(this, new File(filePath));

					Log.v("[ContactAdderActivity] onActivityResult "
							+ uuu.getPath());
					Log.v("[ContactAdderActivity] onActivityResult "
							+ uuu.getEncodedPath());
					Log.v("[ContactAdderActivity] onActivityResult "
							+ uuu.getAuthority());
					Log.v("[ContactAdderActivity] onActivityResult " + uuu);

					mContact.setContactRingtone(uuu.toString());
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Creates a contact entry from the current UI values in the account named
	 * by mSelectedAccount.
	 */
	protected void createContactEntry() {
		// Get values from UI
		String name = mContactNameEditText.getText().toString();
		String phone = mContactPhoneEditText.getText().toString();
		String email = mContactEmailEditText.getText().toString();
		int phoneType = mContactPhoneTypes.get(mContactPhoneTypeSpinner
				.getSelectedItemPosition());
		int emailType = mContactEmailTypes.get(mContactEmailTypeSpinner
				.getSelectedItemPosition());
		;

		// Prepare contact creation request
		//
		// Note: We use RawContacts because this data must be associated with a
		// particular account.
		// The system will aggregate this with any other data for this contact
		// and create a
		// coresponding entry in the ContactsContract.Contacts provider for us.
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,
						mSelectedAccount.getType())
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME,
						mSelectedAccount.getName()).build());
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				.withValue(
						ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
						name).build());
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
				.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
						phoneType).build());
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
				.withValue(ContactsContract.CommonDataKinds.Email.TYPE,
						emailType).build());

		// Ask the Contact provider to create a new contact
		Log.i("Selected account: " + mSelectedAccount.getName() + " ("
				+ mSelectedAccount.getType() + ")");
		Log.i("Creating contact: " + name);
		try {
			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (Exception e) {
			// Display warning
			Context ctx = getApplicationContext();
			CharSequence txt = getString(R.string.contactCreationFailure);
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(ctx, txt, duration);
			toast.show();

			// Log exception
			Log.e("Exceptoin encoutered while inserting contact: " + e);
		}
	}

	/**
	 * Called when this activity is about to be destroyed by the system.
	 */
	@Override
	public void onDestroy() {
		// Remove AccountManager callback
		AccountManager.get(this).removeOnAccountsUpdatedListener(this);
		super.onDestroy();
	}

	/**
	 * Updates account list spinner when the list of Accounts on the system
	 * changes. Satisfies OnAccountsUpdateListener implementation.
	 */
	public void onAccountsUpdated(Account[] a) {
		Log.i("Account list update detected");
		// Clear out any old data to prevent duplicates
		mAccounts.clear();

		// Get account data from system
		AuthenticatorDescription[] accountTypes = AccountManager.get(this)
				.getAuthenticatorTypes();

		// Populate tables
		for (int i = 0; i < a.length; i++) {
			// The user may have multiple accounts with the same name, so we
			// need to construct a
			// meaningful display name for each.
			String systemAccountType = a[i].type;
			AuthenticatorDescription ad = getAuthenticatorDescription(
					systemAccountType, accountTypes);
			AccountData data = new AccountData(a[i].name, ad);
			mAccounts.add(data);
		}

		// Update the account spinner
		mAccountAdapter.notifyDataSetChanged();
	}

	/**
	 * Obtain the AuthenticatorDescription for a given account type.
	 * 
	 * @param type
	 *            The account type to locate.
	 * @param dictionary
	 *            An array of AuthenticatorDescriptions, as returned by
	 *            AccountManager.
	 * @return The description for the specified account type.
	 */
	private static AuthenticatorDescription getAuthenticatorDescription(
			String type, AuthenticatorDescription[] dictionary) {
		for (int i = 0; i < dictionary.length; i++) {
			if (dictionary[i].type.equals(type)) {
				return dictionary[i];
			}
		}
		// No match found
		throw new RuntimeException("Unable to find matching authenticator");
	}

	/**
	 * Update account selection. If NO_ACCOUNT is selected, then we prohibit
	 * inserting new contacts.
	 */
	private void updateAccountSelection() {
		// Read current account selection
		mSelectedAccount = (AccountData) mAccountSpinner.getSelectedItem();
	}

	/**
	 * A container class used to repreresent all known information about an
	 * account.
	 */
	private class AccountData {
		private String mName;
		private String mType;
		private CharSequence mTypeLabel;
		private Drawable mIcon;

		/**
		 * @param name
		 *            The name of the account. This is usually the user's email
		 *            address or username.
		 * @param description
		 *            The description for this account. This will be dictated by
		 *            the type of account returned, and can be obtained from the
		 *            system AccountManager.
		 */
		public AccountData(String name, AuthenticatorDescription description) {
			mName = name;
			if (description != null) {
				mType = description.type;

				// The type string is stored in a resource, so we need to
				// convert it into something
				// human readable.
				String packageName = description.packageName;
				PackageManager pm = getPackageManager();

				if (description.labelId != 0) {
					mTypeLabel = pm.getText(packageName, description.labelId,
							null);
					if (mTypeLabel == null) {
						throw new IllegalArgumentException(
								"LabelID provided, but label not found");
					}
				} else {
					mTypeLabel = "";
				}

				if (description.iconId != 0) {
					mIcon = pm.getDrawable(packageName, description.iconId,
							null);
					if (mIcon == null) {
						throw new IllegalArgumentException(
								"IconID provided, but drawable not " + "found");
					}
				} else {
					mIcon = getResources().getDrawable(
							android.R.drawable.sym_def_app_icon);
				}
			}
		}

		public String getName() {
			return mName;
		}

		public String getType() {
			return mType;
		}

		public CharSequence getTypeLabel() {
			return mTypeLabel;
		}

		public Drawable getIcon() {
			return mIcon;
		}

		public String toString() {
			return mName;
		}
	}

	/**
	 * Custom adapter used to display account icons and descriptions in the
	 * account spinner.
	 */
	private class AccountAdapter extends ArrayAdapter<AccountData> {
		public AccountAdapter(Context context,
				ArrayList<AccountData> accountData) {
			super(context, android.R.layout.simple_spinner_item, accountData);
			setDropDownViewResource(R.layout.account_entry);
		}

		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// Inflate a view template
			if (convertView == null) {
				LayoutInflater layoutInflater = getLayoutInflater();
				convertView = layoutInflater.inflate(R.layout.account_entry,
						parent, false);
			}
			TextView firstAccountLine = (TextView) convertView
					.findViewById(R.id.firstAccountLine);
			TextView secondAccountLine = (TextView) convertView
					.findViewById(R.id.secondAccountLine);
			ImageView accountIcon = (ImageView) convertView
					.findViewById(R.id.accountIcon);

			// Populate template
			AccountData data = getItem(position);
			firstAccountLine.setText(data.getName());
			secondAccountLine.setText(data.getTypeLabel());
			Drawable icon = data.getIcon();
			if (icon == null) {
				icon = getResources().getDrawable(
						android.R.drawable.ic_menu_search);
			}
			accountIcon.setImageDrawable(icon);
			return convertView;
		}
	}

}
