package net.sf.dvstar.kidsdialer.adapters;

import java.util.HashMap;
import java.util.List;

import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.activities.ContactDetailsActivity;
import net.sf.dvstar.kidsdialer.data.ContactItem;
import net.sf.dvstar.kidsdialer.data.ContactPrimaryCursorData;
import net.sf.dvstar.kidsdialer.utils.Commons;
import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.Utils;
import net.sf.dvstar.kidsdialer.utils.theme.ThemeManager;
import net.sf.dvstar.kidsdialer.utils.theme.ThemedResources;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactPrimaryCursorAdapter extends BaseAdapter // CursorAdapter
		implements Commons
// Filterable
{
	/**
	 * A list of columns containing the data to bind to the UI. This field
	 * should be made private, so it is hidden from the SDK. {@hide}
	 */
	protected int[] mFrom;
	/**
	 * A list of View ids representing the views to which the data must be
	 * bound. This field should be made private, so it is hidden from the SDK.
	 * {@hide}
	 */
	protected int[] mToFieldIds;
	String[] mFromFieldNames;

	private Activity mActivity;
	private LayoutInflater mLayoutInflater;

	private int mLayout;
	private int[] mContLayouts;
	private String[] mContNames;
	private Cursor mCursor;
	private HashMap<String, ContactItem> mContactItems;
	private List<ContactItem> mContactItemList;
	private boolean mDataValid;
	private LayoutInflater localInflater;
	private ThemedResources mThemedResource;

	/**
	 * ContactPrimaryCursorAdapter adapter for two items Contacts list
	 * 
	 * @param context
	 *            Activity context
	 * @param layout
	 *            Layout for binding values
	 * @param curData
	 *            .getContactCursor()
	 * @param contNames
	 * @param contLayots
	 * @param fromFieldNames
	 * @param toFieldIds
	 */
	public ContactPrimaryCursorAdapter(Activity activity, int layout,
			ContactPrimaryCursorData curData, String[] contNames,
			int[] contLayots, String[] fromFieldNames, int[] toFieldIds) {
		// super(context, cursor);
		this.mCursor = curData.getContactCursor();
		this.mContactItems = curData.getContactItems();
		//this.mContactItemList = curData.getContactItemList();
		this.mToFieldIds = toFieldIds;
		this.mFromFieldNames = fromFieldNames;
		this.mActivity = activity;
		this.mLayout = layout;
		this.mContLayouts = contLayots;
		this.mContNames = contNames;

		boolean cursorPresent = curData.getContactCursor() != null;
		mDataValid = cursorPresent;
		
		mThemedResource = new ThemedResources(mActivity);
		
		Log.v("ContactPrimaryCursorAdapter " + mActivity.getTheme());
//		mLayoutInflater = mActivity.getLayoutInflater();
		mLayoutInflater =   LayoutInflater.from(mActivity);
		
//		if(ThemesManagerHelper.getThemeResourceId()>0){
//			Context mtContext = new ContextThemeWrapper(mActivity, ThemesManagerHelper.getThemeResourceId());
//			//localInflater = mLayoutInflater.cloneInContext(mtContext);
//			mLayoutInflater =   LayoutInflater.from( mtContext );
//		}
		
		// findColumns(cursor, fromFieldNames);
	}

/*	
	int imagesIds[] = new int[] { 
			R.drawable.cars_172_1, R.drawable.cars_172_2, 
			R.drawable.cars_172_3, R.drawable.cars_172_4, 
			R.drawable.cars_172_5}; 
*/	
	
	// @Override
	public void bindView(View view, Context context, Cursor cursor) {
		// super.bindView(v, context, c);
		final int[] to = mToFieldIds;
		// final int[] from = mFrom;

		final int count = mToFieldIds.length;

		int indexIdL = cursor.getColumnIndex(CONTACT_L + COLUMN_FIELS_SEPARATOR
				+ ContactsContract.Data._ID);
		String idL = cursor.getString(indexIdL);
		
		int indexIdR = cursor.getColumnIndex(CONTACT_R + COLUMN_FIELS_SEPARATOR
				+ ContactsContract.Data._ID);
		String idR = cursor.getString(indexIdR);
		
		Log.v(//TAG,
				"" + "ContactPrimaryCursorAdapter bindView cp[" + cursor.getPosition() + "]"
					+" l["+ indexIdL + "][" + idL + "]"
					+" r["+ indexIdR + "][" + idR + "]"
					+" l("+ mContactItems.get(idL) + ")"
					+" r(" + mContactItems.get(idR) + ")");
		// View two = mLayoutInflater.inflate(mLayout, null, false);
		View tv = view.findViewById(R.id.contact_entry_l);
		if (tv != null) {
			tv.setOnClickListener(new ContactClickListener(mActivity,
					mContactItems.get(idL)));
		
			
		}
		tv = view.findViewById(R.id.contact_entry_r);
		if (tv != null) {
			tv.setOnClickListener(new ContactClickListener(mActivity,
					mContactItems.get(idR)));
		}
		
		for (int cc = 0; cc < mContLayouts.length; cc++) {

			// Log.v(TAG, view.getContentDescription().toString()
			// + "ContactPrimaryCursorAdapter bindView-" +
			// cursor.getPosition());

			final View container = view.findViewById(mContLayouts[cc]);
			ImageView mContactImageView = (ImageView) container.findViewById(R.id.contactImageIcon);
			
			String realContactItemIndex = getRealContactItemKey(cc, idL, idR);
			
			String contactImagePath = mContactItems.get(realContactItemIndex).getContactImagePath();
			
			//Log.v("ContactPrimaryCursorAdapter setimage [" + realContactItemIndex + "]["+cursor.getPosition()+"]["+rIdImages+"]["+contactImagePath+"]"+mContactItems.get(realContactItemIndex));
			
			if (contactImagePath != null && contactImagePath.length() > 0) {
				try {
					Drawable vContactImageDrawable = ThemeManager
							.getDrawableResourcesForTheme(mActivity,
									contactImagePath
									//mContact.getContactImage()
									);
					mContactImageView.setImageDrawable(vContactImageDrawable);
				} catch (NameNotFoundException e) {
				}
			} else {
				Bitmap vContactImage = getRandomContactDrawable();
				mContactImageView.setImageBitmap(vContactImage);
				Log.v("ContactPrimaryCursorAdapter setimage [" + realContactItemIndex + "]["+cursor.getPosition()+"]["+cursor.getCount()+"]["+rIdImages+"]("+vContactImage+")");
			}
			
			for (int i = 0; i < count; i++) {
				boolean bound = false;

				final View v = container.findViewById(to[i]);
				String text = "";
				if (v != null) {

					String columnName = mContNames[cc] + CFS
							+ mFromFieldNames[i];
					
					int columnId = cursor.getColumnIndex(columnName);

					text = cursor.getString(columnId);

					if (!bound) {
						if (text == null) {
							text = "";
						}

						if (v instanceof TextView) {
							setViewText((TextView) v, text);
						} else if (v instanceof ImageView) {
							//setViewImage((ImageView) v, text);
						} else {
							throw new IllegalStateException(
									v.getClass().getName()
											+ " is not a "
											+ " view that can be bounds by this ContactListCursorAdapter");
						}
					}
				}
			}
		}
	}

	private String getRealContactItemKey(int cc, String indexIdL, String indexIdR) {
		String ret = "0";
			if(cc==0) 	ret = indexIdL;
			else 		ret = indexIdR;
		return ret;
	}

	private int rIdImages=0;
	
	private Bitmap getRandomContactDrawable() {
		Bitmap result;
/*		
		if(rIdImages<imagesIds.length-1)rIdImages++;
		else rIdImages=0;
		result = mActivity.getResources().getDrawable( imagesIds[rIdImages] );
*/		
		Log.v("getRandomContactDrawable ["+rIdImages+"]");
		if(rIdImages>=ThemedResources.IC_ITEMS_CNT) rIdImages=0;
		result = mThemedResource.getRandomContactDrawable(rIdImages);
		rIdImages++;

		return result;
	}

	// Apply theme getApplicationContext().setTheme(R.style.MyTheme);
	// @Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View two = mLayoutInflater.inflate(mLayout, parent, false);
		int indexIdL = cursor.getColumnIndex(CONTACT_L + COLUMN_FIELS_SEPARATOR
				+ ContactsContract.Data._ID);
		String idL = cursor.getString(indexIdL);
		int indexIdR = cursor.getColumnIndex(CONTACT_R + COLUMN_FIELS_SEPARATOR
				+ ContactsContract.Data._ID);
		String idR = cursor.getString(indexIdR);

		// Log.v(TAG, ""+ "ContactPrimaryCursorAdapter newView-[" +
		// cursor.getPosition()+"]["+indexIdL+"]["+idL+"]("+mContactItems.get(
		// idL )+") ["+indexIdR+"]["+idR+"]("+mContactItems.get( idR )+")" );

		View v1 = two.findViewById(R.id.contact_entry_l);
		if (v1 != null) {

			// v1.setOnClickListener( new ContactClickListener(
			// mContactItems.get( idL ) ));
			/*
			 * v1.setOnClickListener( new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { TextView phoneText =
			 * (TextView)v.findViewById(R.id.contactPhoneText); TextView
			 * nameText = (TextView)v.findViewById(R.id.contactEntryText);
			 * 
			 * Toast.makeText(v.getContext(),
			 * "L 111111 ["+nameText.getText().toString()+"] " +
			 * phoneText.getText().toString(), Toast.LENGTH_SHORT) .show(); }; }
			 * );
			 */
		}
		View v2 = two.findViewById(R.id.contact_entry_r);
		if (v2 != null) {
			// v2.setOnClickListener( new ContactClickListener(
			// mContactItems.get( idR ) ));
			/*
			 * v2.setOnClickListener( new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { TextView phoneText =
			 * (TextView)v.findViewById(R.id.contactPhoneText); TextView
			 * nameText = (TextView)v.findViewById(R.id.contactEntryText);
			 * 
			 * Toast.makeText(v.getContext(),
			 * "R 222222 ["+nameText.getText().toString()+"] " +
			 * phoneText.getText().toString(), Toast.LENGTH_SHORT) .show(); ; };
			 * } );
			 */
		}
		return two;
	}

	/**
	 * Returns the cursor.
	 * 
	 * @return the cursor.
	 */
	public Cursor getCursor() {
		return mCursor;
	}

	public void setViewText(TextView v, String text) {
		v.setText(text);
	}

	public void setViewImage(ImageView v, String value) {
		try {
			v.setImageResource(Integer.parseInt(value));
		} catch (NumberFormatException nfe) {
			v.setImageURI(Uri.parse(value));
		}
	}

	private void findColumns(Cursor c, String[] from) {
		if (c != null) {
			int i;
			int count = from.length;
			if (mFrom == null || mFrom.length != count) {
				mFrom = new int[count];
			}
			for (i = 0; i < count; i++) {
				mFrom[i] = c.getColumnIndex(from[i]);
			}
		} else {
			mFrom = null;
		}
	}

	@Override
	/**
	 * @see android.widget.ListAdapter#getCount()
	 */
	public int getCount() {
		if (mDataValid && mCursor != null) {
			return mCursor.getCount();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (mDataValid && mCursor != null) {
			mCursor.moveToPosition(position);
			return mCursor;
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		if (mDataValid && mCursor != null) {
			if (mCursor.moveToPosition(position)) {
				return 0;// mCursor.getLong(mRowIDColumn);
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (!mDataValid) {
			throw new IllegalStateException(
					"this should only be called when the cursor is valid");
		}
		if (!mCursor.moveToPosition(position)) {
			throw new IllegalStateException("couldn't move cursor to position "
					+ position);
		}
		View v;
		if (convertView == null) {
			v = newView(mActivity, mCursor, parent);
		} else {
			v = convertView;
		}
		bindView(v, mActivity, mCursor);
		return v;
	}

	void showContactInfo(ContactItem contact) {
		// View view = mLayoutInflater.inflate(R.layout.contact_details_full,
		// null); //(ViewGroup) mActivity.findViewById(R.id.toast_layout_root));
		if (contact.getContactPhonesList()!=null) {
			Intent intent = new Intent(mActivity, ContactDetailsActivity.class);
			intent.putExtra(ContactItem.Desc, contact);
			mActivity.startActivityForResult(intent, REQUEST_ContactActivity);
		}
	}

	class ContactClickListener implements OnClickListener {
		ContactItem mContact;
		String mContactName;
		String mContactPhones;

		public ContactClickListener(Activity parent, ContactItem contact) {
			mContact = contact;
		}

		@Override
		public void onClick(View v) {
			Toast toast = new Toast(v.getContext());
			Utils.showCustomToast(mLayoutInflater, toast, mContact.getStringInfo());
			showContactInfo(mContact);
		}

	}

}

/*
 * http://stackoverflow.com/questions/1721279/how-to-read-contacts-on-android-2-0
 * http://thinkandroid.wordpress.com/2010/01/11/custom-cursoradapters/
 * http://stackoverflow
 * .com/questions/8942068/separate-click-events-for-each-column-in-list-view
 * 
 * Cursor cursor =
 * getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null,
 * null, null); while (cursor.moveToNext()) { String contactId =
 * cursor.getString(cursor.getColumnIndex( ContactsContract.Contacts._ID));
 * String hasPhone =
 * cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts
 * .HAS_PHONE_NUMBER)); if (Boolean.parseBoolean(hasPhone)) { // You know it has
 * a number so now query it like this Cursor phones =
 * getContentResolver().query(
 * ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
 * ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null,
 * null); while (phones.moveToNext()) { String phoneNumber =
 * phones.getString(phones.getColumnIndex(
 * ContactsContract.CommonDataKinds.Phone.NUMBER)); } phones.close(); }
 * 
 * Cursor emails =
 * getContentResolver().query(ContactsContract.CommonDataKinds.Email
 * .CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = "
 * + contactId, null, null); while (emails.moveToNext()) { // This would allow
 * you get several email addresses String emailAddress = emails.getString(
 * emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)); }
 * emails.close(); }
 */