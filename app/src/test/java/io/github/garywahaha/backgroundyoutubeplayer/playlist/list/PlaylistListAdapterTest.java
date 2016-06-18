package io.github.garywahaha.backgroundyoutubeplayer.playlist.list;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import io.github.garywahaha.backgroundyoutubeplayer.BuildConfig;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Created by Gary on 18/6/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class, sdk = 21)
public class PlaylistListAdapterTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	PlaylistListAdapter.PlaylistsAdapterHandler playlistsAdapterHandler;

	@InjectMocks
	PlaylistListAdapter playlistListAdapter;

	@Test
	public void testSetPlaylists() throws Exception {
		Playlist playlist1 = new Playlist();
		playlist1.setTitle("test1");
		Playlist playlist2 = new Playlist();
		playlist2.setTitle("test2");
		List<Playlist> expected = Arrays.asList(playlist1, playlist2);

		playlistListAdapter.setPlaylists(expected);

		assertThat(playlistListAdapter.getItemCount()).isEqualTo(expected.size());

		Playlist playlist3 = new Playlist();
		playlist3.setTitle("test3");
		List<Playlist> newExpected = Arrays.asList(playlist3);

		playlistListAdapter.setPlaylists(newExpected);

		assertThat(playlistListAdapter.getItemCount()).isEqualTo(newExpected.size());
	}

	@Test
	public void testBindViewHolder() throws Exception {
		Context context = RuntimeEnvironment.application;
		Playlist playlist1 = new Playlist();
		playlist1.setTitle("test1");
		playlist1.setItemCount(10L);
		List<Playlist> expected = Arrays.asList(playlist1);

		RecyclerView recyclerView = new RecyclerView(context);
		recyclerView.setLayoutManager(new LinearLayoutManager(context));

		PlaylistListAdapter.PlaylistViewHolder viewHolder = playlistListAdapter.onCreateViewHolder(
				recyclerView, 0
		);
		playlistListAdapter.setPlaylists(expected);

		playlistListAdapter.onBindViewHolder(viewHolder, 0);

		assertThat(viewHolder.titleView.getText()).isEqualTo(playlist1.getTitle());
		assertThat(viewHolder.itemCountView.getText()).isEqualTo("10");

		viewHolder.titleView.performClick();
		verify(playlistsAdapterHandler).onPlaylistClicked(playlist1);
	}
}