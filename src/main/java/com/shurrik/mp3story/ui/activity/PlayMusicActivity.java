package com.shurrik.mp3story.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.shurrik.mp3story.R;

public class PlayMusicActivity extends Activity{

	private int[] _ids;//存放临时ID的数组
	private String _titles[] = null;// 临时存放标题的数组
	private String _artists[] = null;// 临时存放艺术家的数组
	private int position;// 位置
	private ImageButton playbtn = null;// 播放按钮
	private int flag;// 标记
	
	private static final int PLAY = 1;// 定义播放状态
	private static final int PAUSE = 2;// 暂停状态
	private static final int STOP = 3;// 停止
	private static final int PROGRESS_CHANGE = 4;// 进度条改变
	

	private static final int STATE_PLAY = 1;// 播放状态设为1,表示播放状态
	private static final int STATE_PAUSE = 2;// 播放状态设为2，表示暂停状态
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_music);
		Intent intent = this.getIntent();// 获取列表的Intent对象
		Bundle bundle = intent.getExtras();// Bundle存取数据，那么在播放界面提取数据喽
		_ids = bundle.getIntArray("_ids");// 歌名数组的ID，用来临时保存音乐的ID
		position = bundle.getInt("position");// 音乐播放位置
		_titles = bundle.getStringArray("_titles");// 音乐播放标题
		_artists = bundle.getStringArray("_artists");// 传过来的艺术家，歌名一个都不允许遗漏，否则空指针是必须的
		ShowPlayBtn();// 显示或者说监视播放按钮事件
	}
	
    //显示各个按钮并做监视
	private void ShowPlayBtn() {
		playbtn = (ImageButton) findViewById(R.id.playBtn);
		//Toast.makeText(this, "flag:"+flag, 3).show();
		playbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				switch (flag) {
				case STATE_PLAY:
					pause();
					break;

				case STATE_PAUSE:
					play();
					break;
				}

			}
		});

	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		new Thread(){

			public void run() {
/*				Intent intent = new Intent();
				intent.putExtra("_id", _ids[position]);
				intent.setAction("com.shurrik.mp3story.ui.service.LocalMusicService");
				intent.putExtra("op", PLAY);
				startService(intent);*/
				 play();
			};
		}.start();
	}
	
	
	// 播放音乐
	protected void play() {
		flag = PLAY;
		playbtn.setImageResource(R.drawable.pause_button);
		Intent intent = new Intent();
		intent.putExtra("_id", _ids[position]);
		intent.setAction("org.music.service.LocalMusicService");
		intent.putExtra("op", PLAY);
		startService(intent);

	}

	// 暂停
	protected void pause() {
		flag = PAUSE;
		playbtn.setImageResource(R.drawable.play_button);
		Intent intent = new Intent();
		intent.setAction("org.music.service.LocalMusicService");
		intent.putExtra("op", PAUSE);
		startService(intent);

	}
}
