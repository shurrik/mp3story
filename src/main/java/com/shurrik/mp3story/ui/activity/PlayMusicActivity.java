package com.shurrik.mp3story.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.shurrik.mp3story.R;

public class PlayMusicActivity extends Activity{

	private static final int PLAY = 1;// 定义播放状态
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_music);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		new Thread(){

			public void run() {
				Intent intent = new Intent();
				intent.putExtra("_id", 1);
				intent.setAction("com.shurrik.mp3story.ui.service.LocalMusicService");
				intent.putExtra("op", PLAY);
				startService(intent);
			};
		}.start();
	}
}
