package io.github.garywahaha.backgroundyoutubeplayer.playlist;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gary on 4/6/2016.
 */
public class PlaylistModel {
	private YouTube youTube;
	private SharedPreferences sharedPreferences;

	public PlaylistModel(YouTube youTube, SharedPreferences sharedPreferences) {
		this.youTube = youTube;
		this.sharedPreferences = sharedPreferences;
	}

	public interface Callbacks {
		void onSuccess();
		void onError(Exception e);
	}

	public Playlist getPlaylist(String id) {
		return SQLite.select()
				.from(Playlist.class)
				.where(Playlist_Table.playlistId.eq(id))
				.querySingle();
	}

	public List<Playlist> getAll() {
		return SQLite.select()
				.from(Playlist.class)
				.queryList();
	}

	public void refreshAll(Callbacks callbacks) {
		SQLite.delete(Playlist.class);

		new PlaylistListAsyncTask(youTube, getToken(sharedPreferences), callbacks).execute();
	}

	public void save(Playlist playlist) {
		playlist.save();
	}

	public void saveAll(List<Playlist> playlists) {
		for (Playlist playlist: playlists) {
			save(playlist);
		}
	}


	public String getToken(SharedPreferences sharedPreferences) {
		return sharedPreferences.getString(
				io.github.garywahaha.backgroundyoutubeplayer.auth.Constants.KEY_TOKEN,
				null
		);
	}


	private class PlaylistListAsyncTask extends AsyncTask<Void, Void, List<Playlist>> {

		private YouTube youtube;
		private String oauthToken;
		private Callbacks callbacks;

		private Exception e;

		public PlaylistListAsyncTask(YouTube youtube, String oauthToken, Callbacks callbacks) {
			this.youtube = youtube;
			this.oauthToken = oauthToken;
			this.callbacks = callbacks;
		}

		@Override
		protected List<Playlist> doInBackground(Void... voids) {
			try {
				YouTube.Playlists.List playlistsReq = youtube.playlists().list("snippet,contentDetails");
				playlistsReq.setOauthToken(oauthToken);
				playlistsReq.setMine(true);
				playlistsReq.setMaxResults(50L);
				PlaylistListResponse resp = playlistsReq.execute();
				List<com.google.api.services.youtube.model.Playlist> result = resp.getItems();
				List<Playlist> target = new ArrayList<>();
				for (com.google.api.services.youtube.model.Playlist x : result) {
					Playlist playlist = new Playlist();
					playlist.setTitle(x.getSnippet().getTitle());
					playlist.setPlaylistId(x.getId());
					playlist.setThumbnailUrl(x.getSnippet().getThumbnails().getDefault().getUrl());
					playlist.setItemCount(x.getContentDetails().getItemCount());
					target.add(playlist);
				}
				return target;
			}
			catch (Exception e) {
				this.e = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Playlist> playlists) {
			super.onPostExecute(playlists);

			if (e == null) {
				callbacks.onSuccess();
				saveAll(playlists);
			}
			else {
				callbacks.onError(e);
			}
		}
	}
}
