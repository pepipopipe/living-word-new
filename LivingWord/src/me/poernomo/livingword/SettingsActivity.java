package me.poernomo.livingword;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener  {

	private static final String KEY_FONT_SIZES = "font_sizes";
	private static final String KEY_REFRESH_RATES = "refresh_rates";

	private ListPreference fontSizePref, refreshRatePref;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.prefs);

		DBHelper myHelper = new DBHelper(this);
		if (!myHelper.checkDataBase()) {
			myHelper.copyDatabase();
		}

		fontSizePref = (ListPreference) getPreferenceScreen().findPreference(
				KEY_FONT_SIZES);
		refreshRatePref = (ListPreference) getPreferenceScreen()
				.findPreference(KEY_REFRESH_RATES);
	}

	@Override
	protected void onPause() {
		super.onPause();

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		pref.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		final SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		//updateFontSizeSummary(pref);
		updateRefreshRateSummary(pref);
		pref.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
		if (key.equals(KEY_FONT_SIZES)) {
			updateFontSizeSummary(pref);
		} else if (key.equals(KEY_REFRESH_RATES)) {
			updateRefreshRateSummary(pref);
		}
	}

	private void updateFontSizeSummary(SharedPreferences pref) {
		fontSizePref.setSummary(pref.getString(KEY_FONT_SIZES, "Medium"));
	}

	private void updateRefreshRateSummary(SharedPreferences pref) {
		String hour = "hours";
		if (pref.getString(KEY_REFRESH_RATES, "").equals("1"))
			hour = "hour";
		refreshRatePref.setSummary("Every "
				+ pref.getString(KEY_REFRESH_RATES, "3") + " " + hour);
	}

}
