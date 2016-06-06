package io.github.garywahaha.backgroundyoutubeplayer.video.list;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import javax.inject.Inject;

import io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist_Table;
import io.github.garywahaha.backgroundyoutubeplayer.video.Video;
import io.github.garywahaha.backgroundyoutubeplayer.video.VideoComponent;
import io.github.garywahaha.backgroundyoutubeplayer.video.VideoModel;

/**
 * Created by Gary on 25/5/2016.
 */
public class VideoListPresenter extends MvpNullObjectBasePresenter<VideoListView> {
	private final String rawPlaylistID;

	@Inject
	VideoModel videoModel;

	private Playlist playlist;

	public VideoListPresenter(VideoComponent videoComponent, String rawPlaylistID) {
		this.rawPlaylistID = rawPlaylistID;
		videoComponent.inject(this);

		// TODO: Get playlist from playlist model
		this.playlist = SQLite.select()
				.from(Playlist.class)
				.where(Playlist_Table.playlistId.eq(rawPlaylistID))
				.querySingle();
	}

	public void loadVideos(boolean pullToRefresh) {
		if (pullToRefresh) {
			videoModel.refreshAll(playlist, new VideoModel.Callbacks() {
				@Override
				public void onSuccess() {
					loadVideos(false);
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
