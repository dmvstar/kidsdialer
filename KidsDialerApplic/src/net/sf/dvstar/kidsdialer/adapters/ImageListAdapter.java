package net.sf.dvstar.kidsdialer.adapters;

import net.sf.dvstar.kidsdialer.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageListAdapter extends ArrayAdapter<String> {

	private Context mContext;
	private String[] mValues;

	public ImageListAdapter(Context context, int textViewResourceId,
			String[] values) {
		super(context, textViewResourceId);
		this.mContext = context;
		this.mValues = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater
				.inflate(R.layout.list_image_item, parent, false);
		/*
		 * TextView textView = (TextView) rowView.findViewById(R.id.label);
		 * ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		 * textView.setText(values[position]); // Change the icon for Windows
		 * and iPhone String s = values[position]; if (s.startsWith("Windows7")
		 * || s.startsWith("iPhone") || s.startsWith("Solaris")) {
		 * imageView.setImageResource(R.drawable.no); } else {
		 * imageView.setImageResource(R.drawable.ok); }
		 */
		return rowView;
	}

}



/*
http://android-developers.blogspot.ru/2010/07/multithreading-for-performance.html
http://developer.android.com/training/improving-layouts/smooth-scrolling.html
*/