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
