package io.github.garywahaha.backgroundyoutubeplayer.video;

import android.content.SharedPreferences;

import com.google.api.services.youtube.YouTube;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Gary on 5/6/2016.
 */
@Module
public class VideoModule {
	@VideoScope
	@Provides
	public VideoModel providesVideoModel(YouTube youTube, SharedPreferences sharedPreferences) {
		return new VideoModel(youTube, sharedPreferences);
	}
}
