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

package net.sf.dvstar.kidsdialer.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.dvstar.kidsdialer.data.ContactMatrixCursor;
import net.sf.dvstar.kidsdialer.utils.Commons;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;

public class ContactPrimaryCursorData implements Commons {

	private int mContainers[];
	private String mColumnNames[];
	private Context mContext;
	private Cursor contactCursor = null;
	private HashMap<String, ContactItem> contactItems = null;
	private List<ContactItem> contactItemList = null;

	private static final boolean USE_FAVORITES = true; 
	
	public ContactPrimaryCursorData(Context context, int containers[],
			String containerNames[], String columnNames[]) {
		this.mContainers = containers;
		this.mColumnNames = columnNames;
		this.mContext = context;
		prepareContactCursor();
	}

	/*
	 * http://stackoverflow.com/questions/5639856/android-how-to-create-my-own-
	 * cursor-class
	 */

	private void prepareContactCursor() {
		HashMap<String, String> liv, riv;
		List<String> ivv, rvv;
		
		Cursor allContacts;
		if(USE_FAVORITES) {
			allContacts = getContactsFavorites();
		} else {
			allContacts = getContacts();
		}

		contactCursor = new ContactMatrixCursor(mColumnNames);
		Log.v(TAG, "allContacts " + allContacts.getCount());

		contactItems = new HashMap<String, ContactItem>();
		contactItemList = new ArrayList<ContactItem>();

		boolean hasMoreElements = true;
		ContactItem ci = null;

		while (hasMoreElements) {

			// CONTACT_L
			if ((hasMoreElements = allContacts.moveToNext())) {
				ci = getContactItemValue(allContacts);
				contactItemList.add(ci);
				contactItems.put(ci.getContactId(), ci);
				ivv = getItemValueFromContactItem(ci);// getItemValue(allContacts);
			} else {
				continue; // ivv = getItemValue(null, CONTACT_L);
			}

			// CONTACT_R
			if ((hasMoreElements = allContacts.moveToNext())) {
				ci = getContactItemValue(allContacts);
				contactItemList.add(ci);
				contactItems.put(ci.getContactId(), ci);
				rvv = getItemValueFromContactItem(ci);// getItemValue(allContacts);
			} else {
				ci = getContactItemValue(null);
				contactItemList.add(ci);
				contactItems.put(ci.getContactId(), ci);
				rvv = getItemValueFromContactItem(ci);// getItemValue(null);
			}

			if (ivv != null & rvv != null) {
				ivv.addAll(rvv);
				((MatrixCursor) contactCursor).addRow(ivv);
			}
			;

		}
		allContacts.close();

		boolean restoreposition = contactCursor.moveToFirst();

		Log.v(TAG, "prepareContactCursor " + contactCursor.getCount() + " "
				+ contactCursor.getColumnCount() + " " + restoreposition);
	}

	public Cursor getContactCursor() {
		if (contactCursor == null)
			prepareContactCursor();
		return contactCursor;
	}

	public HashMap<String, ContactItem> getContactItems() {
		if (contactItems == null || contactCursor == null
				|| contactItemList == null)
			prepareContactCursor();
		return contactItems;
	}

	public List<ContactItem> getContactItemList() {
		if (contactItems == null || contactCursor == null
				|| contactItemList == null)
			prepareContactCursor();
		return contactItemList;
	}

	private ContactItem getContactItemValue(Cursor allContacts) {
		ContactItem contactItem = null;
		int iColumn = 0;
		String contactId = "";
		String contactName = "";
		List<String> phoneNumbers = null;

		if (allContacts != null) {
			contactId = allContacts.getString(allContacts
					.getColumnIndex(ContactsContract.Contacts._ID));
			contactName = allContacts.getString(allContacts
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

			contactItem = new ContactItem(contactId, contactName);

			
			int phonesColumn = allContacts
					.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

			iColumn = allContacts.getColumnIndex(TableDefs.CONTACT_IMAGE);
			if (iColumn >= 0) {
				contactItem.setContactImagePath(allContacts.getString(iColumn));
			}
			
			if (phonesColumn < 0) {
				String hasPhone = allContacts
						.getString(allContacts
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				if (Integer.parseInt(hasPhone) > 0) {
					// You know it has a number so now query it like this
					phoneNumbers = new ArrayList<String>();

					Cursor phones = mContext.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
					while (phones.moveToNext()) {
						String phoneNumber = phones
								.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						phoneNumbers.add(phoneNumber);
					}
					phones.close();
					contactItem.setContactPhonesList(phoneNumbers);
				}
			} else {
				String sphones = allContacts
						.getString(  allContacts
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)  );
				contactItem.setContactPhones( sphones );
			}
		} else {
			contactItem = new ContactItem("", "", "");
		}

		return contactItem;
	}

	private List<String> getItemValueFromContactItem(ContactItem contactItem) {
		List<String> itemValues = new ArrayList<String>();

		itemValues.add(contactItem.getContactId());
		itemValues.add(contactItem.getContactName());
		itemValues.add(contactItem.getContactPhones());

		return itemValues;
	}

	private List<String> _getItemValue(Cursor allContacts) {
		String contactId = "";
		String contactName = "";
		String phoneNumbers = "";

		// HashMap<String, String> itemValues = new HashMap<String, String>();
		List<String> itemValues = new ArrayList<String>();

		if (allContacts != null) {
			contactId = allContacts.getString(allContacts
					.getColumnIndex(ContactsContract.Contacts._ID));
			contactName = allContacts.getString(allContacts
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			// String contactPhone =
			// cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			phoneNumbers = "";
			String hasPhone = allContacts
					.getString(allContacts
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if (Integer.parseInt(hasPhone) > 0) {
				// You know it has a number so now query it like this
				phoneNumbers = "";
				Cursor phones = mContext.getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phones.moveToNext()) {
					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					phoneNumbers += phoneNumber + (phones.isLast() ? "" : ", ");
				}
				phones.close();
			}
		}
		Log.d(TAG, contactId + " addItemValue " + contactName + " ");
		Log.d(TAG, "   addItemValue " + phoneNumbers);
		/*
		 * itemValues.put(ContactsContract.Contacts._ID, contactId);
		 * itemValues.put(ContactsContract.Contacts.DISPLAY_NAME, contactName);
		 * itemValues
		 * .put(ContactsContract.CommonDataKinds.Phone.NUMBER,phoneNumbers);
		 */
		itemValues.add(contactId);
		itemValues.add(contactName);
		itemValues.add(phoneNumbers);

		return itemValues;
	}

	private Cursor getContactsFavorites() {
		Cursor result;
		FavoriteDataProvider provider = new FavoriteDataProvider(mContext);
		result = provider.getFavoriteCursor();
		return result;
	}

	private Cursor getContacts() {
		// Run query
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER, Phone.NUMBER,
				Phone.TYPE, Phone.LABEL };

		String selection;
		/*
		 * selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
		 * (mShowInvisible ? "0" : "1") + "' AND "+
		 * ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";
		 */
		selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";

		String[] selectionArgs = null;
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		// return managedQuery(uri, projection, selection, selectionArgs,
		// sortOrder);
		return mContext.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, selection, null,
				sortOrder);

	}

}


