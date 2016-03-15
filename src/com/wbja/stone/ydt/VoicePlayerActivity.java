package com.wbja.stone.ydt;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wbja.stone.ydt.util.StringUtil;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
* @ClassName: RecorderActivity
* @Description: TODO(������һ�仰��������������)
* @author Stone
* @date 2015-7-14 ����2:27:34
*
*/ 
public class VoicePlayerActivity extends Activity {

	private final String TAG = "RecorderActivity";

	private Button btnStart;
	private Button btnStop;
	private Button btnUpload;
	private Button btnPlay;
	private Button btnresume;
	private TextView text;
	private MediaRecorder recorder;
	private boolean isSDCardExit; // �ж�SDCard�Ƿ����
	private String src;
	private int type;
	private File tempFile;
	private MediaPlayer mediaPlayer;
	private String srcPath;
	private String pos;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorder);

		Intent intent = getIntent();
		srcPath = intent.getStringExtra("srcPath");
		
		tempFile = new File(srcPath);

		btnStart = (Button) findViewById(R.id.btnStart);
		btnStop = (Button) findViewById(R.id.btnStop);
		btnUpload = (Button) findViewById(R.id.btnUpload);
		btnPlay = (Button) findViewById(R.id.btnPlay);
		btnresume = (Button) findViewById(R.id.btnresume);

		text = (TextView) findViewById(R.id.text);

		mediaPlayer = new MediaPlayer();

		btnStart.setVisibility(View.GONE);
		btnStop.setVisibility(View.GONE);
		btnUpload.setVisibility(View.GONE);
		btnPlay.setVisibility(View.VISIBLE);
		btnresume.setVisibility(View.GONE);
		// btnStop.setEnabled(false);
		// btnUpload.setEnabled(false);

		isSDCardExit = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (isSDCardExit) {
			if (!StringUtil.isEmpty(srcPath)) {
				Log.d("************VOICE SRC*******", srcPath);
				buttonListener();
			} else {
				Toast.makeText(VoicePlayerActivity.this, "δ֪�����뷵����һ����",
						Toast.LENGTH_SHORT).show();
			}
		} 

	}

	/**
	 * ��Ӱ�ť�¼�
	 */
	private void buttonListener() {
		// ����
		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				paly();
			}
		});
		btnStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				btnPlay.setVisibility(View.VISIBLE);
				btnStop.setVisibility(View.GONE);
				text.setText("����ֹͣ��");
				stopRecorder();
			}
		});
	}

	/**
	 * ׼��¼��
	 */
	private void initRecorder() {
		recorder = new MediaRecorder();
		/* ������ƵԴ */
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		/* ���������ʽ */
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		/* ������Ƶ������ */
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		/* ����һ����ʱ�ļ����������¼�� */
		String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		srcPath = src +pos + name + ".wmv";
		Log.d("********VOICE PATH************", srcPath + "----------");
		// SDPathDir=new File(srcPath);
		tempFile = new File(srcPath);// File.createTempFile(name, ".wmv",
										// SDPathDir);
		if (tempFile.exists()) {
			tempFile.delete();
		}
		/* ����¼���ļ� */
		recorder.setOutputFile(tempFile.getAbsolutePath());
	}

	/**
	 * ��ʼ¼��
	 */
	private void startRecorder() {
		try {
			if (!isSDCardExit) {
				Toast.makeText(this, "�����SD��", Toast.LENGTH_LONG).show();
				return;
			}
			recorder.prepare();
			recorder.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ֹͣ¼��
	 */
	private void stopRecorder() {
//		if (recorder != null) {
//			try {
//				recorder.stop();
//
//			} catch (Exception e) {
//
//			}
//			//recorder.release();// �ͷ���Դ
//			//recorder = null;
//		}
		mediaPlayer.stop();
	}

	/**
	 * ����
	 * */
	private void paly() {
		

		mediaPlayer.reset();// ����Ϊ��ʼ״̬
		mediaPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {// ��������¼�
					@Override
					public void onCompletion(MediaPlayer arg0) {
						// mediaPlayer.release();
						btnPlay.setVisibility(View.VISIBLE);
						btnStop.setVisibility(View.GONE);
						text.setText("���Ž�����");
					}
				});
		mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {// �������¼�
					@Override
					public boolean onError(MediaPlayer player, int arg1,
							int arg2) {
						mediaPlayer.release();
						btnPlay.setVisibility(View.VISIBLE);
						btnStop.setVisibility(View.GONE);
						text.setText("���Ŵ���");
						return false;
					}
				});
		try {
			mediaPlayer.setDataSource(tempFile.getAbsolutePath());
			mediaPlayer.prepare();
			mediaPlayer.start();
			text.setText("����¼������");
			btnPlay.setVisibility(View.GONE);
			btnStop.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			btnPlay.setVisibility(View.VISIBLE);
			btnStop.setVisibility(View.GONE);
			Toast.makeText(VoicePlayerActivity.this, e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (recorder != null) {
			recorder.stop();
			recorder.release();// �ͷ���Դ
			recorder = null;
		}
		super.onStop();
	}

}