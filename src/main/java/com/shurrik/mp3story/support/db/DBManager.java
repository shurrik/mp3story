package com.shurrik.mp3story.support.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {  
    private DBHelper helper;  
    private SQLiteDatabase db;  
      
    public DBManager(Context context) {  
        helper = new DBHelper(context);  
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里  
        db = helper.getWritableDatabase();  
    }  
    
    public void add(PlayLog log) {  
        db.beginTransaction();  //开始事务  
        try {  
            db.execSQL("INSERT INTO playlog VALUES(null, ?, ?)", new Object[]{log.getTitle(), log.getCreateTime()});  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    } 
    
    /** 
     * add persons 
     * @param persons 
     */  
    public void add(List<PlayLog> logs) {  
        db.beginTransaction();  //开始事务  
        try {  
            for (PlayLog log : logs) {  
                db.execSQL("INSERT INTO playlog VALUES(null, ?, ?)", new Object[]{log.getTitle(), log.getCreateTime()});  
            }  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    }  
      
    /** 
     * update person's age 
     * @param person 
     */  
    public void update(PlayLog log) {  
        ContentValues cv = new ContentValues();  
        cv.put("title", log.getTitle());  
        db.update("playlog", cv, "id = ?", new String[]{log.getId().toString()});  
    }  
      
    /** 
     * delete old person 
     * @param person 
     */  
    public void delete(PlayLog log) {  
        db.delete("playlog", "id = ?", new String[]{log.getId().toString()});  
    }  
      
    /** 
     * query all persons, return list 
     * @return List<Person> 
     */  
    public List<PlayLog> query() {  
        ArrayList<PlayLog> logs = new ArrayList<PlayLog>();  
        Cursor c = queryTheCursor();  
        while (c.moveToNext()) {  
        	PlayLog log = new PlayLog();  
        	log.setId(c.getInt(c.getColumnIndex("id")));
        	log.setTitle(c.getString(c.getColumnIndex("title")));
        	log.setCreateTime(new Date(c.getString(c.getColumnIndex("create_time"))));
/*            person._id = c.getInt(c.getColumnIndex("_id"));  
            person.name = c.getString(c.getColumnIndex("name"));  
            person.age = c.getInt(c.getColumnIndex("age"));  
            person.info = c.getString(c.getColumnIndex("info"));  */
            logs.add(log);  
        }  
        c.close();  
        return logs;  
    }  
      
    /** 
     * query all persons, return cursor 
     * @return  Cursor 
     */  
    public Cursor queryTheCursor() {  
        Cursor c = db.rawQuery("SELECT * FROM playlog", null);  
        return c;  
    }  
      
    /** 
     * close database 
     */  
    public void closeDB() {  
        db.close();  
    }  
}  
