package com.shurrik.mp3story.ui.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shurrik.mp3story.R;

public class LocalMusicAdapter  extends ArrayAdapter<Map>{
    private List<Map> dataSource ;
    //private AsyncImageLoader imageLoader = new AsyncImageLoader();
    public LocalMusicAdapter(Activity activity, List<Map> data) {
        super(activity, 0, data);
        dataSource = data;
    }
    
    
    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = this.viewMap.get(position);

        if (rowView == null) {
            Map<String,String> map = dataSource.get(position);
            LayoutInflater inflater = ((Activity) this.getContext())
                    .getLayoutInflater();
            rowView = inflater
                    .inflate(R.layout.local_item, null);

            TextView title = (TextView)rowView.findViewById(R.id.local_title);
/*            ImageView courseCover = (ImageView)rowView.findViewById(R.id.courseCover);
            courseName.setText(map.get("courseName").toString());
            String url = StaticVariable.IMG_ROOT+ "/" + map.get("courseCover").toString();
            imageLoader.loadDrawable(url, courseCover);*/
            title.setText(String.valueOf(position));
            viewMap.put(position, rowView);
        }
        return rowView;
    }  
}
