package io.github.garywahaha.backgroundyoutubeplayer.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import io.github.garywahaha.backgroundyoutubeplayer.R;

/**
 * Created by Gary on 24/4/2016.
 */
public class Constants {
	public interface ACTION {
		public static String MAIN_ACTION = "io.github.garywahaha.action.main";
		public static String INIT_ACTION = "io.github.garywahaha.action.init";
		public static String PREV_ACTION = "io.github.garywahaha.action.prev";
		public static String PLAY_ACTION = "io.github.garywahaha.action.play";
		public static String NEXT_ACTION = "io.github.garywahaha.action.next";
		public static String STARTFOREGROUND_ACTION = "io.github.garywahaha.action.startforeground";
		public static String STOPFOREGROUND_ACTION = "io.github.garywahaha.action.stopforeground";
	}

	public interface NOTIFICATION_ID {
		public static int FOREGROUND_SERVICE = 101;
	}

	public static Bitmap getDefaultAlbumArt(Context context) {
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		try {
			bm = BitmapFactory.decodeResource(
					context.getResources(),
					R.drawable.default_album_art,
					options
			);
		}
		catch (Error e) {

		}
		catch (Exception e) {

		}
		return bm;
	}
}
