package io.github.garywahaha.backgroundyoutubeplayer.video;

import dagger.Component;
import io.github.garywahaha.backgroundyoutubeplayer.AppComponent;
import io.github.garywahaha.backgroundyoutubeplayer.video.list.VideoListPresenter;

/**
 * Created by Gary on 5/6/2016.
 */
@VideoScope
@Component(
		dependencies = {AppComponent.class},
		modules = {VideoModule.class}
)
public interface VideoComponent {
	VideoModel VIDEO_MODEL();

	void inject(VideoListPresenter videoListPresenter);
}
