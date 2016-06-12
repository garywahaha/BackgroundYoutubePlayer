package io.github.garywahaha.backgroundyoutubeplayer.video;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist;

/**
 * Created by Gary on 5/6/2016.
 */
public class VideoModel {
	private static final String LOG_TAG = VideoModel.class.getSimpleName();

	private YouTube youTube;
	private SharedPreferences sharedPreferences;

	public VideoModel(YouTube youTube, SharedPreferences sharedPreferences) {
		this.youTube = youTube;
		this.sharedPreferences = sharedPreferences;
	}

	public interface Callbacks {
		void onSuccess();
		void onError(Exception e);
	}

	public Video getVideo(String id) {
		return SQLite.select()
				.from(Video.class)
				.where(Video_Table.videoId.eq(id))
				.querySingle();
	}

	public List<Video> getPlaylistItems(Playlist playlist) {
		return SQLite.select()
				.from(Video.class)
				.where(Video_Table.playlist_playlistId.eq(playlist.getPlaylistId()))
				.queryList();
	}

	public void save(Video video) {
		video.save();
	}

	public void saveAll(List<Video> videos) {
		for (Video video: videos) {
			save(video);
		}
	}

	public void refreshAll(Playlist playlist, Callbacks callbacks) {
		SQLite.delete(Video.class).where(Video_Table.playlist_playlistId.eq(playlist.getPlaylistId()));

		new VideoListAsyncTask(
				playlist,
				youTube,
				getToken(sharedPreferences),
				callbacks
		).execute();
	}

	public String getToken(SharedPreferences sharedPreferences) {
		return sharedPreferences.getString(
				io.github.garywahaha.backgroundyoutubeplayer.auth.Constants.KEY_TOKEN,
				null
		);
	}

	private class VideoListAsyncTask extends AsyncTask<Void, Void, List<Video>> {

		private Playlist playlist;
		private YouTube youtube;
		private String oauthToken;
		private Callbacks callbacks;

		private Exception e;

		public VideoListAsyncTask(Playlist playlist, YouTube youtube, String oauthToken, Callbacks callbacks) {
			this.playlist = playlist;
			this.youtube = youtube;
			this.oauthToken = oauthToken;
			this.callbacks = callbacks;
		}

		@Override
		protected List<Video> doInBackground(Void... voids) {
			try {
				List<Video> target = new ArrayList<>();
				String nextPageToken = null;
				do {
					Log.d(LOG_TAG, "fetching videos");
					Log.d(LOG_TAG, "Page: " + nextPageToken);
					YouTube.PlaylistItems.List playlistItemsReq = youtube.playlistItems().list("snippet,contentDetails");
					playlistItemsReq.setOauthToken(oauthToken);
					playlistItemsReq.setPlaylistId(playlist.getPlaylistId());
					playlistItemsReq.setMaxResults(50L);
					if (nextPageToken != null) playlistItemsReq.setPageToken(nextPageToken);

					PlaylistItemListResponse resp = playlistItemsReq.execute();
					List<com.google.api.services.youtube.model.PlaylistItem> result = resp.getItems();

					if (result.isEmpty()) break;

					nextPageToken = resp.getNextPageToken();
					Log.d(LOG_TAG, "Next Page: " + nextPageToken);
					for (com.google.api.services.youtube.model.PlaylistItem x : result) {
						try {
							Video video = new Video();
							video.setTitle(x.getSnippet().getTitle());
							video.setThumbnailUrl(x.getSnippet().getThumbnails().getDefault().getUrl());
							video.setPlaylist(playlist);
							video.setVideoId(x.getContentDetails().getVideoId());
							target.add(video);
						}
						catch (Exception e) {
							Log.e(LOG_TAG, e.getMessage());
						}
					}
				} while (nextPageToken != null);
				return target;
			}
			catch (Exception e) {
				this.e = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Video> videos) {
			super.onPostExecute(videos);

			if (e == null) {
				saveAll(videos);
				callbacks.onSuccess();
			}
			else {
				callbacks.onError(e);
			}
		}
	}
}
