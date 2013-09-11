package me.poernomo.livingword;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyWidgetProvider extends AppWidgetProvider {

	private PendingIntent service = null;
	String passage, content;
	private int refreshRate = 1;

	@Override
	public void onDisabled(Context context) {
		final AlarmManager m = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (service != null) {
			m.cancel(service);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// Get refresh rate from prefs
		SharedPreferences myPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		refreshRate = Integer.parseInt(myPref.getString("refresh_rates", "1"));

		final AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		final Calendar TIME = Calendar.getInstance();
		TIME.set(Calendar.MINUTE, 0);
		TIME.set(Calendar.SECOND, 0);
		TIME.set(Calendar.MILLISECOND, 0);

		final Intent i = new Intent(context, UpdateWidgetService.class);

		if (service == null) {
			service = PendingIntent.getService(context, 0, i,
					PendingIntent.FLAG_CANCEL_CURRENT);
		}
		am.setRepeating(AlarmManager.RTC, TIME.getTime().getTime(),
				3600000 * refreshRate, service);

	}

}
