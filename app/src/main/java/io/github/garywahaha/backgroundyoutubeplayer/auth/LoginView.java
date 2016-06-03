package io.github.garywahaha.backgroundyoutubeplayer.auth;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Gary on 20/5/2016.
 */
public interface LoginView extends MvpView {
	public void showLoginForm();

	public void showError(Exception e);

	public void showLoading();

	public void loginSuccess();
}
