package io.github.garywahaha.backgroundyoutubeplayer.playlist;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import io.github.garywahaha.backgroundyoutubeplayer.common.AppDatabase;

/**
 * Created by Gary on 22/5/2016.
 */
@Table(database = AppDatabase.class)
public class Playlist extends BaseModel {
	@PrimaryKey
	private String playlistId;

	@Column
	private String title;

	@Column
	private String thumbnailUrl;

	@Column
	private Long itemCount;

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(String playlistId) {
		this.playlistId = playlistId;
	}

	public Long getItemCount() {
		return itemCount;
	}

	public void setItemCount(Long itemCount) {
		this.itemCount = itemCount;
	}
}
