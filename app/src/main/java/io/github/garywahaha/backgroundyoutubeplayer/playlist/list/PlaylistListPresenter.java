package io.github.garywahaha.backgroundyoutubeplayer.playlist.list;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.PlaylistModel;

/**
 * Created by Gary on 22/5/2016.
 */
public class PlaylistListPresenter extends MvpNullObjectBasePresenter<PlaylistListView> {

	PlaylistModel playlistModel;

	public PlaylistListPresenter(PlaylistModel playlistModel) {
		this.playlistModel = playlistModel;
	}

	public void loadPlaylists(boolean pullToRefresh) {
		if (pullToRefresh) {
			playlistModel.refreshAll(new PlaylistModel.Callbacks() {
				@Override
				public void onSuccess() {
					loadPlaylists(false);
				}

				@Override
				public void onError(Exception e) {
					System.out.println(e.getMessage());
				}
			});
		}
		else {
			List<Playlist> playlists = playlistModel.getAll();
			getView().setData(playlists);
			getView().showContent();
		}
	}
}
