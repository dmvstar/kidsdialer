package net.sf.dvstar.kidsdialer.adapters;

import net.sf.dvstar.kidsdialer.KidsDialerMainActivity;
import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.utils.Log;
import android.content.Context;
import android.database.Cursor;

import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListCursorAdapter extends CursorAdapter 
	//implements Filterable
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
	
	protected int[] mTo;
	String[] mOriginalFrom;

	private Context context;
	private LayoutInflater mLayoutInflater;

	private final ContactFilter filter;
	
	private int mLayout;

	public ListCursorAdapter(Context context, int layout, Cursor cursor,
			String[] from, int[] to) {
		super(context, cursor);
		this.mTo = to;
		
		this.mOriginalFrom = from;
		this.context = context;
		this.mLayout = layout;
		mLayoutInflater = LayoutInflater.from(context);
		findColumns(cursor, from);
		this.filter = new ContactFilter();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*
		 * if(convertView == null){ LayoutInflater inflater =
		 * (LayoutInflater)context
		 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE); converView =
		 * inflater.inflate(R.layout.your_layout, null); final ViewHolder
		 * viewHolder = new ViewHolder(); viewHolder.textView1 = (TextView)
		 * view.findViewById(R.id.text1); textView1.setOnClickListener(new
		 * OnClickListener(){
		 * 
		 * @Override public void onClick(View v){ // do whatever you want }; });
		 * viewHolder.textView2 = (TextView) view.findViewById(R.id.text2);
		 * textView2.setOnClickListener(new OnClickListener(){
		 * 
		 * @Override public void onClick(View v){ // do whatever you want }; });
		 * }
		 */
		View view = mLayoutInflater.inflate(mLayout, null);
		//return view; //
		return super.getView(position, convertView, parent);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		//super.bindView(view, context, cursor);
		final int[] to = mTo;
		final int[] from = mFrom;
		// final int[] two = mFrom;

		final int count = mTo.length;

		{

//			Log.v(view.getContentDescription()
//					.toString() + "-"+cursor.getPosition());

			for (int i = 0; i < count; i++) {
				final View v = view.findViewById(to[i]);
				String text = "";
				if (v != null) {
					boolean bound = false;
					if (mOriginalFrom[i] == Phone.NUMBER) {
						String hasPhone = cursor
								.getString(cursor
										.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
						if (Integer.parseInt(hasPhone) > 0) {
							String contactId = cursor
									.getString(cursor
											.getColumnIndex(ContactsContract.Contacts._ID));
							Cursor phones = context
									.getContentResolver()
									.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
											null,
											ContactsContract.CommonDataKinds.Phone.CONTACT_ID
													+ " = " + contactId, null,
											null);
							while (phones.moveToNext()) {
								String phoneNumber = phones
										.getString(phones
												.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								text += phoneNumber
										+ (phones.isLast() ? "" : ", ");
							}
							phones.close();
							Log.v("   " + text);
						} else {
							text = "-";
						}
					} else {
						text = cursor.getString(from[i]);
					}

					if (!bound) {
						if (text == null) {
							text = "";
						}

						if (v instanceof TextView) {
							setViewText((TextView) v, text);
						} else if (v instanceof ImageView) {
							setViewImage((ImageView) v, text);
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

	@Override
	public View newView(Context context, final Cursor cursor, ViewGroup parent) {
		View view = mLayoutInflater.inflate(R.layout.contact_entry_v, parent, false);
		// View view = two.findViewById(R.id.contact_entry_l);
		// final ViewHolder viewHolder = new ViewHolder();
		return view;
	}

	//@Override
	public android.widget.Filter getFilter_() {
	    return filter;
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

	
	private class ContactFilter extends android.widget.Filter {

	    @Override
	    protected FilterResults performFiltering(final CharSequence constraint) {

	        final FilterResults oReturn = new FilterResults();
/*	        
	        final ArrayList<Station> results = new ArrayList<Station>();
	        if (orig == null)
	            orig = _stations;
	        if (constraint != null) {
	            if (orig != null && orig.size() > 0) {
	                for (final Station g : orig) {
	                    if (g.mName.contains(constraint.toString().toUpperCase()))
	                        results.add(g);
	                }
	            }
	            oReturn.values = results;
	        }
*/	        
	        return oReturn;

	    }

	    @Override
	    protected void publishResults(final CharSequence constraint, final FilterResults results) {
	        //_stations = (ArrayList<Station>) results.values;
	        notifyDataSetChanged();
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