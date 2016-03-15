package com.wbja.stone.ydt.util;

import java.util.ArrayList;
import java.util.List;

import com.wbja.stone.ydt.entity.Attachment;
import com.wbja.stone.ydt.entity.Friend;
import com.wbja.stone.ydt.entity.MRClinic;
import com.wbja.stone.ydt.entity.Patient;
import com.wbja.stone.ydt.entity.PatientUpload;
import com.wbja.stone.ydt.entity.Schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 鏁版嵁搴撴搷浣滅�?
 * 
 * @author wwj_748
 * @date 2014/8/9
 */
public class DB extends SQLiteOpenHelper {
	private static final String TAG = "YDT DB";
	// 鏁版嵁搴撳悕绉板父锟�?
	private static final String DATABASE_NAME = "ydt.db";
	// 鏁版嵁搴撶増鏈父锟�?
	private static final int DATABASE_VERSION = 1;
	// 琛ㄥ悕绉板父锟�

	public static final String CREATE_PATIENTUP = "CREATE TABLE IF NOT EXISTS patientup ("
			+ "_ID INTEGER PRIMARY KEY AUTOINCREMENT ,name TEXT,gender TEXT,age INTEGER,marriage TEXT,date_birth TEXT,idcard TEXT,country TEXT,nation TEXT,occupation TEXT,add_birth TEXT,tele TEXT,add_home TEXT,"
			+ "danweimingcheng TEXT,danweidizhi TEXT,contacts TEXT,tele_contact TEXT,add_contact TEXT,xuexing TEXT,jiwangshi TEXT,gerenshi TEXT,jiatingshi TEXT,guominshi TEXT,"
			+ "hunyushi TEXT,zhenliaoshi TEXT,sn_shebao TEXT,sn_yibao TEXT,id_user INTEGER,src TEXT,id_up INTEGER,idPhotoPath TEXT,isUpdate INTEGER);";

	public static final String CREATE_PATIENT="CREATE TABLE IF NOT EXISTS patient (_id integer primary key autoincrement,"
			+ "id_fav integer,id_user integer,usertype integer,name_doctor text not null,mobile text not null,idup INTEGER)";
	
	public static final String CREATE_SCHEDULE = "create table IF NOT EXISTS schedule (_id integer primary key autoincrement, "
			+ "type integer not null,content text not null,date text not null,time text,imgpath text,contel text,voipath text,id_user integer not null);";

	public static final String CREATE_MRC = "create table IF NOT EXISTS mrc (_id integer primary key autoincrement,id_patient integer,name_patient TEXT,sn_bingli TEXT,"
			+ "id_hospital integer,name_hospital TEXT,id_dept integer,name_dept TEXT,id_user integer,name_user TEXT,title TEXT,memo TEXT,"
			+ "jiwangshi TEXT,xianbingshi TEXT,zhusu TEXT,kexiazheng TEXT,shexiang TEXT,maixiang TEXT,tizhengjiancha TEXT,fuzhujianca TEXT,"
			+ "temperature TEXT,maibo TEXT,xueya TEXT,bingming TEXT,zhongyizhenduan TEXT,zhongyizhenghou TEXT,xiyizhenduan TEXT,zhizezhifa TEXT,"
			+ "fangming TEXT,fangjizucheng TEXT,qitazhiliao TEXT,yuhou TEXT,anyu TEXT,dianxingyian TEXT,liaoxiao TEXT,liaoxiaozhuangtai TEXT,jiuzhenshijian TEXT,"
			+ "src TEXT not null,idup integer,isEdit integer,id_chuzhen integer);";

//	public static final String CREATE_RECORD = "create table IF NOT EXISTS record (_id integer primary key autoincrement, "
//			+ "patient_id integer,src text not null,name text not null,mrc_id integer,isedit integer,createtime TEXT );";

	public static final String CREATE_ATTACH = "create table IF NOT EXISTS attachment (_id integer primary key autoincrement, "
			+ "id_user integer,id_patient integer not null,fujianzubie text not null,fujianleixing text not null,directory text not null,id_mrclinic integer not null,ext text not null );";

	public static final String CREATE_Friend = "create table IF NOT EXISTS friend (_id integer primary key autoincrement, "
			+ "id_user integer,id_friend integer not null,tag text,ispass integer not null,displayname_friend text not null,displayname_user text not null);";
	
	private SQLiteDatabase db;

	// 鏋勶�?鏂规�?
	public DB(Context context) {
		// 鍒涘缓鏁版嵁锟�
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.e(TAG, "create database");
	}

