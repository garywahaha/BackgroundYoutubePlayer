package io.github.garywahaha.backgroundyoutubeplayer.auth;

import android.accounts.Account;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

/**
 * Created by Gary on 20/5/2016.
 */
public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {
	LoginManager loginManager;

	private Account account;

	private String scope = "oauth2:https://www.googleapis.com/auth/youtube.readonly";

	public LoginPresenter(LoginManager loginManager) {
		this.loginManager = loginManager;
	}

	public void login() {
		if (account == null) {
			getView().showLoginForm();
		}
		else {
			loginManager.execute(account, new LoginManager.Callbacks() {
				@Override
				public void onLoginSuccess() {
					getView().loginSuccess();
				}

				@Override
				public void onLoginError(Exception e) {
					getView().showError(e);
				}
			});
		}
	}

	public void handleAccount(Account account) {
		this.account = account;
		login();
	}
}
