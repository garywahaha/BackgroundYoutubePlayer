package io.github.garywahaha.backgroundyoutubeplayer.playlist.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.garywahaha.backgroundyoutubeplayer.R;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist;
import io.github.garywahaha.backgroundyoutubeplayer.video.list.VideoListActivity;

/**
 * Created by Gary on 22/5/2016.
 */
public class PlaylistListFragment
		extends MvpLceFragment<SwipeRefreshLayout, List<Playlist>, PlaylistListView, PlaylistListPresenter>
		implements PlaylistListView, PlaylistListAdapter.PlaylistsAdapterHandler {

	@BindView(R.id.fragment_playlists_recycler_view)
	RecyclerView recyclerView;

	PlaylistListAdapter playlistListAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_playlists, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);

		playlistListAdapter = new PlaylistListAdapter(getActivity(), this);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setAdapter(playlistListAdapter);
		loadData(false);
	}

	@Override
	protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
		return null;
	}

	@Override
	public PlaylistListPresenter createPresenter() {
		return new PlaylistListPresenter();
	}

	@Override
	public void setData(List<Playlist> data) {
		playlistListAdapter.setPlaylists(data);
		playlistListAdapter.notifyDataSetChanged();
		contentView.setRefreshing(false);
	}

	@Override
	public void loadData(boolean pullToRefresh) {
		presenter.loadPlaylists(pullToRefresh);
	}

	@Override
	public void onPlaylistClicked(Playlist playlist) {
		Intent intent = VideoListActivity.IntentHelper.getIntent(this.getActivity(), playlist.getPlaylist_id());
		startActivity(intent);
	}
}