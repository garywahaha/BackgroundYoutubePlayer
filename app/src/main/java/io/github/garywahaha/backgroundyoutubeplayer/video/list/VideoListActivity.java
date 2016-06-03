package io.github.garywahaha.backgroundyoutubeplayer.video.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.github.garywahaha.backgroundyoutubeplayer.R;

/**
 * Created by Gary on 25/5/2016.
 */
public class VideoListActivity extends AppCompatActivity {
	public static final class IntentHelper {
		public static final String PLAYLIST_RAW_ID_KEY = "PLAYLIST_RAW_ID_KEY";
		public static Intent getIntent(Context context, String rawID) {
			Intent intent = new Intent(context, VideoListActivity.class);
			intent.putExtra(PLAYLIST_RAW_ID_KEY, rawID);
			return intent;
		}
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_list);

		String playlist_raw_id = getIntent().getStringExtra(IntentHelper.PLAYLIST_RAW_ID_KEY);

		VideoListFragment fragment = new VideoListFragment();
		Bundle args = new Bundle();
		args.putString(VideoListFragment.PLAYLIST_RAW_ID_KEY, playlist_raw_id);
		fragment.setArguments(args);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.activity_video_list_fragment_container, fragment)
				.commit();
	}
}
