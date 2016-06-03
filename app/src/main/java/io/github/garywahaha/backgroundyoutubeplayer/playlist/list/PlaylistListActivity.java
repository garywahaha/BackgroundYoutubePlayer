package io.github.garywahaha.backgroundyoutubeplayer.playlist.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.github.garywahaha.backgroundyoutubeplayer.R;

/**
 * Created by Gary on 22/5/2016.
 */
public class PlaylistListActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playlists);

		getSupportFragmentManager().beginTransaction()
		                           .replace(R.id.activity_playlists_fragment_container, new PlaylistListFragment())
		                           .commit();
	}
}
