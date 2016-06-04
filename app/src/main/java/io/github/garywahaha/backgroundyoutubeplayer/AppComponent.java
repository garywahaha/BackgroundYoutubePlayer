package io.github.garywahaha.backgroundyoutubeplayer;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.api.services.youtube.YouTube;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import javax.inject.Singleton;

import dagger.Component;
import io.github.garywahaha.backgroundyoutubeplayer.auth.AuthModule;
import io.github.garywahaha.backgroundyoutubeplayer.auth.LoginPresenter;

/**
 * Created by Gary on 3/6/2016.
 */
@Singleton
@Component(
		modules = {AppModule.class, AuthModule.class}
)
public interface AppComponent {
	Application application();

	DatabaseWrapper DATABASE_WRAPPER();

	YouTube YOU_TUBE();

	SharedPreferences SHARED_PREFERENCES();

	void inject(LoginPresenter loginPresenter);
}
