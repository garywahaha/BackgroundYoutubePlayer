package io.github.garywahaha.backgroundyoutubeplayer.playlist.list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.garywahaha.backgroundyoutubeplayer.common.Constants;

/**
 * Created by Gary on 22/5/2016.
 */
public class PlaylistListPresenter extends MvpNullObjectBasePresenter<PlaylistListView> {
	public void loadPlaylists(boolean pullToRefresh) {
		new YouTubePlaylistsAsyncTask(((PlaylistListFragment)getView()).getActivity()).execute();
	}

	public class YouTubePlaylistsAsyncTask extends AsyncTask<Void, Void, List<io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist>> {
		Context context;

		public YouTubePlaylistsAsyncTask(Context context) {
			this.context = context;
		}

		@Override
		protected List<io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist> doInBackground(Void... voids) {
			YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
				@Override
				public void initialize(HttpRequest request) throws IOException {

				}
			}).setApplicationName("BackgroundPlayer").build();

			SharedPreferences sharedPreferences = context.getSharedPreferences(
					Constants.PREFERENCE_FILE_KEY,
					Context.MODE_PRIVATE
			);

			String token = sharedPreferences.getString(
					io.github.garywahaha.backgroundyoutubeplayer.auth.Constants.KEY_TOKEN,
					null
			);

			try {
				YouTube.Playlists.List playlistsReq = youtube.playlists().list("snippet,contentDetails");
				playlistsReq.setOauthToken(token);
				playlistsReq.setMine(true);
				playlistsReq.setMaxResults(50L);
				PlaylistListResponse resp = playlistsReq.execute();
				List<com.google.api.services.youtube.model.Playlist> result = resp.getItems();
				List<io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist> target = new ArrayList<>();
				for (com.google.api.services.youtube.model.Playlist x : result) {
					io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist playlist = new io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist();
					playlist.setTitle(x.getSnippet().getTitle());
					playlist.setPlaylist_id(x.getId());
					playlist.setThumbnailUrl(x.getSnippet().getThumbnails().getDefault().getUrl());
					playlist.setItemCount(x.getContentDetails().getItemCount());
					target.add(playlist);
				}
				return target;
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist> playlists) {
			super.onPostExecute(playlists);

			getView().setData(playlists);
			getView().showContent();
		}
	}
}