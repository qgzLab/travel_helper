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

public class Login extends Activity {
	
	Button login_back,login_img,login_send;
	TextView login_user;
	Socket socket = null;
	String buffer = "";
	String Emails,Passwords;
	EditText Email,Password;
	
	
	User user;
	
	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x11) {
				Bundle bundle = msg.getData();
				login_send.setText(bundle.getString("msg")+"\n");
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		login_back = (Button) findViewById(R.id.login_back);
		login_send = (Button) findViewById(R.id.login_send);
		login_img = (Button) findViewById(R.id.login_img);
		login_user = (TextView) findViewById(R.id.login_user);
		Email = (EditText) findViewById(R.id.login_Email);
		Password = (EditText) findViewById(R.id.login_Password);
		
		user= (User)getApplication();
		
		if ((user.getUsername()).equals("?"))
			login_user.setText("登陆");
		else
			login_user.setText(user.getUsername());
		
		login_send.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Emails = Email.getText().toString();
				Passwords=Password.getText().toString();
				new MyThread(Emails,Passwords).start();
				
			}
		});
		
		
		login_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Login.this,VIP.class);
				startActivity(intent);
				finish();
			}
		});
		
		login_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername()=="?")
				{
					Intent intent=new Intent(Login.this,Login.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent=new Intent(Login.this,Mine.class);
					Bundle b=new Bundle();
					b.putString("mine", user.getId());
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
			}
		});
			
	}
	
	class MyThread extends Thread {

		public String name,password;

		public MyThread(String na,String pa) {
			name = na;
			password=pa;
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
				ou.write(("login"+"\n").getBytes("gbk"));
				ou.write((Emails+":"+Passwords+"\n").getBytes("gbk"));
				ou.flush();
				socket.shutdownOutput();
				
				String line = null;
				String[] lines;
				
				buffer="";
				while ((line = bff.readLine()) != null) 
				{
					 if (line.equals("?"))
					 {
						 buffer="邮箱或密码错误";
					 }
					 else
					 {
						 lines=line.split(":");
						 user.setUsername(lines[0]);
						 user.setEmail(lines[1]);
						 user.setPhone(lines[2]);
						 user.setQQ(lines[3]);
						 user.setSex(lines[4]);
						 user.setBirth_year(Integer.parseInt(lines[5]));
						 user.setBirth_month(Integer.parseInt(lines[6]));
						 user.setBirth_day(Integer.parseInt(lines[7]));
						 user.setLive_province(lines[8]);
						 user.setLive_city(lines[9]);
						 user.setId(lines[10]);
						 buffer="登陆成功"; 
						 Intent intent=new Intent(Login.this,VIP.class);
						 startActivity(intent);
						 finish();
					 }
				}
				bundle.putString("msg", buffer);
				msg.setData(bundle);
				myHandler.sendMessage(msg);
				
				bff.close();
				ou.close();
				socket.close();
			} catch (SocketTimeoutException aa) {
				bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
				msg.setData(bundle);
				myHandler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}