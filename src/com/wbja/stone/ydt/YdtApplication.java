package com.wbja.stone.ydt;


import com.wbja.stone.ydt.entity.MRClinic;

/**
 * 小猪短租应用类，用来共享定位结果，列表与地图检索结果数据
 * 
 * @author Lu.Jian
 * 
 */
public class YdtApplication  {

	private static YdtApplication mInstance = new YdtApplication();
	private MRClinic mrc;

//	@Override
//	public void onCreate() {
//		super.onCreate();
//		
//		mInstance = this;
//
//	}

	public MRClinic getMrc() {
		return mrc;
	}

	public void setMrc(MRClinic mrc) {
		this.mrc = mrc;
	}

	public static YdtApplication getInstance() {
		return mInstance;
	}

}