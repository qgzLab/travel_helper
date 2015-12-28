package com.example.travel_helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Loc extends Activity {
	
	Button loc_back,loc_img,loc_send;
	TextView loc_user,loc_place,loc_loc,loc_intro,loc_price,loc_logo;
	String place,loc,intro,price,id;
	Socket socket = null;
	
	
	User user;
	
	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String str;
			int i;
			if (msg.what == 0x11) {
				Bundle bundle = msg.getData();
				str=bundle.getString("msg");
				String row[]=str.split(":");
				loc_place.setText(row[0]);
				loc_loc.setText(row[1]+"-"+row[2]);
				loc_intro.setText(row[4]);
				loc_price.setText(row[3]);
				
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loc);
		
		loc_logo = (TextView) findViewById(R.id.loc_logo);
		loc_back = (Button) findViewById(R.id.loc_back);
		loc_send = (Button) findViewById(R.id.loc_send);
		loc_img = (Button) findViewById(R.id.loc_img);
		loc_user = (TextView) findViewById(R.id.loc_user);
		loc_place = (TextView) findViewById(R.id.loc_place);
		loc_loc = (TextView) findViewById(R.id.loc_loc);
		loc_intro = (TextView) findViewById(R.id.loc_intro);
		loc_price = (TextView) findViewById(R.id.loc_price);
			
		user= (User)getApplication();
		
		if ((user.getUsername()).equals("?"))
			loc_user.setText("登陆");
		else
			loc_user.setText(user.getUsername());
		
		Bundle b = this.getIntent().getExtras();
		id=b.getString("loc");
		
		new MyThread().start();
		
		loc_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Loc.this,Think.class);
				startActivity(intent);
				finish();
			}
		});
		loc_send.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "关注成功!",Toast.LENGTH_SHORT).show();
			}
		});
		
		loc_img.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (user.getUsername().equals("?"))
				{
					Intent intent=new Intent(Loc.this,Login.class);
					startActivity(intent);
					finish();
				}
				else
				{
					Intent intent=new Intent(Loc.this,Mine.class);
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
				ou.write(("loc"+"\n").getBytes("gbk"));
				ou.write((String.valueOf(user.getId())+":"+id+"\n").getBytes("gbk"));
				ou.flush();
				socket.shutdownOutput();
				
				String line = null;
				
				String buffer="";
				while ((line = bff.readLine()) != null) 
				{
					 buffer+=line;	//loc_place:loc_province:loc_city:loc_price:loc_intro
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