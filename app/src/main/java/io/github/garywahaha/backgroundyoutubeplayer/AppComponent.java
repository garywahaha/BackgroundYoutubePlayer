package io.github.garywahaha.backgroundyoutubeplayer;

import android.app.Application;

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

	void inject(LoginPresenter loginPresenter);
}
