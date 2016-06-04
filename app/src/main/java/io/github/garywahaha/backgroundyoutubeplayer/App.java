package io.github.garywahaha.backgroundyoutubeplayer;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Gary on 3/6/2016.
 */
public class App extends Application {

	private AppComponent appComponent;

	@Override
	public void onCreate() {
		super.onCreate();
		FlowManager.init(new FlowConfig.Builder(this).build());
		appComponent = DaggerAppComponent.builder()
		                                 .appModule(new AppModule(this))
		                                 .build();
	}

	public AppComponent getAppComponent() {
		return appComponent;
	}
}
