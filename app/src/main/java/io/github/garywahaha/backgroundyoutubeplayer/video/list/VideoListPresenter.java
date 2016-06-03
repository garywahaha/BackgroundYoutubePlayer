package io.github.garywahaha.backgroundyoutubeplayer.video.list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.garywahaha.backgroundyoutubeplayer.common.Constants;
import io.github.garywahaha.backgroundyoutubeplayer.video.Video;

/**
 * Created by Gary on 25/5/2016.
 */
public class VideoListPresenter extends MvpNullObjectBasePresenter<VideoListView> {
	private final String rawPlaylistID;

	public VideoListPresenter(String rawPlaylistID) {
		this.rawPlaylistID = rawPlaylistID;
	}

	public void loadVideos(boolean pullToRefresh) {
		new YouTubeVideoAsyncTask(
				((VideoListFragment)getView()).getActivity(),
		        this.rawPlaylistID
		).execute();
	}

	public class YouTubeVideoAsyncTask extends AsyncTask<Void, Void, List<Video>> {
		Context context;
		String rawPlaylistID;

		public YouTubeVideoAsyncTask(Context context, String rawPlaylistID) {
			this.context = context;
			this.rawPlaylistID = rawPlaylistID;
		}

		@Override
		protected List<Video> doInBackground(Void... voids) {
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
				YouTube.PlaylistItems.List playlistItemReq = youtube.playlistItems().list("snippet");
				playlistItemReq.setOauthToken(token);
				playlistItemReq.setPlaylistId(rawPlaylistID);
				playlistItemReq.setMaxResults(50L);
				PlaylistItemListResponse resp = playlistItemReq.execute();
				List<PlaylistItem> result = resp.getItems();
				List<Video> target = new ArrayList<>();
				for (PlaylistItem x : result) {
					Video video = new Video();
					video.setTitle(x.getSnippet().getTitle());
					video.setThumbnailUrl(x.getSnippet().getThumbnails().getDefault().getUrl());
					target.add(video);
				}
				return target;
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Video> videos) {
			super.onPostExecute(videos);

			getView().setData(videos);
			getView().showContent();
		}
	}
}
