package me.poernomo.livingword;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
	SharedPreferences myPref;
	String content, passage;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		myPref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
				.getApplicationContext());

		ComponentName thisWidget = new ComponentName(getApplicationContext(),
				MyWidgetProvider.class);
		int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);

		ArrayList<String> topic = getTopics(this.getApplicationContext());
		if (topic.size() <= 0) {
			content = "Please select at least one topic";
			passage = "<Empty>";
		} else {
			Verse verse = getVerse(this.getApplicationContext(), topic);
			content = verse.getContent();
			passage = verse.getPassage();
		}

		for (int widgetId : allWidgetIds2) {

			RemoteViews remoteViews = new RemoteViews(this
					.getApplicationContext().getPackageName(),
					R.layout.widget_display);
			remoteViews.setTextViewText(R.id.widget_tv_passage, passage);
			remoteViews.setTextViewText(R.id.widget_tv_content, content);

			// Register onClickListener
			Intent clickIntent = new Intent(this.getApplicationContext(),
					MyWidgetProvider.class);
			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
					allWidgetIds2);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					getApplicationContext(), 0, clickIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.widget_b_refresh,
					pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);

		}
		stopSelf();

		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * Helper method that sets this.verse and this.content to a random entry in
	 * db
	 */
	private Verse getVerse(Context context, ArrayList<String> topics) {
		if (topics.size() <= 0)
			return null;
		Verse myVerse;
		DBHelper myHelper = new DBHelper(context);

		if (!myHelper.checkDataBase()) {
			myHelper.copyDatabase();
		}

		myHelper.openDB();

		myVerse = myHelper.getRandom(topics);
		myHelper.close();

		return myVerse;
	}

	private ArrayList<String> getTopics(Context context) {
		ArrayList<String> selectedTopics = new ArrayList<String>();

		if (myPref.getBoolean("topic_encouragement", true)) {
			selectedTopics.add("encouragement");
		}
		if (myPref.getBoolean("topic_daily_living", true)) {
			selectedTopics.add("daily_living");
		}
		if (myPref.getBoolean("topic_god", true)) {
			selectedTopics.add("god");
		}
		if (myPref.getBoolean("topic_temptation", true)) {
			selectedTopics.add("temptation");
		}
		return selectedTopics;
	}
}
