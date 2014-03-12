package com.shurrik.mp3story.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.shurrik.mp3story.R;

public class PlayMusicActivity extends Activity{

	private int[] _ids;//存放临时ID的数组
	private String _titles[] = null;// 临时存放标题的数组
	private String _artists[] = null;// 临时存放艺术家的数组
	private int position;// 位置
	
	
	private ImageButton playbtn = null;// 播放按钮
	private ImageButton lastbtn = null;// 上一个
	private ImageButton nextbtn = null;// 下一个
	private TextView playTitle;
	private TextView playArtist;
	
	private int flag;// 标记
	
	private static final int PLAY = 1;// 定义播放状态
	private static final int PAUSE = 2;// 暂停状态
	private static final int STOP = 3;// 停止
	private static final int PROGRESS_CHANGE = 4;// 进度条改变
	

	private static final int STATE_PLAY = 1;// 播放状态设为1,表示播放状态
	private static final int STATE_PAUSE = 2;// 播放状态设为2，表示暂停状态
	private SeekBar seekbar = null;// 歌曲进度
	private TextView playTime = null;// 已播放时间
	private TextView durationTime = null;// 歌曲时间
	
	private static final String MUSIC_CURRENT = "com.shurrik.mp3story.currentTime";
	private static final String MUSIC_DURATION = "com.shurrik.mp3story.duration";
	private static final String MUSIC_NEXT = "com.shurrik.mp3story.next";
	private static final String MUSIC_UPDATE = "com.shurrik.mp3story.update";
	
	private int currentPosition;// 当前播放位置
	private int duration;// 总时间
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
		
		playTime = (TextView) this.findViewById(R.id.playTime);
		durationTime = (TextView) this.findViewById(R.id.durationTime);
		
		ShowPlayBtn();// 显示或者说监视播放按钮事件
		showPlayLast();
		showPlayNext();
		ShowSeekBar();// 进度条
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
	
	private void showPlayLast()
	{
		lastbtn = (ImageButton) findViewById(R.id.lastBtn);
		//Toast.makeText(this, "flag:"+flag, 3).show();
		lastbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				playLast();
			}
		});				
	}
	
	private void showPlayNext()
	{
		nextbtn = (ImageButton) findViewById(R.id.nextBtn);
		//Toast.makeText(this, "flag:"+flag, 3).show();
		nextbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				playNext();
			}
		});				
	}
	
	private void ShowSeekBar() {
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			
			public void onStopTrackingTouch(SeekBar seekBar) {
				play();

			}

			
			public void onStartTrackingTouch(SeekBar seekBar) {
				pause();

			}

			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					seekbar_change(progress);
				}

			}
		});

	}
	
	// 进度条改变
	protected void seekbar_change(int progress) {
		Intent intent = new Intent();
		intent.setAction("com.shurrik.mp3story.ui.service.LocalMusicService");
		intent.putExtra("op", PROGRESS_CHANGE);
		intent.putExtra("progress", progress);
		startService(intent);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setup();//初始化
		play();
		
/*		new Thread(){

			public void run() {
				Intent intent = new Intent();
				intent.putExtra("_id", _ids[position]);
				intent.setAction("com.shurrik.mp3story.ui.service.LocalMusicService");
				intent.putExtra("op", PLAY);
				startService(intent);
				 play();
			};
		}.start();*/
	}
	
	// 停止播放音乐
	private void stop() {
		Intent intent = new Intent();
		intent.setAction("org.music.service.LocalMusicService");
		intent.putExtra("op", STOP);
		startService(intent);
	}
	
	// 准备
	private void setup() {
		loadclip();
		init();
	}
	
	protected void playNext() {
		if (position == _ids.length - 1) {
			position = 0;
		} else if (position < _ids.length - 1) {
			position++;
		}
		stop();
		setup();//初始化
		play();
	}
	
	protected void playLast() {
		if (position == 0) {
			position = _ids.length - 1;
		} else if (position > 0) {
			position--;
		}
		stop();
		setup();//初始化
		play();
	}
	
	// 初始化服务
	private void init() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(MUSIC_CURRENT);
		filter.addAction(MUSIC_DURATION);
		filter.addAction(MUSIC_NEXT);
		filter.addAction(MUSIC_UPDATE);
		registerReceiver(musicreceiver, filter);

	}
	
	// 截取标题，歌词，歌名
	private void loadclip() {
		playTitle = (TextView) this.findViewById(R.id.play_title);
		playTitle.setText(_titles[position]);
		
		playArtist = (TextView) this.findViewById(R.id.play_artist);
		playArtist.setText(_artists[position]);
		
		seekbar.setProgress(0);
		int pos = _ids[position];
/*		name.setText(_titles[position]);
		artist.setText(_artists[position]);*/
		Intent intent = new Intent();
		intent.putExtra("_id", pos);
		intent.putExtra("_titles", _titles);
		intent.putExtra("position", position);
		intent.setAction("com.shurrik.mp3story.ui.service.LocalMusicService");
		startService(intent);

	}
	
	// 播放音乐
	protected void play() {
		flag = PLAY;
		playbtn.setImageResource(R.drawable.pause_button);
		Intent intent = new Intent();
		intent.putExtra("_id", _ids[position]);
		intent.setAction("com.shurrik.mp3story.ui.service.LocalMusicService");
		intent.putExtra("op", PLAY);
		startService(intent);

	}

	// 暂停
	protected void pause() {
		flag = PAUSE;
		playbtn.setImageResource(R.drawable.play_button);
		Intent intent = new Intent();
		intent.setAction("com.shurrik.mp3story.ui.service.LocalMusicService");
		intent.putExtra("op", PAUSE);
		startService(intent);

	}
	
	private BroadcastReceiver musicreceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MUSIC_CURRENT)) {
				currentPosition = intent.getExtras().getInt("currentTime");// 获得当前播放位置
				playTime.setText(toTime(currentPosition));// 初始化播放时间
				seekbar.setProgress(currentPosition);// 初始化播放进度位置
				/*Iterator<Integer> iterator = lrc_map.keySet().iterator();
				while (iterator.hasNext()) {
					Object o = iterator.next();
					LRCbean val = lrc_map.get(o);
					if (val != null) {

						if (currentPosition > val.getBeginTime()
								&& currentPosition < val.getBeginTime()
										+ val.getLineTime()) {
							lrcText.setText(val.getLrcBody());
							break;
						}
					}
				}*/

			} else if (action.equals(MUSIC_DURATION)) {
				duration = intent.getExtras().getInt("duration");// 获取总时间
				seekbar.setMax(duration);// 进度条设置最大值（传总时间）
				durationTime.setText(toTime(duration));// 总时间设置转换的函数
			}
			 else if (action.equals(MUSIC_UPDATE)) {
				position = intent.getExtras().getInt("position");
				setup();
			}
			 else if (action.equals(MUSIC_NEXT)) {
				System.out.println("音乐继续播放下一首");
				playNext();
			 }


		}
	};
	
	/**
	 * 时间的转换
	 * 
	 * @param time
	 * @return
	 */
	public String toTime(int time) {

		time /= 1000;
		int minute = time / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}
}
