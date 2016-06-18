package io.github.garywahaha.backgroundyoutubeplayer.playlist.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.garywahaha.backgroundyoutubeplayer.R;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist;

/**
 * Created by Gary on 22/5/2016.
 */
public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.PlaylistViewHolder> {

	public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.playlist_title)
		TextView titleView;

		@BindView(R.id.playlist_thumbnail)
		ImageView thumbnailView;

		@BindView(R.id.playlist_video_num)
		TextView itemCountView;

		public PlaylistViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

	public interface PlaylistsAdapterHandler {
		void onPlaylistClicked(Playlist playlist);
	}

	private final List<Playlist> playlists;

	private final PlaylistsAdapterHandler handler;

	public PlaylistListAdapter(PlaylistsAdapterHandler handler) {
		this.playlists = new ArrayList<>();
		this.handler = handler;
	}

	@Override
	public PlaylistViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.playlist, viewGroup, false);
		return new PlaylistViewHolder(view);
	}

	@Override
	public void onBindViewHolder(PlaylistViewHolder playlistViewHolder, int i) {
		final Playlist target = playlists.get(i);
		playlistViewHolder.titleView.setText(target.getTitle());
		playlistViewHolder.titleView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				handler.onPlaylistClicked(target);
			}
		});
		playlistViewHolder.itemCountView.setText(target.getItemCount().toString());

		Glide.with(playlistViewHolder.thumbnailView.getContext())
				.load(target.getThumbnailUrl())
				.into(playlistViewHolder.thumbnailView);
	}

	@Override
	public int getItemCount() {
		return playlists.size();
	}

	public void setPlaylists(List<Playlist> playlists) {
		this.playlists.clear();
		this.playlists.addAll(playlists);
	}
}
