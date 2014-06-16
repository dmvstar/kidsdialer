package net.sf.dvstar.kidsdialer.activities;

import java.util.List;

import net.sf.dvstar.kidsdialer.KidsDialerMainActivity;
import net.sf.dvstar.kidsdialer.R;
import net.sf.dvstar.kidsdialer.apps.ThemedListActivity;
import net.sf.dvstar.kidsdialer.utils.theme.ThemeManager;
import net.sf.dvstar.kidsdialer.utils.theme.ThemedResources.ThemeDesc;
import net.sf.dvstar.kidsdialer.utils.theme.ThemesConfiguration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class SwitchThemeActivity extends ThemedListActivity {

	private LayoutInflater mInflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		List<ThemeDesc> themes = ThemeManager.getThemes(this,
				new ThemesConfiguration()
		// .addInnerTheme(R.style.CustomThemeGreen , "Green")
				);

		ArrayAdapter<ThemeDesc> adapter = new ArrayAdapter<ThemeDesc>(
				this, android.R.layout.simple_list_item_1, themes) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View row;

				if (null == convertView) {
					row = mInflater.inflate(
							android.R.layout.simple_list_item_1, null);
				} else {
					row = convertView;
				}

				TextView tv = (TextView) row.findViewById(android.R.id.text1);
				tv.setText(((ThemeDesc) getItem(position)).mTitle);
				return row;
			}

		};

		getListView().setAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ThemeDesc theme = (ThemeDesc) getListView()
						.getAdapter().getItem(position);

				ThemeManager.setTheme(SwitchThemeActivity.this, theme);
				Intent intent = new Intent();
				intent.setClass(SwitchThemeActivity.this,
						KidsDialerMainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				SwitchThemeActivity.this.startActivity(intent);
			}
		});

	}
}
