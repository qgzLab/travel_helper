package com.example.travel_helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

public class Update extends Activity {
	
	Socket socket = null;
	String buffer = "",note;
	
	User user;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		user= (User)getApplication();
		Bundle b = this.getIntent().getExtras();
		note=b.getString("note");
		
	    new MyThread().start();
		
			
	}
	
	class MyThread extends Thread {

		public MyThread() {
		}

		@Override
		public void run() {
			//������Ϣ
			Message msg = new Message();
			msg.what = 0x11;
			Bundle bundle = new Bundle();
			bundle.clear();
			try {
				//���ӷ����� ���������ӳ�ʱΪ5��
				socket = new Socket();
				socket.connect(new InetSocketAddress(user.getIP(), 9999), 5000);
				//��ȡ���������
				OutputStream ou = socket.getOutputStream();
				BufferedReader bff = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				ou.write(("team_update"+"\n").getBytes("gbk"));
				ou.write((note+"\n").getBytes("gbk"));
				ou.flush();
				
				bff.close();
				ou.close();
				socket.close();
				String[] notes=note.split(":");
				Intent intent = new Intent(Update.this, Team_view.class);
				Bundle b=new Bundle();
				b.putString("team_id", notes[1]);
				intent.putExtras(b);
				startActivity(intent);
				finish();
			} catch (SocketTimeoutException aa) {
				bundle.putString("msg", "����������ʧ�ܣ����������Ƿ��");
				msg.setData(bundle);
				//myHandler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}