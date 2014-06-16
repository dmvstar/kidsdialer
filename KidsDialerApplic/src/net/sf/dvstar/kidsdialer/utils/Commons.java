package net.sf.dvstar.kidsdialer.utils;

import android.provider.ContactsContract;

public interface Commons {

	public static final int REQUEST_ImageListActivity = 2;
	public static final int REQUEST_FavoritesManagerActivity = 3;
	public static final int REQUEST_ContactActivity = 4;
		
	
	public final static String PARAM_PINTENT = "pendingIntent";	
	public final static String PARAM_RESULT = "result";	
	public final static int STATUS_FINISH = 200;	
	

	public final static String PARAM_CONTACT = "contact";	
	public final static String PARAM_CONTACT_EDIT_MODE = "contactEditMode";	
	public final static String PARAM_CONTACT_EDIT_ID = "contactEditId";	
	public final static String LAST_PHONE_UID = "LastPhoneUID";
	public final static String LAST_PHONE_NUMBER = "LastPhoneNumber";
	
	
	public final static int CONTACT_EDIT_MODE_EDIT = 200;	
	public final static int CONTACT_EDIT_MODE_ADD = 201;
	
	public final static String PARAM_DATACHANGED = "dataChanged";	
	
	public static final String TAG = "KidsDialer";
	
	public static final String CONTACT_L = "Contact.Left.Data";
	public static final String CONTACT_R = "Contact.Right.Data";
	public static final String[] CONTACT_COLUMNS_PREFS = new String[]{CONTACT_L, CONTACT_R};
	
	public static final String COLUMN_FIELS_SEPARATOR = ".";
	public static final String CFS = COLUMN_FIELS_SEPARATOR;
	
	public static final String[] CONTACT_COLUMNS_NAMES  = new String[]{
		CONTACT_L+COLUMN_FIELS_SEPARATOR+ContactsContract.Data._ID,
		CONTACT_L+COLUMN_FIELS_SEPARATOR+ContactsContract.Data.DISPLAY_NAME,
		CONTACT_L+COLUMN_FIELS_SEPARATOR+ContactsContract.CommonDataKinds.Phone.NUMBER,
		CONTACT_R+COLUMN_FIELS_SEPARATOR+ContactsContract.Data._ID,
		CONTACT_R+COLUMN_FIELS_SEPARATOR+ContactsContract.Data.DISPLAY_NAME,
		CONTACT_R+COLUMN_FIELS_SEPARATOR+ContactsContract.CommonDataKinds.Phone.NUMBER
		};
	
	public static final String[] CONTACT_FIELD_NAMES  = new String[]{
		//ContactsContract.Data._ID,
		ContactsContract.Data.DISPLAY_NAME,
		ContactsContract.CommonDataKinds.Phone.NUMBER
		};
	
}


