package io.github.garywahaha.backgroundyoutubeplayer.auth;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthUtil;

/**
 * Created by Gary on 4/6/2016.
 */
public class LoginManager {
	private Context context;
	private SharedPreferences sharedPreferences;
	private String scope;

	public LoginManager(Context context, SharedPreferences sharedPreferences, String scope) {
		this.context = context;
		this.sharedPreferences = sharedPreferences;
		this.scope = scope;
	}

	public interface Callbacks {
		void onLoginSuccess();
		void onLoginError(Exception e);
	}

	public void execute(Account account, Callbacks callbacks) {
		LoginAsyncTask loginAsyncTask = new LoginAsyncTask(
				context,
		        sharedPreferences,
		        scope,
		        callbacks
		);
		loginAsyncTask.execute(account);
	}

	private class LoginAsyncTask extends AsyncTask<Account, Void, String> {
		Context context;
		SharedPreferences sharedPreferences;
		String scope;
		Callbacks callbacks;

		Exception exception;

		public LoginAsyncTask(Context context, SharedPreferences sharedPreferences, String scope, Callbacks callbacks) {
			this.context = context;
			this.sharedPreferences = sharedPreferences;
			this.scope = scope;
			this.callbacks = callbacks;
		}

		@Override
		protected String doInBackground(Account... accounts) {
			try {
				Account account = accounts[0];
				return GoogleAuthUtil.getToken(context, account, scope);
			} catch (Exception e) {
				exception = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String token) {
			super.onPostExecute(token);
			if (exception == null) {
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString(io.github.garywahaha.backgroundyoutubeplayer.auth.Constants.KEY_TOKEN, token);
				editor.commit();
				callbacks.onLoginSuccess();
			}
			else {
				callbacks.onLoginError(exception);
			}
		}
	}
}