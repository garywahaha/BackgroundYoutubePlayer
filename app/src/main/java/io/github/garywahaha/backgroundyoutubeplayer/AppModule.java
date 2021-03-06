package io.github.garywahaha.backgroundyoutubeplayer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.garywahaha.backgroundyoutubeplayer.common.AppDatabase;
import io.github.garywahaha.backgroundyoutubeplayer.common.Constants;

/**
 * Created by Gary on 3/6/2016.
 */
@Module
public class AppModule {
	private Application application;

	public AppModule(Application application) {
		this.application = application;
	}

	@Provides
	@Singleton
	public Application providesApplication() {
		return application;
	}

	@Provides
	@Singleton
	public Context providesContext() {
		return application;
	}

	@Provides
	@Singleton
	public SharedPreferences providesSharedPreferences() {
		return application.getSharedPreferences(
				Constants.PREFERENCE_FILE_KEY,
				Context.MODE_PRIVATE
		);
	}

	@Provides
	@Singleton
	@Named("scope")
	public String providesScope() {
		// TODO: Move to Constants
		return "oauth2:https://www.googleapis.com/auth/youtube.readonly";
	}

	@Provides
	@Singleton
	public DatabaseWrapper providesDatabase() {
		return FlowManager.getWritableDatabase(AppDatabase.class);
	}


	@Provides
	@Singleton
	public YouTube providesYouTube() {
		return new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) throws IOException {

			}
		}).setApplicationName("BackgroundPlayer").build();
	}
}
