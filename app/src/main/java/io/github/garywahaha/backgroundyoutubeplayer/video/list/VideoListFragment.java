package io.github.garywahaha.backgroundyoutubeplayer.video.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.garywahaha.backgroundyoutubeplayer.service.Constants;
import io.github.garywahaha.backgroundyoutubeplayer.service.NotificationService;
import io.github.garywahaha.backgroundyoutubeplayer.R;
import io.github.garywahaha.backgroundyoutubeplayer.video.Video;

/**
 * Created by Gary on 25/5/2016.
 */
public class VideoListFragment
		extends MvpLceFragment<SwipeRefreshLayout, List<Video>, VideoListView, VideoListPresenter>
		implements VideoListView {

	public static final String PLAYLIST_RAW_ID_KEY = "VideoListFragment.PLAYLIST_RAW_ID_KEY";

	@BindView(R.id.fragment_video_list_recycler_view)
	RecyclerView recyclerView;

	@BindView(R.id.fragment_video_list_play_button)
	ImageButton playButton;

	String playlistRawID;
	VideoListAdapter videoListAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.playlistRawID = this.getArguments().getString(PLAYLIST_RAW_ID_KEY);
		System.out.println(playlistRawID);

		return inflater.inflate(R.layout.fragment_video_list, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);

		videoListAdapter = new VideoListAdapter(getActivity());
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setAdapter(videoListAdapter);
		loadData(false);
	}

	@Override
	protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
		return null;
	}

	@Override
	public VideoListPresenter createPresenter() {
		return new VideoListPresenter(playlistRawID);
	}

	@Override
	public void setData(List<Video> data) {
		videoListAdapter.setVideoList(data);
		videoListAdapter.notifyDataSetChanged();
	}

	@Override
	public void loadData(boolean pullToRefresh) {
		presenter.loadVideos(pullToRefresh);
	}

	@OnClick(R.id.fragment_video_list_play_button)
	public void onPlayButtonClicked() {
		Intent serviceIntent = new Intent(this.getActivity(), NotificationService.class);
		serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
		getActivity().startService(serviceIntent);
	}
}
