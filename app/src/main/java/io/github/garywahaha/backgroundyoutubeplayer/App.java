package io.github.garywahaha.backgroundyoutubeplayer;

import android.app.Application;

/**
 * Created by Gary on 3/6/2016.
 */
public class App extends Application {

	private AppComponent appComponent;

	@Override
	public void onCreate() {
		super.onCreate();

		appComponent = DaggerAppComponent.builder()
		                                 .appModule(new AppModule(this))
		                                 .build();
	}

	public AppComponent getAppComponent() {
		return appComponent;
	}
}
