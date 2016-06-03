package io.github.garywahaha.backgroundyoutubeplayer.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;
import io.github.garywahaha.backgroundyoutubeplayer.MainActivity;
import io.github.garywahaha.backgroundyoutubeplayer.R;

/**
 * Created by Gary on 24/4/2016.
 */
public class NotificationService extends Service {
	public static final String LOG_TAG = NOTIFICATION_SERVICE.getClass().getSimpleName();

	MediaPlayer mediaPlayer;
	Notification status;

	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mediaPlayer = new MediaPlayer();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
			showNotification();
			playMusic();
			Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
		}
		else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
			Toast.makeText(this, "Clicked Prev", Toast.LENGTH_SHORT).show();
			Log.i(LOG_TAG, "Clicked Prev");
		}
		else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
			Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
			Log.i(LOG_TAG, "Clicked Play");
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
			}
			else {
				mediaPlayer.start();
			}
		}
		else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
			Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
			Log.i(LOG_TAG, "Clicked Next");
		}
		else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
			Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
			Log.i(LOG_TAG, "Clicked stop foreground");
			mediaPlayer.release();
			stopForeground(true);
			stopSelf();
		}

		return START_STICKY;
	}

	private void showNotification() {
		RemoteViews views = new RemoteViews(getPackageName(), R.layout.status_bar);
		RemoteViews bigViews = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);

		views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
		views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
		bigViews.setImageViewBitmap(R.id.status_bar_album_art, Constants.getDefaultAlbumArt(this));

		Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
		notificationIntent.setFlags(
				Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
		);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		Intent previousIntent = new Intent(this, NotificationService.class);
		previousIntent.setAction(Constants.ACTION.PREV_ACTION);
		PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0);

		Intent playIntent = new Intent(this, NotificationService.class);
		playIntent.setAction(Constants.ACTION.PLAY_ACTION);
		PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

		Intent nextIntent = new Intent(this, NotificationService.class);
		nextIntent.setAction(Constants.ACTION.PREV_ACTION);
		PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

		Intent closeIntent = new Intent(this, NotificationService.class);
		closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
		PendingIntent pcloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);

		views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
		bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

		views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
		bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

		views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
		bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

		views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
		bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

		views.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
		bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);

		views.setTextViewText(R.id.status_bar_track_name, "Song Title");
		bigViews.setTextViewText(R.id.status_bar_track_name, "Song Title");

		views.setTextViewText(R.id.status_bar_artist_name, "Artist Nmae");
		bigViews.setTextViewText(R.id.status_bar_artist_name, "Artist Nmae");

		bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");

		status = new Notification.Builder(this).build();
		status.contentView = views;
		status.bigContentView = bigViews;
		status.flags = Notification.FLAG_ONGOING_EVENT;
		status.icon = R.drawable.ic_launcher;
		status.contentIntent = pendingIntent;
		startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
	}

	private void playMusic() {
		String videoUrl = "https://www.youtube.com/watch?v=Ya4Fz2RWqq4";

		YouTubeUriExtractor youTubeUriExtractor = new YouTubeUriExtractor(this) {
			@Override
			public void onUrisAvailable(String s, String s1, SparseArray<YtFile> ytFiles) {
				if (ytFiles != null) {
					int itag = 22;
					String downloadUrl = ytFiles.get(itag).getUrl();

					try {
						mediaPlayer.setDataSource(downloadUrl);

						mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
							@Override
							public void onPrepared(MediaPlayer mediaPlayer) {
								mediaPlayer.start();
							}
						});

						mediaPlayer.prepareAsync();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		youTubeUriExtractor.execute(videoUrl);
	}
}