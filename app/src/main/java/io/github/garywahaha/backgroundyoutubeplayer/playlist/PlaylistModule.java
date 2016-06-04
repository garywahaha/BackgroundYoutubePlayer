package io.github.garywahaha.backgroundyoutubeplayer.playlist;

import android.content.SharedPreferences;

import com.google.api.services.youtube.YouTube;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Gary on 4/6/2016.
 */
@Module
public class PlaylistModule {
	@PlaylistScope
	@Provides
	public PlaylistModel providesPlaylistModel(YouTube youTube, SharedPreferences sharedPreferences) {
		return new PlaylistModel(youTube, sharedPreferences);
	}
}
