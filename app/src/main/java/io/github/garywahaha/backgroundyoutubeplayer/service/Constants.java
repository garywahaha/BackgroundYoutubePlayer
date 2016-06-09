package io.github.garywahaha.backgroundyoutubeplayer.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import io.github.garywahaha.backgroundyoutubeplayer.R;

/**
 * Created by Gary on 24/4/2016.
 */
public class Constants {
	public static final class ACTION {
		public static final String MAIN_ACTION = "io.github.garywahaha.action.main";
		public static final String INIT_ACTION = "io.github.garywahaha.action.init";
		public static final String PREV_ACTION = "io.github.garywahaha.action.prev";
		public static final String PLAY_ACTION = "io.github.garywahaha.action.play";
		public static final String NEXT_ACTION = "io.github.garywahaha.action.next";
		public static final String STARTFOREGROUND_ACTION = "io.github.garywahaha.action.startforeground";
		public static final String STOPFOREGROUND_ACTION = "io.github.garywahaha.action.stopforeground";
	}

	public static final class NOTIFICATION_ID {
		public static final int FOREGROUND_SERVICE = 101;
	}

	public static final class INTENT_KEY {
		public static final String VIDEO_LIST_KEY = "io.github.garywahaha.service.intent.key.video.list";
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
