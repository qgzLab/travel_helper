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
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Teamdel extends Activity {
	
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
			//定义消息
			Message msg = new Message();
			msg.what = 0x11;
			Bundle bundle = new Bundle();
			bundle.clear();
			try {
				//连接服务器 并设置连接超时为5秒
				socket = new Socket();
				socket.connect(new InetSocketAddress(user.getIP(), 9999), 5000);
				//获取输入输出流
				OutputStream ou = socket.getOutputStream();
				BufferedReader bff = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				ou.write(("team_del"+"\n").getBytes("gbk"));
				ou.write((note+"\n").getBytes("gbk"));
				ou.flush();
				
				bff.close();
				ou.close();
				socket.close();
				String[] notes=note.split(":");
				Intent intent=null;
				if (!notes[0].equals(user.getId()))
				{
					intent = new Intent(Teamdel.this, Team_view.class);
					Bundle b=new Bundle();
					b.putString("team_id", notes[1]);
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
				else
				{
					intent = new Intent(Teamdel.this, Team.class);
					startActivity(intent);
					finish();
				}
			} catch (SocketTimeoutException aa) {
				bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
				msg.setData(bundle);
				//myHandler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}