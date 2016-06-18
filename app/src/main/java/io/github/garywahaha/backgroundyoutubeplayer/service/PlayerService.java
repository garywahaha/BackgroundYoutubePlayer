package io.github.garywahaha.backgroundyoutubeplayer.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;

import java.util.ArrayList;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;
import io.github.garywahaha.backgroundyoutubeplayer.R;
import io.github.garywahaha.backgroundyoutubeplayer.video.Video;
import io.github.garywahaha.backgroundyoutubeplayer.video.list.VideoListActivity;

/**
 * Created by Gary on 24/4/2016.
 */
public class PlayerService extends Service implements MediaPlayer.OnCompletionListener {
	public static final String LOG_TAG = NOTIFICATION_SERVICE.getClass().getSimpleName();

	MediaPlayer mediaPlayer;
	WifiManager.WifiLock wifiLock;

	Notification status;
	NotificationTarget notificationTarget;

	ArrayList<Video> videos;
	int position;

	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);

		// TODO: Move lock tag to constant
		wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE)).createWifiLock(
				WifiManager.WIFI_MODE_FULL,
		        "mylock"
		);
		wifiLock.acquire();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
			videos = intent.getParcelableArrayListExtra(Constants.INTENT_KEY.VIDEO_LIST_KEY);
			position = 0;
			showNotification(videos.get(position));
			playMusic(videos.get(position));
			Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
		}
		else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
			playPrev();
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
			playNext();
		}
		else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
			Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
			Log.i(LOG_TAG, "Clicked stop foreground");
			mediaPlayer.release();
			wifiLock.release();
			stopForeground(true);
			stopSelf();
		}

		return START_STICKY;
	}

	private void showNotification(Video video) {
		RemoteViews views = new RemoteViews(getPackageName(), R.layout.status_bar);
		RemoteViews bigViews = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);

		views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
		views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
		bigViews.setImageViewBitmap(R.id.status_bar_album_art, Constants.getDefaultAlbumArt(this));

		Intent notificationIntent = new Intent(this, VideoListActivity.class);
		notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
		notificationIntent.setFlags(
				Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
		);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		Intent previousIntent = new Intent(this, PlayerService.class);
		previousIntent.setAction(Constants.ACTION.PREV_ACTION);
		PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0);

		Intent playIntent = new Intent(this, PlayerService.class);
		playIntent.setAction(Constants.ACTION.PLAY_ACTION);
		PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

		Intent nextIntent = new Intent(this, PlayerService.class);
		nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
		PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

		Intent closeIntent = new Intent(this, PlayerService.class);
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

		views.setTextViewText(R.id.status_bar_track_name, video.getTitle());
		bigViews.setTextViewText(R.id.status_bar_track_name, video.getTitle());

		views.setTextViewText(R.id.status_bar_artist_name, "Artist Nmae");
		bigViews.setTextViewText(R.id.status_bar_artist_name, "Artist Nmae");

		bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");

		status = new Notification.Builder(this).build();
		status.contentView = views;
		status.bigContentView = bigViews;
		status.flags = Notification.FLAG_ONGOING_EVENT;
		status.icon = R.drawable.ic_launcher;
		status.contentIntent = pendingIntent;

		notificationTarget = new NotificationTarget(
				this,
		        bigViews,
		        R.id.status_bar_album_art,
		        status,
				Constants.NOTIFICATION_ID.FOREGROUND_SERVICE
		);

		startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
	}

	private void playMusic(Video video) {
		String videoUrl = video.getVideoUrl();

		Log.i(LOG_TAG, videoUrl);

		Glide.with(this)
				.load(video.getThumbnailUrl())
				.asBitmap()
				.into(notificationTarget);

		YouTubeUriExtractor youTubeUriExtractor = new YouTubeUriExtractor(this) {
			@Override
			public void onUrisAvailable(String s, String s1, SparseArray<YtFile> ytFiles) {
				if (ytFiles != null) {
					YtFile ytFile = ytFiles.get(140);
					if (ytFile == null) {
						ytFile = ytFiles.get(18);
					}
					String downloadUrl = ytFile.getUrl();

					try {
						if (mediaPlayer != null) {
							mediaPlayer.reset();
							mediaPlayer.setDataSource(downloadUrl);
							mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
								@Override
								public void onPrepared(MediaPlayer mediaPlayer) {
									mediaPlayer.start();
								}
							});
							mediaPlayer.prepareAsync();
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		youTubeUriExtractor.execute(videoUrl);
	}

	private void playNext() {
		if (position + 1 > videos.size()) {
			Toast.makeText(this, "Last item", Toast.LENGTH_SHORT).show();
		}
		else {
			position += 1;
			showNotification(videos.get(position));
			playMusic(videos.get(position));
		}
	}

	private void playPrev() {
		if (position == 0) {
			Toast.makeText(this, "First item", Toast.LENGTH_SHORT).show();
		}
		else {
			position -= 1;
			showNotification(videos.get(position));
			playMusic(videos.get(position));
		}
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		playNext();
	}

	public static Intent getIntent(Context context, ArrayList<Video> list) {
		Intent intent = new Intent(context, PlayerService.class);
		intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
		intent.putParcelableArrayListExtra(Constants.INTENT_KEY.VIDEO_LIST_KEY, list);
		return intent;
	}
}
