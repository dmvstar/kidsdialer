package net.sf.dvstar.kidsdialer.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.dvstar.kidsdialer.utils.Log;
import net.sf.dvstar.kidsdialer.utils.theme.DrawableItem.DrawableItemInfo;

import android.content.ContentValues;
import android.provider.ContactsContract;

public class ContactItem implements Serializable {


	private static final long serialVersionUID = 9025161276535292107L;

	public static final String Desc = "ContactItem.Desc";
	
	private String mContactId;
	private String mContactName;
	private String mContactNameLast;
	private String mContactLastPhone;
	private String mContactPhones;
	private String mContactPhonesNl;
	
	private String mContactRingtone;
	private String mContactImagePath;
	
	private List mContactPhonesList;
	
	
	public ContactItem() {
	}
	
	public ContactItem(String contactId, String contactName,
			String contactPhones) {
		this.mContactId = contactId;
		this.mContactName = contactName;
		this.mContactPhones = contactPhones;
	}

	public String getContactRingtone() {
		return mContactRingtone;
	}

	public void setContactRingtone(String mContactRingtone) {
		this.mContactRingtone = mContactRingtone;
	}

	public String getContactImagePath() {
		return mContactImagePath;
	}

	public void setContactImagePath(String pContactImagePath) {
		this.mContactImagePath = pContactImagePath;
	}

	public String getContactLastPhone() {
		return mContactLastPhone;
	}

	public void setContactLastPhone(String mContactLastPhone) {
		this.mContactLastPhone = mContactLastPhone;
	}


	public ContactItem(String contactId, String contactName) {
		this.mContactId = contactId;
		this.mContactName = contactName;
	}

	public String getContactName() {
		return mContactName;
	}

	public void setContactName(String mContactName) {
		this.mContactName = mContactName;
	}

	public String getContactNameLast() {
		return mContactNameLast;
	}

	public void setContactNameLast(String mContactNameLast) {
		this.mContactNameLast = mContactNameLast;
	}

	public String getContactPhones() {
		return mContactPhones;
	}

	public List<String> getContactPhonesList() {
		return mContactPhonesList;
	}

	public void setContactPhonesList(List mContactPhonesList) {
		this.mContactPhonesList = mContactPhonesList;
		this.mContactPhones = preparePhones(mContactPhonesList, ", ");
		this.mContactPhonesNl = preparePhones(mContactPhonesList, "\n");
	}

	public void setContactPhones(String contactPhones) {
		this.mContactPhones = contactPhones;
		this.mContactPhonesList = preparePhonesList(mContactPhones);
		this.mContactPhonesNl = preparePhones(mContactPhonesList, "\n");
	}

	private List preparePhonesList(String contactPhones) {
		List result = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(mContactPhones, ",");
		if (st.hasMoreElements()) {
			while (st.hasMoreElements()) {
				result.add(st.nextToken().trim());
			}
		} else {
			result.add(contactPhones.trim());
		}

		return result;
	}

	private String preparePhones(List<String> contactPhonesList,
			String delim) {
		String phoneNumbers = "";

		if (contactPhonesList != null)

			for (int i = 0; i < contactPhonesList.size(); i++) {
				String phoneNumber = contactPhonesList.get(i);
				phoneNumbers += phoneNumber
						+ (i == contactPhonesList.size() - 1 ? "" : delim);
			}

		return phoneNumbers;
	}

	public String getContactId() {
		return mContactId;
	}

	public String getStringInfo() {
		String ret = "[" + mContactId + "]\n[" + mContactName + "]\n{"
				+ getContactPhones() + "}";
		return ret;
	}

	public String getContactPhonesNl() {
		return mContactPhonesNl;
	}
	
	
	public ContentValues getContentValues(){
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(TableDefs.CONTACT_ID, getContactId());
		contentValues.put(TableDefs.CONTACT_NAME, getContactName());
		contentValues.put(TableDefs.CONTACT_PHONES, getContactPhones());
		contentValues.put(TableDefs.CONTACT_RING, getContactRingtone());
		contentValues.put(TableDefs.CONTACT_IMAGE, getContactImagePath());
		
		Log.v("[ContactItem] setContactImageInfo "+contentValues);
		return contentValues;
	}

	public void setContactImageInfo(DrawableItemInfo item) {
		Log.v("[ContactItem] getContentValues "+item);
		setContactImagePath(item.getImagePath());
	}
	
	public String toString() {
		String ret = "ContactItem mContactId[" + mContactId + "] mContactName[" + mContactName + "]("
				+ getContactPhones() + ")"+" mContactImage["+mContactImagePath+"]";
		return ret;
	}


}
