package com.shurrik.mp3story.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.shurrik.mp3story.R;
import com.shurrik.mp3story.ui.adapter.LocalMusicAdapter;

public class LocalMusicActivity extends Activity{

	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_music);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		List<Map> data = new ArrayList();
		for(int i=0;i<10;i++)
		{
			Map map = new HashMap();
			map.put("index", i);
			data.add(map);
		}
		LocalMusicAdapter adapter = new LocalMusicAdapter(this,data);
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setAdapter(adapter);		
	}
}
