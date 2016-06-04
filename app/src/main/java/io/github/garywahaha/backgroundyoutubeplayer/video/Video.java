package io.github.garywahaha.backgroundyoutubeplayer.video;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import io.github.garywahaha.backgroundyoutubeplayer.common.AppDatabase;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist;

/**
 * Created by Gary on 25/5/2016.
 */
@Table(database = AppDatabase.class)
public class Video extends BaseModel {
	@PrimaryKey
	private String videoId;

	@ForeignKey
	@PrimaryKey
	private Playlist playlist;

	@Column
	private String title;

	@Column
	private String thumbnailUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}
}
