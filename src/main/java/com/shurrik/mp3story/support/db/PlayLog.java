package com.shurrik.mp3story.support.db;

import java.util.Date;

public class PlayLog {
    private Integer id;  
    private String title;  
    private Date createTime;  
      
    public PlayLog() {  
    }  
      
    public PlayLog(String title, Date createTime) {  
        this.title = title;  
        this.createTime = createTime;  
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
}
