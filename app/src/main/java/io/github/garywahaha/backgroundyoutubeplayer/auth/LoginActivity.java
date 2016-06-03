package io.github.garywahaha.backgroundyoutubeplayer.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.github.garywahaha.backgroundyoutubeplayer.R;

/**
 * Created by Gary on 20/5/2016.
 */
public class LoginActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.activity_login_fragment_container, new LoginFragment())
				.commit();
	}
}
