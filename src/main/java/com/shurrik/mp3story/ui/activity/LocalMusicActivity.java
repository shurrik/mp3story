package com.shurrik.mp3story.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.shurrik.mp3story.R;
import com.shurrik.mp3story.ui.adapter.MusicListAdapter;

public class LocalMusicActivity extends Activity{
	private int[] _ids;// 保存音乐ID临时数组
	private String[] _artists;// 保存艺术家
	private String[] _titles;// 标题临时数组
	ListView listView;
	String[] media_info = new String[] { MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,
			MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_music);
		showList();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}
	
	private void showList()
	{
		List<Map> data = new ArrayList();
		for(int i=0;i<10;i++)
		{
			Map map = new HashMap();
			map.put("index", i);
			data.add(map);
		}
		Cursor cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, media_info, null,
				null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();// 将游标移动到初始位置
		_ids = new int[cursor.getCount()];// 返回INT的一个列
		_artists = new String[cursor.getCount()];// 返回String的一个列
		_titles = new String[cursor.getCount()];// 返回String的一个列
		for (int i = 0; i < cursor.getCount(); i++) {
			_ids[i] = cursor.getInt(3);
			_titles[i] = cursor.getString(0);
			_artists[i] = cursor.getString(2);
			cursor.moveToNext();// 将游标移到下一行
		}
		
		listView = (ListView) this.findViewById(R.id.listView);
		/*LocalMusicAdapter adapter = new LocalMusicAdapter(this,data);*/
		MusicListAdapter adapter = new MusicListAdapter(this,cursor);
		listView.setAdapter(adapter);	
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				/*Intent intent = new Intent(LocalMusicActivity.this,PlayMusicActivity.class);
				startActivity(intent);*/
				playMusic(position);
			}
		});
	}
	
	// 播放音乐方法
	public void playMusic(int position) {
		Intent intent = new Intent(LocalMusicActivity.this,
				PlayMusicActivity.class);
		intent.putExtra("_ids", _ids);
		intent.putExtra("_titles", _titles);
		intent.putExtra("_artists", _artists);
		intent.putExtra("position", position);
		startActivity(intent);
		finish();

	}
}
