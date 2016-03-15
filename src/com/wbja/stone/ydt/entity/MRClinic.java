package com.wbja.stone.ydt.entity;

public class MRClinic {
	private int id;
	private int id_patient;
	private String name_patient;
	private String sn_bingli;
	private int id_hospital;
	private String name_hospital;
	private int id_dept;
	private String name_dept;
	private int id_user;
	private String name_user;
	private String title;
	private String memo;
	private String jiwangshi;
	private String xianbingshi;
	private String zhusu;
	private String kexiazheng;
	private String shexiang;
	private String maixiang;
	private String tizhengjiancha;
	private String fuzhujianca;
	private String temperature;
	private String maibo;
	private String xueya;
	private String bingming;
	private String zhongyizhenduan;
	private String zhongyizhenghou;
	private String xiyizhenduan;
	private String zhizezhifa;
	private String fangming;
	private String fangjizucheng;
	private String qitazhiliao;
	private String yuhou;
	private String anyu;
	private int dianxingyian;
	private String liaoxiao;
	private int liaoxiaozhuangtai;
	private String jiuzhenshijian;
	private int id_chuzhen;
	
	private String src;
	private int idLocal;
	private int isEdit;
	
	public MRClinic(){
		id_patient=0;
		name_patient="";
		sn_bingli="";
		id_hospital=0;
		name_hospital="";
		id_dept=0;
		name_dept="";
		id_user=0;
		name_user="";
		title="";
		memo="";
		jiwangshi="";
		xianbingshi="";
		zhusu="";
		kexiazheng="";
		shexiang="";
		maixiang="";
		tizhengjiancha="";
		fuzhujianca="";
		temperature="";
		maibo="";
		xueya="";
		bingming="";
		zhongyizhenduan="";
		zhongyizhenghou="";
		xiyizhenduan="";
		zhizezhifa="";
		fangming="";
		fangjizucheng="";
		qitazhiliao="";
		yuhou="";
		anyu="";
		dianxingyian=0;
		liaoxiao="";
		liaoxiaozhuangtai=0;
		jiuzhenshijian="";
		
		src="";
		idLocal=0;
		isEdit=0;
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_patient() {
		return id_patient;
	}
	public void setId_patient(int id_patient) {
		this.id_patient = id_patient;
	}
	public String getName_patient() {
		return name_patient;
	}
	public void setName_patient(String name_patient) {
		this.name_patient = name_patient;
	}
	public String getSn_bingli() {
		return sn_bingli;
	}
	public void setSn_bingli(String sn_bingli) {
		this.sn_bingli = sn_bingli;
	}
	public int getId_hospital() {
		return id_hospital;
	}
	public void setId_hospital(int id_hospital) {
		this.id_hospital = id_hospital;
	}
	public String getName_hospital() {
		return name_hospital;
	}
	public void setName_hospital(String name_hospital) {
		this.name_hospital = name_hospital;
	}
	public int getId_dept() {
		return id_dept;
	}
	public void setId_dept(int id_dept) {
		this.id_dept = id_dept;
	}
	public String getName_dept() {
		return name_dept;
	}
	public void setName_dept(String name_dept) {
		this.name_dept = name_dept;
	}
	public int getId_user() {
		return id_user;
	}
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	public String getName_user() {
		return name_user;
	}
	public void setName_user(String name_user) {
		this.name_user = name_user;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getJiwangshi() {
		return jiwangshi;
	}
	public void setJiwangshi(String jiwangshi) {
		this.jiwangshi = jiwangshi;
	}
	public String getXianbingshi() {
		return xianbingshi;
	}
	public void setXianbingshi(String xianbingshi) {
		this.xianbingshi = xianbingshi;
	}
	public String getZhusu() {
		return zhusu;
	}
	public void setZhusu(String zhusu) {
		this.zhusu = zhusu;
	}
	public String getKexiazheng() {
		return kexiazheng;
	}
	public void setKexiazheng(String kexiazheng) {
		this.kexiazheng = kexiazheng;
	}
	public String getShexiang() {
		return shexiang;
	}
	public void setShexiang(String shexiang) {
		this.shexiang = shexiang;
	}
	public String getMaixiang() {
		return maixiang;
	}
	public void setMaixiang(String maixiang) {
		this.maixiang = maixiang;
	}
	public String getTizhengjiancha() {
		return tizhengjiancha;
	}
	public void setTizhengjiancha(String tizhengjiancha) {
		this.tizhengjiancha = tizhengjiancha;
	}
	public String getFuzhujianca() {
		return fuzhujianca;
	}
	public void setFuzhujianca(String fuzhujianca) {
		this.fuzhujianca = fuzhujianca;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getMaibo() {
		return maibo;
	}
	public void setMaibo(String maibo) {
		this.maibo = maibo;
	}
	public String getXueya() {
		return xueya;
	}
	public void setXueya(String xueya) {
		this.xueya = xueya;
	}
	public String getBingming() {
		return bingming;
	}
	public void setBingming(String bingming) {
		this.bingming = bingming;
	}
	public String getZhongyizhenduan() {
		return zhongyizhenduan;
	}
	public void setZhongyizhenduan(String zhongyizhenduan) {
		this.zhongyizhenduan = zhongyizhenduan;
	}
	public String getZhongyizhenghou() {
		return zhongyizhenghou;
	}
	public void setZhongyizhenghou(String zhongyizhenghou) {
		this.zhongyizhenghou = zhongyizhenghou;
	}
	public String getXiyizhenduan() {
		return xiyizhenduan;
	}
	public void setXiyizhenduan(String xiyizhenduan) {
		this.xiyizhenduan = xiyizhenduan;
	}
	public String getZhizezhifa() {
		return zhizezhifa;
	}
	public void setZhizezhifa(String zhizezhifa) {
		this.zhizezhifa = zhizezhifa;
	}
	public String getFangming() {
		return fangming;
	}
	public void setFangming(String fangming) {
		this.fangming = fangming;
	}
	public String getFangjizucheng() {
		return fangjizucheng;
	}
	public void setFangjizucheng(String fangjizucheng) {
		this.fangjizucheng = fangjizucheng;
	}
	public String getQitazhiliao() {
		return qitazhiliao;
	}
	public void setQitazhiliao(String qitazhiliao) {
		this.qitazhiliao = qitazhiliao;
	}
	public String getYuhou() {
		return yuhou;
	}
	public void setYuhou(String yuhou) {
		this.yuhou = yuhou;
	}
	public String getAnyu() {
		return anyu;
	}
	public void setAnyu(String anyu) {
		this.anyu = anyu;
	}
	public int getDianxingyian() {
		return dianxingyian;
	}
	public void setDianxingyian(int dianxingyian) {
		this.dianxingyian = dianxingyian;
	}
	public String getLiaoxiao() {
		return liaoxiao;
	}
	public void setLiaoxiao(String liaoxiao) {
		this.liaoxiao = liaoxiao;
	}
	public int getLiaoxiaozhuangtai() {
		return liaoxiaozhuangtai;
	}
	public void setLiaoxiaozhuangtai(int liaoxiaozhuangtai) {
		this.liaoxiaozhuangtai = liaoxiaozhuangtai;
	}
	public String getJiuzhenshijian() {
		return jiuzhenshijian;
	}
	public void setJiuzhenshijian(String jiuzhenshijian) {
		this.jiuzhenshijian = jiuzhenshijian;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public int getIsEdit() {
		return isEdit;
	}
	public void setIsEdit(int isEdit) {
		this.isEdit = isEdit;
	}
	public int getIdLocal() {
		return idLocal;
	}
	public void setIdLocal(int idLocal) {
		this.idLocal = idLocal;
	}

	public int getId_chuzhen() {
		return id_chuzhen;
	}

	public void setId_chuzhen(int id_chuzhen) {
		this.id_chuzhen = id_chuzhen;
	}
	
}
