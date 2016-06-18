package io.github.garywahaha.backgroundyoutubeplayer.video.list;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import javax.inject.Inject;

import io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist_Table;
import io.github.garywahaha.backgroundyoutubeplayer.video.Video;
import io.github.garywahaha.backgroundyoutubeplayer.video.VideoModel;

/**
 * Created by Gary on 25/5/2016.
 */
public class VideoListPresenter extends MvpNullObjectBasePresenter<VideoListView> {
	@Inject
	VideoModel videoModel;

	public VideoListPresenter(VideoModel videoModel) {
		this.videoModel = videoModel;
	}

	public void loadVideos(final String playlistRawID, boolean pullToRefresh) {
		// TODO: fetch from playlist model
		Playlist playlist = SQLite.select()
		                          .from(Playlist.class)
		                          .where(Playlist_Table.playlistId.eq(playlistRawID))
		                          .querySingle();

		if (pullToRefresh) {
			videoModel.refreshAll(playlist, new VideoModel.Callbacks() {
				@Override
				public void onSuccess() {
					loadVideos(playlistRawID, false);
				}

				@Override
				public void onError(Exception e) {
					System.out.println(e.getMessage());
				}
			});
		}
		else {
			List<Video> videos = videoModel.getPlaylistItems(playlist);
			getView().setData(videos);
			getView().showContent();
		}
	}
}
