package io.github.garywahaha.backgroundyoutubeplayer.video.list;

import android.content.Context;
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
import io.github.garywahaha.backgroundyoutubeplayer.video.Video;

/**
 * Created by Gary on 28/5/2016.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder> {

	public static class VideoListViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.video_title)
		TextView titleView;

		@BindView(R.id.video_thumbnail)
		ImageView thumbnailView;

		public VideoListViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

	private final Context context;
	private final LayoutInflater layoutInflater;
	private final ArrayList<Video> videoList;

	public VideoListAdapter(Context context) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.videoList = new ArrayList<>();
	}

	@Override
	public VideoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new VideoListViewHolder(layoutInflater.inflate(R.layout.video, parent, false));
	}

	@Override
	public void onBindViewHolder(VideoListViewHolder holder, int position) {
		final Video target = videoList.get(position);
		holder.titleView.setText(target.getTitle());

		Glide.with(context)
				.load(target.getThumbnailUrl())
				.into(holder.thumbnailView);
	}

	@Override
	public int getItemCount() {
		return videoList.size();
	}

	public void setVideoList(List<Video> videoList) {
		this.videoList.clear();
		this.videoList.addAll(videoList);
	}

	public ArrayList<Video> getVideoList() {
		return this.videoList;
	}
}
