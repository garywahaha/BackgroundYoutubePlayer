package io.github.garywahaha.backgroundyoutubeplayer.auth;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Gary on 3/6/2016.
 */
@Module
public class AuthModule {
	@Provides
	@Singleton
	LoginManager providesLoginManager(Context context,
	                                  SharedPreferences sharedPreferences,
	                                  @Named("scope") String scope) {
		return new LoginManager(context, sharedPreferences, scope);
	}
}
