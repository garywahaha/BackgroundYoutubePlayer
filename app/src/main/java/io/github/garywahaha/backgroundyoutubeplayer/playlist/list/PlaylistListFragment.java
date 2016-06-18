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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.garywahaha.backgroundyoutubeplayer.App;
import io.github.garywahaha.backgroundyoutubeplayer.R;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.DaggerPlaylistComponent;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.PlaylistComponent;
import io.github.garywahaha.backgroundyoutubeplayer.video.list.VideoListActivity;

/**
 * Created by Gary on 22/5/2016.
 */
public class PlaylistListFragment
		extends MvpLceFragment<SwipeRefreshLayout, List<Playlist>, PlaylistListView, PlaylistListPresenter>
		implements PlaylistListView, PlaylistListAdapter.PlaylistsAdapterHandler {

	@BindView(R.id.fragment_playlists_recycler_view)
	RecyclerView recyclerView;

	@Inject
	PlaylistListPresenter playlistListPresenter;

	private PlaylistComponent playlistComponent;

	PlaylistListAdapter playlistListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		playlistComponent = DaggerPlaylistComponent.builder()
		                                           .appComponent(getApp().getAppComponent())
		                                           .build();
		playlistComponent.inject(this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_playlists, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);

		playlistListAdapter = new PlaylistListAdapter(this);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setAdapter(playlistListAdapter);

		contentView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				loadData(true);
			}
		});

		loadData(false);
	}

	@Override
	protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
		return null;
	}

	@Override
	public PlaylistListPresenter createPresenter() {
		return playlistListPresenter;
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
		Intent intent = VideoListActivity.IntentHelper.getIntent(this.getActivity(), playlist.getPlaylistId());
		startActivity(intent);
	}

	protected App getApp() {
		return (App) getActivity().getApplicationContext();
	}
}