	// 鍒涘缓鏃惰皟鐢紝鑻ユ暟鎹簱�?樺湪鍒欎笉璋冪敤
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e(TAG, "oncreate DB");
		// create schudule table
		db.execSQL(CREATE_SCHEDULE);
		// create pat table
		db.execSQL(CREATE_PATIENT);
		// create mrc table
		db.execSQL(CREATE_MRC);
		//create attach table
	//	db.execSQL(CREATE_ATTACH);
		//create friend table
		db.execSQL(CREATE_Friend);
	}

	// 鐗堟湰鏇存柊鏃惰皟锟�?
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion != newVersion) {
			// 鍒犻櫎锟�?
			Log.e(TAG, "delete DB");
			
			db.execSQL("DROP TABLE IF EXISTS schedule");
			db.execSQL("DROP TABLE IF EXISTS patient");
			db.execSQL("DROP TABLE IF EXISTS friend");
			db.execSQL("DROP TABLE IF EXISTS mrc");

			onCreate(db);// 鍒涘缓鏂拌�?
		}

	}
	
	/**
	 * id_fav integer,id_user integer,usertype integer,name_doctor text not null,mobile text not null)";
	 * */
	/**
	 * 鎻掑叆鏁版嵁
	 * 
	 * @param list
	 */
	public boolean insertPatient(Patient item) {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("id_fav", item.getId_fav());
		values.put("id_user", item.getId_user());
		values.put("usertype", item.getUsertype());
		values.put("name_doctor", item.getName_doctor());
		values.put("mobile", item.getMobile());
		values.put("idup", item.getId());

		Log.e(TAG, "insert DB patient id=" + item.getId());
		boolean bo = false;
		bo = db.insert("patient", null, values) > 0;
		return (bo);
	}

	/**
	 * 鏌ヨ鐥呬汉鍒楄�?
	 * 
	 * @param blogType
	 */
	public List<Patient> queryPatient(String id_user) {
		List<Patient> list = new ArrayList<Patient>();
		db = this.getReadableDatabase();
		String sql = "select * from patient where id_fav="+id_user;
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		Patient item;
		if (cursor.getCount() > 0) {
			do {
				item = new Patient();
				item.setId_local(cursor.getInt(0));
				item.setId_fav(cursor.getInt(1));
				item.setId_user(cursor.getInt(2));
				item.setUsertype(cursor.getInt(3));
				item.setName_doctor(cursor.getString(4));
				item.setMobile(cursor.getString(5));
				item.setId(cursor.getInt(6));
				

				list.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}
	public Patient queryPatientById(int id) {
		List<Patient> list = new ArrayList<Patient>();
		db = this.getReadableDatabase();
		String sql = "select * from patient where id_user="+id;
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		Patient item=null;
		if (cursor.getCount() > 0) {
		
				item = new Patient();
				item.setId_local(cursor.getInt(0));
				item.setId_fav(cursor.getInt(1));
				item.setId_user(cursor.getInt(2));
				item.setUsertype(cursor.getInt(3));
				item.setName_doctor(cursor.getString(4));
				item.setMobile(cursor.getString(5));
				item.setId(cursor.getInt(6));
				
				list.add(item);
			
		}
		cursor.close();
		return item;
	}
	

	/**
	 * 鎻掑叆鏁版嵁
	 * 
	 * @param list
	 */
	public boolean insertPatientUp(PatientUpload item) {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("name", item.getName());
		values.put("gender", item.getGender());
		values.put("age", item.getAge());
		values.put("marriage", item.getMarriage());
		values.put("date_birth", item.getDate_birth());
		values.put("idcard", item.getIdcard());
		values.put("country", item.getCountry());
		values.put("nation", item.getNation());
		values.put("occupation", item.getOccupation());
		values.put("add_birth", item.getAdd_birth());
		values.put("tele", item.getTele());
		values.put("add_home", item.getAdd_home());
		values.put("danweimingcheng", item.getDanweimingcheng());
		values.put("danweidizhi", item.getDanweidizhi());
		values.put("contacts", item.getContacts());
		values.put("tele_contact", item.getTele_contact());
		values.put("add_contact", item.getAdd_contact());
		values.put("xuexing", item.getXuexing());
		values.put("jiwangshi", item.getJiwangshi());
		values.put("gerenshi", item.getGerenshi());
		values.put("jiatingshi", item.getJiatingshi());
		values.put("guominshi", item.getGuominshi());
		values.put("hunyushi", item.getHunyushi());
		values.put("zhenliaoshi", item.getZhenliaoshi());
		values.put("sn_shebao", item.getSn_shebao());
		values.put("sn_yibao", item.getSn_yibao());
		values.put("id_user", item.getId_user());
		values.put("src", item.getSrc());
		values.put("id_up", item.getId());
		values.put("idPhotoPath", item.getIdPhotoPath());
		values.put("isUpdate", item.getIsUpdate());

		Log.e(TAG, "insert DB id=" + item.getId());
		boolean bo = false;
		bo = db.insert("patientup", null, values) > 0;
		return (bo);
	}

	/**
	 * 鏌ヨ鐥呬汉鍒楄�?
	 * 
	 * @param blogType
	 */
	public List<PatientUpload> queryPatientUp(String id_user) {
		List<PatientUpload> list = new ArrayList<PatientUpload>();
		db = this.getReadableDatabase();
		String sql = "select * from patientup where id_user="+id_user;
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			do {
				PatientUpload item = new PatientUpload();
				item.setId_local(cursor.getInt(0));
				item.setName(cursor.getString(1));
				item.setGender(cursor.getString(2));
				item.setAge(cursor.getInt(3));
				item.setMarriage(cursor.getString(4));
				item.setDate_birth(cursor.getString(5));
				item.setIdcard(cursor.getString(6));
				item.setCountry(cursor.getString(7));
				item.setNation(cursor.getString(8));
				item.setOccupation(cursor.getString(9));
				item.setAdd_birth(cursor.getString(10));
				item.setTele(cursor.getString(11));
				item.setAdd_birth(cursor.getString(12));
				item.setAdd_home(cursor.getString(13));
				item.setDanweimingcheng(cursor.getString(14));
				item.setDanweidizhi(cursor.getString(15));
				item.setContacts(cursor.getString(16));
				item.setTele_contact(cursor.getString(17));
				item.setAdd_contact(cursor.getString(18));
				item.setXuexing(cursor.getString(19));
				item.setJiwangshi(cursor.getString(20));
				item.setGerenshi(cursor.getString(21));
				item.setJiatingshi(cursor.getString(22));
				item.setGuominshi(cursor.getString(23));
				item.setHunyushi(cursor.getString(24));
				item.setZhenliaoshi(cursor.getString(25));
				item.setSn_shebao(cursor.getString(26));
				item.setSn_yibao(cursor.getString(27));
				item.setId_user(cursor.getInt(28));
				item.setId(cursor.getInt(29));
				item.setIdPhotoPath(cursor.getString(30));
				item.setIsUpdate(cursor.getInt(31));

				list.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

	/**
	 * 鏌ヨ鐥呬汉
	 * 
	 * @param blogType
	 * @return
	 */
	public PatientUpload queryPatientUpByID(int id) {
		db = this.getReadableDatabase();
		String sql = "select * from patientup where id_up = " + id;
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();

		PatientUpload item = null;
		if (cursor.getCount() > 0) {
			item = new PatientUpload();
			item.setId_local(cursor.getInt(0));
			item.setName(cursor.getString(1));
			item.setGender(cursor.getString(2));
			item.setAge(cursor.getInt(3));
			item.setMarriage(cursor.getString(4));
			item.setDate_birth(cursor.getString(5));
			item.setIdcard(cursor.getString(6));
			item.setCountry(cursor.getString(7));
			item.setNation(cursor.getString(8));
			item.setOccupation(cursor.getString(9));
			item.setAdd_birth(cursor.getString(10));
			item.setTele(cursor.getString(11));
			item.setAdd_birth(cursor.getString(12));
			item.setAdd_home(cursor.getString(13));
			item.setDanweimingcheng(cursor.getString(14));
			item.setDanweidizhi(cursor.getString(15));
			item.setContacts(cursor.getString(16));
			item.setTele_contact(cursor.getString(17));
			item.setAdd_contact(cursor.getString(18));
			item.setXuexing(cursor.getString(19));
			item.setJiwangshi(cursor.getString(20));
			item.setGerenshi(cursor.getString(21));
			item.setJiatingshi(cursor.getString(22));
			item.setGuominshi(cursor.getString(23));
			item.setHunyushi(cursor.getString(24));
			item.setZhenliaoshi(cursor.getString(25));
			item.setSn_shebao(cursor.getString(26));
			item.setSn_yibao(cursor.getString(27));
			item.setId_user(cursor.getInt(28));
			item.setId(cursor.getInt(29));
			item.setIdPhotoPath(cursor.getString(30));
			item.setIsUpdate(cursor.getInt(31));
		}
		cursor.close();
		return item;
	}

	/**
	 * 
	 * 鏇存柊鐥呬緥
	 * 
	 * */
	public boolean updatePatientUp(PatientUpload item) {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", item.getName());
		values.put("gender", item.getGender());
		values.put("age", item.getAge());
		values.put("marriage", item.getMarriage());
		values.put("date_birth", item.getDate_birth());
		values.put("idcard", item.getIdcard());
		values.put("country", item.getCountry());
		values.put("nation", item.getNation());
		values.put("occupation", item.getOccupation());
		values.put("add_birth", item.getAdd_birth());
		values.put("tele", item.getTele());
		values.put("add_home", item.getAdd_home());
		values.put("danweimingcheng", item.getDanweimingcheng());
		values.put("danweidizhi", item.getDanweidizhi());
		values.put("contacts", item.getContacts());
		values.put("tele_contact", item.getTele_contact());
		values.put("add_contact", item.getAdd_contact());
		values.put("xuexing", item.getXuexing());
		values.put("jiwangshi", item.getJiwangshi());
		values.put("gerenshi", item.getGerenshi());
		values.put("jiatingshi", item.getJiatingshi());
		values.put("guominshi", item.getGuominshi());
		values.put("hunyushi", item.getHunyushi());
		values.put("zhenliaoshi", item.getZhenliaoshi());
		values.put("sn_shebao", item.getSn_shebao());
		values.put("sn_yibao", item.getSn_yibao());
		values.put("id_user", item.getId_user());
		values.put("src", item.getSrc());
		values.put("id_up", item.getId());
		values.put("idPhotoPath", item.getIdPhotoPath());
		values.put("isUpdate", item.getIsUpdate());

		Log.e(TAG, "insert DB id=" + item.getId());
		return (db.update("patientup", values, "_id=?",
				new String[] { String.valueOf(item.getId_local()) }) > 0);
	}

	/**
	 * 鎻掑叆鏁版嵁
	 * 
	 * @param list
	 */
	public boolean insertMRC(MRClinic item) {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("id_patient", item.getId_patient());
		values.put("name_patient", item.getName_patient());
		values.put("sn_bingli", item.getSn_bingli());
		values.put("id_hospital", item.getId_hospital());
		values.put("name_hospital", item.getName_hospital());
		values.put("id_dept", item.getId_dept());
		values.put("name_dept", item.getName_dept());
		values.put("id_user", item.getId_user());
		values.put("name_user", item.getName_user());
		values.put("title", item.getTitle());
		values.put("memo", item.getMemo());
		values.put("jiwangshi", item.getJiwangshi());
		values.put("xianbingshi", item.getXianbingshi());
		values.put("zhusu", item.getZhusu());
		values.put("kexiazheng", item.getKexiazheng());
		values.put("shexiang", item.getShexiang());
		values.put("maixiang", item.getMaixiang());
		values.put("tizhengjiancha", item.getTizhengjiancha());
		values.put("fuzhujianca", item.getFuzhujianca());
		values.put("temperature", item.getTemperature());
		values.put("maibo", item.getMaibo());
		values.put("xueya", item.getXueya());
		values.put("bingming", item.getBingming());
		values.put("zhongyizhenduan", item.getZhongyizhenduan());
		values.put("zhongyizhenghou", item.getZhongyizhenghou());
		values.put("xiyizhenduan", item.getXiyizhenduan());
		values.put("zhizezhifa", item.getZhizezhifa());
		values.put("fangming", item.getFangming());
		values.put("fangjizucheng", item.getFangjizucheng());
		values.put("qitazhiliao", item.getQitazhiliao());
		values.put("yuhou", item.getYuhou());
		values.put("anyu", item.getAnyu());
		values.put("dianxingyian", item.getDianxingyian());
		values.put("liaoxiao", item.getLiaoxiao());
		values.put("liaoxiaozhuangtai", item.getLiaoxiaozhuangtai());
		values.put("jiuzhenshijian", item.getJiuzhenshijian());
		values.put("src", item.getSrc());
		values.put("idup", item.getId());
		values.put("isEdit", item.getIsEdit());
		values.put("id_chuzhen", item.getId_chuzhen());

		Log.e(TAG, "insert DB id=" + item.getId_user()+"bianhao:"+item.getIdLocal());
		boolean bo = false;
		bo = db.insert("mrc", null, values) > 0;
		return (bo);
	}

	/**
	 * 鏌ヨMRC鍒楄�?
	 * 
	 * @param blogType
	 */
	public List<MRClinic> queryMRCFirst(String id_user) {
		List<MRClinic> list = new ArrayList<MRClinic>();
		db = this.getReadableDatabase();
		String sql = "select * from mrc where id_chuzhen=0 and id_user="+id_user;
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			do {
				MRClinic item = new MRClinic();

				item.setIdLocal(cursor.getInt(0));
				item.setId_patient(cursor.getInt(1));
				item.setName_patient(cursor.getString(2));
				item.setSn_bingli(cursor.getString(3));
				item.setId_hospital(cursor.getInt(4));
				item.setName_hospital(cursor.getString(5));
				item.setId_dept(cursor.getInt(6));
				item.setName_dept(cursor.getString(7));
				item.setId_user(cursor.getInt(8));
				item.setName_user(cursor.getString(9));
				item.setTitle(cursor.getString(10));
				item.setMemo(cursor.getString(11));
				item.setJiwangshi(cursor.getString(12));
				item.setXianbingshi(cursor.getString(13));
				item.setZhusu(cursor.getString(14));
				item.setKexiazheng(cursor.getString(15));
				item.setShexiang(cursor.getString(16));
				item.setMaixiang(cursor.getString(17));
				item.setTizhengjiancha(cursor.getString(18));
				item.setFuzhujianca(cursor.getString(19));
				item.setTemperature(cursor.getString(20));
				item.setMaibo(cursor.getString(21));
				item.setXueya(cursor.getString(22));
				item.setBingming(cursor.getString(23));
				item.setZhongyizhenduan(cursor.getString(24));
				item.setZhongyizhenghou(cursor.getString(25));
				item.setXiyizhenduan(cursor.getString(26));
				item.setZhizezhifa(cursor.getString(27));
				item.setFangming(cursor.getString(28));
				item.setFangjizucheng(cursor.getString(29));
				item.setQitazhiliao(cursor.getString(30));
				item.setYuhou(cursor.getString(31));
				item.setAnyu(cursor.getString(32));
				item.setDianxingyian(cursor.getInt(33));
				item.setLiaoxiao(cursor.getString(34));
				item.setLiaoxiaozhuangtai(cursor.getInt(35));
				item.setJiuzhenshijian(cursor.getString(36));
				item.setSrc(cursor.getString(37));
				item.setId(cursor.getInt(38));
				item.setIsEdit(cursor.getInt(39));
				item.setId_chuzhen(cursor.getInt(40));

				list.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}
	public List<MRClinic> queryMRCFu(String id_user,int id_chuzhen) {
		List<MRClinic> list = new ArrayList<MRClinic>();
		db = this.getReadableDatabase();
		String sql = "select * from mrc where id_chuzhen="+id_chuzhen+" and id_user="+id_user;
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			do {
				MRClinic item = new MRClinic();

				item.setIdLocal(cursor.getInt(0));
				item.setId_patient(cursor.getInt(1));
				item.setName_patient(cursor.getString(2));
				item.setSn_bingli(cursor.getString(3));
				item.setId_hospital(cursor.getInt(4));
				item.setName_hospital(cursor.getString(5));
				item.setId_dept(cursor.getInt(6));
				item.setName_dept(cursor.getString(7));
				item.setId_user(cursor.getInt(8));
				item.setName_user(cursor.getString(9));
				item.setTitle(cursor.getString(10));
				item.setMemo(cursor.getString(11));
				item.setJiwangshi(cursor.getString(12));
				item.setXianbingshi(cursor.getString(13));
				item.setZhusu(cursor.getString(14));
				item.setKexiazheng(cursor.getString(15));
				item.setShexiang(cursor.getString(16));
				item.setMaixiang(cursor.getString(17));
				item.setTizhengjiancha(cursor.getString(18));
				item.setFuzhujianca(cursor.getString(19));
				item.setTemperature(cursor.getString(20));
				item.setMaibo(cursor.getString(21));
				item.setXueya(cursor.getString(22));
				item.setBingming(cursor.getString(23));
				item.setZhongyizhenduan(cursor.getString(24));
				item.setZhongyizhenghou(cursor.getString(25));
				item.setXiyizhenduan(cursor.getString(26));
				item.setZhizezhifa(cursor.getString(27));
				item.setFangming(cursor.getString(28));
				item.setFangjizucheng(cursor.getString(29));
				item.setQitazhiliao(cursor.getString(30));
				item.setYuhou(cursor.getString(31));
				item.setAnyu(cursor.getString(32));
				item.setDianxingyian(cursor.getInt(33));
				item.setLiaoxiao(cursor.getString(34));
				item.setLiaoxiaozhuangtai(cursor.getInt(35));
				item.setJiuzhenshijian(cursor.getString(36));
				item.setSrc(cursor.getString(37));
				item.setId(cursor.getInt(38));
				item.setIsEdit(cursor.getInt(39));
				item.setId_chuzhen(cursor.getInt(40));

				list.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}
	//dange 
	public MRClinic queryMRCById(int id) {
		db = this.getReadableDatabase();
		String sql = "select * from mrc where _id="+id;
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		MRClinic item = new MRClinic();
		if (cursor.getCount() > 0) {
				

				item.setIdLocal(cursor.getInt(0));
				item.setId_patient(cursor.getInt(1));
				item.setName_patient(cursor.getString(2));
				item.setSn_bingli(cursor.getString(3));
				item.setId_hospital(cursor.getInt(4));
				item.setName_hospital(cursor.getString(5));
				item.setId_dept(cursor.getInt(6));
				item.setName_dept(cursor.getString(7));
				item.setId_user(cursor.getInt(8));
				item.setName_user(cursor.getString(9));
				item.setTitle(cursor.getString(10));
				item.setMemo(cursor.getString(11));
				item.setJiwangshi(cursor.getString(12));
				item.setXianbingshi(cursor.getString(13));
				item.setZhusu(cursor.getString(14));
				item.setKexiazheng(cursor.getString(15));
				item.setShexiang(cursor.getString(16));
				item.setMaixiang(cursor.getString(17));
				item.setTizhengjiancha(cursor.getString(18));
				item.setFuzhujianca(cursor.getString(19));
				item.setTemperature(cursor.getString(20));
				item.setMaibo(cursor.getString(21));
				item.setXueya(cursor.getString(22));
				item.setBingming(cursor.getString(23));
				item.setZhongyizhenduan(cursor.getString(24));
				item.setZhongyizhenghou(cursor.getString(25));
				item.setXiyizhenduan(cursor.getString(26));
				item.setZhizezhifa(cursor.getString(27));
				item.setFangming(cursor.getString(28));
				item.setFangjizucheng(cursor.getString(29));
				item.setQitazhiliao(cursor.getString(30));
				item.setYuhou(cursor.getString(31));
				item.setAnyu(cursor.getString(32));
				item.setDianxingyian(cursor.getInt(33));
				item.setLiaoxiao(cursor.getString(34));
				item.setLiaoxiaozhuangtai(cursor.getInt(35));
				item.setJiuzhenshijian(cursor.getString(36));
				item.setSrc(cursor.getString(37));
				item.setId(cursor.getInt(38));
				item.setIsEdit(cursor.getInt(39));
				item.setId_chuzhen(cursor.getInt(40));
		}
		cursor.close();
		return item;
	}
	//bydate
	public List<MRClinic> queryMRCByDate(String date,String id_user) {
		List<MRClinic> list = new ArrayList<MRClinic>();
		db = this.getReadableDatabase();
		String sql = "select * from mrc where  jiuzhenshijian='"+date+"' and id_user="+id_user;
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			do {
				MRClinic item = new MRClinic();

				item.setIdLocal(cursor.getInt(0));
				item.setId_patient(cursor.getInt(1));
				item.setName_patient(cursor.getString(2));
				item.setSn_bingli(cursor.getString(3));
				item.setId_hospital(cursor.getInt(4));
				item.setName_hospital(cursor.getString(5));
				item.setId_dept(cursor.getInt(6));
				item.setName_dept(cursor.getString(7));
				item.setId_user(cursor.getInt(8));
				item.setName_user(cursor.getString(9));
				item.setTitle(cursor.getString(10));
				item.setMemo(cursor.getString(11));
				item.setJiwangshi(cursor.getString(12));
				item.setXianbingshi(cursor.getString(13));
				item.setZhusu(cursor.getString(14));
				item.setKexiazheng(cursor.getString(15));
				item.setShexiang(cursor.getString(16));
				item.setMaixiang(cursor.getString(17));
				item.setTizhengjiancha(cursor.getString(18));
				item.setFuzhujianca(cursor.getString(19));
				item.setTemperature(cursor.getString(20));
				item.setMaibo(cursor.getString(21));
				item.setXueya(cursor.getString(22));
				item.setBingming(cursor.getString(23));
				item.setZhongyizhenduan(cursor.getString(24));
				item.setZhongyizhenghou(cursor.getString(25));
				item.setXiyizhenduan(cursor.getString(26));
				item.setZhizezhifa(cursor.getString(27));
				item.setFangming(cursor.getString(28));
				item.setFangjizucheng(cursor.getString(29));
				item.setQitazhiliao(cursor.getString(30));
				item.setYuhou(cursor.getString(31));
				item.setAnyu(cursor.getString(32));
				item.setDianxingyian(cursor.getInt(33));
				item.setLiaoxiao(cursor.getString(34));
				item.setLiaoxiaozhuangtai(cursor.getInt(35));
				item.setJiuzhenshijian(cursor.getString(36));
				item.setSrc(cursor.getString(37));
				item.setId(cursor.getInt(38));
				item.setIsEdit(cursor.getInt(39));
				item.setId_chuzhen(cursor.getInt(40));

				list.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}
	/**
	 * 
	 * 鏇存柊MRC
	 * 
	 * */
	public boolean updateMRC(MRClinic item) {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id_patient", item.getId_patient());
		values.put("name_patient", item.getName_patient());
		values.put("sn_bingli", item.getSn_bingli());
		values.put("id_hospital", item.getId_hospital());
		values.put("name_hospital", item.getName_hospital());
		values.put("id_dept", item.getId_dept());
		values.put("name_dept", item.getName_dept());
		values.put("id_user", item.getId_user());
		values.put("name_user", item.getName_user());
		values.put("title", item.getTitle());
		values.put("memo", item.getMemo());
		values.put("jiwangshi", item.getJiwangshi());
		values.put("xianbingshi", item.getXianbingshi());
		values.put("zhusu", item.getZhusu());
		values.put("kexiazheng", item.getKexiazheng());
		values.put("shexiang", item.getShexiang());
		values.put("maixiang", item.getMaixiang());
		values.put("tizhengjiancha", item.getTizhengjiancha());
		values.put("fuzhujianca", item.getFuzhujianca());
		values.put("temperature", item.getTemperature());
		values.put("maibo", item.getMaibo());
		values.put("xueya", item.getXueya());
		values.put("bingming", item.getBingming());
		values.put("zhongyizhenduan", item.getZhongyizhenduan());
		values.put("zhongyizhenghou", item.getZhongyizhenghou());
		values.put("xiyizhenduan", item.getXiyizhenduan());
		values.put("zhizezhifa", item.getZhizezhifa());
		values.put("fangming", item.getFangming());
		values.put("fangjizucheng", item.getFangjizucheng());
		values.put("qitazhiliao", item.getQitazhiliao());
		values.put("yuhou", item.getYuhou());
		values.put("anyu", item.getAnyu());
		values.put("dianxingyian", item.getDianxingyian());
		values.put("liaoxiao", item.getLiaoxiao());
		values.put("liaoxiaozhuangtai", item.getLiaoxiaozhuangtai());
		values.put("jiuzhenshijian", item.getJiuzhenshijian());
		values.put("src", item.getSrc());
		values.put("idup", item.getId());
		values.put("isEdit", item.getIsEdit());
		values.put("id_chuzhen", item.getId_chuzhen());

		Log.e(TAG, "insert DB id=" + item.getId());
		return (db.update("mrc", values, "_id=?",
				new String[] { String.valueOf(item.getIdLocal()) }) > 0);
	}


	/**
	 * 
	 * 娣诲姞鏃ョ▼
	 * 
	 * */
	public boolean insertSchedule(Schedule item) {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("content", item.getContent());
		values.put("date", item.getDate());
		values.put("time", item.getTime());
		values.put("type", item.getType());
		values.put("imgpath", item.getImgPath());
		values.put("contel", item.getContel());
		values.put("voipath", item.getVoiPath());
		values.put("id_user", item.getId_user());

		Log.e(TAG, "insert DB content=" + item.getDate());
		boolean bo = false;
		bo = db.insert("schedule", null, values) > 0;
		return (bo);
	}

	/**
	 * 
	 * 鏌ヨ鏃ョ▼
	 * 
	 * */
	public List<Schedule> querySchedule(String date,String id_user) {
		List<Schedule> list = new ArrayList<Schedule>();
		db = this.getReadableDatabase();
		String sql = "select * from schedule where date='" + date + "' and id_user="+id_user;
		Log.e(TAG, "QUERY DB sql=" + sql);
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			do {
				Schedule item = new Schedule();
				item.setId(cursor.getInt(0));
				item.setType(cursor.getInt(1));
				item.setContent(cursor.getString(2));
				item.setDate(cursor.getString(3));
				item.setTime(cursor.getString(4));
				item.setImgPath(cursor.getString(5));
				item.setContel(cursor.getString(6));
				item.setVoiPath(cursor.getString(7));
				
				list.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

	
	/**
	 * 
	 * 娣诲姞鏃ョ▼
	 * 
	 * */
	public boolean insertFriend(Friend item) {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id_user", item.getId_user());
		values.put("id_friend", item.getId_friend());
		values.put("tag", item.getTag());
		values.put("ispass", item.getIs_pass());
		values.put("displayname_friend", item.getDisplayname_friend());
		values.put("displayname_user", item.getDisplayname_user());

		
		Log.e(TAG, "insert DB content=" + item.getDisplayname_friend());
		boolean bo = false;
		bo = db.insert("friend", null, values) > 0;
		return (bo);
	}

	public void deleteFriend() {
		db = this.getWritableDatabase();
		
		db.execSQL("delete from friend where 1=1");
	}

	/**
	 * 
	 * 鏌ヨ鏃ョ▼
	 * 
	 * */
	public List<Friend> queryFriend(String id_user) {
		List<Friend> list = new ArrayList<Friend>();
		db = this.getReadableDatabase();
		String sql = "select * from friend where id_user="+id_user+" or id_friend="+id_user;
		Log.e(TAG, "QUERY DB sql=" + sql);
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			do {
				Friend item = new Friend();
				item.setId(cursor.getInt(0));
				item.setId_user(cursor.getString(1));
				item.setId_friend(cursor.getString(2));
				item.setTag(cursor.getString(3));
				item.setIs_pass(cursor.getInt(4));
				item.setDisplayname_friend(cursor.getString(5));
				item.setDisplayname_user(cursor.getString(6));
				
				list.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}


	/**
	 * 
	 * 娣诲姞鏂囦欢public static final String CREATE_ATTACH =
	 * "create table IF NOT EXISTS attachment (_id integer primary key autoincrement, "
	 * +
	 * "id_user integer not null,id_patient integer not null,fujianzubie text not null,directory text not null,id_mrclinic integer not null );"
	 * ;
	 * 
	 * 
	 * */
	public boolean insertAttachment(Attachment item) {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id_user", item.getId_user());
		values.put("id_patient", item.getId_patient());
		values.put("fujianzubie", item.getFujianzubie());
		values.put("directory", item.getDirectory());
		values.put("id_mrclinic", item.getId_mrclinic());
		values.put("fujianleixing", item.getFujianleixing());
		values.put("ext", item.getExt());

		Log.e(TAG, "insert DB directory=" + item.getDirectory());

		boolean bo = false;
		bo = db.insert("attachment", null, values) > 0;
		return (bo);
	}

	/**
	 * 
	 * 鏇存柊鏂囦欢
	 * 
	 * */
	public boolean updateAttachment(Attachment item) {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id_user", item.getId_user());
		values.put("id_patient", item.getId_patient());
		values.put("fujianzubie", item.getFujianzubie());
		values.put("directory", item.getDirectory());
		values.put("id_mrclinic", item.getId_mrclinic());
		values.put("fujianleixing", item.getFujianleixing());

		Log.e(TAG, "update DB content=" + item.getId_mrclinic() + item.getId()
				+ "\t\tid=" + item.getId());

		return (db.update("attachment", values, "_id=?",
				new String[] { String.valueOf(item.getId()) }) > 0);
	}

	/**
	 * 
	 * 鏌ヨ鏂囦欢
	 * 
	 * */
	public List<Attachment> queryAttachment(String fujianzubie,
			String fujianleixing) {
		List<Attachment> list = new ArrayList<Attachment>();
		db = this.getReadableDatabase();

		String sql = "select * from attachment where 1=1";
		if (StringUtil.isEmpty(fujianzubie)) {
			sql = sql + " and fujianzubie='" + fujianzubie + "'";
		}
		Log.e(TAG, "QUERY DB sql=" + sql);
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			do {
				Attachment item = new Attachment();
				item.setId(cursor.getInt(0));
				item.setId_user(cursor.getInt(1));
				item.setId_patient(cursor.getInt(2));
				item.setFujianzubie(cursor.getString(3));
				item.setFujianleixing(cursor.getString(4));
				item.setDirectory(cursor.getString(5));
				item.setId_mrclinic(cursor.getInt(6));

				list.add(item);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}
	public void deletePatient(int id) {

		db = this.getWritableDatabase();

		db.execSQL("delete from patient where _ID=" + id);

	}
	public void deletePatient() {

		db = this.getWritableDatabase();

		db.execSQL("delete from patient where 1=1");

	}
	public void deletePatientUp(int id) {

		db = this.getWritableDatabase();

		db.execSQL("delete from patientup where _ID=" + id);

	}

	public void deleteSch(int id) {

		db = this.getWritableDatabase();

		db.execSQL("delete from schedule where _ID=" + id);

	}

	public void deleteRecord(int id) {

		db = this.getWritableDatabase();

		db.execSQL("delete from mrc where _ID=" + id);

	}
	
	public void deleteAttach(int id) {

		db = this.getWritableDatabase();

		db.execSQL("delete from attachment where _ID=" + id);

	}
	// 鍒嗛�?
	// public List<BlogItem> query(int parentType,int type,int count,int
	// currPage) {
	// int sp=(currPage-1)*count;
	// int ep=currPage*count;
	// List<BlogItem> list = new ArrayList<BlogItem>();
	// SQLiteDatabase db = this.getReadableDatabase();
	// String sql = "select * from blogTable where parentType = " +
	// parentType+" and type = " + type;
	// Cursor cursor = db.rawQuery(sql, null);
	// //cursor.moveToFirst();
	// if (cursor.moveToFirst()) {
	// BlogItem item ;
	// //閬嶅巻娓告爣
	// Log.d("CUSRTSDS**&^%", ""+cursor.getCount());
	// for(int i=sp;i<cursor.getCount()&&i<ep;i++){
	// cursor.moveToPosition(i);
	// Log.d("CUSRTSDS**&^%^&*&&^", ""+i);
	//
	// item = new BlogItem();
	// item.setId(cursor.getInt(1));
	// item.setTitle(cursor.getString(2));
	// item.setUrl(cursor.getString(3));
	// item.setImg(cursor.getString(4));
	// item.setParentType(parentType);
	// item.setType(type);
	//
	// list.add(item);
	// }
	// //
	// // do {
	// // BlogItem item = new BlogItem();
	// // item.setId(cursor.getInt(1));
	// // item.setTitle(cursor.getString(2));
	// // item.setUrl(cursor.getString(3));
	// // item.setImg(cursor.getString(4));
	// // item.setParentType(parentType);
	// // item.setType(type);
	// //
	// // list.add(item);
	// // } while (cursor.moveToNext());
	// }
	// cursor.close();
	// return list;
	// }

}
