package io.github.garywahaha.backgroundyoutubeplayer.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.garywahaha.backgroundyoutubeplayer.App;
import io.github.garywahaha.backgroundyoutubeplayer.R;
import io.github.garywahaha.backgroundyoutubeplayer.base.BaseFragment;
import io.github.garywahaha.backgroundyoutubeplayer.playlist.list.PlaylistListActivity;

/**
 * Created by Gary on 20/5/2016.
 */
public class LoginFragment
		extends BaseFragment<LoginView, LoginPresenter>
		implements LoginView {

	static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
	static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;
	static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1002;

	@BindView(R.id.fragment_login_signin_button)
	SignInButton signinButton;

	@Inject
	LoginPresenter loginPresenter;

	protected int getLayoutRes() {
		return R.layout.fragment_login;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getApp().getAppComponent().inject(this);
	}

	@Override
	public void showLoginForm() {
		// TODO: Move constants to another file
		String[] accountTypes = new String[]{"com.google"};
		Intent intent = AccountPicker.newChooseAccountIntent(null, null,
				accountTypes, false, null, null, null, null);
		startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
	}

	@Override
	public void showError(Exception e) {
		if (e instanceof GooglePlayServicesAvailabilityException) {
			// The Google Play services APK is old, disabled, or not present.
			// Show a dialog created by Google Play services that allows
			// the user to update the APK
			int statusCode = ((GooglePlayServicesAvailabilityException) e)
					.getConnectionStatusCode();
			Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(
					this.getActivity(),
			        statusCode,
			        REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR
			);
			dialog.show();
		} else if (e instanceof UserRecoverableAuthException) {
			// Unable to authenticate, such as when the user has not yet granted
			// the app access to the account, but the user can fix this.
			// Forward the user to an activity in Google Play services.
			Intent intent = ((UserRecoverableAuthException) e).getIntent();
			startActivityForResult(
					intent,
			        REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR
			);
		}
		else {
			Toast.makeText(this.getContext(), "Unexpected Error", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void showLoading() {

	}

	@Override
	public void loginSuccess() {
		Toast.makeText(this.getContext(), "Success!", Toast.LENGTH_SHORT).show();
		startActivity(new Intent(this.getActivity(), PlaylistListActivity.class));
	}

	@NonNull
	@Override
	public LoginPresenter createPresenter() {
		return loginPresenter;
	}

	@OnClick(R.id.fragment_login_signin_button)
	public void onLoginClicked() {
		loginPresenter.login();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
			if (resultCode == Activity.RESULT_OK) {
				String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				String type = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
				Account account = new Account(email, type);

				loginPresenter.handleAccount(account);
			}
		} else if ((requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR ||
				requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
				&& resultCode == Activity.RESULT_OK) {
			loginPresenter.login();
		}
	}

	protected App getApp() {
		return (App) getActivity().getApplicationContext();
	}
}
