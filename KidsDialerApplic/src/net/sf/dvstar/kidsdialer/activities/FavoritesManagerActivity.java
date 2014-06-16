package net.sf.dvstar.kidsdialer.activities;

import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.adapters.ListCursorAdapter;
import net.sf.dvstar.kidsdialer.data.ContactItem;
import net.sf.dvstar.kidsdialer.data.FavoriteDataProvider;
import net.sf.dvstar.kidsdialer.data.TableDefs;
import net.sf.dvstar.kidsdialer.utils.Commons;
import net.sf.dvstar.kidsdialer.utils.Configs;
import net.sf.dvstar.kidsdialer.utils.Log;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

public class FavoritesManagerActivity extends Activity {

	private CursorAdapter mContactListAdapter;
	private SimpleCursorAdapter mFavoritesListAdapter;
	private CheckBox mShowInvisibleControl;

	private ListView mContactList;
	private ListView mFavoritetList;
	private Cursor contactsCursor;

	private FavoriteDataProvider mFavoriteDataProvider;
	private boolean dataChanged = false;
	private Context mContext;
	private EditText mFilterText;
	
	
	private TextWatcher filterTextWatcher = new TextWatcher() {

	    public void afterTextChanged(Editable s) {
	    }

	    public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	    }

	    public void onTextChanged(CharSequence s, int start, int before,
	            int count) {
	    	mContactListAdapter.getFilter().filter(s);
	    }

	};
	
	public FavoritesManagerActivity(){
		mContext = getBaseContext();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v("Activity State: onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_manager_favorites);
		mContactList = (ListView) findViewById(R.id.listview_all_contacts);
		mFavoritetList = (ListView) findViewById(R.id.listview_favorites);
		mContext = getBaseContext();
		
		mFilterText = (EditText) findViewById(R.id.contact_phone_filter_text);
		mFilterText.addTextChangedListener(filterTextWatcher);
		
		populateContactList();
	}

	public void prepareResult() {
		//Intent intent = getIntent();
		//PendingIntent pi = intent.getParcelableExtra(Commons.PARAM_PINTENT);
		
		PendingIntent pendingIntent;
        Intent result = new Intent();
        
        //result.setClass(mContext, KidsDialerMainActivity.class);
        pendingIntent =  PendingIntent.getActivity(mContext, 0, result, 0);		
		
        Configs.saveResultForMain(this, dataChanged);
		
		if (dataChanged) {
			
			
			Intent returnIntent = new Intent();
			returnIntent.putExtra(Commons.PARAM_DATACHANGED, dataChanged);
			setResult(RESULT_OK, returnIntent);

		
			result.putExtra(Commons.PARAM_DATACHANGED, dataChanged);
			try {
				pendingIntent.send(FavoritesManagerActivity.this, Commons.STATUS_FINISH,
						result);
			} catch (CanceledException e) {
				Log.e(e.getMessage());
				e.printStackTrace();
			}

		}
	}

//	private void saveResultForMain(boolean dataChanged) {
//		Context context = getApplicationContext();
//		SharedPreferences prefs = 
//		        PreferenceManager.getDefaultSharedPreferences(context); 
//		Editor editor = prefs.edit();
//		
//		editor.putBoolean(Commons.PARAM_DATACHANGED, dataChanged);
//		
//		editor.commit();		
//		
//	}

	@Override
	public void onStop() {
		Log.v("Activity State: onStop() [" + dataChanged + "]" + this);
		prepareResult();
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		Log.v("Activity State: onBackPressed() [" + dataChanged + "]" + this);
		prepareResult();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    mFilterText.removeTextChangedListener(filterTextWatcher);
	}	
	
	public void closeAction(View v) {
		finish();
	}

	private void populateContactList() {
		// Build adapter with contact entries
		/*
		 * while (cursor.moveToNext()) {
		 * 
		 * String contactId =
		 * cursor.getString(cursor.getColumnIndex(ContactsContract
		 * .Contacts._ID)); String contactName =
		 * cursor.getString(cursor.getColumnIndex
		 * (ContactsContract.Contacts.DISPLAY_NAME)); //String contactPhone =
		 * cursor
		 * .getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds
		 * .Phone.NUMBER)); Log.d(TAG,contactId + " "+ contactName+ " "); String
		 * hasPhone =
		 * cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts
		 * .HAS_PHONE_NUMBER)); if (Integer.parseInt(hasPhone) > 0 ) { // You
		 * know it has a number so now query it like this Cursor phones =
		 * getContentResolver().query(
		 * ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
		 * ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
		 * null, null); while (phones.moveToNext()) { String phoneNumber =
		 * phones.getString(phones.getColumnIndex(
		 * ContactsContract.CommonDataKinds.Phone.NUMBER ) ) ;
		 * Log.d(TAG,"   "+phoneNumber); } phones.close(); } }
		 */

		String[] fields = new String[] { ContactsContract.Data.DISPLAY_NAME,
				Phone.NUMBER };

		String[] favoritesFields = new String[] { TableDefs.CONTACT_NAME,
				TableDefs.CONTACT_PHONES };

		contactsCursor = getContacts(null);
		
		mContactListAdapter = new ListCursorAdapter // SimpleCursorAdapter
		(this, R.layout.contact_entry_v, contactsCursor, fields, new int[] {
				R.id.contactEntryText, R.id.contactPhoneText });

		FilterQueryProvider filterQueryProvider = new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                // Search for states whose names begin with the specified letters.
                Cursor cursor = getContacts(
                        	(constraint != null ? constraint.toString() : null)
                        );
                return cursor;
            }
        };
		
		mContactListAdapter.setFilterQueryProvider( filterQueryProvider );
		
		mContactList.setOnItemClickListener(new ContactOnItemClickListener());
		mContactList.setAdapter(mContactListAdapter);

		mFavoriteDataProvider = new FavoriteDataProvider(this);

		mFavoritesListAdapter = new SimpleCursorAdapter(this,
				R.layout.contact_entry_v,
				mFavoriteDataProvider.getFavoriteCursor(), favoritesFields,
				new int[] { R.id.contactEntryText, R.id.contactPhoneText },
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		mFavoritetList
				.setOnItemClickListener(new FavoritesOnItemClickListener());
		mFavoritetList.setAdapter(mFavoritesListAdapter);
	}

	/**
	 * Obtains the contact list for the currently selected account.
	 * 
	 * @return A cursor for for accessing the contact list.
	 */
	private Cursor getContacts(String constraint) {
		// Run query
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { 
				ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER, 
				Phone.NUMBER,Phone.TYPE, Phone.LABEL };

		String selection = null;

		/*
		 * selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
		 * (mShowInvisible ? "0" : "1") + "' AND "+
		 * ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";
		 */
		selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";
		if (constraint!=null && constraint.length()>0) {
			selection += " AND lower("+ContactsContract.Contacts.DISPLAY_NAME + ") like '%" + constraint +"%' COLLATE NOCASE";
		}

		String[] selectionArgs = null;
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		Cursor result = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, selection,
				selectionArgs, sortOrder);

		return result;
	}

	class ContactOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.v("ContactOnItemClickListener :itemClick: position = "
					+ position + ", id = " + id);

			Cursor c = ((CursorAdapter) parent.getAdapter()).getCursor();
			c.moveToPosition(position);
			addToFavorites(c);
		}
	}

	class FavoritesOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.v("FavoritesOnItemClickListener :itemClick: position = "
					+ position + ", id = " + id);
			Adapter adapter = parent.getAdapter();
			Cursor c = ((SimpleCursorAdapter) adapter).getCursor();
			c.moveToPosition(position);
			delFrFavorites(c);
		}
	}

	public void addToFavorites(Cursor c) {
		ContentValues cv = new ContentValues();
		String value;
		if (c != null) {
			
			ContactItem contactItem = new ContactItem(
					c.getString(c.getColumnIndex(ContactsContract.Data._ID)),
					c.getString(c.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)),
					getContactPhones(this, c)
					);
			/*
			value = c.getString(c.getColumnIndex(ContactsContract.Data._ID));
			cv.put(TableDefs.CONTACT_ID, value);
			value = c.getString(c
					.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
			cv.put(TableDefs.CONTACT_NAME, value);
			value = getContactPhones(this, c);
			// value = c.getString(
			// c.getColumnIndex(ContactsContract.Data.DISPLAY_NAME ) );
			cv.put(TableDefs.CONTACT_PHONES, value);
			*/
			
			mFavoriteDataProvider.addFavorites(contactItem.getContentValues());
			
			mFavoritesListAdapter.swapCursor(mFavoriteDataProvider
					.getFavoriteCursor());
			mFavoritesListAdapter.notifyDataSetChanged();
			setDataChanged(true);
		}

	}

	private void setDataChanged(boolean b) {
		this.dataChanged = b;
	}

	private String getContactPhones(Context context, Cursor cursor) {
		String result = "";
		String hasPhone = cursor.getString(cursor
				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
		if (Integer.parseInt(hasPhone) > 0) {
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor phones = context.getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			while (phones.moveToNext()) {
				String phoneNumber = phones
						.getString(phones
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				result += phoneNumber + (phones.isLast() ? "" : ", ");
			}
			phones.close();
			Log.v("   " + result);
		} else {
			result = "-";
		}

		return result;
	}

	public void delFrFavorites(Cursor cursor) {
		final String itemId = cursor.getString(cursor.getColumnIndex("id"));
		mFavoriteDataProvider.delFavorites(itemId);
		// favoriteDataProvider.delAllFavorites();

		mFavoritesListAdapter.swapCursor(mFavoriteDataProvider
				.getFavoriteCursor());
		mFavoritesListAdapter.notifyDataSetChanged();
		setDataChanged(true);
	}

}
