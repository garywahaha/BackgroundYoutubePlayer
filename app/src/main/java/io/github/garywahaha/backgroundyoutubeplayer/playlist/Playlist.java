package io.github.garywahaha.backgroundyoutubeplayer.playlist;

/**
 * Created by Gary on 22/5/2016.
 */
public class Playlist {
	private String playlist_id;

	private String title;

	private String thumbnailUrl;

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

	public String getPlaylist_id() {
		return playlist_id;
	}

	public void setPlaylist_id(String playlist_id) {
		this.playlist_id = playlist_id;
	}

	public Long getItemCount() {
		return itemCount;
	}

	public void setItemCount(Long itemCount) {
		this.itemCount = itemCount;
	}
}
