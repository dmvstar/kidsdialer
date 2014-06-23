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

package net.sf.dvstar.kidsdialer.utils;

import android.provider.ContactsContract;

public interface Commons {

	public static final int REQUEST_ImageListActivity = 2;
	public static final int REQUEST_FavoritesManagerActivity = 3;
	public static final int REQUEST_ContactActivity = 4;
	public static final int REQUEST_ChanepasswordActivity = 5;	
	
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
	public final static String PARAM_PINPASS = "pinpass";	
	public final static String PARAM_NEWPINPASS = "newpinpass";	
	
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


