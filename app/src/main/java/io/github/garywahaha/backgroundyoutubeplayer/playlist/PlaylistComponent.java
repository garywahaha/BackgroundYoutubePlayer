package io.github.garywahaha.backgroundyoutubeplayer.playlist;

import dagger.Component;
import io.github.garywahaha.backgroundyoutubeplayer.AppComponent;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.list.PlaylistListFragment;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.list.PlaylistListPresenter;

/**
 * Created by Gary on 4/6/2016.
 */
@PlaylistScope
@Component(
		dependencies = {AppComponent.class},
		modules = {PlaylistModule.class}
)
public interface PlaylistComponent {
	PlaylistListPresenter PLAYLIST_LIST_PRESENTER();

	void inject(PlaylistListFragment playlistListFragment);
}
