package com.wbja.stone.ydt.util;

import java.util.Map;

public class Constants {

	public static Map<String, String> checkMap;
	public static boolean isCheckAll = false;
	public static boolean isShow = false;
	// ��������
	public final static String APPKEY = "8luwapkvu1x0l";
	public final static String APPSCRET = "ETMJslMAOwxXz";
	// ��������
	// public final static String APPKEY="8brlm7ufr26s3";
	// public final static String APPSCRET="8TOALx2qE70c7j";

	public final static String RONGURI = "https://api.cn.ronghub.com/user/getToken.json";
	//public final static String HOST="http://210.76.97.193:81";
	public final static String HOST = "http://123.57.245.75:81";

	public final static String UPDATEURL = HOST + "/download/ydtupdate.txt";
	public final static String LOGIN = HOST + "/Portal/MobileLogin";
	public final static String REGCODE = HOST + "/Portal/MobileSendCode";
	public final static String REGE = HOST + "/Portal/MobileReg";
	// ����
	public final static String ADDPATIENT = HOST + "/Patient/CreatePatientM";
	public final static String ADDMRCLINIC = HOST + "/MRClinic/CreateMRClinicM";
	public final static String GETMRCLINICLIST = HOST + "/MRClinic/GetMRClinicListM";
	public final static String GETPATIENTLIST = HOST + "/patient/getpatientlistm";
	// ���ﲡ��
	public final static String GetCLCListM = HOST + "/ClassicCase/GetListM";
	public final static String GetCLCDetailM = HOST + "/ClassicCase/GetDetailM";
	public final static String GetCLRListM = HOST + "/ClinicRoute/GetListM";
	public final static String GetCLRDetailM = HOST + "/ClinicRoute/GetDetailM";
	public final static String BWZZ = HOST + "/ChineseMedicine/BuWeiZhengZhuang";
	public final static String ZZZH = HOST + "/ChineseMedicine/ZhengZhuangZhengHou";
	public final static String ZHBM = HOST + "/ChineseMedicine/ZhengHouBingMing";
	public final static String BMFJ = HOST + "/ChineseMedicine/BingMingFangJi";
	// �Ӻ���
	public final static String SEARCHFRIEND = HOST + "/Friend/FriendSearchMobile";
	public final static String ADDFRIEND = HOST + "/Friend/FriendAddMobile";
	public final static String FRIENDLIST = HOST + "/Friend/FriendListMobile";
	// ԤԼ
	public final static String REGLIST = HOST + "/Registration/RegList";
	public final static String MNGOK = HOST + "/Registration/MngRegOk";
	public final static String MNGNO = HOST + "/Registration/MngRegNo";
	// ҽԺ�б�
	public final static String SEARCHHOSPITAL = HOST + "/PtRegistration/HospList";
	public final static String DOCLIST = HOST + "/PtRegistration/DoctorList";
	public final static String SEARCHDEPT = HOST + "/PtRegistration/DeptList";

	// �ϴ�����
	public final static String HOSTFILE = "http://123.57.245.75:81";
	// public final static String HOSTFILE="http://192.168.70.65";
	public final static String ADDATTACH = HOSTFILE + "/File/Upload";
	// ��ȡ�����б� ������long id_user, string token, long id_mrclinic, string
	// fujianzubie, string fujianleixing
	public final static String GETATTACHLIST = HOSTFILE + "/File/ListFileMR";
	// ��ȡ�ļ� long id_user, string token, long id
	public final static String GETFILE = HOSTFILE + "/File/GetFileInfo";

	public final static String OPADDFRIEND = "addfriend";
	// �û�����
	public final static String USERTYPE = "3";

	public final static String IMAGEPATH = "ydt/image/";
	public final static String SCHEDULEPATH = "ydt/schedule/";
	public final static String VOICEPATH = "ydt/voice/";
	public final static String RECORDPATH = "ydt/record/";
	public final static String VOICESTR = "VOICE";
	public final static String TEXTSTR = "TEXT";
	public final static String USERID = "id";
	public final static String TOKEN = "token";
	public final static String RONGTOKEN = "rongtoken";
	public final static String RONGID = "rongid";
	public final static String DOCNAME = "docname";
	public final static String USERTYPERE = "usertype";
	public final static String HOSPID = "hospid";
	public final static String USERNAME = "username";
	public final static String HOSPNAME = "hospname";
	public final static String DEPTNAME = "nameDept";
	public final static String DEPTID = "idDept";
	public final static String DISPLAYNAME = "displayname";
	public final static String FILEFILTER = "-upload-";
	public final static String GROUP0 = "0-";
	public final static String GROUP1 = "1-";
	public final static String GROUP2 = "2-";
	public final static String GROUP3 = "3-";
	public final static String GROUP4 = "4-";
	public final static String GROUP5 = "5-";
	// �ļ������ǩ
	// ��ʷ
	public final static String JWS = "jws-";
	public final static String XBS = "xbs-";
	// �ٴ�֢״
	public final static String ZS = "zs-";
	public final static String SX = "sx-";
	public final static String MX = "mx-";
	public final static String KXZ = "kxz-";
	// ������
	public final static String TZJC = "tzjc-";
	public final static String FZJC = "fzjc-";
	// ��֤���
	public final static String BZFX = "bzfx-";
	public final static String ZYZD = "zyzd-";
	public final static String XYZD = "xyzd-";
	public final static String JYZH = "jyzh-";
	// ���Ʒ���
	public final static String ZZZF = "zzzf-";
	public final static String JYZF = "jyzf-";
	public final static String FM = "fm-";
	public final static String ZC = "zc-";
	public final static String QTZL = "qtzl-";
	// ��ҩ
	public final static String JF = "jf-";
	public final static String ZNF = "znf-";
	public final static String JYFY = "jyfy-";

	public final static int SUCCESS = 1;
	public final static int FAILURE = 2;
	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 3;// ����
	public static final int PHOTOZOOM = 4; // ����
	public static final int PHOTORESOULT = 5;// ���
	public static final int VOICE = 6;// ���
	public static final int TEXT = 7;// ���
	public static final int SETPATIENT = 8;// ��������
	public static final int IMGID = 9;//
	public static final int IMGSHEBAO = 10;//
	public static final int IMGYIBAO = 11;//

}
