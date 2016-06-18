package io.github.garywahaha.backgroundyoutubeplayer.playlist.list;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import io.github.garywahaha.backgroundyoutubeplayer.playlist.Playlist;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.PlaylistModel;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Gary on 17/6/2016.
 */
public class PlaylistListPresenterTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	PlaylistModel playlistModel;

	@Mock
	PlaylistListView playlistListView;

	@InjectMocks
	PlaylistListPresenter playlistListPresenter;

	@Test
	public void testLoadPlaylistsNormal() throws Exception {
		ArrayList<Playlist> expected = new ArrayList<>();
		when(playlistModel.getAll()).thenReturn(expected);

		playlistListPresenter.attachView(playlistListView);
		playlistListPresenter.loadPlaylists(false);

		verify(playlistModel).getAll();
		verify(playlistListView).setData(expected);
		verify(playlistListView).showContent();
		verify(playlistListView, never()).showError(any(Throwable.class), anyBoolean());
		verifyNoMoreInteractions(playlistListView);
		verifyNoMoreInteractions(playlistModel);
	}

	@Test
	public void testRefreshAllSuccess() throws Exception {
		ArrayList<Playlist> expected = new ArrayList<>();
		when(playlistModel.getAll()).thenReturn(expected);

		playlistListPresenter.attachView(playlistListView);
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				PlaylistModel.Callbacks callbacks = (PlaylistModel.Callbacks) invocation.getArguments()[0];
				callbacks.onSuccess();
				return null;
			}
		}).when(playlistModel).refreshAll(any(PlaylistModel.Callbacks.class));

		playlistListPresenter.loadPlaylists(true);

		verify(playlistModel).refreshAll(any(PlaylistModel.Callbacks.class));
		verify(playlistModel).getAll();
		verify(playlistListView).setData(expected);
		verify(playlistListView).showContent();
		verify(playlistListView, never()).showError(any(Throwable.class), anyBoolean());
		verifyNoMoreInteractions(playlistListView);
		verifyNoMoreInteractions(playlistModel);
	}
}