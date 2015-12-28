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
import android.widget.Toast;

public class Register extends Activity {
	
	Button register_back,register_img,register_send;
	EditText register_username,register_sex,register_password,register_phone,register_email,register_qq,register_birth_year,register_birth_month,register_birth_day,register_live_province,register_live_city;
	TextView register_user;
	Socket socket = null;
	String username,password,sex,phone,email,qq,birth_year,birth_month,birth_day,live_province,live_city;
	User user;
	
	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x11) {
				Bundle bundle = msg.getData();
				String str=bundle.getString("msg");
				register_send.setText(str);
				if (str.equals("注册成功"))
				{
					Intent intent=new Intent(Register.this,VIP.class);
					startActivity(intent);
					finish();
				}
				else if (str.equals("修改成功"))
				{
					Toast.makeText(getApplicationContext(), "修改成功!",Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(Register.this,Mine.class);
					Bundle b=new Bundle();
					b.putString("mine", user.getId());
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		register_back = (Button) findViewById(R.id.register_back);
		register_img = (Button) findViewById(R.id.register_img);
		register_send = (Button) findViewById(R.id.register_send);
		register_user = (TextView) findViewById(R.id.register_user);
		register_username = (EditText) findViewById(R.id.register_username);
		register_password = (EditText) findViewById(R.id.register_password);
		register_sex = (EditText) findViewById(R.id.register_sex);
		register_phone = (EditText) findViewById(R.id.register_phone);
		register_email = (EditText) findViewById(R.id.register_email);
		register_qq = (EditText) findViewById(R.id.register_qq);
		register_birth_year = (EditText) findViewById(R.id.register_birth_year);
		register_birth_month = (EditText) findViewById(R.id.register_birth_month);
		register_birth_day = (EditText) findViewById(R.id.register_birth_day);
		register_live_province = (EditText) findViewById(R.id.register_live_province);
		register_live_city = (EditText) findViewById(R.id.register_live_city);
		
		user= (User)getApplication();
		
		/* 用户判别 */
		if ((user.getUsername()).equals("?"))
			register_user.setText("登陆");
		else
		{
			register_user.setText(user.getUsername());
			register_username.setText(user.getUsername());
			register_sex.setText(user.getSex());
			register_phone.setText(user.getPhone());
			register_email.setText(user.getEmail());
			register_qq.setText(user.getQQ());
			register_birth_year.setText(String.valueOf(user.getBirth_year()));
			register_birth_month.setText(String.valueOf(user.getBirth_month()));
			register_birth_day.setText(String.valueOf(user.getBirth_day()));
			register_live_province.setText(user.getLive_province());
			register_live_city.setText(user.getLive_city());
		}
		
		register_send.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				username = register_username.getText().toString();
				password=register_password.getText().toString();
				sex=register_sex.getText().toString();
				phone=register_phone.getText().toString();
				email=register_email.getText().toString();
				qq=register_qq.getText().toString();
				birth_year=register_birth_year.getText().toString();
				birth_month=register_birth_month.getText().toString();
				birth_day=register_birth_day.getText().toString();
				live_province=register_live_province.getText().toString();
				live_city=register_live_city.getText().toString();
				//启动线程 向服务器发送和接收信息
				new MyThread().start();
			}
		});
		
		register_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Register.this,VIP.class);
				startActivity(intent);
				finish();
			}
		});
		
		register_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername()=="?")
				{
					Intent intent=new Intent(Register.this,Login.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent=new Intent(Register.this,Mine.class);
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
				if (user.getUsername().equals("?"))
				{
					ou.write(("register"+"\n").getBytes("gbk"));
					ou.write((username+":"+password+":"+email+":"+phone+":"+sex+":"+birth_year+":"+birth_month+":"+birth_day+":"+qq+":"+live_province+":"+live_city+"\n").getBytes("gbk"));
					ou.flush();
				}
				else
				{
					ou.write(("user_update"+"\n").getBytes("gbk"));
					ou.write((username+":"+email+":"+phone+":"+sex+":"+birth_year+":"+birth_month+":"+birth_day+":"+qq+":"+live_province+":"+live_city+":"+user.getId()+"\n").getBytes("gbk"));
					ou.flush();
				}
				
				socket.shutdownOutput();
				
				String line = null;
				
				String buffer="";
				while ((line = bff.readLine()) != null) 
				{
					 buffer+=line;	
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