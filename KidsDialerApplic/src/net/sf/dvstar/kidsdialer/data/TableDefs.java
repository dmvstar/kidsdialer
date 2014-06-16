package net.sf.dvstar.kidsdialer.data;

import android.provider.ContactsContract;

public interface TableDefs {

	public static final String TABLE_FAVORITES = "favorites";

	public static final String TABLE_ID = "id";
	public static final String CONTACT_ID = ContactsContract.Contacts._ID; // "_id";
	public static final String CONTACT_NAME = ContactsContract.Contacts.DISPLAY_NAME; // "name";
	public static final String CONTACT_PHONES = ContactsContract.CommonDataKinds.Phone.NUMBER; // "phones";
	public static final String CONTACT_LAST_PHONE = "lastphone";
	public static final String CONTACT_IMAGE = "image";
	public static final String CONTACT_RING = "rington";
	public static final String CONTACT_EMAILS = "email";

}
